package goodapp.company.kissmyass.TiujKiss.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import goodapp.company.kissmyass.Models.User;
import goodapp.company.kissmyass.R;
import goodapp.company.kissmyass.Security.AESEncrypt;
import microsoft.aspnet.signalr.client.Action;

import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentActivity;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.currentUser;
import static goodapp.company.kissmyass.TiujKiss.TiujKissR.HubConnection;
import static goodapp.company.kissmyass.Security.AESEncrypt.*;

/**
 * Created by mehme on 3.08.2016.
 */
public class NLoginActivity extends AppCompatActivity {
    private static final int REQUEST_READ_CONTACTS = 0;
    private ProgressDialog progressDialog;
    private SharedPreferences prefs;

    private int mDay, mMonth, mYear;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;

    private AutoCompleteTextView mRegisterEmailView;
    private EditText mRegisterPasswordView;
    private EditText mRegisterNameView;
    private EditText mRegisterSurnameView;
    private RadioGroup mRegisterGenderRadioGroup;
    private EditText mRegisterBirthdayView;

    private View mLoginFormView;
    private View mRegisterFormView;
    private View mHeaderFormsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        prefs = getSharedPreferences("UserIDPref",MODE_PRIVATE);
        currentActivity = this;
        progressDialog = new ProgressDialog(currentActivity);

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

        mLoginFormView = findViewById(R.id.login_form);
        mRegisterFormView = findViewById(R.id.register_form);
        mHeaderFormsView = findViewById(R.id.header_form);

        setEvents();

        try {
            if (PASSWORD_BASED_KEY) {
                String salt = saltString(generateSalt());
                key = AESEncrypt.keys(PASSWORD);
            } else {
                key = generateKey();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //checkSessionIsNull();
    }
    private void checkSessionIsNull() {
        if(prefs.getString("UserID", "^") == "^") {

            try {
                if (PASSWORD_BASED_KEY) {
                    String salt = saltString(generateSalt());
                    key = AESEncrypt.keys(PASSWORD);
                } else {
                    key = generateKey();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else
        {
            HubConnection.getUserById(prefs.getString("UserID", "^"))
                    .done(new Action<User>()
            {
                @Override
                public void run(User user)
                {
                    loginSuccess(user);
                }
            });
        }

    }

    private void setEvents() {
        mRegisterBirthdayView.setText(getCurrentDate());
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
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
        Button mEmailRegisterButton = (Button) findViewById(R.id.email_register_button);
        mEmailRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        final Button mShowSignIn = (Button) findViewById(R.id.showSignIn);
        final Button mShowRegister = (Button) findViewById(R.id.showRegister);
        mShowSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginFormView.setVisibility(View.VISIBLE);
                mRegisterFormView.setVisibility(View.GONE);
                mShowRegister.setBackgroundResource(R.color.SlateGrey);
                mShowRegister.setTextColor(getResources().getColor(R.color.White));
                mShowSignIn.setBackgroundResource(R.color.White);
                mShowSignIn.setTextColor(getResources().getColor(R.color.SlateGrey));
            }
        });
        mShowRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginFormView.setVisibility(View.GONE);
                mRegisterFormView.setVisibility(View.VISIBLE);
                mShowRegister.setBackgroundResource(R.color.White);
                mShowRegister.setTextColor(getResources().getColor(R.color.SlateGrey));
                mShowSignIn.setBackgroundResource(R.color.SlateGrey);
                mShowSignIn.setTextColor(getResources().getColor(R.color.White));
            }
        });
    }

    private String getCurrentDate() {
        final Calendar c = Calendar.getInstance();
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mYear = c.get(Calendar.YEAR);
        if (mMonth < 9){
            if(mDay>10)
                return mDay + "-" + "0" + (mMonth + 1) + "-" + mYear;
            else
                return "0"+mDay + "-" + "0" + (mMonth + 1) + "-" + mYear;
        }
        else {
            if (mDay > 10)
                return mDay + "-" + (mMonth + 1) + "-" + mYear;
            else
                return "0"+mDay + "-" + (mMonth + 1) + "-" + mYear;
        }
    }

    private void setDate() {
        String date = mRegisterBirthdayView.getText().toString();
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

    public void showDatePickerDialog(View v) {

        Bundle date = new Bundle();
        setDate();
        date.putInt("dayKey", mDay);
        date.putInt("monthKey", mMonth - 1);
        date.putInt("yearKey", mYear);
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(date);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
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
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
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
            try
            {
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                HubConnection.login(email, password);
            }
            catch(Exception ex )
            {
                ex.printStackTrace();
            }
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
        if (mRegisterGenderRadioGroup.getCheckedRadioButtonId() == R.id.radioFemale)
            gender = "Female";

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mRegisterPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mRegisterPasswordView;
            cancel = true;
        }
        if (TextUtils.isEmpty(email) ) {
            mRegisterEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mRegisterEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mRegisterEmailView;
            cancel = true;
        }
        if (TextUtils.isEmpty(name)) {
            mRegisterNameView.setError(getString(R.string.error_field_required));
            focusView = mRegisterNameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            try {
                //userPassword = encrypt(userPassword, key).toString();
                progressDialog.setMessage("Registering. Please Wait");
                progressDialog.show();
                HubConnection.register(email, password, name, surname, birthDay, gender);
            } catch (Exception ex) {
                progressDialog.dismiss();
                runOnUiThread(new Runnable() {
                                  @Override
                                  public void run() {
                                      Toast.makeText(currentActivity, "There was a problem registering. Please check your data connection!", Toast.LENGTH_LONG).show();
                                  }
                              }

                );
            }
        }
    }

    public void loginSuccess(User user) {
        if (prefs.getString("UserID", "^") == "^") {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("UserID", user.getUserID());
            editor.apply();
        }

        currentUser = user;
        progressDialog.dismiss();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void loginFailure(final String status) {
        progressDialog.dismiss();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String message = "";
                switch (status) {
                    case "1":
                        message = "incorrect password";
                        break;
                    case "2":
                        message = "incorect email";
                        break;
                    case "3":
                        message = "Error code 10";
                        break;
                    default:
                        message = status;
                        break;
                }
                Toast toast = Toast.makeText(currentActivity, message, Toast.LENGTH_LONG);
                toast.show();
            }
        });
    }

    public void registerSuccess(User user)
    {
        progressDialog.dismiss();
        currentUser = user;
        startActivity(new Intent(this, MainActivity.class));
    }

    public void registerFailure(final String status)
    {
        progressDialog.dismiss();
        runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              String message = "";
                              switch (status) {
                                  case "2":
                                      message = "incorect email";
                                      break;
                                  case "3":
                                      message = "Error code 10";
                                      break;
                                  default:
                                      message = status;
                                      break;
                              }
                              Toast toast = Toast.makeText(currentActivity, message, Toast.LENGTH_LONG);
                              toast.show();
                          }
                      }
        );
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
            dYear = getArguments().getInt("yearKey");
            dMonth = getArguments().getInt("monthKey");
            dDay = getArguments().getInt("dayKey");
            return new DatePickerDialog(getActivity(), this, dYear, dMonth, dDay);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            EditText e = (EditText) getActivity().findViewById(R.id.register_birthtext);
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
