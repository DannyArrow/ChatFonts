package com.example.danilo.chat1;


import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danilo.chat1.adapters.MyAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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

public class Messaging extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 1888;
    private static final int PICK_FROM_CAMERA = 1;
    private static final int RESULT_OK = -1;
    Button enter;
    EditText messag;
    FirebaseUser user;
    DatabaseReference userref;
    AlertDialog.Builder alert;
    String mypathstring;

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
    private Bitmap photo;
    StorageReference mStorageRef;
    DatabaseReference countref;
   TextView textView;
    DatabaseReference Ref;
    ActionBar actionBar;
    ArrayList<Integer> list;
    ImageButton popupbtn;
    ImageButton photosend;
    ArrayList<Integer> fontlist;
    ArrayList<ChatMessage> messagedata;
    int count;
    ArrayList<String> keylist;
    ArrayList<String> piclist;
    FirebaseDatabase database;
    ArrayList<String> test;
    EditText input;
     boolean j = false;
    AlertDialog alertDialog;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message, container, false);

        test = new ArrayList<>();
        fontlist = new ArrayList<>();
         keylist = new ArrayList<>();
         piclist = new ArrayList<>();
        messagedata = new ArrayList<ChatMessage>();
        messag = (EditText) view.findViewById(R.id.edittext_chatbox);
        enter = (Button) view.findViewById(R.id.button_chatbox_send);
        user = FirebaseAuth.getInstance().getCurrentUser();
        photosend = (ImageButton) view.findViewById(R.id.imageButton);
        database = FirebaseDatabase.getInstance();
        countref = database.getReference();
        Ref = database.getReference("useraccount").child(user.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) view.findViewById(R.id.reyclerview_message_list);
        countmessage = new ArrayList<>();
        counthash = new HashMap<String, ArrayList<Object>>();
        input = new EditText(getContext());

        set_fonts();


        databaseReference.child("fonts")
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("useraccount").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
               myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        //set actionbar
        AppCompatActivity activity = (AppCompatActivity) getContext();
        actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.bar);
        final ImageButton logout = (ImageButton) actionBar.getCustomView().findViewById(R.id.logout_button_1);
        ImageButton mail = (ImageButton) actionBar.getCustomView().findViewById(R.id.imageButton5);
        popupbtn = (ImageButton) actionBar.getCustomView().findViewById(R.id.imageButton11);
        textView = (TextView) actionBar.getCustomView().findViewById(R.id.textView3);
        //spinner = (Spinner) actionBar.getCustomView().findViewById(R.id.spinner);
         list = new ArrayList<Integer>();

        list.add(R.font.aladin);
        list.add(R.font.fenix);
        list.add(R.font.codystar_light);
        list.add(R.font.adamina);
        list.add(R.font.almendra_display);
        list.add(R.font.pacifico);
        list.add(R.font.palanquin_dark);
        list.add(R.font.salsa);



        actionBar.show();
        FirebaseMessaging.getInstance().subscribeToTopic(user.getUid());


        popupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             popup();

            }
        });


        photosend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startPickImageActivity();

            }
        });

        get_userinfomation();

        get_unread_message_count();

        getComments();



        messag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messag.getText().toString().isEmpty()){
                   // imageButton.setVisibility(View.INVISIBLE);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            go_to_mailboxfragment();
            }
        });


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChatMessage chatMessage = new ChatMessage(messag.getText().toString().trim(), username, user.getUid(),user.getUid());
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

    private void applyFontToMenuItem(MenuItem mi, Integer integer) {
        Typeface font = ResourcesCompat.getFont(getContext(),integer);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font, Color.BLACK), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void logout(){
       Usermanagement loginregister = new Usermanagement();
       FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
       transaction.replace(R.id.main,loginregister);
       transaction.addToBackStack(null);
       actionBar.hide();
       transaction.commit();
   }

    private void go_to_mailboxfragment(){
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

    private void get_userinfomation(){
        if (user != null) {
            Ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.i("propic = ", "dataSnapshot key" + dataSnapshot.getValue());
                    HashMap<String, Object> userdetails = (HashMap<String, Object>) dataSnapshot.getValue();
                    propic = userdetails.get("profilepicture").toString();
                    username = userdetails.get("username").toString();

                }
                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("l", "Failed to read value.", error.toException());
                }
            });
        }
    }
           private void set_fonts() {
        databaseReference.child("fonts").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String font = dataSnapshot.getValue(String.class);
                if (font != null) {
                    Typeface typeface;
                    switch (font) {
                        case "aladin":
                            typeface = ResourcesCompat.getFont(getContext(), R.font.aladin);
                            break;
                        case "fenix":
                            typeface = ResourcesCompat.getFont(getContext(), R.font.fenix);
                            break;
                        case "codystar":
                            typeface = ResourcesCompat.getFont(getContext(), R.font.codystar_light);
                            break;
                        case "adamina":
                            typeface = ResourcesCompat.getFont(getContext(), R.font.adamina);
                            break;
                        case "pacifico":
                            typeface = ResourcesCompat.getFont(getContext(), R.font.pacifico);
                            break;
                        case "salsa":
                            typeface = ResourcesCompat.getFont(getContext(), R.font.salsa);
                            break;
                        default:
                            typeface = ResourcesCompat.getFont(getContext(), R.font.pacifico);
                            break;
                    }
                    messag.setTypeface(typeface);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

            private void get_unread_message_count(){
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
    }

    private void popup(){
        PopupMenu popup = new PopupMenu(getContext(),popupbtn);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                String val = String.valueOf(item.getTitle());
               databaseReference.child("fonts").child(user.getUid()).setValue(val);
                return true;
            }
        });

        popup.show();//showing popup menu
        Menu menu = popup.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            applyFontToMenuItem(mi,list.get(i));
        }
    }

    private void getComments() {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagedata.clear();
                fontlist.clear();
                test.clear();
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
                myAdapter.notifyDataSetChanged();

               /* if(messagedata.size() > 0){
                    for(int i = 0; i < messagedata.size(); i++) {
                        userref = database.getReference().child("useraccount").child(messagedata.get(i).getUserid());
                        getfontslist();


                    }

                }*/


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.child("message").addValueEventListener(postListener);
    }

    private void getfontslist() {
       // count = 0;
        //userref = database.getReference().child("useraccount").child(i);

        //userref.addValueEventListener(new ValueEventListener() {
          //  @Override
           // public void onDataChange(DataSnapshot dataSnapshot) {
             //   Usernameinfo usernameinfo =  dataSnapshot.getValue(Usernameinfo.class);
              //  String profile = usernameinfo.getProfilepicture();

               // piclist.add(profile);


              /*  if (font != null) {
                    switch (font) {
                        case "aladin":
                            messagedata.get(count).setFonts(ResourcesCompat.getFont(getContext(), R.font.aladin));

                            break;
                        case "fenix":
                            messagedata.get(count).setFonts(ResourcesCompat.getFont(getContext(), R.font.fenix));
                            fontlist.add(R.font.fenix);
                            break;
                        case "codystar":
                            messagedata.get(count).setFonts(ResourcesCompat.getFont(getContext(), R.font.codystar_light));
                            fontlist.add(R.font.codystar_light);
                            break;
                        case "adamina":
                            messagedata.get(count).setFonts(ResourcesCompat.getFont(getContext(), R.font.adamina));
                            fontlist.add(R.font.adamina);
                            break;
                        case "pacifico":
                            messagedata.get(count).setFonts(ResourcesCompat.getFont(getContext(), R.font.pacifico));
                            fontlist.add(R.font.pacifico);
                            break;
                        case "salsa":
                            messagedata.get(count).setFonts(ResourcesCompat.getFont(getContext(), R.font.salsa));
                            fontlist.add(R.font.salsa);
                            break;
                        case "":
                            fontlist.add(R.font.salsa);
                            break;

                        default:
                            fontlist.add(R.font.pacifico);
                            break;
                    }
                    count++;

                }*/
               /* if(piclist.size() == messagedata.size()){
                    for(int i = 0; i < piclist.size();i++){
                        messagedata.get(i).setProfilepicture(piclist.get(i));
                    }

                    Log.i("log info =", "you on the right track");
                    LinearLayoutManager layoutmanager
                            = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutmanager);
                    recyclerView.scrollToPosition(messagedata.size() - 1);
                    myAdapter = new MyAdapter(messagedata, keylist,getContext());
                    recyclerView.setAdapter(myAdapter);  */

           // }


          //  @Override
          //  public void onCancelled(DatabaseError databaseError) {

          //  }

      //  });

        //fontlist.size();
    }


            public void uploadtocloud(final String filee, final String string)  {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://chat-4c029.appspot.com");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        final Uri file = Uri.fromFile(new File(filee));
        final StorageReference riversRef = storageRef.child(filee);

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                        Log.e("sucessfully", "good job");
                        @SuppressWarnings("VisibleForTests") String downloadUrl = taskSnapshot.getDownloadUrl().toString();


                        ChatMessage chatMessage = new ChatMessage(downloadUrl, username, user.getUid(),user.getUid());
                        HashMap<String, Object> commentInfo = new HashMap<String, Object>();
                        commentInfo.put("messageTime", chatMessage.getMessageTime());
                        commentInfo.put("messageUser", chatMessage.getMessageUser());
                        commentInfo.put("messageText", chatMessage.getMessageText()+string);
                        commentInfo.put("profilepicture", chatMessage.getUserid());
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

            mypathstring = String.valueOf(mypath);
            create_alert();

           // uploadtocloud(String.valueOf(mypath));



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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
                    saveToInternalStorage(bitmap1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public  void create_alert(){
        alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Add description to image");
        alert.setIcon(R.drawable.ic_input_black_24dp);
        alert.setMessage("fill out the description of the image");

        if(input.getParent()!=null)
            ((ViewGroup)input.getParent()).removeView(input);

        alert.setView(input);
        alert.setPositiveButton("submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String code = "ouitxh72n";
                String string = input.getText().toString();
                uploadtocloud(mypathstring,code + string);
                //dialog.dismiss();

            }
        });
        alert.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                uploadtocloud(mypathstring,"");

                //dialog.dismiss();

            }
        });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }


    public void startPickImageActivity() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(getContext(),this);
    }




}
