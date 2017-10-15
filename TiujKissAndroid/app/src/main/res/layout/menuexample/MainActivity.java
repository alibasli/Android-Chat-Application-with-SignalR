package com.mehme.menuexample;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.kobakei.ratethisapp.RateThisApp;
import com.mehme.menuexample.Adapter.AdapterListMessages;
import com.mehme.menuexample.Adapter.AdapterSearch;
import com.mehme.menuexample.Class.ClassSession;
import com.mehme.menuexample.Model.ModelFeedback;
import com.mehme.menuexample.Model.ModelSearch;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ClassSession.SessionUserInfo UserSession = null;
    SharedPreferences sharedpreferences;

    //pages
    private View mHomeView;
    private View mProfileView;
    private View mMessagesView;
    private View mFeedbackView;
    private View mPhotosView;
    private View mSearchView;
    private View mProgressView;

    private View mRowProfileBasicInfo, mRowProfilDetailInfo;

    private int pageViewsCount = 0;

    private EditText searchEditText;

    final List<com.mehme.menuexample.Model.ModelListMessages> messagerList = new ArrayList<com.mehme.menuexample.Model.ModelListMessages>();
    final List<com.mehme.menuexample.Model.ModelPost> postList = new ArrayList<com.mehme.menuexample.Model.ModelPost>();
    final List<com.mehme.menuexample.Model.ModelPost> postProfileList = new ArrayList<com.mehme.menuexample.Model.ModelPost>();
    final List<ModelFeedback> feedbackList = new ArrayList<ModelFeedback>();
    final List<ModelSearch> searchList = new ArrayList<ModelSearch>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  Intent newInt = new Intent(MainActivity.this,MessageActivity.class);
        // startActivity(newInt);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //set up views
        mHomeView = findViewById(R.id.context_home);
        mProfileView = findViewById(R.id.context_profile);
        mMessagesView = findViewById(R.id.context_messages);
        mFeedbackView = findViewById(R.id.context_feedback);
        mPhotosView = findViewById(R.id.context_photos);
        mProgressView = findViewById(R.id.load_progress);
        mSearchView = findViewById(R.id.context_search);

        mRowProfileBasicInfo = findViewById(R.id.row_profile_basic_info);
        mRowProfilDetailInfo = findViewById(R.id.row_profile_detail_info);
        mRowProfileBasicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRowProfilDetailInfo.setVisibility(mRowProfilDetailInfo.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        searchEditText = (EditText) findViewById(R.id.edittext_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final ListView search_list = (ListView) findViewById(R.id.search_list);
                search_list.setVisibility(View.GONE);
                requesterToServerSearch("Api/search/" + searchEditText.getText());
            }
        });

        sharedpreferences = getSharedPreferences(UserSession.userInfoSession, Context.MODE_PRIVATE);

        showProgress(true);
        requesterToServer("Api/home");


        final com.mehme.menuexample.Class.ClassConnectionDetector cd = new com.mehme.menuexample.Class.ClassConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            final ProgressDialog progress = ProgressDialog.show(com.mehme.menuexample.MainActivity.this, "Waiting Connection", "Loading....", true);
            progress.show();

            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    if (!cd.isConnectingToInternet()) {
                        h.postDelayed(this, 1000);
                    } else {
                        progress.dismiss();
                    }
                    rateTheApp();
                }
            }, 1000);
        } else {
            rateTheApp();
        }


    }

    private void rateTheApp() {
        RateThisApp.onStart(this);
        RateThisApp.showRateDialogIfNeeded(this);
        // if(RateThisApp.showRateDialogIfNeeded(getApplicationContext())) {
        //    RateThisApp.init(new RateThisApp.Config(3, 5));
        //    RateThisApp.showRateDialog(MainActivity.this);
        // }
    }

    @Override
    public void onBackPressed() {
      /*  DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
        }*/
        startActivity(new Intent(this, com.mehme.menuexample.MainActivity.class));
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_logout) {
            sharedpreferences = getSharedPreferences(UserSession.userInfoSession, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.commit();

            Intent intent = new Intent(getApplicationContext(), com.mehme.menuexample.LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        showProgress(true);

        if (id == R.id.nav_home) {
            pageViewsCount = 0;
            //request to server
            requesterToServer("Api/home");
        } else if (id == R.id.nav_profile) {
            pageViewsCount = 1;
            //request to server
            requesterToServer("Api/profile");
        } else if (id == R.id.nav_messages) {
            pageViewsCount = 2;
            //request to server
            requesterToServer("Api/homeMessages");
        } else if (id == R.id.nav_feedback) {
            pageViewsCount = 3;
            //request to server
            requesterToServer("Api/feedback");
        } else if (id == R.id.nav_photos) {
            pageViewsCount = 4;
            //request to server
            requesterToServer("Api/photos");
        } else if (id == R.id.nav_search) {
            pageViewsCount = 5;
            //request to server
            //requesterToServer("Api/search");
            showProgress(false);
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            switch (pageViewsCount) {
                case 0:
                    mHomeView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mHomeView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mHomeView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
                    break;
                case 1:
                    mProfileView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mProfileView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mProfileView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
                    break;
                case 2:
                    mMessagesView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mMessagesView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mMessagesView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
                    break;
                case 3:
                    mFeedbackView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mFeedbackView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mFeedbackView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
                    break;
                case 4:
                    mPhotosView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mPhotosView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mPhotosView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
                    break;
                case 5:
                    mSearchView.setVisibility(show ? View.GONE : View.VISIBLE);
                    mSearchView.animate().setDuration(shortAnimTime).alpha(
                            show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mSearchView.setVisibility(show ? View.GONE : View.VISIBLE);
                        }
                    });
                    break;
            }
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            switch (pageViewsCount) {
                case 0:
                    mHomeView.setVisibility(show ? View.GONE : View.VISIBLE);
                    break;
                case 1:
                    mProfileView.setVisibility(show ? View.GONE : View.VISIBLE);
                    break;
                case 2:
                    mMessagesView.setVisibility(show ? View.GONE : View.VISIBLE);
                    break;
                case 3:
                    mFeedbackView.setVisibility(show ? View.GONE : View.VISIBLE);
                    break;
                case 4:
                    mPhotosView.setVisibility(show ? View.GONE : View.VISIBLE);
                    break;
                case 5:
                    mProgressView.setVisibility(show ? View.GONE : View.VISIBLE);
                    break;
            }
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    private void requesterToServer(String urlServer) {

        JSONObject login = new JSONObject();
        try {
            int userId = sharedpreferences.getInt(UserSession.Id, 1);
            login.put("userId", userId);
        } catch (JSONException e) {
        }
        new HttpRequestTask(
                new HttpRequest(ClassSession.SERVER_URL + urlServer, HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {
                        if (response.code == 200) {
                            switch (pageViewsCount) {
                                case 0:
                                    drawHome(response.body);
                                    break;
                                case 1:
                                    drawProfile(response.body);
                                    break;
                                case 2:
                                    drawMessages(response.body);
                                    break;
                                case 3:
                                    drawFeedback(response.body);
                                    break;
                                case 4:
                                    drawPhotos(response.body);
                                    break;
                            }
                        } else if (response.code == 400) {
                            //  try {
                            TextView err = (TextView) findViewById(R.id.res_error_view);
                            err.setText("Failed");
                            //JSONObject myObject = new JSONObject(response.body);
                            //err.setText(myObject.getString("Message"));
                            err.setTextColor(Color.parseColor("#FF0000"));
                            //   }
                            //   catch (JSONException e) {
                            //  }
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                        }
                        showProgress(false);
                    }

                }).execute();
    }

    private void requesterToServerSearch(String url) {
        new HttpRequestTask(
                new HttpRequest(ClassSession.SERVER_URL + url, HttpRequest.GET, null, ClassSession.AUTHORIZATION),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {
                        if (response.code == 200) {
                            drawSearch(response.body);
                        } else if (response.code == 400) {
                            TextView err = (TextView) findViewById(R.id.res_error_view);
                            err.setText("Failed");
                            err.setTextColor(Color.parseColor("#FF0000"));
                        } else {
                            Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                        }                                           }
                }).execute();
    }


    private void drawHome(String jsonString) {
        postList.clear();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                List<byte[]> postImages = new ArrayList<byte[]>();
                if (!jsonObject.isNull("PostImages")) {
                    JSONArray JAPostImages = (JSONArray) jsonObject.get("PostImages");
                    for (int j = 0; j < JAPostImages.length(); j++) {
                        byte[] bImage = Base64.decode(JAPostImages.getString(j), Base64.DEFAULT);
                        postImages.add(bImage);
                    }
                }
                byte[] pImage = Base64.decode(jsonObject.getString("PostOwnerProfileImage"), Base64.DEFAULT);

                postList.add(new com.mehme.menuexample.Model.ModelPost(
                        jsonObject.getInt("PostId"),
                        jsonObject.getInt("PostOwnerId"),
                        jsonObject.getString("PostOwnerNameSurname"),
                        jsonObject.getString("PostDateTime"),
                        pImage,
                        jsonObject.getString("PostTitle"),
                        jsonObject.getString("PostBody"),
                        postImages
                ));

            }
            final ListView post_list = (ListView) findViewById(R.id.post_list);
            com.mehme.menuexample.Adapter.AdapterPost adaptorumuz = new com.mehme.menuexample.Adapter.AdapterPost(this, postList);
            post_list.setAdapter(adaptorumuz);

        } catch (Exception ex) {

        }
    }

    private void drawProfile(String jsonString) {
        postProfileList.clear();
        try {
            JSONObject jsonBase = new JSONObject(jsonString);
            JSONObject jsonUserInfo = new JSONObject(jsonBase.getString("userInfo"));
            /*
            ImageView img_profile_view = (ImageView) findViewById(R.id.profile_image_view);
            byte[] proImage = Base64.decode(jsonUserInfo.getString("ProfileImage"), Base64.DEFAULT);
            img_profile_view.setImageBitmap(BitmapFactory.decodeByteArray(proImage, 0, proImage.length));
            */
            TextView profileUsername = (TextView) findViewById(R.id.txt_profile_username);
            profileUsername.setText(jsonUserInfo.getString("Name") + " " + jsonUserInfo.getString("Surname"));
            TextView profileBirsthday = (TextView) findViewById(R.id.txt_profile_birthday);
            profileBirsthday.setText(jsonUserInfo.getString("Birthday"));
            TextView profileGender = (TextView) findViewById(R.id.txt_profile_gender);
            profileGender.setText(jsonUserInfo.getString("Gender"));

            JSONArray jsonArray = new JSONArray(jsonBase.getString("userPosts"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                List<byte[]> postImages = new ArrayList<byte[]>();
                if (!jsonObject.isNull("PostImages")) {
                    JSONArray JAPostImages = (JSONArray) jsonObject.get("PostImages");
                    for (int j = 0; j < JAPostImages.length(); j++) {
                        byte[] bImage = Base64.decode(JAPostImages.getString(j), Base64.DEFAULT);
                        postImages.add(bImage);
                    }
                }
                byte[] pImage = Base64.decode(jsonObject.getString("PostOwnerProfileImage"), Base64.DEFAULT);

                postProfileList.add(new com.mehme.menuexample.Model.ModelPost(
                        jsonObject.getInt("PostId"),
                        jsonObject.getInt("PostOwnerId"),
                        jsonObject.getString("PostOwnerNameSurname"),
                        jsonObject.getString("PostDateTime"),
                        pImage,
                        jsonObject.getString("PostTitle"),
                        jsonObject.getString("PostBody"),
                        postImages
                ));

            }
            final ListView post_list = (ListView) findViewById(R.id.profile_post_list);
            com.mehme.menuexample.Adapter.AdapterPost adaptorumuz = new com.mehme.menuexample.Adapter.AdapterPost(this, postProfileList);
            post_list.setAdapter(adaptorumuz);
        } catch (Exception ex) {
        }
    }

    private void drawMessages(String jsonString) {
        messagerList.clear();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                int id = jsonObject.getInt("SenderId");
                String name = jsonObject.getString("SenderNameSurname");
                byte[] byteArray = Base64.decode(jsonObject.getString("SenderPhoto"), Base64.DEFAULT);
                boolean date = jsonObject.getBoolean("MessageRead");
                messagerList.add(new com.mehme.menuexample.Model.ModelListMessages(id, name, byteArray, date));
            }
            final ListView messager_list = (ListView) findViewById(R.id.messager_list);
            AdapterListMessages adaptorumuz = new AdapterListMessages(this, messagerList);
            messager_list.setAdapter(adaptorumuz);

        } catch (Exception ex) {

        }

    }

    private void drawFeedback(String jsonString) {
        feedbackList.clear();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                byte[] byteArray = Base64.decode(jsonObject.getString("OwnerPhoto"), Base64.DEFAULT);
                feedbackList.add(new ModelFeedback(
                        jsonObject.getInt("FeedId"),
                        jsonObject.getString("FeedBody"),
                        jsonObject.getInt("OwnerId"),
                        jsonObject.getString("OwnerNameSurname"),
                        byteArray,
                        jsonObject.getInt("PostId"),
                        jsonObject.getString("PostTitle"),
                        jsonObject.getString("FeedDateTime"),
                        true
                ));
            }
            final ListView feedback_list = (ListView) findViewById(R.id.feedback_list);
            com.mehme.menuexample.Adapter.AdapterFeedback adaptorumuz = new com.mehme.menuexample.Adapter.AdapterFeedback(this, feedbackList);
            feedback_list.setAdapter(adaptorumuz);

        } catch (Exception ex) {

        }
    }

    private void drawPhotos(String jsonString) {
        /*
        try{
            JSONArray jsonArray = new JSONArray(jsonString);
             for (int i = 0; i < jsonArray.length(); i++) {
                 JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                 int id = jsonObject.getInt("PostId");
                 String name = jsonObject.getString("PostOwnerNameSurname");
                 byte[] byteArray = Base64.decode(jsonObject.getString("PostOwnerProfileImage"), Base64.DEFAULT);

            }
        }catch (Exception ex){

        }
        */
    }

    private void drawSearch(String jsonString) {
        searchList.clear();
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                int id = jsonObject.getInt("UserId");
                String name = jsonObject.getString("UserNameSurname");
                byte[] byteArray = Base64.decode(jsonObject.getString("UserProfileImage"), Base64.DEFAULT);

                searchList.add(new ModelSearch(id, name, byteArray));
            }
            final ListView search_list = (ListView) findViewById(R.id.search_list);
            search_list.setVisibility(View.VISIBLE);
            AdapterSearch adaptorumuz = new AdapterSearch(this, searchList);
            search_list.setAdapter(adaptorumuz);
        } catch (Exception ex) {

        }
    }


}
