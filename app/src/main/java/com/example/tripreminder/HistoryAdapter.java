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
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{


    Context context;
    List<String> list;
    public boolean flage=false;
    OnClickItem onClickItem;


    public HistoryAdapter(Context context, List<String> list,OnClickItem onClickItem) {
        this.context = context;
        this.list = list;
        this.onClickItem=onClickItem;
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





    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{



        Button viewNotesBtn;
        TextView upcomingDateText, upcomingTimeText, upcomingTripName, upcomingFrom, upcomingTo, txt_status;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            viewNotesBtn=itemView.findViewById(R.id.viewNotesBtn);
            upcomingDateText=itemView.findViewById(R.id.upcomingDateText);
            upcomingTimeText=itemView.findViewById(R.id.upcomingTimeText);
            upcomingTripName=itemView.findViewById(R.id.upcomingTripName);
            upcomingFrom=itemView.findViewById(R.id.upcomingFrom);
            upcomingTo=itemView.findViewById(R.id.upcomingTo);
            txt_status=itemView.findViewById(R.id.txt_status);




        }





    }
    public interface OnClickItem{

        void onItemDelete(int position);

    }
}
