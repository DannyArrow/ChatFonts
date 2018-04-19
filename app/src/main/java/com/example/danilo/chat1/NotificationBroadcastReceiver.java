package com.example.danilo.chat1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import static com.example.danilo.chat1.Notificationn.REPLY_ACTION;

/**
 * Created by Danilo on 3/5/18.
 */

public class NotificationBroadcastReceiver extends BroadcastReceiver {

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();
    private static Usernameinfo userinformation;
    private static Usernameinfo currentuser;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    CharSequence message;
    private static String KEY_NOTIFICATION_ID = "key_noticiation_id";
    private static String KEY_MESSAGE_ID = "key_message_id";
    private static String Sender_id = "sender_id";
    private static String Username = "user_id";
    static Context context1;
    DatabaseReference myref = firebaseDatabase.getReference();
    DatabaseReference checkref = firebaseDatabase.getReference().child("useraccount").child(user.getUid()).child("private_messages");
    String key;
     String id;
     String username;

    public static Intent getReplyMessageIntent(Context context, int mNotificationId, int mMessageId, String id, String user) {
        context1 = context;
        Intent intent = new Intent(context, NotificationBroadcastReceiver.class);
        intent.setAction(REPLY_ACTION);
        intent.putExtra(KEY_NOTIFICATION_ID, mNotificationId);
        intent.putExtra(KEY_MESSAGE_ID, mMessageId);
        intent.putExtra(Sender_id, id);
        intent.putExtra(Username,user);
        return intent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (REPLY_ACTION.equals(intent.getAction())) {
            context1 = context;
            // do whatever you want with the message. Send to the server or add to the db.
            // for this tutorial, we'll just show it in a toast;
            message = Notificationn.getReplyMessage(intent);
            int messageId = intent.getIntExtra(KEY_MESSAGE_ID, 0);
            id = intent.getStringExtra(Sender_id);
            username = intent.getStringExtra(Username);

            Toast.makeText(context, "Message ID: " + messageId + "\nMessage: " + message,
                    Toast.LENGTH_SHORT).show();

            // update notification
            int notifyId = intent.getIntExtra(KEY_NOTIFICATION_ID, 1);
            updateNotification(context, notifyId);
        }
    }
    private void updateNotification(Context context, int notifyId) {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_send_black_24dp)
                .setContentText("sent");

        notificationManager.notify(notifyId, builder.build());
        sendmessagetocloud();
    }

    private void sendmessagetocloud() {

        myref.child("useraccount").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentuser = dataSnapshot.getValue(Usernameinfo.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.child("useraccount").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userinformation = dataSnapshot.getValue(Usernameinfo.class);
                HashMap<String,String> notification = new HashMap<>();
                notification.put("message", String.valueOf(message));
                notification.put("name",currentuser.getUsername());
                notification.put("profilepic", currentuser.getProfilepicture());
                notification.put("recieveid", id);
                notification.put("senderid",user.getUid());

                myref.child("notificationRequests").push().setValue(notification);

                getkey();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });}

    private void getkey() {
        checkref.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot eachmessage : dataSnapshot.getChildren()) {
                    Log.i("key --", eachmessage.getKey());

                    if (eachmessage.getKey().equalsIgnoreCase(id)) {
                        key = eachmessage.getValue(String.class);
                        Log.i("lobbyid", key);
                        break;
                    } else {
                        //key = userrefrence.push().getKey();
                    }
                }
                sendmessagedatabase();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }

    private void sendmessagedatabase(){
        if(key == null){
            return;
        }
        ChatMessage comment = new ChatMessage(String.valueOf(message), currentuser.getUsername(), user.getUid(), user.getUid());

        myref.child("useraccount").child(id).child("private_messages").child(user.getUid()).setValue(key);
        myref.child("useraccount").child(user.getUid()).child("private_messages").child(id).setValue(key);
        myref.child("useraccount").child(id).child("messangecount").child(user.getUid()).push().setValue("");
        myref.child("private_messages").child(key).push().setValue(comment);

    }





}
