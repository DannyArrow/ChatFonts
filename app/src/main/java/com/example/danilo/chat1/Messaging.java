package com.example.danilo.chat1;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

/**
 * Created by Danilo on 12/6/17.
 */

public class Messaging extends Fragment{
    private static final int PICK_IMAGE_REQUEST = 1888;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int RESULT_OK = 2;
    Button enter;
    EditText messag;
    private FirebaseDatabase firebaseDatabase;
    FirebaseUser user;
    String name;
    DatabaseReference databaseReference;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    String username;
    String propic;
    ArrayList<String> countmessage;
    HashMap<String, ArrayList<Object>> counthash;
    HashMap<String, Object> data;
    int countofmsg;
    //ImageButton imageButton;
    String st;
    Intent cameraIntent;
    Intent intent;
    Bitmap Filebit;
    private Bitmap photo;
    private StorageReference mStorageRef;


    public boolean onBackPressed() {
        return false;
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, container, false);
        messag = (EditText) view.findViewById(R.id.edittext_chatbox);
        enter = (Button) view.findViewById(R.id.button_chatbox_send);
        user = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference countref = database.getReference();
        final DatabaseReference Ref = database.getReference("useraccount").child(user.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        countmessage = new ArrayList<>();
        counthash = new HashMap<String, ArrayList<Object>>();
        //imageButton = (ImageButton) view.findViewById(R.id.imageButton6);



        AppCompatActivity activity = (AppCompatActivity) getContext();
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.bar);
        ImageButton logout = (ImageButton) actionBar.getCustomView().findViewById(R.id.logout_button_1);
        ImageButton mail = (ImageButton) actionBar.getCustomView().findViewById(R.id.imageButton5);
        final TextView textView = (TextView) actionBar.getCustomView().findViewById(R.id.textView3);
        actionBar.show();





        messag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(messag.getText().toString().isEmpty()){
                    //imageButton.setVisibility(View.INVISIBLE);



                }
            }
        });

        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.custom);
                dialog.setTitle("Title...");

                ImageButton galleryButton = (ImageButton) dialog.findViewById(R.id.gallery);
                ImageButton cameraButton = (ImageButton) dialog.findViewById(R.id.camera);
                //galleryButton.setImageResource(R.drawable.gallery);
                //cameraButton.setImageResource(R.drawable.camera);

                // if button is clicked, close the custom dialog
                galleryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "gallery click", Toast.LENGTH_SHORT);
                        Log.e("gallery", "gallery button click");
                        intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);


                        dialog.dismiss();
                    }
                });
                cameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), "camera click", Toast.LENGTH_SHORT);
                        Log.e("camera", "camera button click");
                        //Intent cameraIntent;
                        cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, PICK_FROM_CAMERA);



                        dialog.dismiss();
                    }
                });

                dialog.show();

            }

        });*/







        countref.child("useraccount").child(user.getUid()).child("messangecount").addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {
                countofmsg = 0;

                for(DataSnapshot child: dataSnapshot.getChildren()){
                    data = (HashMap<String, Object>) child.getValue();
                    String  datakey = child.getKey();

                    countofmsg += (int) child.getChildrenCount();


                    Object[] arr =  data.keySet().toArray();
                    ArrayList<Object> arrayList = new ArrayList<Object>(Arrays.asList(arr));
                    counthash.put(datakey,arrayList);
                }
                Log.i("count -==", String.valueOf(countofmsg));

                Log.i("counthash =", String.valueOf(counthash.keySet()));
                String count = String.valueOf(countofmsg);
                textView.setText(count);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toasty.info(getContext(),"it works", Toast.LENGTH_LONG,true).show();
                Usermanagement loginregister = new Usermanagement();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main,loginregister);
                transaction.addToBackStack(null);
                actionBar.hide();
                transaction.commit();


            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Toasty.info(getContext(),"mail is click", Toast.LENGTH_LONG,true).show();
               /* Bundle bundle = new Bundle();
                 bundle.putSerializable("hashmap",counthash);
                 */

                mailbox mail = new mailbox();
                //mail.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.main,mail);
                transaction.addToBackStack(null);
                actionBar.hide();
                transaction.commit();

            }
        });




        getComments();


        if (user != null) {
            Ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("propic = ", "dataSnapshot key" + dataSnapshot.getValue());
                    HashMap<String, Object> userdetails = (HashMap<String, Object>) dataSnapshot.getValue();
                    propic = userdetails.get("profilepicture").toString();
                    username = userdetails.get("username").toString();
                    FirebaseMessaging.getInstance().subscribeToTopic("hi");
                }


                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("l", "Failed to read value.", error.toException());
                }
            });
        }
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chatMessage = new ChatMessage(messag.getText().toString().trim(), username, propic,user.getUid());
                HashMap<String, Object> commentInfo = new HashMap<String, Object>();
                commentInfo.put("messageTime", chatMessage.getMessageTime());
                commentInfo.put("messageUser", chatMessage.getMessageUser());
                commentInfo.put("messageText", chatMessage.getMessageText());
                commentInfo.put("profilepicture", chatMessage.getUserid());
                commentInfo.put("userid", chatMessage.getUserid());
                databaseReference.child("message").push().setValue(commentInfo);


            }
        });

        return view;
    }

    //set pictures


    private void getComments() {
        ValueEventListener postListener = new ValueEventListener() {
            ArrayList<ChatMessage> messagedata = new ArrayList<ChatMessage>();
            ArrayList<String> keylist = new ArrayList<String>();

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagedata.clear();
                keylist.clear();
                for (DataSnapshot eachmessage : dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = eachmessage.getValue(ChatMessage.class);
                    String keyy = eachmessage.getKey();
                    keylist.add(keyy);

                    messagedata.add(chatMessage);

                }

                LinearLayoutManager layoutmanager
                        = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(layoutmanager);
                recyclerView.scrollToPosition(messagedata.size() - 1);
                myAdapter = new MyAdapter(messagedata, keylist,getContext());
                recyclerView.setAdapter(myAdapter);
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("message").addValueEventListener(postListener);
    }


    public void uploadtocloud(final String filee)  {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://mchat-master-7d5c8.appspot.com");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final Uri file = Uri.fromFile(new File(filee));
        final StorageReference riversRef = storageRef.child(filee);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("sucessfully", "good job");
                        @SuppressWarnings("VisibleForTests") String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                        ChatMessage chatMessage = new ChatMessage(downloadUrl, username, propic,user.getUid());
                        HashMap<String, Object> commentInfo = new HashMap<String, Object>();
                        commentInfo.put("messageTime", chatMessage.getMessageTime());
                        commentInfo.put("messageUser", chatMessage.getMessageUser());
                        commentInfo.put("messageText", chatMessage.getMessageText());
                        commentInfo.put("profilepicture", chatMessage.getProfilepicture());
                        commentInfo.put("userid", chatMessage.getUserid());
                        databaseReference.child("message").push().setValue(commentInfo);




                            //reference.child("Users").child(user.getUid()).setValue(value);


                        }
                    });
                };


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity());


        File directory = cw.getDir("imagess", Context.MODE_PRIVATE);
        // Create imageDir
       String key =  databaseReference.push().getKey();

        final File mypath = new File(directory, "img" + key);

       // Log.e("user = ", usernameString);

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            uploadtocloud(String.valueOf(mypath));


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return directory.getAbsolutePath();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FROM_CAMERA) {

            photo = (Bitmap) data.getExtras().get("data");
            saveToInternalStorage(photo);



        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                saveToInternalStorage(bitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
