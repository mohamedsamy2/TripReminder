package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FloatingActionButton fab;
    FragmentManager mgr;
    FragmentTransaction trns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
        navigationView = findViewById(R.id.navigation_menu);
        fab = findViewById(R.id.fab);

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
                        UpcomingFragment upcomingFragment = new UpcomingFragment();
                        mgr = getSupportFragmentManager();
                        trns = mgr.beginTransaction();
                        trns.replace(R.id.fragment_container_view, upcomingFragment,"currentFragment").commit();
                        //switch fragment to upcoming
                        break;

                    case R.id.nav_history:
                        Toast.makeText(MainActivity.this,"HISTORY",Toast.LENGTH_LONG).show();
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
                }
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }
}