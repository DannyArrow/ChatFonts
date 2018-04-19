package com.example.danilo.chat1.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.danilo.chat1.ChatMessage;
import com.example.danilo.chat1.Privatemessage;
import com.example.danilo.chat1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

/**
 * Created by Danilo on 12/6/17.
 */

public class MyAdapter extends RecyclerView.Adapter{
    private static final int TEXT_MESSAGE = 1;
    private static final int  PHOTO_SEND = 2;
    private Context context;
    String loadurl;
    int num_of_strings;
    Typeface typeface;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();
    AlertDialog ad;

    ArrayList<String> idlist = new ArrayList<>();


    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ChatMessage> values;
    ArrayList<String> key;
    private MyAdapter mAdapter;
    Boolean b = true;
     Typeface type;


    public MyAdapter(ArrayList<ChatMessage> myDataset, ArrayList<String> keylist,Context context1) {
        values = myDataset;
        key = keylist;
        context = context1;
    }





    @Override
    public int getItemViewType(int position) {
        ChatMessage message =  values.get(position);
        String pic = "https://firebasestorage.googleapis.com/";
        num_of_strings = pic.length();

        if(pic.length() <= message.getMessageText().length()){

        if (message.getMessageText().substring(0,pic.length()).equals(pic)) {
            // If the current user is the sender of the message
            return PHOTO_SEND;
        }
        }
            // If some other user sent the message
            return TEXT_MESSAGE;



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        if (viewType == PHOTO_SEND) {
            View v2 = inflater.inflate(R.layout.picture_row, viewGroup, false);
            viewHolder = new ViewHolder2(v2);

        } else if (viewType == TEXT_MESSAGE) {

            View v1 = inflater.inflate(R.layout.messagerow, viewGroup, false);
            viewHolder = new ViewHolder1(v1);
        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        switch (this.getItemViewType(position)) {
            case PHOTO_SEND:
                ViewHolder2 vh2 = (ViewHolder2) viewHolder;
                configureViewHolder2(vh2, position);
                break;
            case TEXT_MESSAGE:
                ViewHolder1 vh1 = (ViewHolder1) viewHolder;
                configureViewHolder1(vh1, position);
                break;
        }


    }

    private void configureViewHolder1(final ViewHolder1 vh1, int position)  {
        ChatMessage user = (ChatMessage) values.get(position);
        if (user != null) {
            vh1.getLabel1().setText(user.getMessageUser());
            vh1.message.setText(user.getMessageText());

            get_fonts(user.getUserid());
           // vh1.getTime().setText(user.getMessageTime());



            //retrieve the pictures from firebase
            databaseReference.child("useraccount").child(user.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                    String profile = usernameinfo.get("profilepicture");
                    Glide.with(context).load(profile).into(vh1.circleImageView);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            //retrieve the fonts from firebase
            databaseReference.child("fonts").child(user.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String font = dataSnapshot.getValue(String.class);


                    if(font != null) {
                        switch (font) {
                            case "aladin":
                                type = ResourcesCompat.getFont(context, R.font.aladin);
                                break;
                            case "fenix":
                                type = ResourcesCompat.getFont(context, R.font.fenix);
                                break;
                            case "codystar":
                                type = ResourcesCompat.getFont(context, R.font.codystar_light);
                                break;
                            case "adamina":
                                type = ResourcesCompat.getFont(context, R.font.adamina);
                                break;
                            case "pacifico":
                                type = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                            case "salsa":
                                type = ResourcesCompat.getFont(context, R.font.salsa);
                                break;
                            default:
                                type = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                        }

                    }
                    vh1.message.setTypeface(type);
                    vh1.getLabel1().setTypeface(type);
                    Log.i("test..", "testing");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }
    }


    private void configureViewHolder2(final ViewHolder2 vh2, int position) {
        ChatMessage user = (ChatMessage) values.get(position);
        if(user != null){
           // vh2.getTime().setText(user.getMessageTime());
            vh2.getUsername().setText(user.getMessageUser());
            String code = "ouitxh72n";
            if(user.getMessageText().contains(code)){
                int index = user.getMessageText().indexOf(code);
                String imagetext = user.getMessageText().substring(index + code.length());
                Log.i("final result  = ", imagetext);
                vh2.messages.setText(imagetext);
                Glide.with(context).load(user.getMessageText().substring(0,index)).into(vh2.ivExample);
            };





            databaseReference.child("useraccount").child(user.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                    String profile = usernameinfo.get("profilepicture");
                    Glide.with(context).load(profile).into(vh2.circleImageView);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    // messagerow holder
    public class ViewHolder2 extends RecyclerView.ViewHolder {

        private ImageView ivExample;
        private CircleImageView circleImageView;
        private TextView time, username, messages ;

        public ViewHolder2(View v) {
            super(v);
            circleImageView = (CircleImageView) v.findViewById(R.id.imageView);
            ivExample = (ImageView) v.findViewById(R.id.imageView10);
            time = (TextView) v.findViewById(R.id.textView4);
            username = (TextView) v.findViewById(R.id.textView2);
            messages = (TextView) v.findViewById(R.id.textView14);

        }

        public ImageView getIvExample() {
            return ivExample;
        }

        public void setIvExample(ImageView ivExample) {
            this.ivExample = ivExample;
        }

        public CircleImageView getCircleImageView() {
            return circleImageView;
        }

        public void setCircleImageView(CircleImageView circleImageView) {
            this.circleImageView = circleImageView;
        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public TextView getUsername() {
            return username;
        }

        public void setUsername(TextView username) {
            this.username = username;
        }

        public TextView getMessages() {
            return messages;
        }

        public void setMessages(TextView messages) {
            this.messages = messages;
        }

        public ImageView getImageView() {
            return ivExample;
        }

        public void setImageView(ImageView ivExample) {
            this.ivExample = ivExample;
        }
    }

      //photo row viewholder
    public class ViewHolder1 extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView label1, label2, message, time;
        private CircleImageView circleImageView;

        public ViewHolder1(View v) {
            super(v);

            label1 = (TextView) v.findViewById(R.id.textView2);
            time = (TextView) v.findViewById(R.id.textView4);
            circleImageView = (CircleImageView) v.findViewById(R.id.imageView);
            message = (TextView) v.findViewById(R.id.textView7);
            itemView.setOnClickListener(this);
            circleImageView.setOnClickListener(this);




        }

        public TextView getTime() {
            return time;
        }

        public void setTime(TextView time) {
            this.time = time;
        }

        public TextView getMessage() {
            return message;
        }

        public void setMessage(TextView message) {
            this.message = message;
        }

        public CircleImageView getCircleImageView() {
            return circleImageView;
        }

        public void setCircleImageView(CircleImageView circleImageView) {
            this.circleImageView = circleImageView;
        }

        public TextView getLabel1() {
            return label1;
        }

        public void setLabel1(TextView label1) {
            this.label1 = label1;
        }

        public TextView getLabel2() {
            return label2;
        }

        public void setLabel2(TextView label2) {
            this.label2 = label2;
        }

        @Override
        public void onClick(View v) {
            String id = firebaseUser.getUid();

            String idpos =  values.get(getAdapterPosition()).getUserid();

            if(idpos.equals(id)){
                // get_fonts(id);
                get_fonts(id);
                final AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                LayoutInflater li = LayoutInflater.from(itemView.getContext());
                final View Dialogview = li.inflate(R.layout.editdelete, null);
                alert.setTitle("Edit comment");
                alert.setView(Dialogview);
                final AlertDialog ad = alert.show();

                final ImageButton delete = (ImageButton) Dialogview.findViewById(R.id.imageButton2);
                final ImageButton edit = (ImageButton) Dialogview.findViewById(R.id.imageButton4);
                final EditText text = (EditText) Dialogview.findViewById(R.id.editText3);
                text.setTypeface(type);
                text.setText(values.get(getAdapterPosition()).getMessageText());



                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        databaseReference.child("message").child(key.get((getAdapterPosition()))).removeValue();

                        values.remove(getAdapterPosition());

                        notifyDataSetChanged();




                        ad.dismiss();

                    }});


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        values.get(getAdapterPosition()).setMessageText(text.getText().toString());

                        databaseReference.child("message").child(key.get((getAdapterPosition()))).setValue(values.get(getAdapterPosition()));



                        notifyDataSetChanged();

                        ad.dismiss();

                    }
                });
            }

            if(!values.get(getAdapterPosition()).getUserid().equals(firebaseUser.getUid())){
                final AlertDialog.Builder alert = new AlertDialog.Builder((AppCompatActivity)context);
                LayoutInflater li = LayoutInflater.from(((AppCompatActivity)context));
                final View Dialogview = li.inflate(R.layout.profile, null);
                alert.setView(Dialogview);
                ad = alert.show();

                final CircleImageView propic = (CircleImageView) Dialogview.findViewById(R.id.profile_image);
                final TextView messagetext = (TextView) Dialogview.findViewById(R.id.message);
                final TextView addtext = (TextView) Dialogview.findViewById(R.id.add);
                final ImageButton messageicon = (ImageButton) Dialogview.findViewById(R.id.imageButton6);

                databaseReference.child("useraccount").child(values.get(getAdapterPosition()).getUserid()).addListenerForSingleValueEvent(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> userinfo = (HashMap<String, String>) dataSnapshot.getValue();

                        loadurl = userinfo.get("profilepicture");
                        String name = userinfo.get("username");
                        Glide.with((AppCompatActivity)context).load(loadurl).into(propic);
                        addtext.setText("Add " + name);
                        messagetext.setText("Message " + name);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                addtext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        check_if_already_added_to_user_friendlist();
                    }
                });

                messageicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        privatemessagefragment();
                        ad.dismiss();
                    }
                });

                messagetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        privatemessagefragment();
                        ad.dismiss();
                    }
                });

            }




        }

        private void send_invitation_to_users(){
            if(b) {

                databaseReference.child("useraccount")
                        .child(values.get(getAdapterPosition()).getUserid()).child("invitaion").push().setValue(firebaseUser.getUid());

                databaseReference.child("useraccount")
                        .child(firebaseUser.getUid()).child("pending").push().setValue(values.get(getAdapterPosition()).getUserid())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toasty.error(context,"you sent " +"user "+ values.get(getAdapterPosition()).getMessageUser() + " a friend request",
                                        Toast.LENGTH_LONG,true).show();
                                HashMap<String,String>  hashMap = new HashMap<>();
                                hashMap.put("recieverid",values.get(getAdapterPosition()).getUserid());
                                hashMap.put("senderid", firebaseUser.getUid());
                                hashMap.put("type", "request");

                                databaseReference.child("request_notification").push().setValue(hashMap);

                            }
                        });
            }
        }

        private void check_if_already_added_to_user_friendlist(){
            final String id = values.get(getAdapterPosition()).getUserid();
            databaseReference.child("useraccount").child(firebaseUser.getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children : dataSnapshot.getChildren()){
                        if(id.equals(children.getValue())){
                            ad.dismiss();
                            Toasty.success(context,"user "+ values.get(getAdapterPosition()).getMessageUser() + " already is your friend",Toast.LENGTH_LONG,true).show();
                            return;
                        }
                    }
                    check_if_invitaion_is_still_pending();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void check_if_invitaion_is_still_pending(){
            idlist.clear();
            databaseReference.child("useraccount")
                    .child(firebaseUser.getUid()).child("pending").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children: dataSnapshot.getChildren()){
                        String id = children.getValue(String.class);
                        idlist.add(id);
                    }
                    if(idlist.size() > 0){
                        check_pendinglist();
                    } else {
                        check_if_invitaion_is_sent();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void check_if_invitaion_is_sent(){
            idlist.clear();
            databaseReference.child("useraccount")
                    .child(firebaseUser.getUid()).child("invitaion").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children: dataSnapshot.getChildren()) {
                        String id = children.getValue(String.class);
                        idlist.add(id);
                    }
                    if(idlist.size() > 0){
                        check_invitaionlist();
                    } else{
                        send_invitation_to_users();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void check_invitaionlist(){
            for(int i = 0; i < idlist.size(); i++){
                if(idlist.get(i).equals(values.get(getAdapterPosition()).getUserid())){

                    b = false;
                    Toasty.error(context,"user already sent an invitation", Toast.LENGTH_LONG,true).show();
                    ad.dismiss();
                    return;

                }
            }
            if(b){
                send_invitation_to_users();
            }

        }

        private void check_pendinglist(){
            Log.i("adapter click id = ", values.get(getAdapterPosition()).getUserid());
            for(int i = 0; i < idlist.size(); i++){
                if(idlist.get(i).equals(values.get(getAdapterPosition()).getUserid())){


                    b = false;
                    ad.dismiss();
                    Toasty.error(context,"you already sent a friend request to this user, request is pending", Toast.LENGTH_LONG,true).show();
                    return;

                }
            }
            if(b){
                send_invitation_to_users();
            }
            if(!b){
                check_if_invitaion_is_sent();
            }
        }

        private void privatemessagefragment(){
            Privatemessage privatemessage = new Privatemessage();
            FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("name", values.get(getAdapterPosition()).getMessageUser());
            bundle.putString("url", loadurl);
            bundle.putString("id", values.get(getAdapterPosition()).getUserid());
            privatemessage.setArguments(bundle);
            fragmentTransaction.replace(R.id.main,privatemessage);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        private void get_fonts(String userid){
            databaseReference.child("useraccount").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                    String profile = usernameinfo.get("profilepicture");
                    String font =  usernameinfo.get("fonts");


                    if(font != null) {
                        switch (font) {
                            case "aladin":
                                type = ResourcesCompat.getFont(context, R.font.aladin);
                                break;
                            case "fenix":
                                type = ResourcesCompat.getFont(context, R.font.fenix);
                                break;
                            case "codystar":
                                type = ResourcesCompat.getFont(context, R.font.codystar_light);
                                break;
                            case "adamina":
                                type = ResourcesCompat.getFont(context, R.font.adamina);
                                break;
                            case "pacifico":
                                type = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                            case "salsa":
                                type = ResourcesCompat.getFont(context, R.font.salsa);
                                break;
                            default:
                                type = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                        }

                    }

                    //message.setTypeface(type);
                    //username.setTypeface(type);
                    Log.i("test..", "testing");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        }


    @Override
    public long getItemId(int position) {
        return position;
    }
    private void get_fonts(String userid){
        databaseReference.child("useraccount").child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                String profile = usernameinfo.get("profilepicture");
                String font =  usernameinfo.get("fonts");


                if(font != null) {
                    switch (font) {
                        case "aladin":
                            type = ResourcesCompat.getFont(context, R.font.aladin);
                            break;
                        case "fenix":
                            type = ResourcesCompat.getFont(context, R.font.fenix);
                            break;
                        case "codystar":
                            type = ResourcesCompat.getFont(context, R.font.codystar_light);
                            break;
                        case "adamina":
                            type = ResourcesCompat.getFont(context, R.font.adamina);
                            break;
                        case "pacifico":
                            type = ResourcesCompat.getFont(context, R.font.pacifico);
                            break;
                        case "salsa":
                            type = ResourcesCompat.getFont(context, R.font.salsa);
                            break;
                        default:
                            type = ResourcesCompat.getFont(context, R.font.pacifico);
                            break;
                    }

                }


                Log.i("test..", "testing");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

/*
    private class SentPhotoHolder  extends RecyclerView.ViewHolder  {

        public TextView timee;
        public TextView username;
        public ImageView image;
        public CircleImageView propic;

        public SentPhotoHolder(View itemView) {
            super(itemView);
            propic = itemView.findViewById(R.id.imageView);
            image = itemView.findViewById(R.id.imageView10);
            username = itemView.findViewById(R.id.textView2);
            timee = itemView.findViewById(R.id.textView4);

            //get_fonts();

        }

        void bind(ChatMessage message) {
            username.setText(message.getMessageUser());
            timee.setText(message.getMessageTime());
            Glide.with(context).load(message.getMessageText()).into(image);
        }


    }


    private class Messageholder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView message;
        public TextView timee;
        public TextView username;
        public CircleImageView propic;

        public Messageholder(View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.textView7);
            timee = itemView.findViewById(R.id.textView4);
            username = itemView.findViewById(R.id.textView2);
            propic = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            propic.setOnClickListener(this);
        }

        public void bind(ChatMessage msg) {
            databaseReference.child("useraccount").child(msg.getUserid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Usernameinfo usernameinfo = dataSnapshot.getValue(Usernameinfo.class);
                    String pic = usernameinfo.getProfilepicture();
                    Glide.with(context).load(pic).into(propic);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //message.setTypeface(msg.getFonts());
           // username.setTypeface(msg.getFonts());
            message.setText(msg.getMessageText());
            timee.setText(msg.getMessageTime());
            username.setText(msg.getMessageUser());


        }

        @Override
        public void onClick(View v) {
            String id = firebaseUser.getUid();

            String idpos =  values.get(getAdapterPosition()).getUserid();

            if(idpos.equals(id)){
               // get_fonts(id);
                final AlertDialog.Builder alert = new AlertDialog.Builder(itemView.getContext());
                LayoutInflater li = LayoutInflater.from(itemView.getContext());
                final View Dialogview = li.inflate(R.layout.editdelete, null);
                alert.setTitle("Edit comment");
                alert.setView(Dialogview);
                final AlertDialog ad = alert.show();

                final ImageButton delete = (ImageButton) Dialogview.findViewById(R.id.imageButton2);
                final ImageButton edit = (ImageButton) Dialogview.findViewById(R.id.imageButton4);
                final EditText text = (EditText) Dialogview.findViewById(R.id.editText3);
                text.setTypeface(type);
                text.setText(values.get(getAdapterPosition()).getMessageText());



                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        databaseReference.child("message").child(key.get((getAdapterPosition()))).removeValue();

                        values.remove(getAdapterPosition());

                        notifyDataSetChanged();




                        ad.dismiss();

                    }});


                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        values.get(getAdapterPosition()).setMessageText(text.getText().toString());

                        databaseReference.child("message").child(key.get((getAdapterPosition()))).setValue(values.get(getAdapterPosition()));



                        notifyDataSetChanged();

                        ad.dismiss();

                    }
                });
            }

            if(!values.get(getAdapterPosition()).getUserid().equals(firebaseUser.getUid())){
                final AlertDialog.Builder alert = new AlertDialog.Builder((AppCompatActivity)context);
                LayoutInflater li = LayoutInflater.from(((AppCompatActivity)context));
                final View Dialogview = li.inflate(R.layout.profile, null);
                alert.setView(Dialogview);
                ad = alert.show();

                final CircleImageView propic = (CircleImageView) Dialogview.findViewById(R.id.profile_image);
                final TextView messagetext = (TextView) Dialogview.findViewById(R.id.message);
                final TextView addtext = (TextView) Dialogview.findViewById(R.id.add);
                final ImageButton messageicon = (ImageButton) Dialogview.findViewById(R.id.imageButton6);

                databaseReference.child("useraccount").child(values.get(getAdapterPosition()).getUserid()).addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, String> userinfo = (HashMap<String, String>) dataSnapshot.getValue();

                        loadurl = userinfo.get("profilepicture");
                        String name = userinfo.get("username");
                        Glide.with((AppCompatActivity)context).load(loadurl).into(propic);
                        addtext.setText("Add " + name);
                        messagetext.setText("Message " + name);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                addtext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        check_if_already_added_to_user_friendlist();
                    }
                });

                messageicon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        privatemessagefragment();
                        ad.dismiss();
                    }
                });

                messagetext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        privatemessagefragment();
                        ad.dismiss();
                    }
                });

            }




        }

        private void send_invitation_to_users(){
            if(b) {

                databaseReference.child("useraccount")
                        .child(values.get(getAdapterPosition()).getUserid()).child("invitaion").push().setValue(firebaseUser.getUid());

                databaseReference.child("useraccount")
                        .child(firebaseUser.getUid()).child("pending").push().setValue(values.get(getAdapterPosition()).getUserid())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toasty.error(context,"you sent " +"user "+ values.get(getAdapterPosition()).getMessageUser() + " a friend request",
                                        Toast.LENGTH_LONG,true).show();
                                HashMap<String,String>  hashMap = new HashMap<>();
                                hashMap.put("recieverid",values.get(getAdapterPosition()).getUserid());
                                hashMap.put("senderid", firebaseUser.getUid());
                                hashMap.put("type", "request");

                                databaseReference.child("request_notification").push().setValue(hashMap);

                            }
                        });
            }
        }

        private void check_if_already_added_to_user_friendlist(){
            final String id = values.get(getAdapterPosition()).getUserid();
            databaseReference.child("useraccount").child(firebaseUser.getUid()).child("friends").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children : dataSnapshot.getChildren()){
                        if(id.equals(children.getValue())){
                            ad.dismiss();
                            Toasty.success(context,"user "+ values.get(getAdapterPosition()).getMessageUser() + " already is your friend",Toast.LENGTH_LONG,true).show();
                            return;
                        }
                    }
                    check_if_invitaion_is_still_pending();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void check_if_invitaion_is_still_pending(){
            idlist.clear();
            databaseReference.child("useraccount")
                    .child(firebaseUser.getUid()).child("pending").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children: dataSnapshot.getChildren()){
                        String id = children.getValue(String.class);
                        idlist.add(id);
                    }
                    if(idlist.size() > 0){
                        check_pendinglist();
                    } else {
                        check_if_invitaion_is_sent();
                    }
                }


                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        private void check_if_invitaion_is_sent(){
            idlist.clear();
            databaseReference.child("useraccount")
                    .child(firebaseUser.getUid()).child("invitaion").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot children: dataSnapshot.getChildren()) {
                        String id = children.getValue(String.class);
                        idlist.add(id);
                    }
                    if(idlist.size() > 0){
                        check_invitaionlist();
                    } else{
                        send_invitation_to_users();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        private void check_invitaionlist(){
            for(int i = 0; i < idlist.size(); i++){
                if(idlist.get(i).equals(values.get(getAdapterPosition()).getUserid())){

                    b = false;
                    Toasty.error(context,"user already sent an invitation", Toast.LENGTH_LONG,true).show();
                    ad.dismiss();
                    return;

                }
            }
            if(b){
                send_invitation_to_users();
            }

        }

        private void check_pendinglist(){
            Log.i("adapter click id = ", values.get(getAdapterPosition()).getUserid());
            for(int i = 0; i < idlist.size(); i++){
                if(idlist.get(i).equals(values.get(getAdapterPosition()).getUserid())){


                    b = false;
                    ad.dismiss();
                    Toasty.error(context,"you already sent a friend request to this user, request is pending", Toast.LENGTH_LONG,true).show();
                    return;

                }
            }
            if(b){
                send_invitation_to_users();
            }
            if(!b){
                check_if_invitaion_is_sent();
            }
        }

        private void privatemessagefragment(){
            Privatemessage privatemessage = new Privatemessage();
            FragmentTransaction fragmentTransaction = ((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("name", values.get(getAdapterPosition()).getMessageUser());
            bundle.putString("url", loadurl);
            bundle.putString("id", values.get(getAdapterPosition()).getUserid());
            privatemessage.setArguments(bundle);
            fragmentTransaction.replace(R.id.main,privatemessage);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

       /* private void get_fonts(String userid){
            databaseReference.child("useraccount").child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                    String profile = usernameinfo.get("profilepicture");
                    String font =  usernameinfo.get("fonts");


                    if(font != null) {
                        switch (font) {
                            case "aladin":
                                type = ResourcesCompat.getFont(context, R.font.aladin);
                                break;
                            case "fenix":
                                type = ResourcesCompat.getFont(context, R.font.fenix);
                                break;
                            case "codystar":
                                type = ResourcesCompat.getFont(context, R.font.codystar_light);
                                break;
                            case "adamina":
                                type = ResourcesCompat.getFont(context, R.font.adamina);
                                break;
                            case "pacifico":
                                type = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                            case "salsa":
                                type = ResourcesCompat.getFont(context, R.font.salsa);
                                break;
                            default:
                                type = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                        }

                    }
                    Glide.with(context).load(profile).into(propic);

                    message.setTypeface(type);
                    username.setTypeface(type);
                    Log.i("test..", "testing");
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }*/


        }




