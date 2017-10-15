package goodapp.company.kissmyass.Notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import goodapp.company.kissmyass.TiujKiss.Activities.MessageActivity;
import goodapp.company.kissmyass.TiujKiss.Activities.ProfileActivity;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.Services.MessageNotifyService;

/**
 * Created by mehme on 27.06.2016.
 */
public class MessageNotify extends IntentService {

    private static int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public MessageNotify() {
        super(MessageNotify.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context, String data) {
        Intent intent = new Intent(context, MessageNotify.class);
        intent.setAction(ACTION_START);
        intent.putExtra("Data", data);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, MessageNotify.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                String data = intent.getStringExtra("Data");
                processStartMessageNotification(data);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
        // Log something?
    }

    private void processStartMessageNotification(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                NOTIFICATION_ID++;
                JSONObject data = (JSONObject) jsonArray.get(i);
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

                builder.setContentTitle(data.getString("Name") +" "+ data.getString("Surname"))
                        .setAutoCancel(true)
                        .setColor(getResources().getColor(R.color.SlateGrey))

                        .setSmallIcon(R.drawable.notification_icon);
                if(!data.getString("OwnerImage").equals("null")) {
                    byte[] pImage = Base64.decode(data.getString("OwnerImage"), Base64.DEFAULT);
                    Bitmap bm = BitmapFactory.decodeByteArray(pImage, 0, pImage.length);
                    builder.setLargeIcon(bm);
                }

                Intent mainIntent;
                if (data.getInt("Type") == 0) {
                    mainIntent = new Intent(this, MessageActivity.class);
                    mainIntent.putExtra("SenderId", data.getString("FromUserID"));
                    builder.setContentText(data.getString("Content"));
                } else {
                    mainIntent = new Intent(this, ProfileActivity.class);
                    mainIntent.putExtra("SenderId", data.getString("FromUserID"));
                    builder.setContentText(getResources().getString(R.string.kissed_your_ass));//"This User " + data.getString("Content") + "ed your ass.");
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                builder.setDeleteIntent(MessageNotifyService.getDeleteIntent(this));
                final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(NOTIFICATION_ID, builder.build());
            }
        } catch (JSONException e) {
        }
    }
/*
    private void processStartFeedbackNotification(String response) {
        NOTIFICATION_ID++;
        try {
            JSONObject data = new JSONObject(response);
            final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
            byte[] pImage = Base64.decode(data.getString("NotifyOwnerImage"), Base64.DEFAULT);
            Bitmap bm = BitmapFactory.decodeByteArray(pImage, 0, pImage.length);
            builder.setContentTitle(data.getString("Name") + data.getString("Surname"))
                    .setAutoCancel(true)
                    .setColor(getResources().getColor(R.color.SlateGrey))
                    .setContentText("This user" + data.getString("Event") + " your profile. ")
                    .setLargeIcon(bm)
                    .setSmallIcon(R.drawable.notification_icon);
            Intent mainIntent = new Intent(this, ProfileActivity.class);
            mainIntent.putExtra("UserId", data.getString("OwnerId"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, mainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            builder.setDeleteIntent(MessageNotifyService.getDeleteIntent(this));
            final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.notify(NOTIFICATION_ID, builder.build());
        } catch (JSONException e) {
        }
    }
    */
}
