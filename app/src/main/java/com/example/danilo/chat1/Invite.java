package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.example.danilo.chat1.adapters.Invite_adapter;
import es.dmoral.toasty.Toasty;

/**
 * Created by Danilo on 3/7/18.
 */

public class Invite extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> arrayList;
    ArrayList<Usernameinfo> userlist;
    ArrayList<String> keylist;
    Invite_adapter inviteadapter;
    RecyclerView recyclerView;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invite, container, false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        arrayList = new ArrayList<String>();
        userlist = new ArrayList<>();
        keylist = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        get_invitation_id();

        AppCompatActivity activity = (AppCompatActivity) getContext();
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.setting);
        TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.textView9);
        ImageButton back = (ImageButton) actionBar.getCustomView().findViewById(R.id.back1);

        textView.setText("Friends invites");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.normal(getContext(),"back clicked", Toast.LENGTH_LONG).show();
            }
        });
        actionBar.show();


        return view;
    }


    private void get_invitation_id(){
        arrayList.clear();
        databaseReference.child("useraccount").child(firebaseUser.getUid()).child("invitaion").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot eachmessage : dataSnapshot.getChildren()){
                    String user_id = eachmessage.getValue(String.class);
                    String keys = eachmessage.getKey();
                    keylist.add(keys);
                    arrayList.add(user_id);

                }
                if(arrayList.size() > 0){
                    userinfomation();
                }else {
                    return ;
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        }

        );

    }

    private void userinfomation(){
        userlist.clear();
        for(int i = 0; i < arrayList.size();i++) {
            databaseReference.child("useraccount").child(arrayList.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usernameinfo user = dataSnapshot.getValue(Usernameinfo.class);
                    userlist.add(user);
                    Log.i("userlist = ", String.valueOf(userlist.size()));
                    if(userlist.size() == arrayList.size()){
                        setadapter();
                    }

                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void setadapter() {
        inviteadapter = new Invite_adapter(arrayList,userlist,keylist,getActivity());
        LinearLayoutManager layoutmanager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(inviteadapter);

    }


}
