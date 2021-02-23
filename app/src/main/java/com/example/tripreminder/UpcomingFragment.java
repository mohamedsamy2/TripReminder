package com.example.tripreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.helper.AlarmHelper;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.services.FloatingViewService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class UpcomingFragment extends Fragment implements UpcomingAdapter.OnItemClickLisener {
    private static final String TAG = "UpcomingFragment";
    RecyclerView recyclerView;
    UpcomingAdapter upcomingAdapter;
    List<Trip> upcomingList = new ArrayList<>();
    RoomDatabase database;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    AlarmHelper alarmHelper;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = RoomDatabase.getInstance(getContext());
        alarmHelper=new AlarmHelper(getContext());

    }

    @Override
    public void onResume() {
        super.onResume();
        database.roomTripDao().getUpcomingTripsByUser(FirebaseAuth.getInstance().getUid()).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Trip>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                Log.i(TAG, "onSubscribe: "+FirebaseAuth.getInstance().getUid());

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {
                if (trips.size()==0)
                    Toast.makeText(getContext(),"You have no upcoming trips", Toast.LENGTH_SHORT).show();

                upcomingList = trips;
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
        upcomingAdapter.setOnItemClickLisener(this);
        recyclerView.setAdapter(upcomingAdapter);

    }

    @Override
    public void onCancleClickLisener(Trip trip) {
        openDialog(getContext(),trip);
    }

    @Override
    public void onStartClickLisener(int position, String to) {
        if(checkFloatingViewServicePermission()) {
            setDoneStatus(upcomingList.get(position).getTripID());
            Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr=" + to);
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
            startFloatingViewService(new Gson().toJson(upcomingList.get(position).getNotes()));
        }
        else {
            askPermission();
            Toast.makeText(getContext(), "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onViewNotesClickListener(int posation) {
        if (upcomingList.get(posation).getNotes() !=null && !upcomingList.get(posation).getNotes().isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString("notes", new Gson().toJson(upcomingList.get(posation).getNotes()));
            NotesFragment notesFragment = new NotesFragment();
            notesFragment.setArguments(bundle);
            notesFragment.show(getParentFragmentManager(),"note_dialog");
        }else{
            Toast.makeText(getContext(), "This item doesn't have any notes", Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkFloatingViewServicePermission(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return true;
        if (Settings.canDrawOverlays(getContext()))
            return true;

        return false;
    }

    private void startFloatingViewService(String notes) {
            getContext().startService(new Intent(getContext(), FloatingViewService.class).putExtra("notes",notes));
    }

    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getContext().getPackageName()));
        startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getContext())) {
                    Toast.makeText(getContext(), "permission denied by user.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void openDialog(Context context, Trip trip) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Are you sure you want to delete " + trip.getTripName() + " ? ");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database = RoomDatabase.getInstance(getContext());
                database.roomTripDao().deleteTrip(trip).subscribeOn(Schedulers.computation())
                        .subscribe(new CompletableObserver() {
                            @Override
                            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                            }
                            @Override
                            public void onComplete() {

                                alarmHelper.cancelAlarm(trip);

                            }
                            @Override
                            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                            }
                        });
                //trip.setStatus("Cancelled");
                //upcomingAdapter.setList(upcomingList); //doesn't update recyclerview
                upcomingList.remove(trip);
                upcomingAdapter.notifyDataSetChanged();
            }
        });

        builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder1.create();
        builder1.show();

    }


    public void setDoneStatus(int tripID) {
        database = RoomDatabase.getInstance(getContext());
        database.roomTripDao().tripStarted(tripID).subscribeOn(Schedulers.computation())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

}