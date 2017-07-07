package com.example.asus.windowsprotected.activityset;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asus.windowsprotected.R;
import com.example.asus.windowsprotected.otherset.DataBaseSQLHelp;
import com.example.asus.windowsprotected.otherset.SystemBarTintManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

public class RegisterActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private UserLoginTask mAuthTask = null;
    private EditText et_password;
    private LinearLayout mLinearLayout_register;
    private LinearLayout mLinearLayout_register_card;
    private AutoCompleteTextView mAutoCompleteTextView_account;
    private EditText et_sure_password;
    private View mProgressView;
    private View mRegisterFormView;
    private Button mButton_register;
    private DataBaseSQLHelp mDataBaseSQLHelp;
    private int NO = 0;
    private static final int REQUEST_READ_CONTACTS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String str = formatter.format(curDate);
            String hour = str.substring(12,14);
            String minute = str.substring(15,17);
            int hour1 = Integer.valueOf(hour);
            int minute1 = Integer.valueOf(minute);
            if ((hour1>18)||(hour1==18&&minute1>=30)||(hour1>=0&&hour1<=6)){
                tintManager.setStatusBarTintResource(R.color.night_mode);//通知栏所需颜色
            }else {
                tintManager.setStatusBarTintResource(R.color.top_bg_color);//通知栏所需颜色
            }
        }
        setContentView(R.layout.activity_register);
        mButton_register = (Button) findViewById(R.id.btn_register);
        mLinearLayout_register = (LinearLayout) findViewById(R.id.linear_register);
        mLinearLayout_register_card = (LinearLayout) findViewById(R.id.linear_register_card);
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd- HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        String hour = str.substring(12,14);
        String minute = str.substring(15,17);
        int hour1 = Integer.valueOf(hour);
        int minute1 = Integer.valueOf(minute);
        if ((hour1>18)||(hour1==18&&minute1>=30)||(hour1>=0&&hour1<=6)){
            mLinearLayout_register.setBackgroundResource(R.drawable.login_background_night);
            mLinearLayout_register_card.setBackgroundResource(R.drawable.login_card_background_night);
            mButton_register.setBackgroundResource(R.drawable.button_style_night);
        }else {
            mLinearLayout_register.setBackgroundResource(R.drawable.login_background);
            mLinearLayout_register_card.setBackgroundResource(R.drawable.login_card_background);
            mButton_register.setBackgroundResource(R.drawable.button_style);
        }
        et_password = (EditText) findViewById(R.id.et_password);
        mAutoCompleteTextView_account = (AutoCompleteTextView) findViewById(R.id.actv_account);
        et_sure_password = (EditText) findViewById(R.id.et_sure_password);
        mProgressView = findViewById(R.id.register_progress);
        mRegisterFormView = findViewById(R.id.scrollView_register);
        mDataBaseSQLHelp = new DataBaseSQLHelp(this,"Register.db",null,1);
        populateAutoComplete();
        et_sure_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.et_sure_password||id == EditorInfo.IME_NULL) {
                    attemptRegister();
                    return true;
                }
                return false;
            }
        });
        mButton_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("llllllllllllll","uuuuuuuu");
                mAuthTask=null;
                attemptRegister();

            }
        });

    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if (email.contains("@")||email.length()==11){
            return true;
        }else {
            return false;
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 6;
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, RegisterActivity.this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mAutoCompleteTextView_account, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
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

    private void attemptRegister() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mAutoCompleteTextView_account.setError(null);
        et_password.setError(null);
        et_sure_password.setError(null);

        // Store values at the time of the login attempt.
        String email = mAutoCompleteTextView_account.getText().toString();
        String password = et_password.getText().toString();
        String sure_password = et_sure_password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password)&&!isPasswordValid(password)) {
            et_password.setError(getString(R.string.error_invalid_password));
            focusView = et_password;
            cancel = true;
        }
        if (!password.equals(sure_password)){
            et_sure_password.setError(getString(R.string.error_password_unequale));
            focusView = et_sure_password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mAutoCompleteTextView_account.setError(getString(R.string.error_field_required));
            focusView = mAutoCompleteTextView_account;
            cancel = true;
        }else if (!isEmailValid(email)) {
            mAutoCompleteTextView_account.setError(getString(R.string.error_invalid_email));
            focusView = mAutoCompleteTextView_account;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            mAuthTask = new UserLoginTask(email, password,sure_password);
            mAuthTask.execute((Void) null);
            Boolean h = mAuthTask.doInBackground();
            if (h){
                int j=5;
                SQLiteDatabase db = mDataBaseSQLHelp.getWritableDatabase();
                ContentValues values = new ContentValues();
                Cursor cursor =  db.query("Register",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String s = cursor.getString(cursor.getColumnIndex("register_name"));
                        String y = cursor.getString(cursor.getColumnIndex("register_password"));
                        if (s.equals(email)){
                            j=NO;
                        }
                        Log.d("MainActivity",s);
                        Log.d("MainActivity",y);
                    }while (cursor.moveToNext());
                }
                if (j==NO){
                    Toast.makeText(this,"用户已经存在 注册失败",Toast.LENGTH_SHORT).show();

                }else {
                    showProgress(true);
                    values.put("register_name",email);
                    values.put("register_password",password);
                    db.insert("Register",null,values);
                    values.clear();
                    Intent intent = new Intent();
                    intent.putExtra("et_account", email);
                    setResult(RESULT_OK, intent);
                    finish();
                    Toast.makeText(this,"注册成功",Toast.LENGTH_SHORT).show();
                }
            }else {
                //Toast.makeText(LoginActivity.this,"账号与密码不匹配",Toast.LENGTH_SHORT).show();
            }

        }
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), RegisterActivity.ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(RegisterActivity.ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(RegisterActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mAutoCompleteTextView_account.setAdapter(adapter);
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private final String mSure_password;

        UserLoginTask(String email, String password,String sure_password) {
            mEmail = email;
            mPassword = password;
            mSure_password = sure_password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            if (mPassword.equals(mSure_password)&&!TextUtils.isEmpty(mPassword)&&!TextUtils.isEmpty(mEmail)){
                return true;
            }else return false;
            // TODO: register the new account here.
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_mediumAnimTime);

                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
