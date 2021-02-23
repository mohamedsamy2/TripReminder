package com.example.tripreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.helper.AlarmHelper;
import com.example.tripreminder.model.Trip;
import com.example.tripreminder.reciever.AlarmReciever;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddTripActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener
{
    static final int PLACE_AUTOCOMPLETE_START_PLACE_REQUEST_CODE = 1;
    static final int PLACE_AUTOCOMPLETE_END_PLACE_REQUEST_CODE = 2;
    RoomDatabase database;
    ImageButton datePickerBtn;
    ImageButton timePickerBtn;
    EditText startPoint;
    EditText endPoint;
    EditText tripName;
    TextView timePicked;
    TextView datePicked;
    Spinner tripTypes;
    Button addTrip;
    AlarmHelper alarmHelper;

    static String TAG="main";

    Intent intent;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent intentgoing;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);


        alarmHelper=new AlarmHelper(this);
        initViews();
        initDropDownList();
        initGooglePlaces();

        datePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DatePicker"); //show calendar dialog
            }
        });

        timePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "TimePicker"); //show clock dialog
            }
        });

        startPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddTripActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_START_PLACE_REQUEST_CODE);
            }
        });

        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(AddTripActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_END_PLACE_REQUEST_CODE);
            }
        });

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tripName.getText().toString().isEmpty() || startPoint.getText().toString().isEmpty()
                        || endPoint.getText().toString().isEmpty() || timePicked.getText().toString().isEmpty()
                        || datePicked.getText().toString().isEmpty()) {
                    Toast.makeText(AddTripActivity.this, "Please fill all fields to add trip", Toast.LENGTH_LONG).show();
                } else {
                    database = RoomDatabase.getInstance(AddTripActivity.this);
                    Trip trip = new Trip();
                    trip.setUserID(FirebaseAuth.getInstance().getUid());
                    trip.setTripName(tripName.getText().toString());
                    trip.setSource(startPoint.getText().toString());
                    trip.setDestination(endPoint.getText().toString());
                    trip.setDate(datePicked.getText().toString());
                    trip.setTime(timePicked.getText().toString());
                    trip.setStatus("Upcoming");
                    trip.setType(tripTypes.getSelectedItem().toString());
                    trip.setNotes(new ArrayList<>());
                    trip.setTripID((int) Calendar.getInstance().getTimeInMillis());


                    database.roomTripDao().insertTrip(trip).subscribeOn(Schedulers.computation())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onComplete() {


                                    alarmHelper.addAlarm(trip);

                                    finish();
                                    ;

                                }

                                @Override
                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                                }
                            });
                }
            }
        });

    }

    private void initViews() {
        tripName = findViewById(R.id.tripNameText);
        datePickerBtn = findViewById(R.id.datePickerButton);
        timePickerBtn = findViewById(R.id.timePickerButton);
        startPoint = findViewById(R.id.startPointEditText);
        endPoint = findViewById(R.id.endPointEditText);
        tripTypes = findViewById(R.id.tripType);


        timePicked = findViewById(R.id.timePickerTextView);
        datePicked = findViewById(R.id.datePickerTextView);
        addTrip = findViewById(R.id.addTripBtn);
    }

    private void initGooglePlaces() {
        Places.initialize(getApplicationContext(), "AIzaSyAgqIxtaivfYwAkTXbID2Ew1hra3Exy7Rg");
        startPoint.setFocusable(false);
        endPoint.setFocusable(false);
    }

    private void initDropDownList() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.trip_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripTypes.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if(requestCode== PLACE_AUTOCOMPLETE_START_PLACE_REQUEST_CODE)
                startPoint.setText(place.getAddress());
            else if (requestCode== PLACE_AUTOCOMPLETE_END_PLACE_REQUEST_CODE)
                endPoint.setText(place.getAddress());
        }
        else {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(hourOfDay <= Calendar.getInstance().get(Calendar.HOUR_OF_DAY)  && minute <= Calendar.getInstance().get(Calendar.MINUTE)) {
            Toast.makeText(AddTripActivity.this, "This time has already passed, please choose a different time", Toast.LENGTH_LONG).show();
        }
        else {
            String AM_PM = " AM";
            String mm_precede = "";
            if (hourOfDay >= 12) {
                AM_PM = " PM";
                if (hourOfDay >= 13 && hourOfDay < 24) {
                    hourOfDay -= 12;
                } else {
                    hourOfDay = 12;
                }
            } else if (hourOfDay == 0) {
                hourOfDay = 12;
            }
            if (minute < 10) {
                mm_precede = "0";
            }
            timePicked.setText("" + hourOfDay + ":" + mm_precede + minute + AM_PM);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datePicked.setText(dayOfMonth +"/"+(month+1)+"/"+year);
    }




}