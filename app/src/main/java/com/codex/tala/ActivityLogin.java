package com.codex.tala;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityLogin extends AppCompatActivity {
    private EditText mail, pass;
    private TextView forgotPass, signUp;
    private Switch rememberMe;
    private Button loginBtn;
    private ImageView googleSignInBtn;

    private DBHelper db;
    private Boolean rememberCond;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        mail = (EditText) findViewById(R.id.email_editText);
        pass = (EditText) findViewById(R.id.password_editText);
        forgotPass = (TextView) findViewById(R.id.forgotPasswordTextView);
        signUp = (TextView) findViewById(R.id.signUp);
        rememberMe = (Switch) findViewById(R.id.rememberSwitch);
        loginBtn = (Button) findViewById(R.id.login_Btn);
        googleSignInBtn = (ImageView) findViewById(R.id.google_Btn);
        db = new DBHelper(this);

        rememberCond = false;

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberCond = isChecked;
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivitySignUp.class);
                startActivity(intent);
            }
        });

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void login() {
        String email = mail.getText().toString().trim();
        String pwd = pass.getText().toString();

        if (email.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(ActivityLogin.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isValidCredentials = db.checkemailpass(email, pwd);
        if (isValidCredentials) {
            int userid = db.getUserId(email,pwd);
            if (rememberCond) {
                SessionManager sessionManager = new SessionManager(ActivityLogin.this);
                sessionManager.saveSession(userid);
            }
            Log.d("USERID IN LOGIN", String.valueOf(userid));
            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
            intent.putExtra("userId", userid);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(ActivityLogin.this, "Invalid email and/or password", Toast.LENGTH_SHORT).show();
        }
    }

}