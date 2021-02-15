package com.example.tripreminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.model.Trip;
import com.example.tripreminder.Database.Room.RoomDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpcomingFragment extends Fragment {
    private static final String TAG = "UpcomingFragment";
    RecyclerView recyclerView;
    UpcomingAdapter upcomingAdapter;
    List<Trip> upcomingList = new ArrayList<>();
    RoomDatabase database;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDatabase.getInstance(getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        database.roomTripDao().getTripsByUser(FirebaseAuth.getInstance().getUid()).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Trip>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                Log.i(TAG, "onSubscribe: "+FirebaseAuth.getInstance().getUid());

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {

                upcomingAdapter.setList(trips);
                upcomingAdapter.notifyDataSetChanged();
                Log.i(TAG, "onSuccess: ");
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                Log.i(TAG, "onError: ");

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity upcomingFragment = getActivity();
        recyclerView = view.findViewById(R.id.upcomingRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(upcomingFragment);
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        upcomingAdapter = new UpcomingAdapter(upcomingFragment,upcomingList);
        recyclerView.setAdapter(upcomingAdapter);





    }


}