package com.example.tripreminder.Dao.RoomDao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

    @Query("UPDATE trips_table SET notes=:notes WHERE tripID = :id")
    Completable update(String notes, int id);

    @Query("UPDATE trips_table SET status=:status WHERE tripID = :id")
    Completable updateStatus(String status, int id);


}
