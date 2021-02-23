package com.example.tripreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EditTripActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{


    RoomDatabase database;
    ImageButton datePickerBtn;
    ImageButton timePickerBtn;
    EditText startPoint;
    EditText endPoint;
    EditText tripName;
    TextView timePicked;
    TextView datePicked;
    Spinner tripTypes;
    Button saveTrip;
    Trip trip;
    AlarmHelper alarmHelper;
    int year,month,day;
    private static final String TAG = "EditTripActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_trip);
        alarmHelper=new AlarmHelper(this);
        Intent intent = getIntent();
        trip = intent.getParcelableExtra("trip");
        initViews();
        initDropDownList();
        initGooglePlaces();
        tripName.setText(trip.getTripName());
        startPoint.setText(trip.getSource());
        endPoint.setText(trip.getDestination());
        timePicked.setText(trip.getTime());
        datePicked.setText(trip.getDate());

        String[] date = trip.getDate().split("/");
        day = Integer.valueOf(date[0]);
        month = Integer.valueOf(date[1]) - 1;
        year = Integer.valueOf(date[2]);
        Log.i(TAG, "onCreate: ");
        Log.i(TAG, "onCreate: date " + day + " " + month + " " + year);
        Log.i(TAG, "onCreate: " + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + " " + Calendar.getInstance().get(Calendar.MONTH) + " " + Calendar.getInstance().get(Calendar.YEAR));
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

                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(EditTripActivity.this);
                intent.putExtra("button","start");
                startActivityForResult(intent, 100);
            }
        });

        endPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fieldList = Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG, Place.Field.NAME);
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fieldList).build(EditTripActivity.this);
                intent.putExtra("button","end");
                startActivityForResult(intent, 100);
            }
        });

        saveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tripName.getText().toString().isEmpty() || startPoint.getText().toString().isEmpty()
                        || endPoint.getText().toString().isEmpty() || timePicked.getText().toString().isEmpty()
                        || datePicked.getText().toString().isEmpty()) {
                    Toast.makeText(EditTripActivity.this, "Please fill all fields to save trip", Toast.LENGTH_LONG).show();
                } else {
                    Log.i(TAG, "onClick: " + trip.getTripID() + trip.getTripName());
                    trip.setTripName(tripName.getText().toString());
                    trip.setSource(startPoint.getText().toString());
                    trip.setDestination(endPoint.getText().toString());
                    trip.setDate(datePicked.getText().toString());
                    trip.setTime(timePicked.getText().toString());
                    trip.setType(tripTypes.getSelectedItem().toString());
                    database = RoomDatabase.getInstance(v.getContext());
                    database.roomTripDao().EditTrip(trip).subscribeOn(Schedulers.computation())
                            .subscribe(new CompletableObserver() {
                                @Override
                                public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                }

                                @Override
                                public void onComplete() {

                                    alarmHelper.addAlarm(trip);
                                    finish();
                                }

                                @Override
                                public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                                }
                            });

                    if (tripTypes.getSelectedItem().toString().equals("Round trip"))
                    {
                        database = RoomDatabase.getInstance(EditTripActivity.this);
                        Trip tripBack = new Trip();
                        tripBack.setUserID(FirebaseAuth.getInstance().getUid());
                        tripBack.setTripName(tripName.getText().toString());
                        tripBack.setSource(endPoint.getText().toString());
                        tripBack.setDestination(startPoint.getText().toString());
                        tripBack.setStatus("Upcoming");
                        tripBack.setType(tripTypes.getSelectedItem().toString());
                        tripBack.setNotes(new ArrayList<>());

                        database.roomTripDao().insertTrip(tripBack).subscribeOn(Schedulers.computation())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onComplete() {
                                        finish();
                                    }

                                    @Override
                                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                                    }
                                });
                    }
                }
            }
        });
    }

    private void initViews() {
        tripName = findViewById(R.id.EditTripName);
        datePickerBtn = findViewById(R.id.EditDatePickerBtn);
        timePickerBtn = findViewById(R.id.EditTimePickerBtn);
        startPoint = findViewById(R.id.EditStartPoint);
        endPoint = findViewById(R.id.EditEndPoint);
        tripTypes = findViewById(R.id.EditTripTypeSpinner);
        timePicked = findViewById(R.id.EditTimePickerTextView);
        datePicked = findViewById(R.id.EditDatePickerTextView);
        saveTrip = findViewById(R.id.SaveTripBtn);
    }

    private void initDropDownList() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.trip_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tripTypes.setAdapter(adapter);
        tripTypes.setSelection(adapter.getPosition(trip.getType()));
    }

    private void initGooglePlaces() {
        Places.initialize(getApplicationContext(), "AIzaSyAgqIxtaivfYwAkTXbID2Ew1hra3Exy7Rg");
        startPoint.setFocusable(false);
        endPoint.setFocusable(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            Place place = Autocomplete.getPlaceFromIntent(data);
            if(data.getStringExtra("button").equals("start"))
                startPoint.setText(place.getAddress());
            else if (data.getStringExtra("button").equals("end"))
                startPoint.setText(place.getAddress());
        }
        else {
            Status status = Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();

        if ( (day == calendar.get(Calendar.DAY_OF_MONTH) && month == calendar.get(Calendar.MONTH) && year == calendar.get(Calendar.YEAR))
                &&
                hourOfDay <= calendar.get(Calendar.HOUR_OF_DAY)  && minute <= calendar.get(Calendar.MINUTE)) {
            Toast.makeText(EditTripActivity.this, "This time has already passed, please choose a different time", Toast.LENGTH_LONG).show();
        } else {
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
        this.year = year;
        this.month = month;
        this.day = dayOfMonth;
        datePicked.setText(dayOfMonth +"/"+(month+1)+"/"+year);
        timePicked.setText("");
    }

}