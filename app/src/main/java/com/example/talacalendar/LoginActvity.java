package com.example.talacalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActvity extends AppCompatActivity {
    private EditText mail, pass;
    private TextView forgotPass, signUp;
    private Switch rememberMe;
    private Button loginBtn;
    private ImageView googleSignInBtn;

    private DBHelper db;

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString();
                String pwd = pass.getText().toString();

                if(email.isEmpty() || pwd.isEmpty()){
                    Toast.makeText(LoginActvity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean checkemailpass = db.checkemailpass(email, pwd);
                    if (checkemailpass == true){
                        Intent i = new Intent(LoginActvity.this, MainActivity.class);
                        startActivity(i);
                    }else{
                        Toast.makeText(LoginActvity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        rememberMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                Intent intent = new Intent(LoginActvity.this, SignUp.class);
                startActivity(intent);
            }
        });

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}