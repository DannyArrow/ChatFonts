package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity  {
    private Fragment fragment;
    FirebaseUser user;
    String username;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            databaseReference.child("useraccount").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String, Object> userdetails = (HashMap<String, Object>) dataSnapshot.getValue();

                    //Log.i("username =", String.valueOf(userdetails.get("username")));
                    username = String.valueOf(userdetails.get("username"));
                    FirebaseMessaging.getInstance().subscribeToTopic("hi");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if (user != null) {
            Messaging message = new Messaging();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main, message);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Loginregister loginregister = new Loginregister();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main, loginregister);
            transaction.addToBackStack(null);
            transaction.commit();
        }

        // Log.d("TOKEN", FirebaseInstanceId.getInstance().getToken().toString());


    }
    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }*/


}
