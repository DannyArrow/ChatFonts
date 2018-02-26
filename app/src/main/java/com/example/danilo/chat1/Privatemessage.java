package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

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

/**
 * Created by Danilo on 12/13/17.
 */

public class Privatemessage extends Fragment {

    Toolbar toolbar;
    ImageView userpic;
    TextView textView;
    DatabaseReference Reference;
    DatabaseReference databaseReference;
    String count;
    String pic;
    FirebaseUser firebaseUser;
    Button send;
    String id;
    String propic;
    String username;
    EditText message;
    RecyclerView recyclerView;
    Privatemyadapter adapter;
    String lobbyID;
    PrivateLobby lobby;
    ArrayList<ChatMessage> messagedata;
    String userkey;
    String name;
    HashMap<String,String> map;

    FirebaseDatabase database;
    DatabaseReference Ref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
         databaseReference = FirebaseDatabase.getInstance().getReference();
       Reference = FirebaseDatabase.getInstance().getReference().child("useraccount").child(firebaseUser.getUid()).child("private_messages");
        ;
        database = FirebaseDatabase.getInstance();
        Ref = database.getReference("useraccount").child(firebaseUser.getUid());


       // Ref = database.getReference("useraccount").child(firebaseUser.getUid());

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, container, false);

       // textView = (TextView) view.findViewById(R.id.textView3);
        message = (EditText) view.findViewById(R.id.edittext_chatbox);
        recyclerView = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        send = (Button) view.findViewById(R.id.button_chatbox_send);

        AppCompatActivity activity = (AppCompatActivity) getContext();
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.privaterow);
        ImageButton homegroup = (ImageButton) actionBar.getCustomView().findViewById(R.id.imageButton8);
        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.message);
        ImageView userpic = (ImageView) actionBar.getCustomView().findViewById(R.id.imageView3);
        actionBar.show();

        homegroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Messaging messaging = new Messaging();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main,messaging);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        Ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> userdetails = (HashMap<String, Object>) dataSnapshot.getValue();
                propic = userdetails.get("profilepicture").toString();
                username = userdetails.get("username").toString();
                Log.i("user info = ", username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            bundle.getString("name");
            pic = bundle.getString("url");
            id = bundle.getString("id");
            userkey = bundle.getString("userkey");
            name = bundle.getString("name");
            textView.setText(name);
            if(userkey != null) {
                unread();
            }
        }


        check();

        lobby = new PrivateLobby(firebaseUser.getUid(), id);
        Glide.with(this).load(propic).into(userpic);












        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                send_data();
            }});
        return view;
    }

    private void unread(){
        Ref.child("messangecount").child(userkey).removeValue();
    }


    private void check(){
        //Reference.child("useraccount").child(firebaseUser.getUid()).child("private_messagess");

        Reference.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eachmessage : dataSnapshot.getChildren()) {
                        Log.i("key --",eachmessage.getKey());

                    if(eachmessage.getKey().equalsIgnoreCase(id)){
                        lobbyID =   eachmessage.getValue(String.class);
                        Log.i("lobbyid", lobbyID);
                        break;
                    } else {
                        lobbyID = databaseReference.push().getKey();
                    }

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }



        });
    }

    private  void send_data() {
    map = new HashMap<>();

    map.put("username",name);
    map.put("message",message.getText().toString().trim());

       databaseReference.child("notificationRequests").push().setValue(map);

        Log.i("id l =", "null" + lobbyID);
        ChatMessage chatMessage = new ChatMessage(message.getText().toString().trim(), username, propic, firebaseUser.getUid());
        String chatmessageID = Reference.push().getKey();

        //Log.i("chatmessageid =", " null"+chatmessageID);
        //Log.i("lobbyid = ", lo)

        HashMap<String,ChatMessage>  hashMap = new HashMap<String,ChatMessage>();
        hashMap.put(chatmessageID,chatMessage);
        //count =  databaseReference.push().getKey();


        databaseReference.child("private_messages").child(lobbyID).push().setValue(hashMap);
        databaseReference.child("useraccount").child(id).child("private_messages").child(firebaseUser.getUid()).setValue(lobbyID);

        databaseReference.child("useraccount").child(id).child("messangecount").child(firebaseUser.getUid()).push().setValue("");


        databaseReference.child("useraccount").child(firebaseUser.getUid()).child("private_messages").child(id).setValue(lobbyID);

        getComments();
    }


    private void getComments() {
        ValueEventListener postListener = new ValueEventListener() {
            HashMap<String, ChatMessage> userdetails;
            ArrayList<ChatMessage> keylist = new ArrayList<ChatMessage>();
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //HashMap<String,ChatMessage> map =  new HashMap<String,ChatMessage>();
                //keylist.clear();
                for (DataSnapshot eachmessage : dataSnapshot.getChildren()) {

                  ChatMessage k  = eachmessage.getChildren().iterator().next().getValue(ChatMessage.class);
                    Log.i("key= ","" + k.getMessageText());
                    keylist.add(k);

                }

                // Log.i("list= ","" +);
               LinearLayoutManager layoutmanager
                      = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutmanager);

                adapter = new Privatemyadapter(keylist, getContext());
                recyclerView.setAdapter(adapter);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("private_messages").child(lobbyID).addListenerForSingleValueEvent(postListener);
    }}