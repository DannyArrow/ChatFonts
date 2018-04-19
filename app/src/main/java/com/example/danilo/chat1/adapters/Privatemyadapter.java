package com.example.danilo.chat1.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.danilo.chat1.ChatMessage;
import com.example.danilo.chat1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Danilo on 12/18/17.
 */

public class Privatemyadapter extends RecyclerView.Adapter {
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;
    Context context;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference =  database.getReference();


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    ArrayList<ChatMessage> data;

    public Privatemyadapter(ArrayList<ChatMessage> list, Context context) {
        data = list;
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        ChatMessage message =  data.get(position);

        if (message.getUserid().equals(user.getUid())) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChatMessage message =  data.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.image_message_profile);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getMessageText());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getMessageTime());


            // Insert the profile image from the URL into the ImageView.

            databaseReference.child("useraccount").child(message.getUserid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                    String profile = usernameinfo.get("profilepicture");
                    String name = usernameinfo.get("username");
                    String fonts = usernameinfo.get("fonts");
                    if(fonts != null) {
                        Typeface typeface;
                        switch (fonts) {
                            case "aladin":
                                typeface = ResourcesCompat.getFont(context, R.font.aladin);
                                break;
                            case "fenix":
                                typeface = ResourcesCompat.getFont(context, R.font.fenix);
                                break;
                            case "codystar":
                                typeface = ResourcesCompat.getFont(context, R.font.codystar_light);
                                break;
                            case "adamina":
                                typeface = ResourcesCompat.getFont(context, R.font.adamina);
                                break;
                            case "pacifico":
                                typeface = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                            case "salsa":
                                typeface = ResourcesCompat.getFont(context, R.font.salsa);
                                break;
                            default:
                                typeface = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                        }
                        messageText.setTypeface(typeface);
                    }

                    if (profile != null) {
                        nameText.setText(name);
                        Glide.with(context).load(profile).into(profileImage);

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;


        public SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.text_message_time);
        }

        void bind(ChatMessage message) {
            databaseReference.child("useraccount").child(message.getUserid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                    String fonts = usernameinfo.get("fonts");
                    if(fonts != null) {
                        Typeface typeface;
                        switch (fonts) {
                            case "aladin":
                                typeface = ResourcesCompat.getFont(context, R.font.aladin);
                                break;
                            case "fenix":
                                typeface = ResourcesCompat.getFont(context, R.font.fenix);
                                break;
                            case "codystar":
                                typeface = ResourcesCompat.getFont(context, R.font.codystar_light);
                                break;
                            case "adamina":
                                typeface = ResourcesCompat.getFont(context, R.font.adamina);
                                break;
                            case "pacifico":
                                typeface = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                            case "salsa":
                                typeface = ResourcesCompat.getFont(context, R.font.salsa);
                                break;
                            default:
                                typeface = ResourcesCompat.getFont(context, R.font.pacifico);
                                break;
                        }
                       messageText.setTypeface(typeface);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            messageText.setText(message.getMessageText());

            // Format the stored timestamp into a readable String using method.
            timeText.setText(message.getMessageTime());

        }
    }
}


