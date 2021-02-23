package com.example.tripreminder.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.reciever.AlarmReciever;
import com.google.gson.Gson;
import java.util.Calendar;
import java.util.Date;


public class AlarmHelper {
    static private String TAG="main";
    Context context;
    Intent intent;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public AlarmHelper(Context context) {

        this.context=context;
    }



    public void addAlarm(Trip trip){

        String date=trip.getDate();
        String time=trip.getTime();
        Log.i(TAG, "addAlarm: "+time);
        String[] arr=date.split("/");
        String[] timearr=time.split(" ")[0].split(":");
        Calendar calendar=Calendar.getInstance();

        if(time.split(" ")[1].equals("pm")){
            calendar.set(Calendar.YEAR,Integer.parseInt(arr[2]));
            calendar.set(Calendar.MONTH,Integer.parseInt(arr[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(arr[0]));
            calendar.set(Calendar.HOUR,Integer.parseInt(timearr[0])+12);
            calendar.set(Calendar.MINUTE,Integer.parseInt(timearr[1]));
            calendar.set(Calendar.SECOND,0);

        }else{

            calendar.set(Calendar.YEAR,Integer.parseInt(arr[2]));
            calendar.set(Calendar.MONTH,Integer.parseInt(arr[1])-1);
            calendar.set(Calendar.DAY_OF_MONTH,Integer.parseInt(arr[0]));
            calendar.set(Calendar.HOUR,Integer.parseInt(timearr[0]));
            calendar.set(Calendar.MINUTE,Integer.parseInt(timearr[1]));
            calendar.set(Calendar.SECOND,0);

        }


        Log.i(TAG, ">>>>>>>>>>addAlarmManager: "+calendar.getTime().toString());

        intent=new Intent(context, AlarmReciever.class);
        intent.putExtra("trip",new Gson().toJson(trip));
        alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Log.i(TAG, "addAlarm: "+trip.getTripID());

        pendingIntent= PendingIntent.getBroadcast(context,trip.getTripID(),intent,0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);

        }else{

            alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }


    }

    public void cancelAlarm(Trip trip){
        Log.i(TAG, ">>>>>>>>>>>>>>>>cancelAlarm: ");

        intent=new Intent(context, AlarmReciever.class);

        alarmManager=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pendingIntent= PendingIntent.getBroadcast(context,trip.getTripID(),intent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);

    }

}



