package com.mehme.menuexample.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by mehme on 27.06.2016.
 */
public final class NotifyServicesStarter extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       MessageNotifyService.setupAlarm(context);
        //NotificationEventReceiver.cancelAlarm(context);
    }

}
