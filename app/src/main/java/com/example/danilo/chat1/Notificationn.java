package com.example.danilo.chat1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Danilo on 12/18/17.
 */

public class Notificationn extends FirebaseMessagingService {
    public static String REPLY_ACTION = "com.example.danilo.chat1.service.Notificationn.REPLY_ACTION";
    public static String KEY_QUICK_REPLY = "key_reply_message";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = database.getReference();

    private static final String TAG = "FirebaseMessagingServce";
    int mNotificationId = 1;
    private int mMessageId = 123;
    Integer fontvalue;
    private String id;
    private String user;
    Integer list[] = {R.font.aladin,R.style.fenix,R.style.codystar,R.style.adamina,R.style.almendra,R.style.pacifico,R.style.palanquin_dark,R.style.salsa};
    String fontslist[] = {"aladin","fenix","codystar","adamina","almendra","pacifico","palanquin","salsa"};
    HashMap<String,Integer> map = new HashMap<>();
    String first;
    String second;
    private String name;
    int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        Log.i("log =", String.valueOf(data));
        String notificationTitle = null, notificationBody = null, photourl = null, type = null, senderid = null;
        Bitmap photo = null;

        type =  data.get("type");
        senderid = data.get("senderid");

        if(!type.isEmpty()){
            sendrequest(senderid);
            return;
        }
        notificationTitle = data.get("title");
        notificationBody = data.get("message");

        user = data.get("title");
        photourl = data.get("photo");
        photo = getBitmapfromUrl(photourl);
        id = data.get("sender");
        if(id != null){
            setfonts();
        }


        sendmessage(notificationTitle,notificationBody,photo);

    }

    private void sendrequest(String senderid) {

        databaseReference.child("useraccount").child(senderid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usernameinfo usernameinfo = dataSnapshot.getValue(Usernameinfo.class);
                 name =  usernameinfo.getUsername();
                if(name.length()> 1) {
                    Bitmap icon  = BitmapFactory.decodeResource(getResources(),  R.drawable.ic_person_add_3x);

                    android.support.v4.app.NotificationCompat.Builder builder =
                            new NotificationCompat.Builder(getApplicationContext())
                                    .setContentText("new friend request from " +name )
                                    .setLargeIcon(icon)
                                    .setSmallIcon(R.drawable.ic_message_black_24dp)
                                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                                    .setAutoCancel(true);


                    // Add as notification
                    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    manager.notify(Integer.parseInt(name), builder.build());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        }





    private void sendmessage(String notificationTitle, String notificationBody, Bitmap photo) {
        RemoteViews contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.custom_notification);
        int color = Color.BLACK;

        if (notificationBody.length() > 50){
         int half = notificationBody.length()/2;
         first = notificationBody.substring(0,half);
         second = notificationBody.substring(half);
         contentView.setImageViewBitmap(R.id.tvNotificationMessage, textAsBitmap(getApplicationContext(), first, color));
         contentView.setImageViewBitmap(R.id.imageView5, textAsBitmap(getApplicationContext(), second, color));
     }else {
            first = notificationBody;
        }

        // Set text on a TextView in the RemoteViews programmatically.
        contentView.setTextColor(R.id.tvNotificationTitle, ContextCompat.getColor(this, android.R.color.black));
        contentView.setTextViewText(R.id.tvNotificationTitle, notificationTitle);
        contentView.setImageViewBitmap(R.id.ivNotificationImage,photo);
        contentView.setImageViewBitmap(R.id.tvNotificationMessage, textAsBitmap(getApplicationContext(), first, color));
        contentView.setViewVisibility(R.id.imageView5, View.INVISIBLE);






        String replyLabel = "Reply to message";
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_QUICK_REPLY).setLabel(replyLabel).build();

        android.support.v4.app.NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.ic_reply_black_24dp, replyLabel,getReplyPendingIntent())
                        .addRemoteInput(remoteInput)
                        .setAllowGeneratedReplies(true).build();

        android.support.v4.app.NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_message_black_24dp)
                        .addAction(action)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setCustomContentView(contentView)
                        .setAutoCancel(true);




        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(Integer.parseInt(id), builder.build());


    }

    private PendingIntent getReplyPendingIntent() {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // start a
            // (i)  broadcast receiver which runs on the UI thread or
            // (ii) service for a background task to b executed , but for the purpose of this codelab, will be doing a broadcast receiver
            intent = NotificationBroadcastReceiver.getReplyMessageIntent(this, mNotificationId, mMessageId,id,user);
            return PendingIntent.getBroadcast(getApplicationContext(), 100, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            // start your activity
            intent = ReplyActivity.getReplyMessageIntent(this, mNotificationId, mMessageId,id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            return PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
    }

    public static CharSequence getReplyMessage(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_QUICK_REPLY);
        }
        return null;

    }


    private Bitmap getBitmapfromUrl(String image){
        try {
            URL url = new URL(image);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    public  Bitmap textAsBitmap(Context context, String text, int textColor) {

        String fontName = "digital-7";
        float textSize = 50;
        Typeface font = ResourcesCompat.getFont(context, R.font.pacifico);
        Paint paint = new Paint();
        paint.setTextSize(textSize);
        paint.setTypeface(font);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.5f); // round
        int height = (int) (baseline + paint.descent() + 20f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);

        return image;
    }

    private void setfonts() {
        map.clear();
        for(int i = 0; i < list.length; i++){
            map.put(fontslist[i],list[i]);
        }
        fetch_front();

    }

    private void fetch_front(){
        databaseReference.child("useraccount").child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,String> usernameinfo = (HashMap<String, String>) dataSnapshot.getValue();
                String fontfetch =  usernameinfo.get("fonts");
              fontvalue =  map.get(fontfetch);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
