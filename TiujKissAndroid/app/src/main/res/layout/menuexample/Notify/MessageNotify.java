package com.mehme.menuexample.Notify;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.mehme.menuexample.*;
import com.mehme.menuexample.Service.MessageNotifyService;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mehme on 27.06.2016.
 */
public class MessageNotify extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public MessageNotify() {
        super(com.mehme.menuexample.Notify.MessageNotify.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context , String data) {
        Intent intent = new Intent(context, com.mehme.menuexample.Notify.MessageNotify.class);
        intent.setAction(ACTION_START);
        intent.putExtra("Data", data);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, com.mehme.menuexample.Notify.MessageNotify.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                String data = intent.getStringExtra("Data");
                processStartNotification(data);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartNotification(String response) {
        try {
            JSONObject data = new JSONObject(response);

            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
           // byte[] pImage = Base64.decode(data.getString("NotifyOwnerImage"), Base64.DEFAULT);
            //Bitmap bm = BitmapFactory.decodeByteArray(pImage, 0, pImage.length);
            builder.setContentTitle("Scheduled Notification")
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setContentText(data.getString("Message"))
                    //.setLargeIcon(bm)
                    .setSmallIcon(R.drawable.notification_icon);

            //          .setContentIntent(contentIntent); // The intent to send when the entry is clicked);

            Intent mainIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    NOTIFICATION_ID,
                    mainIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            builder.setDeleteIntent(MessageNotifyService.getDeleteIntent(this));

            final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());
        }
        catch (JSONException e) {

        }

    }
}
