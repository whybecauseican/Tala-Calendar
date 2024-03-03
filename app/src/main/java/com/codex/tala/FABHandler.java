package com.codex.tala;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FABHandler { //floating action button handler

    private Animation rotateOpen, rotateClose, fromBottom, toBottom;
    private TextView text_cal, text_ai;
    private FloatingActionButton add_cal, talk_ai, add_btn;

    private boolean clicked = false;

    public FABHandler(MainActivity activity) {
        rotateOpen = AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim);
        text_cal = activity.findViewById(R.id.text_cal);
        text_ai = activity.findViewById(R.id.text_Ai);
        add_btn = activity.findViewById(R.id.add_btn);
        add_cal = activity.findViewById(R.id.event_shortcut_btn);
        talk_ai = activity.findViewById(R.id.talk_ai_btn);
    }

    public void onButtonClicked() {
        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }

    private void setVisibility(boolean clicked) {
        int visibility = clicked ? View.INVISIBLE : View.VISIBLE;
        add_cal.setVisibility(visibility);
        text_cal.setVisibility(visibility);
        talk_ai.setVisibility(visibility);
        text_ai.setVisibility(visibility);
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
        talk_ai.setClickable(!clicked);
        add_cal.setClickable(!clicked);
    }
}
