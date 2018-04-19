package com.example.danilo.chat1;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Danilo on 3/14/18.
 */
public  class Pending_adapter extends RecyclerView.Adapter<Pending_adapter.ViewHolder>{

    @NonNull
    @Override
    public Pending_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.messagerow, parent, false);
        ViewHolder vh = new ViewHolder(contactView);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull Pending_adapter.ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt;
        public ViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.textView11);
        }
    }
}