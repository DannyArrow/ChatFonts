package com.example.danilo.chat1.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danilo.chat1.R;
import com.example.danilo.chat1.Usernameinfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import es.dmoral.toasty.Toasty;

/**
 * Created by Danilo on 3/7/18.
 */

public class Invite_adapter extends RecyclerView.Adapter<Invite_adapter.ViewHolder> {
    private ArrayList<Usernameinfo> usernameinfoArrayList;
    private Context context;
    private  ArrayList<String> idlist;
    private  ArrayList<String> keylist;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<String> pendingkeylist = new ArrayList<>();

    public Invite_adapter(ArrayList<String> arrayList, ArrayList<Usernameinfo> usernameinfoArrayList, ArrayList<String> keylist, Context context) {
        this.keylist = keylist;
        this.usernameinfoArrayList = usernameinfoArrayList;
        this.idlist = arrayList;
    }

    @Override
    public Invite_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.row_invite, parent, false);
        // Return a new holder instance
        Invite_adapter.ViewHolder viewHolder = new Invite_adapter.ViewHolder(contactView);

         context =  parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Invite_adapter.ViewHolder holder, int position) {
        Usernameinfo usernameinfo = usernameinfoArrayList.get(position);
        holder.username.setText(usernameinfo.getUsername());
        Glide.with(context).load(usernameinfo.getProfilepicture()).into(holder.circleImageView);

    }

    @Override
    public int getItemCount() {
        return usernameinfoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        public TextView username;
        public CircleImageView circleImageView;
        public ImageButton accept;
        public  ImageButton cancel;
        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.textView6);
            circleImageView = (CircleImageView) itemView.findViewById(R.id.imageView4);
            accept = (ImageButton) itemView.findViewById(R.id.imageButton9);
            cancel = (ImageButton) itemView.findViewById(R.id.imageButton3);
            accept.setOnClickListener(this);
            cancel.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            switch(view.getId()) {
                case R.id.imageButton3:
                        //show dialog
                        Toasty.success(context, "you decline " + usernameinfoArrayList.get(getAdapterPosition()).getUsername() + " friend request", Toast.LENGTH_LONG, true).show();

                        remove_datebase_values();



                    Log.i("cancel is clicked =", "cliecked");
                    break;
                case  R.id.imageButton9:

                    Toasty.success(context,"accept button is clicked", Toast.LENGTH_LONG,true).show();
                    myref.child("useraccount").child(idlist.get(getAdapterPosition())).child("friends").push().setValue(user.getUid());
                    myref.child("useraccount").child(user.getUid()).child("friends").push()
                            .setValue(idlist.get(getAdapterPosition())).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            remove_datebase_values();

                        }
                    });
                    break;

            }
            }
            // removes values from database and adapter
            private void remove_datebase_values(){
                final HashMap<String, String> pending_map = new HashMap<>();

                myref.child("useraccount").child(idlist.get(getAdapterPosition())).child("pending").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot children: dataSnapshot.getChildren()){
                            pending_map.put(children.getValue(String.class),children.getKey());
                        }
                        if(pending_map.size() > 0) {
                            myref.child("useraccount").child(idlist.get(getAdapterPosition())).child("pending").child(pending_map.get(user.getUid())).removeValue();
                            myref.child("useraccount").child(user.getUid()).child("invitaion").child(keylist.get(getAdapterPosition())).removeValue();
                            usernameinfoArrayList.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

        }
    }


