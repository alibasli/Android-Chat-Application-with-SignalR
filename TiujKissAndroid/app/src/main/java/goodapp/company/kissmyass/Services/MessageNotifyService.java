package goodapp.company.kissmyass.Services;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.SharedPreferences;

import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ClassSocketObject;
import goodapp.company.kissmyass.Notifications.MessageNotify;

import android.os.Handler;


/**
 * Created by mehme on 27.06.2016.
 */
public class MessageNotifyService extends WakefulBroadcastReceiver {

    private static final String ACTION_START_NOTIFICATION_SERVICE = "ACTION_START_NOTIFICATION_SERVICE";
    private static final String ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION";

    private static final int NOTIFICATIONS_INTERVAL_IN_SECONS = 10000; // 6 seconds

    private ClassSession.SessionUserInfo UserSession = null;
    SharedPreferences sharedpreferences;

    public static void setupAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                getTriggerAt(new Date()),
                NOTIFICATIONS_INTERVAL_IN_SECONS,//* alarmManager.INTERVAL_FIFTEEN_MINUTES,
                alarmIntent);
    }

    public static void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent alarmIntent = getStartPendingIntent(context);
        alarmManager.cancel(alarmIntent);
    }

    private static long getTriggerAt(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        //calendar.add(Calendar.HOUR, NOTIFICATIONS_INTERVAL_IN_SECONS);
        return calendar.getTimeInMillis();
    }

    private static PendingIntent getStartPendingIntent(Context context) {
        Intent intent = new Intent(context, MessageNotifyService.class);
        intent.setAction(ACTION_START_NOTIFICATION_SERVICE);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, MessageNotifyService.class);
        intent.setAction(ACTION_DELETE_NOTIFICATION);
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Intent serviceIntent = null;
        if (ACTION_START_NOTIFICATION_SERVICE.equals(action)) {
            //Log.i(getClass().getSimpleName(), "onReceive from alarm, starting notification service");
            //serviceIntent = NotificationIntentService.createIntentStartNotificationService(context);
            startHttpRequestForMessage(context);
            //startHttpRequestForFeedback(context);
        } else if (ACTION_DELETE_NOTIFICATION.equals(action)) {
            Log.i(getClass().getSimpleName(), "onReceive delete notification action, starting notification service to handle delete");
            serviceIntent = MessageNotify.createIntentDeleteNotification(context);
        }

        if (serviceIntent != null) {
            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, serviceIntent);

        }
    }

    private void startHttpRequestForMessage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("UserIDPref", context.MODE_PRIVATE);
        //Intent serviceIntent = null;

        try {
            JSONObject data = new JSONObject();
            String userId = prefs.getString("UserID", "");
            if (userId.equals(""))
                return;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            data.put("UserID",userId);
            data.put("DateTimeNow",format.format(new Date()) +".601");
            final Context cnt = context;
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/Notifies" , HttpRequest.POST, data.toString(), "Basic cmltYWxpQGdtYWlsLmNvbTpyaW1hbGlSZXNwb25zZQ=="),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                Intent serviceIntent = MessageNotify.createIntentStartNotificationService(cnt, response.body);
                                startWakefulService(cnt, serviceIntent);
                                return;
                            } else {
                                return;
                            }

                        }

                    }).execute();
        } catch (Exception e) {
            return;
        }
    }
    /*
    private void startHttpRequestForFeedback(Context context){
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            JSONObject login = new JSONObject();
            String userId = sharedpreferences.getString(UserSession.Id, "");
            if (sharedpreferences.getString(UserSession.Id, "").equals(""))
                return;
            final Context cnt = context;
            login.put("UserID", userId);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/notifyFeedback", HttpRequest.GET, login.toString() , "Basic cmltYWxpQGdtYWlsLmNvbTpyaW1hbGlSZXNwb25zZQ=="),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                Intent serviceIntent = MessageNotify.createIntentStartNotificationService(cnt, response.body,"Feedback");
                                startWakefulService(cnt, serviceIntent);
                                return;
                            } else {
                                return;
                            }
                        }

                    }).execute();
        }catch (Exception e){
            return;
        }
    }
    */
}
