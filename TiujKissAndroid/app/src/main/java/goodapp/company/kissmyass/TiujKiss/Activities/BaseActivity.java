package goodapp.company.kissmyass.TiujKiss.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import goodapp.company.kissmyass.BuildConfig;
import goodapp.company.kissmyass.Class.ClassConnectionDetector;
import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.VersionChecker;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.SignalR.Connection;
import goodapp.company.kissmyass.Tasks.SignalRConnection;
import goodapp.company.kissmyass.TiujKiss.TiujKissR;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;

/**
 * Created by mehme on 3.08.2016.
 */
public class BaseActivity extends Activity {

    public NetworkInfo Wifi;
    public NetworkInfo MobileData;
    private SharedPreferences prefs;

    public final int SPLASH_DISPLAY_LENGTH = 600;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        currentActivity = this;
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkConnection();
    }

    @Override
    protected void onResume() {
        super.onResume();
        currentActivity = this;
        final Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
                if (HubConnection != null) {
                    checkSessionIsNull();
                } else {
                    h.postDelayed(this, 500);
                }
            }
        }, 200);
    }

    private void checkConnection() {
        final ClassConnectionDetector cd = new ClassConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            final ProgressDialog dialog = ProgressDialog.show(this, "Waiting Connection", "Loading....", true);
            dialog.show();
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    if (!cd.isConnectingToInternet()) {
                        h.postDelayed(this, 100);
                    } else {
                        dialog.dismiss();
                        if (!checkVersion())
                            connectToSignalR();
                    }
                }
            }, 100);
        } else {
            if (!checkVersion())
                connectToSignalR();
        }
    }

    private boolean checkVersion() {
        try {
            VersionChecker versionChecker = new VersionChecker();
            String latestVersion = versionChecker.execute().get();
            String versionName = BuildConfig.VERSION_NAME;
            if (latestVersion.equals(versionName))
                return false;
            else {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    finish();
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=goodapp.company.kiss")));
                    finish();
                }
                return true;
            }
        } catch (Exception ex) {
            return false;
        }
    }

    private void connectToSignalR() {
        prefs = getSharedPreferences("UserIDPref", MODE_PRIVATE);
        ConnectivityManager mgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        Wifi = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        MobileData = mgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        Object[] TaskParams = {ClassSession.SOCKET_URL, currentActivity, Wifi, MobileData};
        SignalRConnection ConnectionTask = new SignalRConnection();
        try {
            if (HubConnection == null) {
                HubConnection = (Connection) ConnectionTask.execute(TaskParams).get();
            }
        } catch (InterruptedException | ExecutionException ex) {
            Toast.makeText(this, "Unable to connect to web service!", Toast.LENGTH_LONG).show();
        }
        // repo = new DBManager(currentActivity);
        // repo.open();

    }

    private void goLoginPage() {
        Intent mainIntent = new Intent(BaseActivity.this, NLoginActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void checkSessionIsNull() {
        if (prefs.getString("UserID", "^") == "^") {
            goLoginPage();
        } else {
            HubConnection.getUserById(prefs.getString("UserID", "^"))
                    .done(new Action<User>() {
                        @Override
                        public void run(User user) {
                            if (user != null)
                                loginSuccess(user);

                            else
                                goLoginPage();
                        }
                    });
        }

    }

    public void loginSuccess(User user) {
        if (prefs.getString("UserID", "^") == "^") {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserID", user.getUserID());
            editor.apply();
        }
        currentUser = user;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

}

