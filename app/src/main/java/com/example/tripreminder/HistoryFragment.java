package com.example.tripreminder;

import android.database.Observable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment implements HistoryAdapter.OnClickItem {

    RecyclerView recyclerView;
    HistoryAdapter myAdapter;

    List<String> history=new ArrayList<>();
    FragmentActivity historyFragment=getActivity();




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        history.add("mostafa");
        history.add("ahmed");
        history.add("alaa");
        history.add("mohamed");



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


        recyclerView=view.findViewById(R.id.history_recycler);
        recyclerView.setHasFixedSize(true);


        LinearLayoutManager LayoutManager=new LinearLayoutManager(historyFragment);
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        myAdapter=new HistoryAdapter(historyFragment,history,this);
        recyclerView.setAdapter(myAdapter);



    }



    @Override
    public void onItemDelete(int position) {
        history.remove(position);
        myAdapter.notifyDataSetChanged();


    }
}