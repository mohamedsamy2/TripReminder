package com.example.tripreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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



        holder.nameTxt.setText(list.get(position));



        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(context,"deleted",Toast.LENGTH_LONG).show();

                onClickItem.onItemDelete(position);

            }
        });

        holder.upDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(context,"deleted",Toast.LENGTH_LONG).show();

                if(flage) {
                    holder.upDown.setImageResource(R.drawable.down);
                    holder.dateImag.setVisibility(View.GONE);
                    holder.timeImag.setVisibility(View.GONE);
                    holder.fromImag.setVisibility(View.GONE);
                    holder.toImag.setVisibility(View.GONE);
                    holder.statusImag.setVisibility(View.GONE);
                    holder.fromTxt.setVisibility(View.GONE);
                    holder.toTxt.setVisibility(View.GONE);
                    holder.statusTxt.setVisibility(View.GONE);
                    holder.dateTxt.setVisibility(View.GONE);
                    holder.timeTxt.setVisibility(View.GONE);
                }else{
                    holder.upDown.setImageResource(R.drawable.up);

                    holder.dateImag.setVisibility(View.VISIBLE);
                    holder.timeImag.setVisibility(View.VISIBLE);
                    holder.fromImag.setVisibility(View.VISIBLE);
                    holder.toImag.setVisibility(View.VISIBLE);
                    holder.statusImag.setVisibility(View.VISIBLE);
                    holder.fromTxt.setVisibility(View.VISIBLE);
                    holder.toTxt.setVisibility(View.VISIBLE);
                    holder.statusTxt.setVisibility(View.VISIBLE);
                    holder.dateTxt.setVisibility(View.VISIBLE);
                    holder.timeTxt.setVisibility(View.VISIBLE);



                }
                flage=!flage;

            }
        });

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
    public interface OnClickItem{

        void onItemDelete(int position);

    }
}
