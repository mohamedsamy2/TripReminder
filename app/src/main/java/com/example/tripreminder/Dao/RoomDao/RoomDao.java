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

    @Query("SELECT * FROM trips_table WHERE userID = :id AND status = 'Upcoming'")
    Single<List<Trip>> getUpcomingTripsByUser(String id);

    @Query("SELECT * FROM trips_table WHERE userID = :id AND (status = 'Done' or status = 'Cancelled')")
    Single<List<Trip>> getPastTripsByUser(String id);

    @Delete
    Completable deleteTrip(Trip trip);

    @Query("UPDATE trips_table SET notes=:notes WHERE tripID = :id")
    Completable update(String notes, int id);

    @Query("UPDATE trips_table SET status='Done' WHERE tripID = :id")
    Completable tripStarted(int id);

    @Query("UPDATE trips_table SET status='Cancelled' WHERE tripID = :id")
    Completable tripCancelled(int id);

}
