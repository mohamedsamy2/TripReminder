package com.example.tripreminder.Dao.RoomDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tripreminder.model.Trip;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

@Dao
public interface RoomDao {

    @Insert
    Completable insertTrip(Trip trip);

    @Query("SELECT * FROM trips_table WHERE userID = :id")
    Single<List<Trip>> getTripsByUser(String id);

    @Delete
    Completable deleteTrip(Trip trip);
}