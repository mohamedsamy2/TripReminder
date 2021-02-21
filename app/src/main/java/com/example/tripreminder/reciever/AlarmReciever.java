package com.example.tripreminder.reciever;

import android.app.ActionBar;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.DialogeTrip;
import com.example.tripreminder.MainActivity;
import com.example.tripreminder.R;
import com.example.tripreminder.model.Trip;
import com.google.gson.Gson;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmReciever extends BroadcastReceiver {

    NotificationManager notificationManager;
    public static String TAG ="main";
    Intent intent1;


    @Override
    public void onReceive(Context context, Intent intent) {

       String gson=intent.getStringExtra("trip");
       Trip trip=new Gson().fromJson(gson,Trip.class);
       Log.i(TAG, ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onReceive: ");



        Intent intent2 = new Intent("android.intent.action.MAIN");
        intent2.setClass(context, DialogeTrip.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent2.putExtra("trip",new Gson().toJson(trip));
        context.startActivity(intent2);

       /* notificationManager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

         shoe(context,"first trip",trip.getTripName());*/

    }

    void shoe(Context context,String text,String name) {
        NotificationManager notificationManager = null;
        Intent intent = new Intent(context, MainActivity.class);
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

                    .setColor(2555).setAutoCancel(false)//.setSound(R.raw.)

                    /*.setStyle(new Notification() .bigTextStyle().setSummaryText("").setBigContentTitle("title").bigText("message"))*/
                    .setContentTitle(name).setContentText(text)
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