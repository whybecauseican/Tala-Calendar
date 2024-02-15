package com.example.talacalendar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText mail = findViewById(R.id.email_editText);
        EditText pass = findViewById(R.id.password_editText);
        TextView forgotPass = findViewById(R.id.forgotPasswordTextView);
        TextView signUp = findViewById(R.id.signUp);
        Switch rememberMe = findViewById(R.id.rememberSwitch);
        Button loginBtn = findViewById(R.id.login_Btn);
        ImageView googleSignInButton = findViewById(R.id.google_Btn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //after clicking login, function must check if submitted details are valid. if so, nextActivity. if not, print user doesnt exist
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

            }
        });

        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}