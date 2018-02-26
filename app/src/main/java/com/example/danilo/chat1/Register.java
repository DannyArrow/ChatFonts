package com.example.danilo.chat1;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
 * Created by Danilo on 12/5/17.
 */

public class Register extends Fragment{
    private static final int SELECT_FILE = 22;
    StorageReference mStorageRef;
    Button uploadpic;
    FirebaseUser user;
    boolean isuploadpicclick;
    Button register;
    EditText pass;
    EditText username;
    EditText email;
    private FirebaseAuth mAuth;
    public static final int RESULT_OK = -1;
    private Bitmap photo;
    HashMap<String, Object> userdetails;
    private ImageView img;
    FirebaseDatabase database;
    DatabaseReference myRef;
    DatabaseReference ref;
    Fragment fragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    String usernameString;
    Intent cameraIntent;
    Intent intent;
    Bitmap Filebit;
    CircleImageView image;
    private Uri mCropImageUri;

    public void bitmap(Bitmap bitmap){
        Filebit = bitmap;

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register, container, false);

        isuploadpicclick = false;
        uploadpic = (Button) view.findViewById(R.id.button4);
        image = (CircleImageView) view.findViewById(R.id.imageView6);
        register = (Button) view.findViewById(R.id.reg);
        pass = (EditText) view.findViewById(R.id.editText);
        email = (EditText)  view.findViewById(R.id.email);
        username = (EditText) view.findViewById(R.id.username);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //myRef = database.getReference("username");
        ref = database.getReference();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
       // changing the tint color of circleimageview
        //image.setColorFilter(Color.argb(255, 182, 208, 207));





        uploadpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                 startPickImageActivity();
                isuploadpicclick = true;

/*
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
                */

            }

        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailstring = email.getText().toString().trim();
                String passstring = pass.getText().toString().trim();
                usernameString = username.getText().toString().trim();

                if(usernameString.isEmpty()){
                    return;
                }

                if(emailstring.isEmpty()){
                    return;
                }
                if(passstring.isEmpty()){
                    return;
                }
                if(!isuploadpicclick){
                    return;
                }
                if(Filebit == null){
                    return;
                }

                mAuth.createUserWithEmailAndPassword(emailstring,passstring).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                        Log.i("res = ","task successful");
                            Toasty.success(getActivity(),"succesfully created account",Toast.LENGTH_SHORT,true).show();
                            user = FirebaseAuth.getInstance().getCurrentUser();



                            saveToInternalStorage(Filebit);
                           // ref.setValue(usernameString);

                           // Usernameinfo usernameinfo = new Usernameinfo(usernameString, "klklk");
                           // userdetails = new HashMap<>();
                           // userdetails.put("username", usernameinfo.getUsername());
                           // userdetails.put("profilepicture", usernameinfo.getProfilepicture());
                            //ref.child("users").child(user.getUid()).setValue(usernameString);





                        if(!task.isSuccessful()){
                            Log.i("res = ","task not successful");

                            Toasty.error(getActivity(),"Something wrent wrong, check if the password has minimun of 6 characters",Toast.LENGTH_SHORT,true).show();


                        }
                    }

            };





});
            }
        });
        return view;

    }
        public void startPickImageActivity() {

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(getContext(),this);
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
                        @SuppressWarnings("VisibleForTests") String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                        Usernameinfo usernameinfo = new Usernameinfo(usernameString, downloadUrl);
                        userdetails = new HashMap<>();
                        userdetails.put("username", usernameinfo.getUsername());
                        userdetails.put("profilepicture", usernameinfo.getProfilepicture());

                        if (user != null) {
                            ref.child("useraccount").child(user.getUid()).setValue(userdetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    fragment = new Messaging();
                                    fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.main, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            });


                            //reference.child("Users").child(user.getUid()).setValue(value);


                        }
                    }
                });}


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity());


        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        final File mypath = new File(directory, "profile.jpg" + usernameString);
        Log.e("user = ", usernameString);

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
                    bitmap(bitmap1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                loadimage(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


        }
        // handle result of pick image chooser
        if (requestCode == PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(getActivity(), data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(getActivity(), imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                //((ImageButton) findViewById(R.id.quick_start_cropped_image)).setImageURI(result.getUri());
                Toast.makeText(getContext(), "Cropping successful, Sample: " + result.getSampleSize(), Toast.LENGTH_LONG).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(getContext(), "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // required permissions granted, start crop image activity
            startCropImageActivity(mCropImageUri);
        } else {
            Toast.makeText(getContext(), "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
        }
    }
*/




    private void loadimage(Uri imageUri) {
        Glide.with(getContext()).load(imageUri).into(image);
    }


    }



