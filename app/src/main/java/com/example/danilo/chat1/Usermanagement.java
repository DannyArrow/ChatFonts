package com.example.danilo.chat1;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    FragmentTransaction fragmentTransaction;
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
    boolean b = false;
    ProgressDialog progress;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.manageusersetting, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(b) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {

                            return true;
                        }
                    }
                }
                    return false;

            }
        });



        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        username = (TextView) view.findViewById(R.id.txt_username);
        propic = (CircleImageView) view.findViewById(R.id.profile_image);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar10);
        messagelayout = (ConstraintLayout)  view.findViewById(R.id.messages_layout);
        friendslayout = (ConstraintLayout) view.findViewById(R.id.friends_layout);
        invitelayout = (ConstraintLayout) view.findViewById(R.id.invites_layout);
        accountsetting = (ConstraintLayout) view.findViewById(R.id.settings_layout);
        message = (TextView) view.findViewById(R.id.num_messages) ;
        friends =(TextView) view.findViewById(R.id.num_friends);
        invites = (TextView) view.findViewById(R.id.num_invites);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        progress = new ProgressDialog(getActivity());

        progressBar.setVisibility(View.VISIBLE);

        loadcurrentimage();
        loadusernametotext();
        count_of_current_message();
        friends_counts();
        invitation_counts();

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

          b = true;

            CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(),this);
                break;

            case R.id.messages_layout:
                mailbox mail = new mailbox();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main,mail);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                Toasty.normal(getContext(),"message clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.friends_layout:
                Toasty.normal(getContext(),"friends clicked", Toast.LENGTH_LONG).show();
                Friends friends = new Friends();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main,friends);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;

            case R.id.invites_layout:
                Invite invite = new Invite();
                fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.main,invite);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                Toasty.normal(getContext(),"ivites clicked", Toast.LENGTH_LONG).show();
                break;

            case R.id.settings_layout:
                Toasty.normal(getContext(),"setting clicked", Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void friends_counts(){
        databaseReference.child("useraccount").child(firebaseUser.getUid()).child("friends").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = (int) dataSnapshot.getChildrenCount();
                String countstring = String.valueOf(count);
                friends.setText(countstring);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void invitation_counts(){
        databaseReference.child("useraccount").child(firebaseUser.getUid()).child("invitaion").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int countt = (int) dataSnapshot.getChildrenCount();
                String countstr = String.valueOf(countt);
                invites.setText(countstr);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        databaseReference.child("useraccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                            databaseReference.child("useraccount").child(firebaseUser.getUid()).child("profilepicture").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                   Toasty.success(getContext(), "pictues uploaded", Toast.LENGTH_LONG).show();
                                    progress.dismiss();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    b = false;



                                }
                            });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getContext(), "profile pictures didn't upload properly", Toast.LENGTH_LONG).show();

                progress.setCancelable(false);
                progress.dismiss();
                progressBar.setVisibility(View.INVISIBLE);
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
                    progress.setMessage("uploading profile picture to server. Please wait...");
                    progress.show();
                    progressBar.setVisibility(View.VISIBLE);
                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
                  saveToInternalStorage(bitmap1);
                   loadimage(resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                 progress.dismiss();
                progressBar.setVisibility(View.INVISIBLE);
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
                    Glide.with(getContext()).load(profilepicture).listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    }).into(propic);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }







}
