package com.example.danilo.chat1;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Danilo on 12/6/17.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private Context context;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    ArrayList<ChatMessage> values;
    ArrayList<String> key;
    private MyAdapter mAdapter;

    public MyAdapter(ArrayList<ChatMessage> myDataset, ArrayList<String> keylist,Context context1) {
        values = myDataset;
        key = keylist;
        context = context1;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.messagerow, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, int position) {

        final ChatMessage msg = values.get(position);
        holder.username.setText(msg.getMessageUser());
        holder.message.setText(" " + msg.getMessageText());
        holder.timee.setText(msg.getMessageTime());

        databaseReference.child("useraccount").child(msg.getUserid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
               String profile = usernameinfo.get("profilepicture");
                if (profile != null) {
                    Glide.with(holder.propic.getContext()).load(profile).into(holder.propic);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements  View.OnClickListener{

        public TextView message;
        public  TextView timee;
        public TextView username;
        public ImageView propic;
        public ViewHolder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.tv_long);
            timee = itemView.findViewById(R.id.textView4);
            username = itemView.findViewById(R.id.textView2);
            propic = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            propic.setOnClickListener(this);







        }

        @Override
        public void onClick(View view) {
            String id = firebaseUser.getUid();
           String idpos =  values.get(getAdapterPosition()).getUserid();

            if(idpos.equals(id)){
                final AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                LayoutInflater li = LayoutInflater.from(itemView.getContext());
                final View Dialogview = li.inflate(R.layout.editdelete, null);
                alert.setTitle("Edit comment");
                alert.setView(Dialogview);
                final AlertDialog ad = alert.show();

                final ImageButton delete = (ImageButton) Dialogview.findViewById(R.id.imageButton2);
                final ImageButton edit = (ImageButton) Dialogview.findViewById(R.id.imageButton4);
                final EditText text = (EditText) Dialogview.findViewById(R.id.editText3);
                text.setText(values.get(getAdapterPosition()).getMessageText());


                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        databaseReference.child("message").child(key.get((getAdapterPosition()))).removeValue();

                        values.remove(getAdapterPosition());

                        notifyDataSetChanged();




                        ad.dismiss();

                    }});


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        values.get(getAdapterPosition()).setMessageText(text.getText().toString());

                        databaseReference.child("message").child(key.get((getAdapterPosition()))).setValue(values.get(getAdapterPosition()));



                        notifyDataSetChanged();

                        ad.dismiss();

                    }
                });
            }

            if(!values.get(getAdapterPosition()).getUserid().equals(firebaseUser.getUid())){

                final AlertDialog.Builder alert = new AlertDialog.Builder((AppCompatActivity)context);
                LayoutInflater li = LayoutInflater.from(((AppCompatActivity)context));
                final View Dialogview = li.inflate(R.layout.profile, null);
                alert.setView(Dialogview);
                final AlertDialog ad = alert.show();

                final CircleImageView propic = (CircleImageView) Dialogview.findViewById(R.id.profile_image);
                final TextView messagetext = (TextView) Dialogview.findViewById(R.id.message);
                final TextView addtext = (TextView) Dialogview.findViewById(R.id.add);
                final ImageButton messageicon = (ImageButton) Dialogview.findViewById(R.id.imageButton6);

                databaseReference.child("useraccount").child(values.get(getAdapterPosition()).getUserid()).addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> userinfo = (HashMap<String, String>) dataSnapshot.getValue();

                         String loadurl = userinfo.get("profilepicture");
                         String name = userinfo.get("username");
                         Glide.with((AppCompatActivity)context).load(loadurl).into(propic);
                        addtext.setText("Add " + name);
                        messagetext.setText("Message " + name);
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                messageicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        privatemessagefragment();
                    }
                });

                messagetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        privatemessagefragment();
                    }
                });

            }



        }
        private void privatemessagefragment(){
             Privatemessage privatemessage = new Privatemessage();
            FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("name", values.get(getAdapterPosition()).getMessageUser());
            bundle.putString("url", values.get(getAdapterPosition()).getProfilepicture());
            bundle.putString("id", values.get(getAdapterPosition()).getUserid());
            privatemessage.setArguments(bundle);
            fragmentTransaction.replace(R.id.main,privatemessage);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
}

    }
}
