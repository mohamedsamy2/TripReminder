package com.example.tripreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder>{


    Context context;
    List<String> list;

    public HistoryAdapter(Context context, List<String> list) {
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

    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{



        public ImageButton delete;
        public ImageButton upDown;
        public ImageView dateImag;
        public ImageView timeImag;
        public ImageView fromImag;
        public ImageView toImag;
        public ImageView nameImag;
        public ImageView statusImag;
        public TextView nameTxt;
        public TextView fromTxt;
        public TextView toTxt;
        public TextView statusTxt;
        public TextView dateTxt;
        public TextView timeTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            delete=itemView.findViewById(R.id.delete_history);
            upDown=itemView.findViewById(R.id.up_down_history);
            dateImag=itemView.findViewById(R.id.date_img);
            timeImag=itemView.findViewById(R.id.time_img);
            fromImag=itemView.findViewById(R.id.from_img);
            toImag=itemView.findViewById(R.id.to_img);
            nameImag=itemView.findViewById(R.id.name_img);
            statusImag=itemView.findViewById(R.id.status_img);
            nameTxt=itemView.findViewById(R.id.name_txt);
            fromTxt=itemView.findViewById(R.id.from_txt);
            toTxt=itemView.findViewById(R.id.to_txt);
            statusTxt=itemView.findViewById(R.id.status_txt);
            dateTxt=itemView.findViewById(R.id.date_txt);
            timeTxt=itemView.findViewById(R.id.time_txt);





        }





    }
}
