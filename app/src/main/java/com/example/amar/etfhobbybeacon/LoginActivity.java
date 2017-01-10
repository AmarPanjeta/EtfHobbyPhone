package com.example.amar.etfhobbybeacon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String tokenG;
    private String usernameG;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
              /*  Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);*/
            }
        });
    }

    public void login() {

        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this/*, R.style.AppTheme_Dark_Dialog*/);
/*
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();*/

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        usernameG=email;

        // TODO: Implement your own authentication logic here.


        JsonObject json = new JsonObject();
        json.addProperty("username",_emailText.getText().toString());
        json.addProperty("password",_passwordText.getText().toString());

        Ion.with(this)
                .load("POST", getString(R.string.server_url)+"/user/login")
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e == null) {
                            Log.w("TAG","NEMA ERRORA");
                            Log.w("TAG",result);
                            JSONObject o = null; //silentStringToJson(result);
                            try {
                                o = new JSONObject(result);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            String error = o.optString("error");
                            String token = o.optString("token");

                            if (TextUtils.isEmpty(error)) {
                                Log.w("TAG","to je to");
                                Log.w("TAG",token);
                                tokenG=token;
                                onLoginSuccess();
                                //TODO: Save token
                            } else {
                                onLoginFailed();
                                //Show error
                                                /*
                                                Snackbar.make(mEmailView,
                                                        error,
                                                        Snackbar.LENGTH_SHORT)
                                                        .show();*/

                            }
                        } else {
                                            /*
                                            Snackbar.make(mEmailView,
                                                    "ERROR",
                                                    Snackbar.LENGTH_SHORT)
                                                    .show();*/

                            onLoginFailed();
                            Log.w("TAG","ERROR");
                            Log.w("TAG",e.getMessage().toString());
                        }
                        //showProgress(false);
                       // progressDialog.dismiss();
                    }
                });
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        Log.w("TAG","4");

                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                Log.w("TAG","1");
                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {

        _loginButton.setEnabled(true);
        Log.w("TAG","2");
        finish();
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("token",tokenG);
        i.putExtra("username",usernameG);
        startActivity(i);
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Neuspjesan login!", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() /*|| !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()*/) {
            _emailText.setError("Username ne moze biti prazan");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("Password mora biti duzine izmedu 4 i 10 karaktera");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

}
