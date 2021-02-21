package com.example.tripreminder;


import android.content.Context;
import android.content.Intent;
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
import com.google.gson.Gson;
import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder>{
    private static final String TAG = "UpcomingAdapter";

    Context context;
    List<Trip> list;
    RoomDatabase database;

    OnItemClickLisener onItemClickLisener;

    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

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

                onItemClickLisener.onStartClickLisener(position,holder.toText.getText().toString().replace(" ","+"));
            }
        });


        holder.cancelTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemClickLisener.onCancleClickLisener(list.get(position));
            }
        });

        holder.addNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent  intent=new Intent(holder.itemView.getContext(),AddNotesActivity.class);
                intent.putExtra("trip",new Gson().toJson(list.get(position)));
                holder.itemView.getContext().startActivity(intent);

            }
        });
        holder.viewNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickLisener.onViewNotesClickListener(position);
            }
        });

        holder.editTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Trip trip = list.get(position);
                Intent intent = new Intent(holder.itemView.getContext(), EditTripActivity.class);
                intent.putExtra("trip", trip);
                holder.itemView.getContext().startActivity(intent);

            }
        });


        Log.i(TAG, "onBindViewHolder: ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickLisener{
        void onCancleClickLisener(Trip trip);
        void onStartClickLisener(int positon, String to);
        void onViewNotesClickListener(int posation);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView fromText;
        public TextView toText;
        public TextView timeText;
        public TextView dateText;
        public TextView tripNameTxt;
        public ImageButton cancelTripBtn, editTripBtn, viewNoteBtn;
        public Button startTripBtn, addNoteBtn;
        public ConstraintLayout constraintLayout;
        public View layout;
        public Button addNotes;


        public ViewHolder(View v) {
            super(v);
            fromText = v.findViewById(R.id.upcomingFrom);
            toText = v.findViewById(R.id.upcomingTo);
            timeText = v.findViewById(R.id.upcomingTime);
            dateText = v.findViewById(R.id.upcomingDateText);
            tripNameTxt = v.findViewById(R.id.upcomingTripName);
            cancelTripBtn = v.findViewById(R.id.cancelTripBtn);
            startTripBtn = v.findViewById(R.id.startTripBtn);
            editTripBtn=v.findViewById(R.id.editTripBtn);
            viewNoteBtn=v.findViewById(R.id.viewNoteBtn);
            addNoteBtn=v.findViewById(R.id.addNotesBtn);
            constraintLayout = v.findViewById(R.id.upcomingRow);
            addNotes=v.findViewById(R.id.addNotesBtn);


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




