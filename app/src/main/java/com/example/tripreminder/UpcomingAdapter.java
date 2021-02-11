package com.example.tripreminder;

import android.content.Context;
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

import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder>{
    private static final String TAG = "UpcomingAdapter";
    Context context;
    List<String> list;

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

    public UpcomingAdapter(Context _context, List<String> list)
    {
        this.context = _context;
        this.list = list;
        Log.i(TAG, "UpcomingAdapter: ");
    }


}
