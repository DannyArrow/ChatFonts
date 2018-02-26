package com.example.danilo.chat1;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * Created by Danilo on 2/13/18.
 */

public class Usermanagement extends Fragment implements View.OnClickListener {
    private static final int RESULT_OK = -1;
    TextView username;
    StorageReference mStorageRef;

    ProgressBar progressBar;
    ConstraintLayout messagelayout;
    ConstraintLayout friendslayout;
    ConstraintLayout invitelayout;
    ConstraintLayout accountsetting;
    CircleImageView propic;
    TextView message;
    TextView friends;
    TextView invites;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    private String name;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manageusersetting, container, false);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        username = (TextView) view.findViewById(R.id.txt_username);
        propic = (CircleImageView) view.findViewById(R.id.profile_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        messagelayout = (ConstraintLayout)  view.findViewById(R.id.messages_layout);
        friendslayout = (ConstraintLayout) view.findViewById(R.id.friends_layout);
        invitelayout = (ConstraintLayout) view.findViewById(R.id.invites_layout);
        accountsetting = (ConstraintLayout) view.findViewById(R.id.settings_layout);
        message = (TextView) view.findViewById(R.id.num_messages) ;
        friends =(TextView) view.findViewById(R.id.num_friends);
        invites = (TextView) view.findViewById(R.id.num_invites);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        loadcurrentimage();
        loadusernametotext();
        count_of_current_message();

        // action bars
        AppCompatActivity activity = (AppCompatActivity) getContext();
        final ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.setting);
        ImageButton back = (ImageButton) actionBar.getCustomView().findViewById(R.id.back1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.normal(getContext(),"back clicked", Toast.LENGTH_LONG).show();
            }
        });
        actionBar.show();



        propic.setOnClickListener(this);
        messagelayout.setOnClickListener(this);
        invitelayout.setOnClickListener(this);
        friendslayout.setOnClickListener(this);
        accountsetting.setOnClickListener(this);



       return view;
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.profile_image:
                Toasty.normal(getContext(),"picture click", Toast.LENGTH_LONG).show();
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(),this);
                break;

            case R.id.messages_layout:
                mailbox mail = new mailbox();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main,mail);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toasty.normal(getContext(),"message clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.friends_layout:
                Toasty.normal(getContext(),"friends clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.invites_layout:
                Toasty.normal(getContext(),"ivites clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.settings_layout:
                Toasty.normal(getContext(),"setting clicked", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void count_of_current_message() {
        databaseReference.child("useraccount").child(firebaseUser.getUid()).child("messangecount").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               int  countofmsg = 0;
                for(DataSnapshot child: dataSnapshot.getChildren()){
                    countofmsg += (int) child.getChildrenCount();
                }

                String count = String.valueOf(countofmsg);
                message.setText(count);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void loadusernametotext(){
        databaseReference.child("useraccount").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> userinfo = (HashMap<String, String>) dataSnapshot.getValue();
                name = userinfo.get("username");
                if(name != null) {
                    username.setText(name);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }


    public void uploadtocloud(final String filee)  {
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
                        @SuppressWarnings("VisibleForTests") final String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                        Usernameinfo usernameinfo = new Usernameinfo(name, downloadUrl);
                        if (usernameinfo.getUsername() != null) {
                            databaseReference.child("useraccount").child(firebaseUser.getUid()).setValue(usernameinfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toasty.success(getContext(), "pictues uploaded", Toast.LENGTH_LONG).show();
                                    Glide.with(getContext()).load(downloadUrl).into(propic);

                                }
                            });
                        }
                    }
                });
}

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity());
        //String username = accountinfo();
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        final File mypath = new File(directory, "profile.jpg" + name);
       // Log.e("user = ", username);
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
                loadimage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void loadimage(Uri imageUri) {
        Glide.with(getContext()).load(imageUri).into(propic);
    }

    private void loadcurrentimage() {
        databaseReference.child("useraccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> userinfo = (HashMap<String, String>) dataSnapshot.getValue();
                String profilepicture = userinfo.get("profilepicture");
                if (profilepicture != null) {
                    Glide.with(getContext()).load(profilepicture).into(propic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




}
