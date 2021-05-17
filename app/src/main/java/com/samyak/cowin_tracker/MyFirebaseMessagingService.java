package com.samyak.cowin_tracker;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            String date = remoteMessage.getData().get("date");
            String pincode = remoteMessage.getData().get("pincode");
            String slottype = remoteMessage.getData().get("slot_type");


            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Title: " + remoteMessage.getNotification().getTitle());
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

//                Intent intent = new Intent(this, MainActivity.class);
//                intent.putExtra("date", date);
//                intent.putExtra("pincode", pincode);
//                intent.putExtra("slot_type", slottype);

            }


        }

    }

}
