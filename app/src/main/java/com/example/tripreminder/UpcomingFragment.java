package com.example.tripreminder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tripreminder.R;

import java.util.ArrayList;
import java.util.List;

public class UpcomingFragment extends Fragment {
    private static final String TAG = "UpcomingFragment";
    RecyclerView recyclerView;
    UpcomingAdapter upcomingAdapter;
    List<String> upcomingList = new ArrayList<>();



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        upcomingList.add("test");
        upcomingList.add("test");
        upcomingList.add("test");
        upcomingList.add("test");
        upcomingList.add("test");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentActivity upcomingFragment = getActivity();
        recyclerView = view.findViewById(R.id.upcomingRecycler);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager LayoutManager = new LinearLayoutManager(upcomingFragment);
        LayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(LayoutManager);
        upcomingAdapter = new UpcomingAdapter(upcomingFragment,upcomingList);
        recyclerView.setAdapter(upcomingAdapter);
        Log.i(TAG, "onViewCreated: DONE");


    }
}