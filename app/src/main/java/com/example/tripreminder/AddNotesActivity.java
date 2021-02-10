package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class AddNotesActivity extends AppCompatActivity {

    public static String TAG="main";
    List<String> notes= new ArrayList<>();
    RecyclerView recyclerView;
    NotesAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        notes.add("mostafa ahmed");
        notes.add("mostafa ahmed");
        notes.add("mostafa ahmed");
        notes.add("mostafa ahmed");
        notes.add("mostafa ahmed");
        notes.add("mostafa ahmed");
        notes.add("mostafa ahmed");

        recyclerView=findViewById(R.id.notes_recycler);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager LayoutManager=new LinearLayoutManager(this);
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        myAdapter=new NotesAdapter(AddNotesActivity.this,notes);
        recyclerView.setAdapter(myAdapter);



    }
}



/*
public class MainActivity extends AppCompatActivity {

    public static String TAG="main";
    ArrayList<Player> players;
    RecyclerView recyclerView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView=findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager LayoutManager=new LinearLayoutManager(this);
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);


        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

                myAdapter=new MyAdapter(MainActivity.this,players);
                recyclerView.setAdapter(myAdapter);


                Log.i(TAG, "handleMessage: ");


            }
        };

 */