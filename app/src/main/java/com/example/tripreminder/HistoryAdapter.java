package com.example.tripreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.model.Trip;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{


    Context context;
    List<Trip> list;
    public boolean flage=false;
    OnItemClickListener onItemClickLisener;

    public void setOnItemClickLisener(OnItemClickListener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }



    public HistoryAdapter(Context context, List<Trip> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.history_row,parent,false);
        HistoryAdapter.ViewHolder viewHolder=new HistoryAdapter.ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.upcomingTo.setText(list.get(position).getDestination());
        holder.upcomingFrom.setText(list.get(position).getSource());
        holder.txt_status.setText(list.get(position).getStatus());
        holder.upcomingTripName.setText(list.get(position).getTripName());
        holder.upcomingDateText.setText(list.get(position).getDate());
        holder.upcomingTimeText.setText(list.get(position).getTime());
        holder.viewNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickLisener.onViewNotesClickListener(position);
            }
        });
        holder.deletelTripBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickLisener.onDeleteClickLisener(list.get(position));
            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setList(List<Trip> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    class ViewHolder extends RecyclerView.ViewHolder{



        Button viewNotesBtn;
        TextView upcomingDateText, upcomingTimeText, upcomingTripName, upcomingFrom, upcomingTo, txt_status;
        ImageButton deletelTripBtn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNotesBtn=itemView.findViewById(R.id.viewNotesBtn);
            upcomingDateText=itemView.findViewById(R.id.upcomingDateText);
            upcomingTimeText=itemView.findViewById(R.id.upcomingTimeText);
            upcomingTripName=itemView.findViewById(R.id.upcomingTripName);
            upcomingFrom=itemView.findViewById(R.id.upcomingFrom);
            upcomingTo=itemView.findViewById(R.id.upcomingTo);
            txt_status=itemView.findViewById(R.id.txt_status);
            deletelTripBtn=itemView.findViewById(R.id.deletelTripBtn);




        }





    }
    public interface OnItemClickListener{

        void onDeleteClickLisener(Trip trip);
        void onViewNotesClickListener(int posation);

    }
}
