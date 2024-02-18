package com.example.talacalendar;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private TextView text_cal, text_ai;
    private FloatingActionButton add_btn, add_cal, talk_ai;

    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        rotateOpen = AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim);
        text_cal = findViewById(R.id.text_cal);
        text_ai = findViewById(R.id.text_Ai);

        add_btn = findViewById(R.id.add_btn);
        add_cal = findViewById(R.id.event_shortcut_btn);
        talk_ai = findViewById(R.id.talk_ai_btn);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });

        // Set onClickListeners for other buttons if needed
    }

    private void onAddButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        if (!clicked) {
            add_cal.setVisibility(View.VISIBLE);
            text_cal.setVisibility(View.VISIBLE);
            talk_ai.setVisibility(View.VISIBLE);
            text_ai.setVisibility(View.VISIBLE);
        } else {
            add_cal.setVisibility(View.INVISIBLE);
            text_cal.setVisibility(View.INVISIBLE);
            talk_ai.setVisibility(View.INVISIBLE);
            text_ai.setVisibility(View.INVISIBLE);
        }
    }

    private void setAnimation(boolean clicked) {
        if (!clicked) {
            talk_ai.startAnimation(fromBottom);
            add_cal.startAnimation(fromBottom);
            text_ai.startAnimation(fromBottom);
            text_cal.startAnimation(fromBottom);
            add_btn.startAnimation(rotateOpen);
        } else {
            talk_ai.startAnimation(toBottom);
            add_cal.startAnimation(toBottom);
            text_ai.startAnimation(toBottom);
            text_cal.startAnimation(toBottom);
            add_btn.startAnimation(rotateClose);
        }
    }

    private void setClickable(boolean clicked) {
        if(!clicked) {
            talk_ai.setClickable(true);
            add_cal.setClickable(true);
        }else {
            talk_ai.setClickable(false);
            add_cal.setClickable(false);
        }
    }
}
