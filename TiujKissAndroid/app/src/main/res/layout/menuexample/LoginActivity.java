package com.mehme.menuexample;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apptakk.http_request.HttpRequest;
import com.apptakk.http_request.HttpRequestTask;
import com.apptakk.http_request.HttpResponse;
import com.mehme.menuexample.Class.ClassConnectionDetector;
import com.mehme.menuexample.Class.ClassSession;
import com.mehme.menuexample.Service.MessageNotifyService;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * login session
     */
    private ClassSession.SessionUserInfo UserSession = null;
    SharedPreferences sharedpreferences;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private AutoCompleteTextView mRegisterEmailView;
    private EditText mRegisterPasswordView;
    private EditText mRegisterNameView;
    private EditText mRegisterSurnameView;
    private RadioGroup mRegisterGenderRadioGroup;
    private EditText mRegisterBirthdayView;

    private View mProgressView;
    private View mLoginFormView;
    private View mRegisterFormView;


    //variable for register or login
    private Boolean isLoginOrRegister = true;
    private WebSocketClient mWebSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent inx = new Intent(getApplicationContext(), NewMainActivity.class);
        startActivity(inx);

        //Check Session
        sharedpreferences = getSharedPreferences(UserSession.userInfoSession, Context.MODE_PRIVATE);
        checkSessionIsNull();

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        //set up register form
        mRegisterEmailView = (AutoCompleteTextView) findViewById(R.id.register_email);
        mRegisterPasswordView = (EditText) findViewById(R.id.register_password);
        mRegisterNameView = (EditText) findViewById(R.id.register_name);
        mRegisterSurnameView = (EditText) findViewById(R.id.register_surname);
        mRegisterGenderRadioGroup = (RadioGroup) findViewById(R.id.register_sex);
        mRegisterBirthdayView = (EditText) findViewById(R.id.register_birthtext);

        //set up views
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        mRegisterFormView = findViewById(R.id.register_form);

        //Set button and edittext events
        setEvents();

        populateAutoComplete();
        
    }

    private void checkSessionIsNull() {
        if (sharedpreferences.getInt(UserSession.Id, 999999) != 999999) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        checkConnection();
    }

    private void checkConnection() {
        final ClassConnectionDetector cd = new ClassConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            final ProgressDialog progress = ProgressDialog.show(com.mehme.menuexample.LoginActivity.this, "Waiting Connection", "Loading....", true);
            progress.show();
            final Handler h = new Handler();
            h.postDelayed(new Runnable() {
                public void run() {
                    if (!cd.isConnectingToInternet()) {
                        h.postDelayed(this, 1000);

                    } else {
                        progress.dismiss();
                    }
                }
            }, 1000);
        }
    }

    private void setEvents() {
        final Calendar c = Calendar.getInstance();
        mRegisterBirthdayView.setText(c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR));
        mRegisterBirthdayView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent arg1) {
                if (mRegisterBirthdayView.isFocusableInTouchMode()) {
                    mRegisterBirthdayView.setFocusableInTouchMode(false);
                    showDatePickerDialog(arg0);

                }
                return false;
            }
        });

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        final Button mShowSignIn = (Button) findViewById(R.id.showSignIn);
        final Button mShowRegister = (Button) findViewById(R.id.showRegister);
        mShowSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginFormView.setVisibility(View.VISIBLE);
                mRegisterFormView.setVisibility(View.GONE);
                mShowRegister.setBackgroundResource(R.color.colorBackGround);
                mShowSignIn.setBackgroundResource(R.color.White);
                isLoginOrRegister = true;
            }
        });
        mShowRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginFormView.setVisibility(View.GONE);
                mRegisterFormView.setVisibility(View.VISIBLE);
                mShowRegister.setBackgroundResource(R.color.White);
                mShowSignIn.setBackgroundResource(R.color.colorBackGround);
                isLoginOrRegister = false;
            }
        });
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(getApplicationContext(), com.mehme.menuexample.LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        //Bundle date = new Bundle();
        //date.putString("dateKey","dateValue");
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            loginHttpRequest(email, password);
        }
    }

    private void attemptRegister() {
        // Reset errors.
        mRegisterEmailView.setError(null);
        mRegisterPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mRegisterEmailView.getText().toString();
        String password = mRegisterPasswordView.getText().toString();
        String name = mRegisterNameView.getText().toString();
        String surname = mRegisterSurnameView.getText().toString();
        String gender = "Male";
        String birthDay = mRegisterBirthdayView.getText().toString();
        if (mRegisterGenderRadioGroup.indexOfChild(findViewById(mRegisterGenderRadioGroup.getCheckedRadioButtonId())) == R.id.radioFemale)
            gender = "Female";

        boolean cancel = false;
        View focusView = null;
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mRegisterPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRegisterPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email)) {
            mRegisterEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mRegisterEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mRegisterEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            registerHttpRequest(email, password, name, surname, birthDay, gender);
        }
    }

    protected void loginHttpRequest(String mEmail, String mPassword) {
        JSONObject login = new JSONObject();
        try {
            login.put("Email", mEmail);
            login.put("Password", mPassword);
        } catch (JSONException e) {

        }
        new HttpRequestTask(
                new HttpRequest(ClassSession.SERVER_URL + "Api/Login", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {

                        showProgress(false);
                        if (response.code == 200) {
                            JSONObject myObject;
                            try {
                                myObject = new JSONObject(response.body);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt(UserSession.Id, myObject.getInt("Id"));
                                editor.putString(UserSession.Email, myObject.getString("Email"));
                                editor.putString(UserSession.Name, myObject.getString("Name"));
                                editor.putString(UserSession.Surname, myObject.getString("Surname"));
                                editor.putString(UserSession.Gender, myObject.getString("Gender"));
                                editor.putString(UserSession.Birthday, myObject.getString("Birthday"));
                                editor.commit();

                                MessageNotifyService.setupAlarm(getApplicationContext());

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                finish();
                            }
                        } else if (response.code == 400) {
                            JSONObject myObject;
                            try {
                                myObject = new JSONObject(response.body);
                                if (myObject.getString("Message") == "Email or Username Wrong!") {
                                    mEmailView.setError("Email or username not correct!");
                                    mEmailView.requestFocus();
                                } else if (myObject.getString("Message") == "Password is not Correct!") {
                                    mPasswordView.setError(getString(R.string.error_incorrect_password));
                                    mPasswordView.requestFocus();
                                }
                            } catch (JSONException e) {
                                finish();
                            }
                        } else {
                            //finish();
                            Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }

    protected void registerHttpRequest(String mEmail, String mPassword, String name, String surname, String birthDay, String gender) {
        JSONObject login = new JSONObject();
        try {
            login.put("Email", mEmail);
            login.put("Password", mPassword);
            login.put("Name", name);
            login.put("Surname", surname);
            login.put("Birthday", birthDay);
            login.put("Gender", gender);
        } catch (JSONException e) {

        }
        new HttpRequestTask(
                new HttpRequest(ClassSession.SERVER_URL + "Api/Register", HttpRequest.POST, login.toString(), ClassSession.AUTHORIZATION),
                new HttpRequest.Handler() {
                    @Override
                    public void response(HttpResponse response) {

                        showProgress(false);
                        if (response.code == 200) {
                            JSONObject myObject;
                            try {
                                myObject = new JSONObject(response.body);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putInt(UserSession.Id, myObject.getInt("Id"));
                                editor.putString(UserSession.Email, myObject.getString("Email"));
                                editor.putString(UserSession.Name, myObject.getString("Name"));
                                editor.putString(UserSession.Surname, myObject.getString("Surname"));
                                editor.putString(UserSession.Gender, myObject.getString("Gender"));
                                editor.putString(UserSession.Birthday, myObject.getString("Birthday"));
                                editor.commit();

                                MessageNotifyService.setupAlarm(getApplicationContext());

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                finish();
                            }
                        } else if (response.code == 400) {
                            JSONObject myObject;
                            try {
                                myObject = new JSONObject(response.body);
                                if (myObject.getString("Message") == "Email incorrect!") {
                                    mRegisterEmailView.setError("Incorrect Email!");
                                    mRegisterEmailView.requestFocus();
                                }
                            } catch (JSONException e) {
                                finish();
                            }
                        } else {
                            //finish();
                            Toast.makeText(getApplicationContext(), "Connection Failed! Please try later again.", Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            if (isLoginOrRegister) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            } else {
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
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
            if (isLoginOrRegister) {
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            } else {
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(com.mehme.menuexample.LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                requestHttp();
                // Simulate network access.
                Thread.sleep(2000);

            } catch (InterruptedException e) {
                return false;
            }
            /*
            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }
            */
            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            showProgress(false);

            if (success) {
                /*
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(UserSession.userEmail, mEmail);
                editor.putString(UserSession.userPassword, mPassword);
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                */
                // finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {

            showProgress(false);
        }

        protected void requestHttp() {
            JSONObject login = new JSONObject();
            JSONObject authorization = new JSONObject();
            try {
                login.put("UserName", mEmail);
                login.put("Password", mPassword);
                authorization.put("apiUser", "ali");
                authorization.put("apiPassword", "basli");
            } catch (JSONException e) {

            }

            new HttpRequestTask(

                    new HttpRequest("http://192.168.0.12:2255/api/login", HttpRequest.POST, login.toString()),
                    new HttpRequest.Handler() {
                        @Override
                        public void response(HttpResponse response) {
                            if (response.code == 200) {
                                mEmailView.setText("başarılı@email.com");
                                // textView.setText(response.body);
                            }
                        }
                    }).execute();
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            //String date = savedInstanceState.getString("dateKey");

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            EditText e = (EditText) getActivity().findViewById(R.id.register_birthtext);
            e.setText(day + "/" + month + "/" + year);
            e.setFocusableInTouchMode(true);

        }
    }
}

