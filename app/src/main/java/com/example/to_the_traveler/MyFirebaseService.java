package com.example.to_the_traveler;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // בדיקה אם ההודעה מכילה נתונים
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
        }

        // בדיקה אם ההודעה מכילה הודעה רגילה
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d(TAG, "New token: " + token);
        // שלח את ה-Token לשרת שלך אם נדרש
    }
}
