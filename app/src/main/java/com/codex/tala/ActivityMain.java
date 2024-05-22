package com.codex.tala;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDate;
import java.util.Objects;

public class ActivityMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DBHelper db;
    private SessionManager sessionManager;
    private FragmentManager fragmentManager;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private FloatingActionButton add_btn, add_cal, talk_ai;
    private FABHandler FAB;
    private NavigationView navigationView;
    private View headerView, dimView;
    private TextView textViewUsername, textViewEmail;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        userId = getIntent().getIntExtra("userId", -1);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        sessionManager = new SessionManager(ActivityMain.this);
        db = new DBHelper(this);
        FAB = new FABHandler(this);

        if (userId != -1 || sessionManager.isLoggedIn()){ //is set to if not null so database checks wouldnt return error
            if (userId == -1){
                userId = sessionManager.getSession(); //get userId from session if userId is null but isLoggedin which indicates userId is present but not stored
            }
            fragmentManager = getSupportFragmentManager();
            CalendarUtils.selectedDate = (LocalDate) LocalDate.now();
            openFragment(new MonthFragment(userId));

            setNavHeaders();
            setupBtnClickListeners();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean isLoggedin = sessionManager.isLoggedIn();
        if (!isLoggedin && userId == -1) {
            Intent i = new Intent(ActivityMain.this, ActivityLogin.class);
            startActivity(i);
            finish();
        }
    }

    private void setNavHeaders() {
        navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);

        headerView = navigationView.getHeaderView(0);

        textViewUsername = headerView.findViewById(R.id.nav_username);
        textViewEmail = headerView.findViewById(R.id.nav_email);

        textViewUsername.setText(db.getUsername(userId));
        textViewEmail.setText(db.getEmail(userId));
    }

    private void setupBtnClickListeners() {
        add_btn = findViewById(R.id.add_btn);
        add_cal = findViewById(R.id.event_shortcut_btn);
        talk_ai = findViewById(R.id.talk_ai_btn);
        dimView = findViewById(R.id.dimView);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAB.onButtonClicked();
            }
        });
        add_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAB.onButtonClicked();
                Intent intent = new Intent(ActivityMain.this, ActivityEventAdd.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_anim,0);
            }
        });
        talk_ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAB.onButtonClicked();
                Intent intent = new Intent(ActivityMain.this, ActivityAI.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_up_anim,0);
            }
        });

        dimView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FAB.onButtonClicked();
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.month_view) {
            openFragment(new MonthFragment(userId));
        } else if (itemId == R.id.day_view) {
            openFragment(new DayFragment(userId));
        }
//        else if (itemId == R.id.schedule_view) {
//            openFragment(new ScheduleFragment(userId));
//        }
        else if (itemId == R.id.about_us) {
            Intent intent = new Intent(ActivityMain.this, ActivityAboutUs.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,0);
        } else if (itemId == R.id.logout_btn) {
            sessionManager.removeSession();
            Intent i = new Intent(ActivityMain.this, ActivityLogin.class);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}
