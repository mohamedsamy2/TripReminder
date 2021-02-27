package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.model.Trip;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddNotesActivity extends AppCompatActivity {

    public static String TAG="main";
    List<String> notes;
    RecyclerView recyclerView;
    NotesAdapter myAdapter;
    Trip trip;
    Button btn;
    TextView txt;
    Button btnSave;
    RoomDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        btn=findViewById(R.id.add_note_btn);
        txt=findViewById(R.id.note_txt);
        btnSave=findViewById(R.id.save_notes);
        recyclerView=findViewById(R.id.notes_recycler);
        Intent intent=getIntent();
        trip=new Gson().fromJson(intent.getStringExtra("trip"),Trip.class);
        notes=trip.getNotes();

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager LayoutManager=new LinearLayoutManager(this);
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        myAdapter=new NotesAdapter(AddNotesActivity.this,notes);
        recyclerView.setAdapter(myAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=txt.getText().toString();
                if(text.length()>0){
                    notes.add(text);
                    txt.setText("");
                    myAdapter.notifyDataSetChanged();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = RoomDatabase.getInstance(AddNotesActivity.this);
                database.roomTripDao().update(new Gson().toJson(notes) ,trip.getTripID()).subscribeOn(Schedulers.io())
                       .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }
                    @Override
                    public void onComplete() {

                    }
                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });


            }
        });

    }
}



