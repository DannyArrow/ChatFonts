package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * Created by Danilo on 1/4/18.
 */

public class Mailadapter extends RecyclerView.Adapter<Mailadapter.ViewHolder> {
    HashMap<String, ArrayList<Object>> hashMap;
    ArrayList<Mailmessage> datamail;

    public Mailadapter(ArrayList<Mailmessage> data, HashMap<String, ArrayList<Object>> hashMap) {
        datamail = data;
        this.hashMap = hashMap;
    }

    @Override
    public Mailadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.messagerow, parent, false);
        ViewHolder holder = new ViewHolder(contactView);
        return holder;
    }

    @Override
    public void onBindViewHolder(Mailadapter.ViewHolder holder, int position) {
            Mailmessage mailmessage = datamail.get(position);

        ArrayList<Object> list =    hashMap.get(mailmessage.getUsertalking());
        if(list != null) {
            String count = String.valueOf(list.size());

            holder.constraintLayout.setBackgroundResource(R.color.colorAccent);
            holder.unreadcount.setText("unread message "+ count);
        }

            holder.messagetext.setText(mailmessage.getMessage());
            holder.textname.setText(mailmessage.getUsername());
            holder.time.setText(mailmessage.getTime());
        Glide.with(holder.imageView.getContext()).load(mailmessage.getProfilepicture()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return datamail.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textname;
        public TextView messagetext;
        public TextView time;
        public TextView unreadcount;
        public ConstraintLayout constraintLayout;

        public ViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            textname = (TextView) itemView.findViewById(R.id.textView2);
            messagetext = (TextView) itemView.findViewById(R.id.tv_long);
            time = (TextView) itemView.findViewById(R.id.textView4);
            unreadcount = (TextView) itemView.findViewById(R.id.textView7);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.background);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id", datamail.get(getAdapterPosition()).getUsertalking());
                    bundle.putString("name", datamail.get(getAdapterPosition()).getUsername());
                    bundle.putString("url", datamail.get(getAdapterPosition()).getProfilepicture());
                    bundle.putString("userkey", datamail.get(getAdapterPosition()).getUsertalking());


                    Toasty.normal(view.getContext(),"positin clicked == " + getAdapterPosition(), Toast.LENGTH_LONG).show();

                    Privatemessage privatemessage = new Privatemessage();
                    FragmentTransaction fragmentTransaction = ((AppCompatActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
                    privatemessage.setArguments(bundle);
                    fragmentTransaction.replace(R.id.main,privatemessage);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                }
            });
        }


    }

}
