package com.example.danilo.chat1.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.danilo.chat1.R;
import com.example.danilo.chat1.Usernameinfo;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Danilo on 3/12/18.
 */

public class Friend_adapter extends RecyclerView.Adapter<Friend_adapter.Viewholder>{
     ArrayList<Usernameinfo> userlist;
    public Friend_adapter(ArrayList<Usernameinfo> userlist) {
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public Friend_adapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View contactView = inflater.inflate(R.layout.friend_row, parent, false);
        Viewholder viewHolder = new Viewholder(contactView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Friend_adapter.Viewholder holder, int position) {
        Usernameinfo usernameinfo = userlist.get(position);
        holder.name.setText(usernameinfo.getUsername());
        Glide.with(holder.circleImageView.getContext()).load(usernameinfo.getProfilepicture()).into(holder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        public TextView name;
        public CircleImageView circleImageView;
        public Viewholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textView10);
            circleImageView = itemView.findViewById(R.id.imageButton10);
        }
    }
}
