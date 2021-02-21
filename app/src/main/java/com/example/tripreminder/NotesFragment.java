package com.example.tripreminder;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class NotesFragment extends DialogFragment {

    RecyclerView recyclerView;
    NotesDialogAdapter notesDialogAdapter;
    Button btnClose;
    String notes;
    List<String>notesList=new ArrayList<>();
    public NotesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView=view.findViewById(R.id.recyclerNote);
        btnClose=view.findViewById(R.id.btnClose);
        notes=getArguments().getString("notes");
        if (!notes.equals("")){
            Type listType = new TypeToken<ArrayList<String>>() {}.getType();
            notesList=new Gson().fromJson(notes,listType);
            notesDialogAdapter=new NotesDialogAdapter(notesList,getContext());
            recyclerView.setAdapter(notesDialogAdapter);
        }
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}