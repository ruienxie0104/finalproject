package com.example.bottomnavigationviewwithfragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ListAdapter extends FirestoreRecyclerAdapter<List,ListAdapter.ListViewHolder> {
    Context context;
    public ListAdapter(@NonNull FirestoreRecyclerOptions<List> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ListViewHolder holder, int position, @NonNull List list) {
        holder.titleTextView.setText(list.title);
        holder.UIDTextView.setText(list.title);
        holder.timestampTextView.setText(Utility.timestampToString(list.timestamp));


    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item,parent,false);
        return new ListViewHolder(view);
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView,UIDTextView,timestampTextView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.list_title_text_view);
            UIDTextView = itemView.findViewById(R.id.list_UID_text_view);
            timestampTextView = itemView.findViewById(R.id.list_timestamp_text_view);

        }
    }
}
