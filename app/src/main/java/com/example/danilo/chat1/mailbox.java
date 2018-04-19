package com.example.danilo.chat1;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.danilo.chat1.adapters.Mailadapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Danilo on 12/19/17.
 */

public class mailbox extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference countref = database.getReference();
    DatabaseReference lobbyref = database.getReference().child("useraccount").child(firebaseUser.getUid()).child("private_messages");
    ArrayList<String> lobbylist;
    ChatMessage chatMessage;
    ArrayList<ChatMessage> lastmessage;
    ArrayList<String> userkey;
    int count;
    DatabaseReference privatelobby;
    DatabaseReference userref;
    ArrayList<String> namelist;
    ArrayList<String> picturelist;
    ArrayList<Mailmessage> mailmessageArrayList;
    HashMap<String, ArrayList<Object>> hashMap;
    TextView textViewhead;
    ArrayList<Object> arrayList;
    AlertDialog.Builder alert;
    RecyclerView recyclerView;
    Mailadapter mailadapter;
    private HashMap<String, Object> data;
    private HashMap<String,ArrayList<Object>> counthash;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mail, container, false);

        lobbylist = new ArrayList<>();
        lastmessage = new ArrayList<>();
        userkey = new ArrayList<>();
        namelist = new ArrayList<>();
        picturelist = new ArrayList<>();
        mailmessageArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        hashMap = new HashMap<>();
        counthash = new HashMap<>();
        alert = new AlertDialog.Builder(getContext());
        setRetainInstance(true);





        countref.child("useraccount").child(firebaseUser.getUid()).child("messangecount").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                counthash.clear();


                for(DataSnapshot child: dataSnapshot.getChildren()){
                    data = (HashMap<String, Object>) child.getValue();
                    String  datakey = child.getKey();
                    Object[] arr =  data.keySet().toArray();
                    arrayList = new ArrayList<Object>(Arrays.asList(arr));
                    counthash.put(datakey,arrayList);
                }
               // arrayList.clear();
                Log.i("counthash =", String.valueOf(counthash.keySet()));
                getlobbyid();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       /* Bundle bundle = this.getArguments();
        if(bundle != null){
           hashMap = (HashMap<String, ArrayList<Object>>) bundle.getSerializable("hashmap");
            Log.i("hashmap = ", String.valueOf(hashMap));
        }*/
        ///getlobbyid();


        AppCompatActivity activity = (AppCompatActivity) getContext();
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.back);
        TextView title = (TextView) actionBar.getCustomView().findViewById(R.id.textView8);
        title.setText("Mailbox");
        actionBar.show();
        return view;
    }

       // ImageButton back = (ImageButton) actionBar.getCustomView().findViewById(R.id.back);
        //textViewhead = (TextView) actionBar.getCustomView().findViewById(R.id.textView8) ;


        /*back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Messaging messaging = new Messaging();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main,messaging);

                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        name ();
        return view;
    }*/

    private void name () {
        userref = database.getReference().child("useraccount").child(firebaseUser.getUid());

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> userdetails = (HashMap<String, Object>) dataSnapshot.getValue();
                String string = userdetails.get("username").toString();
                 textViewhead.setText(string +  " inbox");
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    };

            private void getlobbyid (){
            lobbylist.clear();
            userkey.clear();
            lastmessage.clear();
            namelist.clear();
            picturelist.clear();
            mailmessageArrayList.clear();

            lobbyref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children: dataSnapshot.getChildren()){
                      String value =  children.getValue(String.class);
                        String key =  children.getKey();
                        lobbylist.add(value);
                        userkey.add(key);
                        Log.d("value ==", "Value is: " + value);
                        Log.d("key ==", "key is: " + key);
                    }
                    for(int i = 0; i < lobbylist.size(); i++){
                        privatelobby = database.getReference().child("private_messages").child(lobbylist.get(i));
                       getthelastmessage();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    private void getthelastmessage(){


        privatelobby.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eachmessage : dataSnapshot.getChildren()){
                     chatMessage = eachmessage.getValue(ChatMessage.class);

                    Log.i("loop =",chatMessage.getMessageText());

                }
                Log.i("msg =",chatMessage.getMessageText());
                lastmessage.add(chatMessage);


                  if(lastmessage.size() == lobbylist.size()) {
                      for (int i = 0; i < lastmessage.size(); i++) {
                          userinfo(i);
                      }
                  }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }

    private void userinfo (int i){
        userref = database.getReference().child("useraccount").child(userkey.get(i));

        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> userdetails = (HashMap<String, Object>) dataSnapshot.getValue();
                Log.i("user info = ", String.valueOf(userdetails));
                picturelist.add(userdetails.get("profilepicture").toString());
                namelist.add(userdetails.get("username").toString());
                //createobject();
                Log.i("name list = ", String.valueOf(namelist.size()));
                      if(((namelist.size() == lastmessage.size())  &&  (lobbylist.size() == lastmessage.size()))){
                          createobject ();
                      }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       // Log.i("name list = ", String.valueOf(namelist.size()));
    }




    private  void createobject () {
        for (int i = 0; i < lobbylist.size(); i++) {
            Mailmessage mailmessage = new Mailmessage(firebaseUser.getUid(), lobbylist.get(i), namelist.get(i), lastmessage.get(i).getMessageTime(), picturelist.get(i), lastmessage.get(i).getMessageText(),userkey.get(i));
            mailmessageArrayList.add(mailmessage);

        }
        Collections.sort(mailmessageArrayList);
        Collections.sort(mailmessageArrayList, Collections.reverseOrder());
        Log.i("mail list", String.valueOf(mailmessageArrayList.size()));
        LinearLayoutManager layoutmanager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutmanager);
        mailadapter = new Mailadapter(mailmessageArrayList,counthash);
        recyclerView.setAdapter(mailadapter);

    }


}







