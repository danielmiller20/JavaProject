package com.foodie.app.ui;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.dx.dxloadingbutton.lib.LoadingButton;
import com.foodie.app.Helper.DebugHelper;
import com.foodie.app.R;
import com.foodie.app.database.AsyncData;
import com.foodie.app.database.CallBack;
import com.foodie.app.database.DBManagerFactory;
import com.foodie.app.database.DataManagerType;
import com.foodie.app.database.DataStatus;
import com.foodie.app.entities.Business;
import com.foodie.app.entities.CPUser;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.RegexpValidator;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    public static final String email = "EMAIL_KEY";
    public static final String password = "PASSWORD_KEY";
    public static final String mypreference = "mypref";


    ConstraintLayout constraintLayout;
    Snackbar snackbar;


    /**
     * Calles when the activity is created.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        constraintLayout = (ConstraintLayout) findViewById(R.id.activity_login); // By David


        final TextView signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO: Implement transition animation to register activity.
                signUpTextView.setTextColor(Color.parseColor("#BDBDBD"));

                try {

                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    } else {
                        startActivity(intent);
                    }

                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }

                signUpTextView.setTextColor(Color.parseColor("#FAFAFA"));


            }
        });


        final MaterialEditText emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        final MaterialEditText pwdEditText = (MaterialEditText) findViewById(R.id.pwdEditText);

        setTitle(getString(R.string.title_activity_login));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String mEmail = sharedPreferences.getString(email, "");
        if (mEmail.length() > 0) {
            emailEditText.setText(mEmail);
        }
        String mPassword = sharedPreferences.getString(password, "");
        if (mPassword.length() > 0) {
            pwdEditText.setText(mPassword);
        }

        final LoadingButton signInBtn = (LoadingButton) findViewById(R.id.sign_in_btn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLogin();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * Validates the user credentials.
     */
    private void checkLogin() {
        final MaterialEditText emailEditText = (MaterialEditText) findViewById(R.id.emailEditText);
        final MaterialEditText pwdEditText = (MaterialEditText) findViewById(R.id.pwdEditText);
        emailEditText.addValidator(new RegexpValidator(getString(R.string.error_invalid_email), "^([a-zA-Z0-9]+(?:(\\.|_)[A-Za-z0-9!#$%&'*+/=?^`{|}~-]+)*@(?!([a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.[a-zA-Z0-9]*\\.))(?:[A-Za-z0-9](?:[a-zA-Z0-9-]*[A-Za-z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?)$"));
        final LoadingButton signIn = (LoadingButton) findViewById(R.id.sign_in_btn);
        emailEditText.setError(null);
        signIn.startLoading();

        signIn.postDelayed(new Runnable() {
            @Override
            public void run() {
                final String emailAddress = emailEditText.getText().toString().trim();
                if (emailAddress.length() == 0) {
                    emailEditText.setError(getString(R.string.error_empty_email));
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                } else if (emailEditText.validate()) {

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sharedPreferences.edit().putString(email, emailAddress).apply();
                    sharedPreferences.edit().putString(password, pwdEditText.getText().toString()).apply();


                    //Toast.makeText(getApplicationContext(), getString(R.string.not_yet_implemented), Toast.LENGTH_SHORT).show();

                    /***************************Login*****************************/

                    DBManagerFactory.login(emailEditText.getText().toString(), pwdEditText.getText().toString(), new CallBack<CPUser>() {
                        @Override
                        public void onSuccess(final List<CPUser> data) {
                            signIn.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    final Intent intent = new Intent(LoginActivity.this, BusinessActivity.class);
                                    signIn.loadingSuccessful();
                                    Snackbar.make(constraintLayout, "Success", Snackbar.LENGTH_LONG).show();
                                    boolean b = new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {

                                            // test(); //By David

                                            if (DBManagerFactory.getCurrentUser() != null) {
                                                Bundle b = new Bundle();
                                                b.putString("Id", DBManagerFactory.getCurrentUser().get_ID()); //Your id
                                                b.putString("Fullname", DBManagerFactory.getCurrentUser().getUserFullName()); //Your id
                                                intent.putExtras(b); //Put your id to your next Intent
                                            }
                                            startActivity(intent);
                                            signIn.reset();

                                        }
                                    }, 400);
                                }
                            }, 1200);

                        }

                        @Override
                        public void onFailed(final DataStatus status, final String error) {

                            switch (status) {
                                case Success:


                                case Failed:

                                    signIn.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            signIn.loadingFailed();
                                            Snackbar.make(constraintLayout, error, Snackbar.LENGTH_LONG).show();

                                        }
                                    }, 3000);
                                case ConectionError:
                                    signIn.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            signIn.loadingFailed();
                                            Snackbar.make(constraintLayout, error, Snackbar.LENGTH_LONG).show();

                                        }
                                    }, 3000);
                                default:
                                    DebugHelper.Log("Default switch in callBack");
                                    signIn.loadingFailed();
                                    break;
                            }
                        }

                    });

                    /***************************David*****************************/

                } else {
                    //emailEditText.validate();
                    signIn.loadingFailed();
                    Toast.makeText(getApplicationContext(), getString(R.string.error_login), Toast.LENGTH_SHORT).show();
                }
            }
        }, 1);

    }

    /**
     * For debugging purposes.
     */
    void test() {
        CPUser user = new CPUser();
        try {


            user.setUserFullName("Foodie Business");
            user.setUserEmail("a@a.c");
            user.setUserPwd("12345678");


        } catch (Exception e) {

            snackbar = Snackbar.make(constraintLayout, e.getMessage(), Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }


        //Create an AsyncData object and set the constructor
        AsyncData<CPUser> data = new AsyncData<>(getApplicationContext(), CPUser.getURI());
        // Set the task to insert
        data.setDatamanagerType(DataManagerType.Insert);
        // Set the function to get status
        data.setCallBack(new CallBack<CPUser>() {
            @Override
            public void onSuccess(List<CPUser> data) {

            }

            @Override
            public void onFailed(DataStatus status, String error) {

            }


        });
        // Execute the AsyncTask
        data.execute(user.toContentValues());

        /******************Business test ***************************/


        Business demo;


        CallBack<Business> callBack = new CallBack<Business>() {
            @Override
            public void onSuccess(List<Business> data) {

            }

            @Override
            public void onFailed(DataStatus status, String error) {

            }


        };


        try {

            String name1 = "Burgeranch ";


            Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.burgeranch);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 400, 320, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo1 = stream.toByteArray();

            demo = new Business(name1, "Derech Agudat Sport Beitar 1, Jerusalem, 9695235", "0543051733", "Burgeranch@burgeranch.co.il", "burgeranch.co.il", "UAkQb4TN3yYsb4RrNDNyRzXDudB2", logo1);
            (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Insert, callBack)).execute(demo.toContentValues());


            String name2 = "McDonald's ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.mcdonalds_logo);
            stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 400, 320, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo2 = stream.toByteArray();

            demo = new Business(name2, "Sderot Yitshak Rabin 10, Jerusalem, 1234558", "0543051733", "McDonald@mcdonald.com", "mcdonald.com", "UAkQb4TN3yYsb4RrNDNyRzXDudB2", logo2);
            (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Insert, callBack)).execute(demo.toContentValues());


            String name3 = "Duda Lapizza ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.duda_lapizza_logo);
            stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 400, 320, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo3 = stream.toByteArray();

            demo = new Business(name3, "Sderot Hatsvi 5, Jerusalem, 6546185", "0543051733", "duda@lapizza.com", "duda-lapizza.com", "ZIaGMBmujlWMKSsqCs3Cs3zOt1y1", logo3);
            (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Insert, callBack)).execute(demo.toContentValues());


            String name4 = "Pizza Hut ";

            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.pizza_hut_logo);
            stream = new ByteArrayOutputStream();
            bmp = Bitmap.createScaledBitmap(bmp, 256, 256, true);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] logo4 = stream.toByteArray();

            demo = new Business(name4, "Nayot 9, Jerusalem, 6546185", "0543051733", "pizza@pizza-hut.com", "pizza-hut.com", "ZIaGMBmujlWMKSsqCs3Cs3zOt1y1", logo4);
            (new AsyncData<>(getApplicationContext(), Business.getURI(), DataManagerType.Insert, callBack)).execute(demo.toContentValues());


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

        }


    }

}

