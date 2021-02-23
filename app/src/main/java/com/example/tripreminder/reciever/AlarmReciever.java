package com.example.tripreminder.reciever;

import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.DialogeTrip;
import com.example.tripreminder.MainActivity;
import com.example.tripreminder.R;
import com.example.tripreminder.UpcomingFragment;
import com.example.tripreminder.helper.NotificationHelper;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.services.FloatingViewService;
import com.google.gson.Gson;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReciever extends BroadcastReceiver {

    NotificationManager notificationManager;
    public static String TAG ="main";
    Intent intent1;
    NotificationHelper notificationHelper;
    RoomDatabase database;
    Trip trip;




    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onReceive: " );

        if(intent.getAction()==null){

           String gson=intent.getStringExtra("trip");
           trip=new Gson().fromJson(gson,Trip.class);
           Intent intent2 = new Intent("android.intent.action.MAIN");
           intent2.setClass(context, DialogeTrip.class);
           intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           intent2.putExtra("trip",new Gson().toJson(trip));
           context.startActivity(intent2);

        }else if(intent.getAction()=="start"){

            Log.i(TAG, "................onReceive:start ");
            String gson=intent.getStringExtra("trip");
            trip=new Gson().fromJson(gson,Trip.class);
            Intent intent2 = new Intent(context,MainActivity.class);
            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            notificationHelper=new NotificationHelper(context,trip);
            notificationHelper.getManager().cancel(trip.getTripID());

            context.startActivity(intent2);

        }else if(intent.getAction()=="cancle"){
            Log.i(TAG, "................onReceive:cancle ");

            String gson=intent.getStringExtra("trip");
            trip=new Gson().fromJson(gson,Trip.class);
            notificationHelper=new NotificationHelper(context,trip);
            notificationHelper.getManager().cancel(trip.getTripID());

            database.roomTripDao().tripCancelled(trip.getTripID()).subscribeOn(Schedulers.computation())
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




    }






}