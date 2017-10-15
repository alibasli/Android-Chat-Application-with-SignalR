package goodapp.company.kissmyass.TiujKiss.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ImageLoader;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.R;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;

/**
 * Created by mehme on 17.07.2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView mName, mSurname, mBirthday, mGender, mAbout, mEmail;
    private ClassSession.SessionUserInfo UserSession = null;
    SharedPreferences prefs, sharedpreferences;
    private String SenderId;
    private Button mKiss, mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedpreferences = getSharedPreferences(UserSession.userInfoSession, Context.MODE_PRIVATE);
        mName = (TextView) findViewById(R.id.txt_name);
        mSurname = (TextView) findViewById(R.id.txt_surname);
        mBirthday = (TextView) findViewById(R.id.txt_birthday);
        mGender = (TextView) findViewById(R.id.txt_gender);
        mEmail = (TextView) findViewById(R.id.txt_email);
        mAbout = (TextView) findViewById(R.id.txt_about);
        imageView = (ImageView) findViewById(R.id.image_view);

        mKiss = (Button) findViewById(R.id.btn_kiss);
        mMessage = (Button) findViewById(R.id.btn_message);
        mKiss.setOnClickListener(requesterToServerForKiss);
        mMessage.setOnClickListener(sentMessagePage);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                SenderId = null;
            } else {
                SenderId = extras.getString("SenderId");
            }
        } else {
            SenderId = (String) savedInstanceState.getSerializable("SenderId");
        }
        // requesterToServer(SenderId);


        prefs = getSharedPreferences("UserIDPref", MODE_PRIVATE);

        currentActivity = this;

        HubConnection.getUserById(SenderId)
                .done(new Action<User>() {
                    @Override
                    public void run(final User user) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawUserProfile(user);
                            }
                        });

                    }
                });

          doStuff();
    }

    private void doStuff() {

        AdView adView = (AdView) ProfileActivity.this.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }

    public void drawUserProfile(User user) {
        try {
            if (user.getPhoto() != "null") {
                ImageLoader imgLoad = new ImageLoader(this);
                imgLoad.DisplayImage(ClassSession.SOCKET_URL + user.getPhoto(), imageView);
            }
            mName.setText(user.getName());
            mSurname.setText(user.getLastname());
            mBirthday.setText(user.getBirthDate());
            mGender.setText(user.getGender());
            mEmail.setText(user.getEmail());
            mAbout.setText(user.getAbout());

        } catch (Exception ex) {
            Toast.makeText(this, "draw user profile error", Toast.LENGTH_LONG).show();
        }
    }

    public void goBack(View view) {
        this.onBackPressed();
    }

    private void requesterToServer(String SenderId) {
        JSONObject login = new JSONObject();
        try {
            login.put("userId", SenderId);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/profile", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                drawProfile(response.body);
                            } else if (response.code == 400) {
                                Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void drawProfile(String data) {
        try {
            JSONObject jsonUserInfo = new JSONObject(data);
            if (jsonUserInfo.getString("ProfileImage") != "null") {
                byte[] proImage = Base64.decode(jsonUserInfo.getString("ProfileImage"), Base64.DEFAULT);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(proImage, 0, proImage.length));
            }

            mName.setText(jsonUserInfo.getString("Name"));
            mSurname.setText(jsonUserInfo.getString("Surname"));
            mBirthday.setText(jsonUserInfo.getString("Birthday"));
            mGender.setText(jsonUserInfo.getString("Gender"));
            mEmail.setText(jsonUserInfo.getString("Email"));
            mAbout.setText(jsonUserInfo.getString("About"));

        } catch (Exception ex) {

        }
    }

    View.OnClickListener requesterToServerForKiss = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mKiss.setClickable(false);

            //setRequesterToServerForKiss();

            HubConnection.createKiss(currentUser.getUserID(), SenderId)
                    .done(new Action<Boolean>() {
                        @Override
                        public void run(final Boolean status) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (status){
                                        Toast.makeText(getApplicationContext(),"your kiss delivered!",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Could not sent your kiss!",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                        }
                    });

        }
    };
    View.OnClickListener sentMessagePage = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intx = new Intent(getApplicationContext(), MessageActivity.class);
            intx.putExtra("SenderId", SenderId);
            startActivity(intx);
        }
    };

    private void setRequesterToServerForKiss() {
        JSONObject login = new JSONObject();
        try {
            String userId = sharedpreferences.getString(UserSession.Id, "");
            if (userId.equals(""))
                return;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            login.put("DateTimeNow", format.format(new Date()) + ".601");
            login.put("UserID", userId);
            login.put("ToUserID", SenderId);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/Kiss", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                Toast.makeText(getApplicationContext(), "Your kiss delivered :)", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
