package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;

import android.os.Bundle;
import android.view.View;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tripreminder.Dao.firebaseDao.FirebaseTripsDao;
import com.example.tripreminder.Dao.firebaseDao.FirebaseUserDao;
import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.Database.firebase.DataHolder;
import com.example.tripreminder.Database.firebase.FireBaseDB;
import com.example.tripreminder.helper.AlarmHelper;
import com.example.tripreminder.model.FirebaseTrip;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {

    private static final String TAG ="MainActivity";
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    FloatingActionButton fab;
    TextView txtUserName,txtEmail;
    AlarmHelper alarmHelper;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private DatabaseReference rootRef;

    FragmentManager mgr;
    FragmentTransaction trns;
    Toolbar toolbar;
    SharedPreferences preferences;
    List<Trip> trips=new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarmHelper=new AlarmHelper(this);

        mAuth=FirebaseAuth.getInstance();
        rootRef= FirebaseDatabase.getInstance().getReference();
        currentUser=mAuth.getCurrentUser();
        //////////////////////////////////////////////////////////////////////
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            //when screen is black but not locked it will light-up
            setShowWhenLocked(true);
            setTurnScreenOn(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)) {
                checkDrawOverAppsPermissionsDialog();
            }
        }
        runBackgroundPermissions();
///////////////////////////////////////////////////////////////////////////////////////



        setUpToolbar();
        navigationView = findViewById(R.id.navigation_menu);
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbar);
        progressDialog=new ProgressDialog(MainActivity.this);

        txtUserName=navigationView.getHeaderView(0).findViewById(R.id.txtName);
        txtEmail=navigationView.getHeaderView(0).findViewById(R.id.txtUserEmail);
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

                        fab.setVisibility(View.VISIBLE);
                        getSupportActionBar().setTitle("Upcoming trips");
                        UpcomingFragment upcomingFragment = new UpcomingFragment();
                        mgr = getSupportFragmentManager();
                        trns = mgr.beginTransaction();
                        trns.replace(R.id.fragment_container_view, upcomingFragment,"currentFragment").commit();
                        //switch fragment to upcoming
                        break;

                    case R.id.nav_history:

                        fab.setVisibility(View.GONE);
                        getSupportActionBar().setTitle("Trips history");
                        HistoryFragment f = new HistoryFragment();
                        mgr = getSupportFragmentManager();
                        trns = mgr.beginTransaction();
                        trns.replace(R.id.fragment_container_view, f, "currentFragment").commit();
                        //switch fragment to history
                        break;

                    case R.id.nav_sync:

                        syncWithFirebase();
                        break;

                    case R.id.historyMap:

                        fab.setVisibility(View.GONE);
                        getSupportActionBar().setTitle("Past trips map");
                        MapsFragment mapsFragment = new MapsFragment();
                        mgr = getSupportFragmentManager();
                        trns = mgr.beginTransaction();
                        trns.replace(R.id.fragment_container_view, mapsFragment, "currentFragment").commit();
                        break;

                    case R.id.nav_logout:

                        logout();
                        break;

                }
                drawerLayout.closeDrawers();
                return false;
            }


        });



getDataFromSharedPerefrence();
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
                    //txtUserName.setText(databaseUser.getUserName());
                    //txtEmail.setText(databaseUser.getEmail());
                    //saveDataInSharedPerefrence(databaseUser.getUserName(),databaseUser.getEmail());

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
    private void syncWithFirebase() {
         getTripsFromRoom();
        //Log.e(TAG,""+tripList.get(0));

        //Log.e(TAG,""+firebaseTrips.get(0));

    }

    private List<FirebaseTrip> convertRoomListToFirebaseList(List<Trip> tripList) {
        List<FirebaseTrip> firebaseTrips=new ArrayList<>();
        for (int i=0;i<tripList.size();i++){
            Trip trip=tripList.get(i);
            firebaseTrips.add(new FirebaseTrip(trip.getTripID(),trip.getTripName(),trip.getUserID(),trip.getSource(),new Gson().toJson(trip.getNotes()),
                    trip.getDestination(),trip.getDate(),trip.getTime(),trip.getStatus(),trip.getType()));
        }
        Log.e(TAG,""+firebaseTrips.size());
        Log.e(TAG,""+tripList.size());
        return firebaseTrips;
    }

    private void saveTripsOnFirebase(List<FirebaseTrip> firebaseTrips) {
        if (firebaseTrips==null && firebaseTrips.size()==0){
            Toast.makeText(this, "There is no data to sync", Toast.LENGTH_SHORT).show();
        }else{
            progressDialog.setTitle(getString(R.string.sync_data));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            FireBaseDB.getUsers().child(mAuth.getCurrentUser().getUid()).child(FireBaseDB.USER_Trip_REF).setValue("");
            FirebaseTripsDao.addUserTrips(firebaseTrips, mAuth.getCurrentUser().getUid(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, getString(R.string.sync_success), Toast.LENGTH_LONG).show();
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, getString(R.string.sync_faild), Toast.LENGTH_LONG).show();
                    }
                }
            });
            }



    }

    private void getTripsFromRoom() {

        RoomDatabase.getInstance(MainActivity.this).roomTripDao().getTripsByUser(FirebaseAuth.getInstance().getUid()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Trip>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: "+FirebaseAuth.getInstance().getUid());
            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> tripList) {
                trips=new ArrayList<>();
                trips=tripList;
                Log.e(TAG,""+trips.get(0).getTripName());
                List<FirebaseTrip>firebaseTrips=convertRoomListToFirebaseList(trips);
                Log.e(TAG,""+trips.get(0).getTripName());
                saveTripsOnFirebase(firebaseTrips);
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });
        //Log.e(TAG,""+trips.get(0).getTripName());
        //return trips;
    }

    private void saveDataInSharedPerefrence(String name,String email) {
        preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("Name",name);
        editor.putString("Email",email);
        Log.e("preferences",name);
        Log.e("preferences",email);
        editor.commit();
    }
    public void getDataFromSharedPerefrence(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        txtUserName.setText(preferences.getString("Name","default"));
        txtEmail.setText(preferences.getString("Email","default"));

    }
    public void logout(){
        //delete all trips


        RoomDatabase.getInstance(MainActivity.this).roomTripDao().getUpcomingTripsByUser(FirebaseAuth.getInstance().getUid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Trip>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                Log.i(TAG, "onSubscribe: "+FirebaseAuth.getInstance().getUid());

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {
                if (trips.size()!=0){

                    for(Trip trip:trips){
                        alarmHelper.cancelAlarm(trip);
                        Log.i(TAG, "onSuccess: "+trip.tripID);
                    }
                }
                deleteAllTripsFromRoom();
                FirebaseAuth.getInstance().signOut();
                DataHolder.dataBaseUser=null;
                DataHolder.authUser=null;
                sendToLoginActivity();
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Log.i(TAG, "onError: ");
            }
        });


    }

    private void deleteAllTripsFromRoom() {
        RoomDatabase.getInstance(MainActivity.this).roomTripDao().deleteAllRecords()
                .subscribeOn(Schedulers.io())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    //methods permissions
    private void checkDrawOverAppsPermissionsDialog(){
        new AlertDialog.Builder(this).setTitle("Permission request").setCancelable(false).setMessage("please allow Draw Over Apps permission to be able to use application properly")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawOverAppPermission();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                errorWarningForNotGivingDrawOverAppsPermissions();
            }
        }).show();
    }

    private void errorWarningForNotGivingDrawOverAppsPermissions(){
        new AlertDialog.Builder(this).setTitle("Warning").setCancelable(false).setMessage("Unfortunately the display over other apps permission" +
                " is not granted so the application might not behave properly \nTo enable this permission kindly restart the application" )
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                }).show();
    }

    public void runBackgroundPermissions() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (Build.BRAND.equalsIgnoreCase("xiaomi")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                startActivity(intent);
            } else if (Build.BRAND.equalsIgnoreCase("Honor") || Build.BRAND.equalsIgnoreCase("HUAWEI")) {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                startActivity(intent);
            }
        }
    }

    public void drawOverAppPermission (){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 80);
            }
        }
    }



}