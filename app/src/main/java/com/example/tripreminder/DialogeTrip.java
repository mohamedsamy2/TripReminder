package com.example.tripreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.services.FloatingViewService;
import com.google.gson.Gson;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DialogeTrip extends AppCompatActivity {

    static String TAG="main";

    Uri uri;
    Ringtone ringtone;
    Trip trip;
    RoomDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        database = RoomDatabase.getInstance(this);
        Intent intent=getIntent();
        String gson=intent.getStringExtra("trip");
        trip=new Gson().fromJson(gson,Trip.class);

        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
        ringtone.play();

        Log.i(TAG, "......................................onCreate: ");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Trip Reminder")
                .setMessage(trip.getTripName()).setIcon(R.drawable.calendar)

                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        ringtone.stop();



                        database.roomTripDao(). updateStatus("done",trip.getTripID()).subscribeOn(Schedulers.computation())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }
                                    @Override
                                    public void onComplete() {
                                        Log.i(TAG, "onComplete:done ");

                                    }
                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }
                                });

                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr="+trip.getDestination());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        startActivity(mapIntent);


                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ringtone.stop();

                        database.roomTripDao(). updateStatus("cancled",trip.getTripID()).subscribeOn(Schedulers.computation())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }
                                    @Override
                                    public void onComplete() {
                                        Log.i(TAG, "onComplete:cancled ");

                                    }
                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }
                                });




                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();



    }




   /* private void startFloatingViewService() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            this.startService(new Intent(this, FloatingViewService.class));
        } else if (Settings.canDrawOverlays(this)) {
            startService(new Intent(this, FloatingViewService.class));
            //finish();
        } else {
            askPermission();
            Toast.makeText(this, "You need System Alert Window Permission to do this", Toast.LENGTH_SHORT).show();
        }
    }
    private void askPermission() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));

        startActivityForResult(intent,SYSTEM_ALERT_WINDOW_PERMISSION);
    }*/
}