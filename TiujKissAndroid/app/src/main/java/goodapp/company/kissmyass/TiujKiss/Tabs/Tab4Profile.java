package goodapp.company.kissmyass.TiujKiss.Tabs;

/**
 * Created by mehme on 14.07.2016.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.TiujKiss.Activities.LoginActivity;
import goodapp.company.kissmyass.TiujKiss.Activities.MainActivity;
import goodapp.company.kissmyass.Class.ClassImagePickerWithCrop;
import goodapp.company.kissmyass.Class.ClassSession;
import goodapp.company.kissmyass.Class.ImageLoader;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.Services.MessageNotifyService;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;

//Our class extending fragment
public class Tab4Profile extends Fragment {

    private View mView;
    private MainActivity mMainActivityActivity;
    ImageView imageView;
    private TextView mName, mSurname, mBirthday, mGender, mAbout, mEmail;
    private EditText mEditName, mEditSurname, mEditBirthday, mEditAbout;
    private Button mEdit, mCancel, mSave,mDelete,mLogout;
    private RadioGroup mGenderRadioGroup;
    private View  view_body,view_image_full;

    private int mDay, mMonth, mYear;

    private ClassSession.SessionUserInfo UserSession = null;
    SharedPreferences sharedpreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mMainActivityActivity = (MainActivity) getActivity();
       // mMainActivityActivity.showProgress(true);
        sharedpreferences = getActivity().getSharedPreferences(UserSession.userInfoSession, Context.MODE_PRIVATE);

        return inflater.inflate(R.layout.tab4_profile, container, false);
    }

    /*  @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
      }
      */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mView = view;
        mName = (TextView) view.findViewById(R.id.txt_name);
        mSurname = (TextView) view.findViewById(R.id.txt_surname);
        mBirthday = (TextView) view.findViewById(R.id.txt_birthday);
        mGender = (TextView) view.findViewById(R.id.txt_gender);
        mEmail = (TextView) view.findViewById(R.id.txt_email);
        mAbout = (TextView) view.findViewById(R.id.txt_about);
        imageView = (ImageView) view.findViewById(R.id.image_view);

        mEditName = (EditText) view.findViewById(R.id.edit_txt_name);
        mEditSurname = (EditText) view.findViewById(R.id.edit_txt_surname);
        mEditBirthday = (EditText) view.findViewById(R.id.edit_txt_birthday);
        mGenderRadioGroup = (RadioGroup) view.findViewById(R.id.radio_gender);
        // mEditEmail = (EditText) view.findViewById(R.id.edit_txt_email);
        mEditAbout = (EditText) view.findViewById(R.id.edit_txt_about);

        mEdit = (Button) view.findViewById(R.id.btn_edit);
        mCancel = (Button) view.findViewById(R.id.btn_cancel);
        mSave = (Button) view.findViewById(R.id.btn_save);
        mDelete = (Button) view.findViewById(R.id.btn_delete_account);
        mLogout = (Button) view.findViewById(R.id.btn_logout);

        view_body = mView.findViewById(R.id.view_body);
        view_image_full = mView.findViewById(R.id.view_image_full);

        setActions();
        //requesterToServer();
        drawUser();
    }

    private void setActions(){
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageNotifyService.cancelAlarm(getActivity().getApplicationContext());
                sharedpreferences.edit().clear().apply();

                JSONObject login = new JSONObject();
                try {
                    String str = sharedpreferences.getString(UserSession.Id, "");
                    if (str.equals("") || str.equals(null))
                        return;
                    login.put("UserID", str);
                    new HttpRequestTask(
                            new HttpRequest(ClassSession.SERVER_URL + "Api/deleteAccount", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                            new HttpRequest.Handler() {
                                @Override
                                public void response(HttpResponse response) {
                                    if (response.code == 200) {
                                        drawProfile(response.body);
                                    } else {
                                    }
                                    Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                }
                            }).execute();
                } catch (JSONException e) {
                    showFailedView("Profile/requesterToServer load error");
                }
            }
        });
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MessageNotifyService.cancelAlarm(getActivity().getApplicationContext());
                sharedpreferences.edit().clear().apply();
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        // ImageView change_profile = (ImageView) view.findViewById(R.id.change_profile);
        view_image_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_body.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.show();
                }
                view_image_full.setVisibility(View.GONE);
                view_body.setVisibility(View.VISIBLE);
                mMainActivityActivity.tabLayout.setVisibility(View.VISIBLE);

            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence OptionMenu[] = new CharSequence[]{getString(R.string.show_image),getString(R.string.change_image),getString(R.string.cancel)};

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getString(R.string.choose_event));
                builder.setItems(OptionMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                                if (actionBar != null) {
                                    actionBar.hide();
                                }
                                view_image_full.setVisibility(View.VISIBLE);
                                view_image_full.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                                view_body.setVisibility(View.GONE);
                                mMainActivityActivity.tabLayout.setVisibility(View.GONE);
                                break;
                            case 1:
                                ClassImagePickerWithCrop.pickImage(getActivity());
                                break;
                            default:
                                dialog.dismiss();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void requesterToServer() {
        JSONObject login = new JSONObject();
        try {
            String str = sharedpreferences.getString(UserSession.Id, "");
            if (str.equals("") || str.equals(null))
                return;
            login.put("userId", str);
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/profile", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                drawProfile(response.body);
                            } else {
                                showFailedView("Profile/requesterToServer " + response.code + "  error");
                                //Toast.makeText(getActivity().getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                            mMainActivityActivity.showProgress(false);
                        }
                    }).execute();
        } catch (JSONException e) {
            //throw new RuntimeException(e);
            mMainActivityActivity.showProgress(false);
            showFailedView("Profile/requesterToServer load error");
        }

    }

    private void drawUser(){
        try{
            final ImageView image_view_full_screen = (ImageView) mView.findViewById(R.id.image_view_full_screen);
            if (currentUser.getPhoto() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageLoader imgLoad = new ImageLoader(getActivity());
                        imgLoad.DisplayImage(ClassSession.SOCKET_URL+currentUser.getPhoto(),image_view_full_screen);
                        imgLoad.DisplayImage(ClassSession.SOCKET_URL+currentUser.getPhoto(),imageView);
                    }
                });

            }
            else{
                imageView.setImageResource(R.drawable.default_profile);
                image_view_full_screen.setImageResource(R.drawable.default_profile);
            }
            TextView txt_header_name = (TextView) mView.findViewById(R.id.txt_header_name);
            txt_header_name.setText(currentUser.getName() + " " + currentUser.getLastname());
            mName.setText(currentUser.getName());
            mSurname.setText(currentUser.getLastname());
            mBirthday.setText(currentUser.getBirthDate());
            if (currentUser.getGender().equals("Female"))
                mGender.setText(R.string.radio_female);
            else
                mGender.setText(R.string.radio_male);
            mEmail.setText(currentUser.getEmail());
            mAbout.setText(currentUser.getAbout());

            mEditName.setText(currentUser.getName());
            mEditSurname.setText(currentUser.getLastname());
            mEditBirthday.setText(currentUser.getBirthDate());
            mEditBirthday.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    if (mEditBirthday.isFocusableInTouchMode()) {
                        mEditBirthday.setFocusableInTouchMode(false);
                        showDatePickerDialog(arg0);

                    }
                    return false;
                }
            });
            if (currentUser.getGender().equals("Female"))
                mGenderRadioGroup.check(R.id.radioFemale);
            //mEditEmail.setText(jsonUserInfo.getString("Email"));
            mEditAbout.setText(currentUser.getAbout());

            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    showEdit(true);
                    mMainActivityActivity.tabLayout.setVisibility(View.GONE);
                }
            });
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEdit(false);
                    mMainActivityActivity.tabLayout.setVisibility(View.VISIBLE);
                }
            });
            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestToServerForEditProfile();
                }
            });
        }catch (Exception ex ){
            Toast.makeText(getActivity(),"draw User Profile error",Toast.LENGTH_LONG).show();
        }
    }

    private void drawProfile(String data) {
        try {
            if (data.equals(null) || data.equals("[]") || data.equals("")) {
                showFailedView("Profile load error");
                return;
            }
            JSONObject jsonUserInfo = new JSONObject(data);
            ImageView image_view_full_screen = (ImageView) mView.findViewById(R.id.image_view_full_screen);
            if (!jsonUserInfo.getString("ProfileImage").equals("null")) {
                byte[] proImage = Base64.decode(jsonUserInfo.getString("ProfileImage"), Base64.DEFAULT);
                imageView.setImageBitmap(BitmapFactory.decodeByteArray(proImage, 0, proImage.length));
                image_view_full_screen.setImageBitmap(BitmapFactory.decodeByteArray(proImage, 0, proImage.length));
            }
            TextView txt_header_name = (TextView) mView.findViewById(R.id.txt_header_name);
            txt_header_name.setText(jsonUserInfo.getString("Name") + " " + jsonUserInfo.getString("Surname"));
            mName.setText(jsonUserInfo.getString("Name"));
            mSurname.setText(jsonUserInfo.getString("Surname"));
            mBirthday.setText(jsonUserInfo.getString("Birthday"));
            if (jsonUserInfo.getString("Gender").equals("Female"))
                mGender.setText(R.string.radio_female);
            else
                mGender.setText(R.string.radio_male);
            mEmail.setText(jsonUserInfo.getString("Email"));
            mAbout.setText(jsonUserInfo.getString("About"));

            mEditName.setText(jsonUserInfo.getString("Name"));
            mEditSurname.setText(jsonUserInfo.getString("Surname"));
            mEditBirthday.setText(jsonUserInfo.getString("Birthday"));
            mEditBirthday.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View arg0, MotionEvent arg1) {
                    if (mEditBirthday.isFocusableInTouchMode()) {
                        mEditBirthday.setFocusableInTouchMode(false);
                        showDatePickerDialog(arg0);

                    }
                    return false;
                }
            });
            if (jsonUserInfo.getString("Gender").equals("Female"))
                mGenderRadioGroup.check(R.id.radioFemale);
            //mEditEmail.setText(jsonUserInfo.getString("Email"));
            mEditAbout.setText(jsonUserInfo.getString("About"));

            mEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                    showEdit(true);
                    mMainActivityActivity.tabLayout.setVisibility(View.GONE);
                }
            });
            mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showEdit(false);
                    mMainActivityActivity.tabLayout.setVisibility(View.VISIBLE);
                }
            });
            mSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    requestToServerForEditProfile();
                }
            });

        } catch (Exception ex) {
            showFailedView("drawProfile JSONObject Error");
        }
    }

    public void showFailedView(String text) {
        View view_body = mView.findViewById(R.id.view_body);
        view_body.setVisibility(View.GONE);
        View view_failed = mView.findViewById(R.id.view_failed);
        view_failed.setVisibility(View.VISIBLE);
        TextView txt_failed = (TextView) mView.findViewById(R.id.txt_failed);
        txt_failed.setText(text);
    }

    private void showEdit(boolean bool) {
        mName.setVisibility(bool ? View.GONE : View.VISIBLE);
        mSurname.setVisibility(bool ? View.GONE : View.VISIBLE);
        mBirthday.setVisibility(bool ? View.GONE : View.VISIBLE);
        mGender.setVisibility(bool ? View.GONE : View.VISIBLE);
        mEmail.setVisibility(bool ? View.GONE : View.VISIBLE);
        mAbout.setVisibility(bool ? View.GONE : View.VISIBLE);
        mDelete.setVisibility(bool ? View.GONE : View.VISIBLE);
        mLogout.setVisibility(bool ? View.GONE : View.VISIBLE);

        mEditName.setVisibility(bool ? View.VISIBLE : View.GONE);
        mEditSurname.setVisibility(bool ? View.VISIBLE : View.GONE);
        mEditBirthday.setVisibility(bool ? View.VISIBLE : View.GONE);
        mGenderRadioGroup.setVisibility(bool ? View.VISIBLE : View.GONE);
        // mEditEmail.setVisibility(bool ? View.VISIBLE : View.GONE);
        mEditAbout.setVisibility(bool ? View.VISIBLE : View.GONE);

        mEdit.setVisibility(bool ? View.GONE : View.VISIBLE);
        mCancel.setVisibility(bool ? View.VISIBLE : View.GONE);
        mSave.setVisibility(bool ? View.VISIBLE : View.GONE);
    }

    private void requestToServerForEditProfile() {
        mMainActivityActivity.showProgress(true);
        JSONObject login = new JSONObject();
        try {
            String str = sharedpreferences.getString(UserSession.Id, "");
            if (str.equals("") || str.equals(null))
                return;
            login.put("UserID", str);
            login.put("Name", mEditName.getText().toString());
            login.put("Surname", mEditSurname.getText().toString());
            setDate();
            login.put("BirthDate", mMonth + "-" + mDay + "-" + mYear);
            String gender = "Male";
            if (mGenderRadioGroup.getCheckedRadioButtonId() == R.id.radioFemale)
                gender = "Female";
            login.put("Gender", gender);
            //   login.put("Email", mEditEmail.getText().toString());
            login.put("About", mEditAbout.getText().toString());
            new HttpRequestTask(
                    new HttpRequest(ClassSession.SERVER_URL + "Api/EditProfile", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                try {
                                    HubConnection.getUserById(currentUser.UserID)
                                            .done(new Action<User>() {
                                                @Override
                                                public void run(User user) {
                                                    currentUser = user;
                                                    mMainActivityActivity.reloadTab();
                                                }
                                            });
                                } catch (Exception e) {
                                    showFailedView("requestToServerForEditProfile jsononject error");
                                }
                            } else {
                                showFailedView("Profile/requesterToServer " + response.code + "  error");
                                //Toast.makeText(getActivity().getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                            }
                            mMainActivityActivity.showProgress(false);
                        }
                    }).execute();
        } catch (JSONException e) {
            //throw new RuntimeException(e);
            mMainActivityActivity.showProgress(false);
            showFailedView("requestToServerForEditProfile load error");
        }
        mMainActivityActivity.tabLayout.setVisibility(View.VISIBLE);
    }

    public void showDatePickerDialog(View v) {

        Bundle date = new Bundle();
        setDate();
        date.putInt("dayKey", mDay);
        date.putInt("monthKey", mMonth - 1);
        date.putInt("yearKey", mYear);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(date);
        newFragment.show(getActivity().getFragmentManager(), "datePicker");
    }

    private void setDate() {
        String date = mEditBirthday.getText().toString();
        int begin = 0;
        boolean dx = true;
        for (int i = 0; i < date.length(); i++) {
            if (dx) {
                if (date.substring(i, i + 1).equals("-")) {
                    mDay = Integer.parseInt(date.substring(begin, i));
                    begin = i + 1;
                    dx = false;
                }
            } else {
                if (date.substring(i, i + 1).equals("-")) {
                    mMonth = Integer.parseInt(date.substring(begin, i));
                    begin = i + 1;
                    mYear = Integer.parseInt(date.substring(begin, date.length()));
                }
            }
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        int dYear, dMonth, dDay;

        public DatePickerFragment() {
            final Calendar c = Calendar.getInstance();
            dYear = c.get(Calendar.YEAR);
            dMonth = c.get(Calendar.MONTH);
            dDay = c.get(Calendar.DAY_OF_MONTH);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            /* Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            */
            try {
                dYear = getArguments().getInt("yearKey");
                dMonth = getArguments().getInt("monthKey");
                dDay = getArguments().getInt("dayKey");
            } catch (Exception e) {
            }

            return new DatePickerDialog(getActivity(), this, dYear, dMonth, dDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            EditText e = (EditText) getActivity().findViewById(R.id.edit_txt_birthday);
            month++;
            if (month < 10) {
                if (day < 10)
                    e.setText("0" + day + "-" + "0" + month + "-" + year);
                else
                    e.setText(day + "-" + "0" + month + "-" + year);
            } else {
                if (day < 10)
                    e.setText("0" + day + "-" + month + "-" + year);
                else
                    e.setText(day + "-" + month + "-" + year);
            }

            e.setFocusableInTouchMode(true);

        }
    }
}
