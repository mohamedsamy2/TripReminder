package com.example.tripreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesDialogAdapter extends RecyclerView.Adapter<NotesDialogAdapter.ViewHolder>{
    List<String> notesList;
    Context context;

    public NotesDialogAdapter(List<String> notesList, Context context) {
        this.notesList = notesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.note_row_items,parent,false);
        NotesDialogAdapter.ViewHolder viewHolder=new NotesDialogAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.noteTxt.setText(notesList.get(position));
    }

    @Override
    public int getItemCount() {
        if (notesList.size() != 0)
            return notesList.size();
        else
            return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView noteTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTxt=itemView.findViewById(R.id.noteTxt);
        }
    }
}
