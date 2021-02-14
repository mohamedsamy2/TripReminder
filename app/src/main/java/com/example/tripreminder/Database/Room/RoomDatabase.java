package com.example.tripreminder.Database.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.TypeConverters;

import com.example.tripreminder.Dao.RoomDao.Converter;
import com.example.tripreminder.Dao.RoomDao.RoomDao;
import com.example.tripreminder.model.Trip;

@Database(entities = Trip.class, version = 1)
@TypeConverters(Converter.class)
    public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static RoomDatabase instance;
    public abstract RoomDao roomTripDao();

    public static RoomDatabase getInstance(Context context){
        if(instance == null)
        {
            synchronized (RoomDatabase.class){
                if(instance == null)
                {
                    instance = Room.databaseBuilder(context.getApplicationContext(), RoomDatabase.class, "Trips Database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
