package com.example.tripreminder.Database.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseDB {
    static FirebaseDatabase dp=FirebaseDatabase.getInstance();
    static String USER_REF="Users";
    public static DatabaseReference getUsers(){
        return dp.getReference().child(USER_REF);
    }
}
