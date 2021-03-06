package com.example.tripreminder.helper;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.tripreminder.AddNotesActivity;
import com.example.tripreminder.R;
import com.example.tripreminder.UpcomingFragment;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.reciever.AlarmReciever;
import com.google.gson.Gson;

import java.io.Serializable;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";


    private NotificationManager mManager;
    Context context;
    Trip trip;

    public NotificationHelper(Context base, Trip trip) {
        super(base);
        this.context = base;
        this.trip =trip;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

//        else{
//            sendNotification();
//
//        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        Intent intentStart = new Intent(context, AlarmReciever.class);
        intentStart.setAction("Start");
        intentStart.putExtra("trip",new Gson().toJson(trip));
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(context,0,intentStart, PendingIntent.FLAG_CANCEL_CURRENT);

        /////////////////////////////////////////////////////////////////////////////////////////////////////////
        Intent intentCancel = new Intent(context, AlarmReciever.class);
        intentCancel.putExtra("trip",new Gson().toJson(trip));
        intentCancel.setAction("Cancel");
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(context,trip.getTripID(),intentCancel,PendingIntent.FLAG_CANCEL_CURRENT);
        //////////////////////////////////////////////////////////////////////////////////////////////////////////
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(trip.getTripName() + "")
                .setContentText(trip.getDestination())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSound(sound).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setLights(0xff00ff00, 300, 100)
                .setContentIntent(null)
                .addAction(R.id.icon_only,"Start",pendingIntentStart)
                .addAction(R.id.icon_only,"Cancel",pendingIntentCancel);


    }

    public void sendNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(context, UpcomingFragment.class);
        intent.putExtra("TripID", (Serializable) trip.getTripID());
        PendingIntent pendingIntent = PendingIntent.getActivity(this, trip.getTripID(), intent, 0);
        builder.setContentIntent(pendingIntent);
        builder.setContentTitle(trip.getTripName());
        builder.setContentText(trip.getDestination());
        builder.setSubText("Tap to view the website.");

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(1, builder.build());
    }
}
