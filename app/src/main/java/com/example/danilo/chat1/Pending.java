package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Danilo on 3/11/18.
 */

public class Pending extends Fragment {
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    ArrayList<String> list;
    ArrayList<Usernameinfo> userlist;
    Pending_adapter pending_adater;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.pending, container, false);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference  = firebaseDatabase.getReference();
            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            list = new ArrayList<>();
            userlist = new ArrayList<>();


            return view;


    }

    private void setadapter(){
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);


    }

    private void getuserinfomation() {
        for(int i = 0; i < list.size(); i++) {
            databaseReference.child("useraccount").child(list.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usernameinfo usernameinfo = dataSnapshot.getValue(Usernameinfo.class);
                    userlist.add(usernameinfo);
                    if(userlist.size() == list.size()){
                        setadapter();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    private void fetch_pending_id(){
            databaseReference.child("useraccount").child(firebaseUser.getUid())
                    .child("pending").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot children: dataSnapshot.getChildren()){
                        String id = children.getValue(String.class);
                        list.add(id);
                    }
                    if(list.size() > 0){
                        getuserinfomation();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


    }
}
