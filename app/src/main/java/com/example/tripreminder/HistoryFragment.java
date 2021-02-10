package com.example.tripreminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    HistoryAdapter myAdapter;

    List<String> history=new ArrayList<>();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history.add("mostaf");
        history.add("mostaf");
        history.add("mostaf");
        history.add("mostaf");

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentActivity historyFragment=getActivity();

        recyclerView=view.findViewById(R.id.history_recycler);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager LayoutManager=new LinearLayoutManager(historyFragment);
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        myAdapter=new HistoryAdapter(historyFragment,history);
        recyclerView.setAdapter(myAdapter);



    }
}