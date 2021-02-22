package com.example.tripreminder;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Observable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.model.Trip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class HistoryFragment extends Fragment implements HistoryAdapter.OnItemClickListener {


    RecyclerView recyclerView;
    HistoryAdapter historyAdapter;

    List<Trip> historyList=new ArrayList<>();
    FragmentActivity historyFragment=getActivity();


    RoomDatabase database;

    private static final String TAG = "HistoryFragment";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = RoomDatabase.getInstance(getContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        database.roomTripDao().getPastTripsByUser(FirebaseAuth.getInstance().getUid()).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<Trip>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                Log.i(TAG, "onSubscribe: "+FirebaseAuth.getInstance().getUid());


            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull List<Trip> trips) {
                historyList = trips;
                historyAdapter.setList(trips);
                historyAdapter.notifyDataSetChanged();
                Log.i(TAG, "onSuccess: ");
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                Log.i(TAG, "onError: ");

            }
        });
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
        historyAdapter=new HistoryAdapter(historyFragment,historyList);
        recyclerView.setAdapter(historyAdapter);
        historyAdapter.setOnItemClickLisener(this);

    }




    @Override
    public void onDeleteClickLisener(Trip trip) {
        openDialog(getContext(),trip);
    }

    @Override
    public void onViewNotesClickListener(int posation) {
        if (historyList.get(posation).getNotes() !=null && !historyList.get(posation).getNotes().isEmpty()) {
            Bundle bundle = new Bundle();
            bundle.putString("notes", new Gson().toJson(historyList.get(posation).getNotes()));
            NotesFragment notesFragment = new NotesFragment();
            notesFragment.setArguments(bundle);
            notesFragment.show(getParentFragmentManager(),"note_dialog");
        }else{
            Toast.makeText(getContext(), "This item doesn't have any notes", Toast.LENGTH_LONG).show();
        }

    }
    private void openDialog(Context context, Trip trip) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Are you sure delete trip " + trip.getTripName() + " ? ");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database = RoomDatabase.getInstance(getContext());
                database.roomTripDao().deleteTrip(trip).subscribeOn(Schedulers.computation())
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
                //trip.setStatus("Cancelled");
                //upcomingAdapter.setList(upcomingList); //doesn't update recyclerview
                historyList.remove(trip);
                historyAdapter.notifyDataSetChanged();
            }
        });

        builder1.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder1.create();
        builder1.show();

    }
}