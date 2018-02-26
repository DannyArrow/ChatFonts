package com.example.danilo.chat1;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Danilo on 12/18/17.
 */

public class Notificationn extends FirebaseMessagingService {
    private static final String TAG = "FirebaseMessagingServce";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String notificationTitle = null, notificationBody = null;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getTitle());

            notificationTitle = remoteMessage.getNotification().getTitle();
            notificationBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().get("title"));
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getData().get("message"));


        }



        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
       // sendNotification(notificationTitle, notificationBody);
        sendmessage(notificationTitle,notificationBody);

    }

    private void sendmessage(String notificationTitle, String notificationBody) {
        /*NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle(notificationTitle);
        mBuilder.setContentText(notificationBody);
        Intent notificationIntent = new Intent(this, PrivateLobby.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setico*/


    }
}
