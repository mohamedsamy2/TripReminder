package com.example.tripreminder.Dao.firebaseDao;



import com.example.tripreminder.Database.firebase.FireBaseDB;
import com.example.tripreminder.model.FirebaseTrip;
import com.example.tripreminder.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class FirebaseTripsDao {
    public static void addUserTrips(List<FirebaseTrip> tripList, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        FireBaseDB.getUsers().child(currentUserId).child(FireBaseDB.USER_Trip_REF)
                .setValue(Arrays.asList(tripList))
                .addOnCompleteListener(onCompleteListener);
    }
    public static void getUserTrips(String currentUserId, ValueEventListener valueEventListener){
        FireBaseDB.getUsers().child(currentUserId).child(FireBaseDB.USER_Trip_REF).child(FireBaseDB.USER_Trip_branch)
                .addValueEventListener(valueEventListener);
    }
}
