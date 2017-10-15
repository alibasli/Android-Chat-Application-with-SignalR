package goodapp.company.kissmyass.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import goodapp.company.kissmyass.Class.NetworkUtil;
import goodapp.company.kissmyass.Services.MessageNotifyService;

/**
 * Created by mehme on 17.07.2016.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        int status = NetworkUtil.getConnectivityStatusString(context);

        if (!"android.net.conn.CONNECTIVITY_CHANGE".equals(intent.getAction())) {
            if(status==NetworkUtil.NETWORK_STATUS_NOT_CONNECTED){
                MessageNotifyService.cancelAlarm(context);
                return;
            }else{
                MessageNotifyService.setupAlarm(context);
                return;
            }
        }else
            MessageNotifyService.setupAlarm(context);
    }
}
