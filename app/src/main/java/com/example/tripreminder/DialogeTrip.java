package com.example.tripreminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
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
import com.example.tripreminder.helper.NotificationHelper;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.services.FloatingViewService;
import com.google.gson.Gson;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DialogeTrip extends AppCompatActivity {

    static String TAG="main";
    NotificationManager notificationManager;
    Uri uri,uri2;
    Ringtone ringtone;
    Trip trip;
    RoomDatabase database;
    Intent intent;
    String gson;
    NotificationHelper notificationHelper;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        database = RoomDatabase.getInstance(this);
        intent=getIntent();
        gson=intent.getStringExtra("trip");
        trip=new Gson().fromJson(gson,Trip.class);
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        uri2=RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

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
                        database.roomTripDao().tripStarted(trip.getTripID()).subscribeOn(Schedulers.computation())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }
                                    @Override
                                    public void onComplete() {
                                        Log.i(TAG, "onComplete:done ");

                                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr="+trip.getDestination());
                                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                                        mapIntent.setPackage("com.google.android.apps.maps");
                                        startActivity(mapIntent);
                                        startFloatingViewService();

                                    }
                                    @Override
                                    public void onError(@NonNull Throwable e) {





                                    }
                                });




                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ringtone.stop();

                        database.roomTripDao().tripCancelled(trip.getTripID()).subscribeOn(Schedulers.computation())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }
                                    @Override
                                    public void onComplete() {
                                        Log.i(TAG, "onComplete:cancled ");
                                        finish();

                                    }
                                    @Override
                                    public void onError(@NonNull Throwable e) {

                                    }
                                });



                    }
                }).setNeutralButton("snooze", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ringtone.stop();

                        notificationHelper = new NotificationHelper(DialogeTrip.this, trip);
                        NotificationCompat.Builder builder1 = notificationHelper.getChannelNotification();
                        notificationHelper.getManager().notify(trip.getTripID(), builder1.build());

                        // notificationManager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                         //shoe(DialogeTrip.this,"first trip",trip.getTripName());
                         finish();

                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

    }







   private void startFloatingViewService() {
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
    }

    void shoe(Context context, String text, String name) {



        NotificationManager notificationManager = null;
        Intent intent = new Intent(context, UpcomingFragment.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        /*Intent intent2 = new Intent(context, MainActivity2.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(context, 0, intent2, 0);*/

        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "mostafa", importance);
            channel.setDescription("description");
            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                    .addAction(R.drawable.clock, "Start", null)
                    .addAction(R.drawable.clock, "Cancle", null)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                    .setColor(2555)
                    .setAutoCancel(false)
                    .setSound(uri2)
                    .setContentIntent(pendingIntent)
                    .setContentTitle(name)
                    .setContentText(text)
                    .setSmallIcon(R.drawable.clock);

        } else {

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                    .setSmallIcon(R.drawable.clock)
                    .setContentTitle("My notification").setContentText("Hello World!").setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent).setAutoCancel(true);

        }


        notificationManager.notify(1, builder.build());

    }
}