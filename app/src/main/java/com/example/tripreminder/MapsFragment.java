package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.model.Trip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapsFragment extends Fragment {

    List<Trip> pastTrips = new ArrayList<>();
    RoomDatabase database;
    Geocoder geocoder;
    List<Address> addresses;
    int color;
    Random rnd;
    double startLong, endLong, startLat, endLat;
    private static final String TAG = "MapsFragment";
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            database = RoomDatabase.getInstance(getContext());
            geocoder = new Geocoder(getContext(), Locale.getDefault());
            database.roomTripDao().getPastTripsByUser(FirebaseAuth.getInstance().getUid()).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Trip>>() {
                @Override
                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                }

                @Override
                public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {

                    Log.i(TAG, "onSuccess: roomPastTrips");
                    for (Trip trip: trips)
                    {

                        try {
                            rnd = new Random();
                            color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                            addresses = geocoder.getFromLocationName(trip.getSource(), 1);
                            Address address = addresses.get(0);
                            startLong = address.getLongitude();
                            startLat = address.getLatitude();
                            addresses = geocoder.getFromLocationName(trip.getDestination(), 1);
                            address = addresses.get(0);
                            endLong = address.getLongitude();
                            endLat = address.getLatitude();
                            googleMap.addPolyline(new PolylineOptions()
                                    .clickable(true)
                                    .color(color)
                                    .add(
                                            new LatLng(startLat, startLong),
                                            new LatLng(endLat, endLong)
                                    ));
                            Log.i(TAG, "onMapReady: " + endLat + " " + endLong);




                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    Log.i(TAG, "onMapReady: afterloop " + endLat + " " + endLong);

                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(endLat, endLong), 1));
                }

                @Override
                public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                    Log.i(TAG, "onError: " + e.getMessage());
                }
            });





        }

    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



}