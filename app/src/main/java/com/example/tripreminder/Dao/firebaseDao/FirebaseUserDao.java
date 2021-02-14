package com.example.tripreminder.Dao.firebaseDao;

import com.example.tripreminder.Database.firebase.FireBaseDB;
import com.example.tripreminder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ValueEventListener;

public class FirebaseUserDao {
    public static void addUser(User user, String currentUserId, OnCompleteListener<Void> onCompleteListener){
        FireBaseDB.getUsers().child(currentUserId)
                .setValue(user)
                .addOnCompleteListener(onCompleteListener);
    }
    public static void getUser(String userId, ValueEventListener valueEventListener){
        FireBaseDB.getUsers().child(userId)
                .addValueEventListener(valueEventListener);

    }

}
