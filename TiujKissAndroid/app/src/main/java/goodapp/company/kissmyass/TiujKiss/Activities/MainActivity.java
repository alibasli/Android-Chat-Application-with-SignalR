package goodapp.company.kissmyass.TiujKiss.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.kobakei.ratethisapp.RateThisApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import goodapp.company.kissmyass.Adapters.AdapterSearch;
import goodapp.company.kissmyass.BuildConfig;
import goodapp.company.kissmyass.Class.ClassConnectionDetector;
import goodapp.company.kissmyass.Class.ClassImagePickerWithCrop;
import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.VersionChecker;
import goodapp.company.kissmyass.Models.Feedback;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.Models.Users;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.Services.MessageNotifyService;
import goodapp.company.kissmyass.TiujKiss.Tabs.Pager;
import goodapp.company.kissmyass.TiujKiss.Tabs.Tab2Chats;
import goodapp.company.kissmyass.TiujKiss.Tabs.Tab3Feedback;
import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.chats;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.feedbacks;



/**
 * Created by mehme on 1.08.2016.
 */
public class MainActivity extends AppCompatActivity {

    public TabLayout tabLayout;
    //This is our viewPager
    private ViewPager viewPager;
    private Pager adapter;
    public SearchView searchView;

    ProgressDialog dialog;
    public int responsed = 0;
    public String responseData;
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        currentActivity = this;
        HubConnection.getServerTimezone();
        HubConnection.getFeedbacks();
        HubConnection.getChats();
        HubConnection.getAllUserMessages();

        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.Home)).setIcon(R.drawable.homeicon));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.Messages)).setIcon(R.drawable.messageicon2));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.Feedbacks)).setIcon(R.drawable.feedbackicon2));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.Profile)).setIcon(R.drawable.profileicon2));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setBackgroundResource(R.drawable.border_top_slade_green);
        tabLayout.setTabTextColors(getResources().getColor(R.color.SlateGrey), getResources().getColor(R.color.colorAccent));
        //tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.White));
        viewPager = (ViewPager) findViewById(R.id.pager);
        adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        viewPager.addOnPageChangeListener(onPageChangeListener);
        tabLayout.setOnTabSelectedListener(tabClickListener);
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        rateTheApp();
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onResume(){
        super.onResume();
        currentActivity = this;
        ((Tab2Chats)adapter.getItem(1)).populateChats();
        ((Tab3Feedback) adapter.getItem(2)).populateFeedbacks();
     /*   new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {

                return null;
            }
        }.execute();*/

    }
    public void rateTheApp() {
        RateThisApp.onStart(currentActivity);
        RateThisApp.showRateDialogIfNeeded(currentActivity);

        AdView adView = (AdView) MainActivity.this.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private TabLayout.OnTabSelectedListener tabClickListener = new TabLayout.OnTabSelectedListener() {

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
            if (tab.getText().toString() == getString(R.string.Home))
                tab.setIcon(R.drawable.homeicon);
            else if (tab.getText().toString() == getString(R.string.Messages)) {
                tab.setIcon(R.drawable.messageicon);
                ((Tab2Chats)adapter.getItem(1)).populateChats();
            }
            else if (tab.getText().toString() == getString(R.string.Feedbacks)) {
                tab.setIcon(R.drawable.feedbackicon);
                ((Tab3Feedback) adapter.getItem(2)).populateFeedbacks();
            }
            else if (tab.getText().toString() == getString(R.string.Profile))
                tab.setIcon(R.drawable.profileicon);

            tabLayout.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            if (tab.getText().toString() == getString(R.string.Home))
                tab.setIcon(R.drawable.homeicon2);
            else if (tab.getText().toString() == getString(R.string.Messages))
                tab.setIcon(R.drawable.messageicon2);
            else if (tab.getText().toString() == getString(R.string.Feedbacks))
                tab.setIcon(R.drawable.feedbackicon2);
            else if (tab.getText().toString() == getString(R.string.Profile))
                tab.setIcon(R.drawable.profileicon2);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // viewPager.setCurrentItem(position);

        }

        @Override
        public void onPageSelected(int position) {
            // viewPager.setCurrentItem(position);
            tabLayout.getTabAt(position).select();
            //onTabSelected(tabLayout.getTabAt(position));
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final View search_view = (View) findViewById(R.id.search_view);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();
        searchView.setBackgroundResource(R.color.White);
        searchView.setQueryHint(getString(R.string.search));
        searchView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View arg0) {
                // search was detached/closed
                search_view.setVisibility(View.GONE);
            }

            @Override
            public void onViewAttachedToWindow(View arg0) {
                // search was opened
                search_view.setVisibility(View.VISIBLE);
                ListView feedback_list = (ListView) findViewById(R.id.list_search);
                feedback_list.setAdapter(null);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
               // if (query != null)
                   // getSearchUsers(query);
                  //  requesterToServerForSearch(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                String query = searchView.getQuery().toString();
                if (!query.equals(""))
                    getSearchUsers(query);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

     /*   if (id == R.id.action_settings) {
            //Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            //startActivity(intent);
        } else if (id == R.id.action_logout) {
            MessageNotifyService.cancelAlarm(getApplicationContext());
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK && requestCode == ClassImagePickerWithCrop.REQUEST_PICK) {
            ClassImagePickerWithCrop.beginCrop(this, resultCode, data);
        } else if (requestCode == ClassImagePickerWithCrop.REQUEST_CROP) {
            Bitmap bitmap = ClassImagePickerWithCrop.getImageCropped(this, resultCode, data,
                    ClassImagePickerWithCrop.ResizeType.FIXED_SIZE, 600);//AVATAR_SIZE);
            if (bitmap != null) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Image", encodeTobase64(bitmap));
                    showDialog("Image Upload","Please Wait...");
                    if (httpRequest("Api/AddUserPhoto", jsonObject, "POST")) {
                        final Handler h = new Handler();
                        h.postDelayed(new Runnable() {
                            public void run() {
                                if (responsed == 1) {
                                    responsed = 0;
                                    dismissDialog();
                                    //reloadTab();
                                    HubConnection.getUserById(currentUser.UserID)
                                            .done(new Action<User>() {
                                                @Override
                                                public void run(User user) {
                                                    currentUser = user;
                                                    reloadTab();
                                                }
                                            });
                                } else if (responsed == 2) {
                                    responsed = 0;
                                    dismissDialog();
                                    Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                                    //TODO request failed
                                } else {
                                    h.postDelayed(this, 1000);
                                }
                            }
                        }, 1000);
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 90, baos);
        //return baos.toByteArray();
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        final View mProgressView = findViewById(R.id.login_progress);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            viewPager.setVisibility(show ? View.GONE : View.VISIBLE);
            viewPager.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    viewPager.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            viewPager.setVisibility(show ? View.GONE : View.VISIBLE);
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }

    public void getSearchUsers(String key){
        HubConnection.getSearchUsers(currentUser.getUserID(), key)
                .done(new Action<Users>() {
                    @Override
                    public void run(final Users users) {
                        currentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ListView feedback_list = (ListView) findViewById(R.id.list_search);
                                AdapterSearch adaptorumuz = new AdapterSearch(currentActivity, users);
                                feedback_list.setAdapter(adaptorumuz);
                            }
                        });

                    }
                });
    }

    public void reloadTab() {
        int position = tabLayout.getSelectedTabPosition();
        adapter.notifyDataSetChanged();
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
    }
    private boolean checkVersion() {
        checkConnection();
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
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=goodapp.company.kissmyass")));
                }
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean checkConnection() {
        final ClassConnectionDetector cd = new ClassConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            showDialog();
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    if (!cd.isConnectingToInternet()) {
                        h.postDelayed(this, 1000);
                    } else
                        dismissDialog();
                }
            }, 1000);
            return false;
        }
        else
            return true;
    }

    public void showFullScreenAds() {
        if (ClassSession.SHOW_ADDVERTISMENT_5++ % 5 == 0) {
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitial = new InterstitialAd(currentActivity);
            interstitial.setAdUnitId(getString(R.string.full_screen_ad));
            interstitial.loadAd(adRequest);
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    if (interstitial.isLoaded()) {
                        interstitial.show();
                    }
                }
            });
        }
    }

    public void reloadActivity(Activity activity){
        startActivity(new Intent(activity,activity.getClass()));
    }

    public void showDialog() {
        dialog = ProgressDialog.show(this, "Waiting Connection", "Loading....", true);
        dialog.show();
    }

    public void showDialog(String title) {
        dialog = ProgressDialog.show(this, title, "Loading....", true);
        dialog.show();
    }

    public void showDialog(String title, String body) {
        dialog = ProgressDialog.show(this, title, body, true);
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }

    public void goBack() {
        super.onBackPressed();
    }
    public void logOut(){
        MessageNotifyService.cancelAlarm(getApplicationContext());
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    public boolean httpRequest(String Url, JSONObject data, String requestType) {
        responsed = 0;
        responseData = null;
        if(!checkConnection())
            return false;
        try {

            data.put("UserID", currentUser.getUserID());
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + Url, requestType, data.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                responsed = 1;
                                responseData = response.body;
                            } else {
                                responsed = 2;
                            }
                        }
                    }).execute();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public boolean httpRequest(String Url, String requestType) {
        if(!checkConnection())
            return false;
        try {
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + Url, requestType, null, ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                responsed = 1;
                                responseData = response.body;
                            } else {
                                responsed = 2;
                            }
                        }
                    }).execute();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
