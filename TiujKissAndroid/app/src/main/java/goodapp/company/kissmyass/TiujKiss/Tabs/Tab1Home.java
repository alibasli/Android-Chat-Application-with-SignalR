package goodapp.company.kissmyass.TiujKiss.Tabs;

/**
 * Created by mehme on 14.07.2016.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import goodapp.company.kissmyass.TiujKiss.Activities.*;
import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ImageLoader;
import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.R;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;


//Our class extending fragment
public class Tab1Home extends Fragment {
    View mView;
    ImageView mProfile, mCancel, mMessage, mKiss;
    TextView mName, mAge;
    View view_show_kissed, view_show_kiss;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab1_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        // mMainActivityActivity.showProgress(true);
        mProfile = (ImageView) view.findViewById(R.id.img_profile);
        mCancel = (ImageView) view.findViewById(R.id.img_cancel);
        mMessage = (ImageView) view.findViewById(R.id.img_message);
        mKiss = (ImageView) view.findViewById(R.id.img_kiss);
        mName = (TextView) view.findViewById(R.id.txt_name);
        mAge = (TextView) view.findViewById(R.id.txt_age);
        view_show_kissed = view.findViewById(R.id.view_show_kissed);
        view_show_kiss = view.findViewById(R.id.view_show_kiss);


        //requesterToServer();
        getRandomUser();
    }

    public void showFailedView(String text) {
        view_show_kiss.setVisibility(View.GONE);
        view_show_kissed.setVisibility(View.GONE);
        View view_failed = mView.findViewById(R.id.view_failed);
        view_failed.setVisibility(View.VISIBLE);
        TextView txt_failed = (TextView) mView.findViewById(R.id.txt_failed);
        txt_failed.setText(text);
    }

    private void getRandomUser() {
        //progressDialog.setMessage("loading...");
        // progressDialog.show();
        HubConnection.getUserByDefault(currentUser.UserID).done(new Action<User>() {
            @Override
            public void run(final User user) {
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity mnactivity = (MainActivity)currentActivity;
                        mnactivity.showFullScreenAds();
                        drawRandomUser(user);
                    }
                });

            }
        });
    }

    private void drawRandomUser(final User user) {

        try {
            if (user.getPhoto() != null) {
                ImageLoader imgLoad = new ImageLoader(currentActivity);
                imgLoad.DisplayImage(ClassSession.SOCKET_URL + user.getPhoto(), mProfile);
            } else {
                mProfile.setImageResource(R.drawable.default_profile);
            }

            mProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent newInt = new Intent(currentActivity, ProfileActivity.class);
                    newInt.putExtra("SenderId", user.getUserID());
                    currentActivity.startActivity(newInt);
                }
            });
            mName.setText(getString(R.string.prompt_name) + " : " + user.getName().toString() + " " + user.getLastname().toString());
            mAge.setText(getString(R.string.Age) + " : " + user.getBirthDate());
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getRandomUser();
                }
            });
            mMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intx = new Intent(currentActivity, MessageActivity.class);
                    intx.putExtra("SenderId", user.getUserID());
                    currentActivity.startActivity(intx);
                }
            });
            mKiss.setVisibility(View.VISIBLE);
            mKiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view_show_kissed.setVisibility(View.VISIBLE);
                    createKiss(user.getUserID());
                    //requesterToServerForKiss(user.getUserID());
                    mKiss.setVisibility(View.GONE);
                }
            });
        } catch (Exception ex) {
            Toast.makeText(currentActivity, "draw Random User Error", Toast.LENGTH_LONG).show();
        }
        //  progressDialog.dismiss();
    }

    private void createKiss(String toId) {
        HubConnection.createKiss(currentUser.getUserID(), toId)
                .done(new Action<Boolean>() {
                    @Override
                    public void run(final Boolean status) {
                        currentActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (status) {
                                    Toast.makeText(currentActivity, "your kiss delivered!", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(currentActivity, "Could not sent your kiss!", Toast.LENGTH_LONG).show();
                                }
                                view_show_kissed.setVisibility(View.GONE);
                            }
                        });

                    }
                });
    }

    /*

    private void requesterToServer() {
        JSONObject login = new JSONObject();
        try {
            String userId = sharedpreferences.getString(UserSession.Id, "");
            if (userId.equals(""))
                return;
            // login.put("userId", userId);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/home/" + userId, HttpRequest.GET, null, ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                drawHome(response.body);
                            } else {
                                showFailedView("home/requesterToServer " + response.code + " Error!");
                                Toast.makeText(getActivity().getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                            mMainActivityActivity.showProgress(false);
                        }
                    }).execute();
        } catch (Exception e) {
            mMainActivityActivity.showProgress(false);
            showFailedView("home/requestToServer jsonObject error.");
            //throw new RuntimeException(e);
        }
    }

    private void drawHome(String data) {
        try {
            if (data.equals(null) || data.equals("[]") || data.equals("")) {
                showFailedView("No Any Person!");
                return;
            }
            JSONObject jsonObject = new JSONObject(data);
            if (!jsonObject.getString("Photo").equals("null")) {
                byte[] byteArray = Base64.decode(jsonObject.getString("Photo"), Base64.DEFAULT);
                Bitmap bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                mProfile.setImageBitmap(bm);
            }


            final String uId = jsonObject.getString("UserID");

            //mProfile.setTag(jsonObject.getString("UserID"));
            mProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // view.getTag();
                    Intent newInt = new Intent(getActivity(), ProfileActivity.class);
                    newInt.putExtra("SenderId", uId);
                    getActivity().startActivity(newInt);
                }
            });
            mName.setText(getString(R.string.prompt_name) + " : " + jsonObject.getString("Name") + " " + jsonObject.getString("Surname"));
            mAge.setText(getString(R.string.Age) + " : " + jsonObject.getString("Age"));
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intx = new Intent(getActivity(), MainActivity.class);
                    getActivity().startActivity(intx);
                }
            });
            mMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intx = new Intent(getActivity(), MessageActivity.class);
                    intx.putExtra("SenderId", uId);
                    getActivity().startActivity(intx);
                }
            });
            mKiss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view_show_kissed.setVisibility(View.VISIBLE);
                    requesterToServerForKiss(uId);
                    mKiss.setVisibility(View.GONE);
                }
            });
            mMainActivityActivity.showProgress(false);
        } catch (Exception ex) {
            showFailedView("home/drawHome JsonObject error.");
        }
    }

    private void requesterToServerForKiss(String uId) {

        JSONObject login = new JSONObject();
        try {
            String userId = sharedpreferences.getString(UserSession.Id, "");
            if (userId.equals(""))
                return;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            login.put("DateTimeNow", format.format(new Date()) + ".601");
            login.put("UserID", userId);
            login.put("ToUserID", uId);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/Kiss", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                try {
                                    wait(3000);
                                    view_show_kissed.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    view_show_kissed.setVisibility(View.GONE);
                                }
                            } else {
                                view_show_kissed.setVisibility(View.GONE);
                                showFailedView("home/requesterToServerForKiss " + response.code + " error.");
                                //Toast.makeText(getActivity().getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }).execute();
        } catch (JSONException e) {
            showFailedView("home/requesterToServerForKiss JsonObject error.");
            //throw new RuntimeException(e);
        }
    }*/

}