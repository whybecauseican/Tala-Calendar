package com.example.talacalendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton add_btn, add_cal, talk_ai;
    private FABHandler FAB;
    private DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        FAB = new FABHandler(this);
        db = new DBHelper(this);

        add_btn = findViewById(R.id.add_btn);
        add_cal =findViewById(R.id.event_shortcut_btn);
        talk_ai = findViewById(R.id.talk_ai_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAB.onButtonClicked();
            }
        });
        add_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Add_cal clicked", Toast.LENGTH_SHORT).show();
                FAB.onButtonClicked();
            }
        });
        talk_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "TALk to ai clicked", Toast.LENGTH_SHORT).show();
                FAB.onButtonClicked();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        SessionManager sessionManager = new SessionManager(MainActivity.this);
        boolean isLoggedin = sessionManager.isLoggedIn();

        if (isLoggedin == false){ // TODO: 26/02/2024 change loginactivity.class to whatever class is the login and register page is
            Intent i = new Intent(MainActivity.this, LoginActvity.class);
            startActivity(i);
            finish();
        }
    }
}
