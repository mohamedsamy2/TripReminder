package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.example.tripreminder.Dao.firebaseDao.FirebaseUserDao;
import com.example.tripreminder.Database.firebase.DataHolder;
import com.example.tripreminder.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FloatingActionButton fab;
    TextView txtUserName,txtEmail;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    FragmentManager mgr;
    FragmentTransaction trns;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        currentUser=mAuth.getCurrentUser();

        setUpToolbar();
        navigationView = findViewById(R.id.navigation_menu);
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);

        txtUserName=findViewById(R.id.txtName);
        txtEmail=findViewById(R.id.txtUserEmail);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTripActivity.class);
                startActivity(intent);
                
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_upcoming:
                        Toast.makeText(MainActivity.this,"UPCOMING",Toast.LENGTH_LONG).show();
                        getSupportActionBar().setTitle("Upcoming trips");
                        UpcomingFragment upcomingFragment = new UpcomingFragment();
                        mgr = getSupportFragmentManager();
                        trns = mgr.beginTransaction();
                        trns.replace(R.id.fragment_container_view, upcomingFragment,"currentFragment").commit();
                        //switch fragment to upcoming
                        break;

                    case R.id.nav_history:
                        Toast.makeText(MainActivity.this,"HISTORY",Toast.LENGTH_LONG).show();
                        getSupportActionBar().setTitle("Trips history");
                        HistoryFragment f = new HistoryFragment();
                        mgr = getSupportFragmentManager();
                        trns = mgr.beginTransaction();
                        trns.replace(R.id.fragment_container_view, f, "currentFragment").commit();
                        //switch fragment to history
                        break;

                    case R.id.nav_sync:
                        Toast.makeText(MainActivity.this,"SYNC",Toast.LENGTH_LONG).show();
                        //sync with firebase
                        break;
                    case R.id.nav_logout:
                        logout();
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }


        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getSupportFragmentManager().findFragmentByTag("currentFragment") == null)
        {
            UpcomingFragment upcomingFragment = new UpcomingFragment();
            mgr = getSupportFragmentManager();
            trns = mgr.beginTransaction();
            trns.add(R.id.fragment_container_view, upcomingFragment,"currentFragment").commit();
        }
        else
        {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("currentFragment");
            mgr = getSupportFragmentManager();
            trns = mgr.beginTransaction();
            trns.replace(R.id.fragment_container_view, fragment,"currentFragment").commit();

        }
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Upcoming trips");
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }
    @Override
    protected void onStart() {
        super.onStart();

        if(currentUser==null){
            sendToLoginActivity();
        }

        else{
            FirebaseUserDao.getUser(mAuth.getUid(), new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User databaseUser=dataSnapshot.getValue(User.class);
                    DataHolder.dataBaseUser=databaseUser;
                    DataHolder.authUser=mAuth.getCurrentUser();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            //getDataFromSharedPerefrence();

        }
    }
    private void sendToLoginActivity() {
        Intent intent= new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    public void getDataFromSharedPerefrence(){

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        txtUserName.setText(preferences.getString("Name","default"));
        txtEmail.setText(preferences.getString("Email","default"));

    }
    public void logout(){
        //delete all trips
        FirebaseAuth.getInstance().signOut();
        DataHolder.dataBaseUser=null;
        DataHolder.authUser=null;
        sendToLoginActivity();
    }


}