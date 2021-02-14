package com.example.tripreminder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.Database.Room.RoomDatabase;
import com.example.tripreminder.model.Trip;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder>{
    private static final String TAG = "UpcomingAdapter";

    Context context;
    List<Trip> list;
    RoomDatabase database;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.upcoming_row,parent,false);
        UpcomingAdapter.ViewHolder viewHolder = new UpcomingAdapter.ViewHolder(v);
        Log.i(TAG, "onCreateViewHolder: ");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tripNameTxt.setText(list.get(position).getTripName());
        holder.fromText.setText(list.get(position).getSource());
        holder.toText.setText(list.get(position).getDestination());
        holder.timeText.setText(list.get(position).getTime());
        holder.dateText.setText(list.get(position).getDate());

        holder.startTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        Uri gmmIntentUri = Uri.parse("http://maps.google.com/maps?daddr="+holder.toText.getText().toString().replace(" ","+"));
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);

            }
        });


        holder.cancelTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = RoomDatabase.getInstance(holder.itemView.getContext());
                database.roomTripDao().deleteTrip(list.get(position)).subscribeOn(Schedulers.computation())
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
        });



        Log.i(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fromText;
        public TextView toText;
        public TextView timeText;
        public TextView dateText;
        public TextView tripNameTxt;
        public ImageButton cancelTripBtn;
        public Button startTripBtn;
        public ConstraintLayout constraintLayout;
        public View layout;

        public ViewHolder(View v)
        {
            super(v);
            fromText = v.findViewById(R.id.upcomingFrom);
            toText = v.findViewById(R.id.upcomingTo);
            timeText = v.findViewById(R.id.upcomingTime);
            dateText = v.findViewById(R.id.upcomingDateText);
            tripNameTxt = v.findViewById(R.id.upcomingTripName);
            cancelTripBtn = v.findViewById(R.id.cancelTripBtn);
            startTripBtn = v.findViewById(R.id.startTripBtn);
            constraintLayout = v.findViewById(R.id.upcomingRow);
            Log.i(TAG, "ViewHolder: ");
        }

    }

    public UpcomingAdapter(Context _context, List<Trip> list)
    {
        this.context = _context;
        this.list = list;
        Log.i(TAG, "UpcomingAdapter: ");
    }

    public void setList(List<Trip> list) {
        this.list = list;
    }
}
