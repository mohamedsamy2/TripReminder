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
import com.example.tripreminder.DialogeTrip;
import com.example.tripreminder.R;
import com.example.tripreminder.UpcomingFragment;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.reciever.AlarmReciever;

import java.io.Serializable;

public class NotificationHelper extends ContextWrapper {
    public static final String channelID = "channelID";
    public static final String channelName = "Channel Name";


    private NotificationManager mManager;
    Context base;
    Trip trip;

    public NotificationHelper(Context base, Trip trip) {
        super(base);
        this.base = base;
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
        Intent intent = new Intent(base, AddNotesActivity.class);
        //intent.putExtra("TripID", (Serializable) trip.getTripID());

        PendingIntent pendingIntent = PendingIntent.getActivity(this, trip.getTripID(), intent, 0);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(trip.getTripName() + "")
                .setContentText(trip.getDestination())
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setAutoCancel(false)
                .setOngoing(true)
                .setSound(sound).setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setLights(0xff00ff00, 300, 100)
                .setContentIntent(pendingIntent)
                .addAction(R.id.icon_only,"start",pendingIntent)
                .addAction(R.id.icon_only,"cancle",pendingIntent);


    }

    public void sendNotification() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(android.R.drawable.ic_dialog_alert);
        Intent intent = new Intent(base, UpcomingFragment.class);
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
