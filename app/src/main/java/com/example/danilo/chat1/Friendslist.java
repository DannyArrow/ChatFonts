package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.danilo.chat1.adapters.Friend_adapter;
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

public class Friendslist extends Fragment {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> idlist;
    ArrayList<Usernameinfo> userlist;
    Friend_adapter friend_adapter;
    RecyclerView recyclerView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendlist, container, false);

        firebaseDatabase = FirebaseDatabase.getInstance();
    databaseReference = firebaseDatabase.getReference();
    recyclerView = (RecyclerView)  view.findViewById(R.id.rc);
    firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    idlist = new ArrayList<>();
    userlist = new ArrayList<>();

    fetch_friendlist_ids();





        return view;
    }



    private void getuserinfomation() {
        for(int i = 0; i < idlist.size(); i++) {
            databaseReference.child("useraccount").child(idlist.get(i)).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usernameinfo usernameinfo = dataSnapshot.getValue(Usernameinfo.class);
                    userlist.add(usernameinfo);
                    if(userlist.size() == idlist.size()){
                        setadapter();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    private void fetch_friendlist_ids(){
        databaseReference.child("useraccount").child(firebaseUser.getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot children : dataSnapshot.getChildren()) {
                    String id = children.getValue(String.class);
                    idlist.add(id);
                }
                if(idlist.size() > 0){
                    getuserinfomation();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setadapter(){
        friend_adapter = new Friend_adapter(userlist);
        LinearLayoutManager layoutmanager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutmanager);
        recyclerView.setAdapter(friend_adapter);

    }

}
