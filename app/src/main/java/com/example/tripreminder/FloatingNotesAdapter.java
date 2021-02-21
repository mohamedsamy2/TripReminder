package com.example.tripreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FloatingNotesAdapter extends RecyclerView.Adapter<FloatingNotesAdapter.FloatingNoteViewHolder>{
    Context context;
    private List<String> notes ;
    public FloatingNotesAdapter(Context context, List<String> notes) {
        this.context=context;
        this.notes= notes;
    }

    @NonNull
    @Override
    public FloatingNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v= inflater.inflate(R.layout.item_floating_note,parent,false);
        FloatingNotesAdapter.FloatingNoteViewHolder viewHolder = new FloatingNotesAdapter.FloatingNoteViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FloatingNoteViewHolder holder, int position) {
        holder.txtNote.setText(notes.get(position));
    }

    @Override
    public int getItemCount() {
        if (notes.size() != 0)
            return notes.size();
        else
            return 0;
    }
    public void setNotes(List<String> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public class FloatingNoteViewHolder extends RecyclerView.ViewHolder {
        public TextView txtNote ;
        public FloatingNoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNote = itemView.findViewById(R.id.txt_floating_note);

        }
    }
}
