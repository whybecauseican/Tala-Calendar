package com.codex.tala;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityLogin extends AppCompatActivity {
    private EditText mail, pass;
    private TextView forgotPass, signUp;
    private Switch rememberMe;
    private Button loginBtn;
    private ImageView googleSignInBtn;

    private DBHelper db;
    private Boolean rememberCond;
    private FirebaseAuth mAuth;
    private DatabaseReference rootNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        mail = findViewById(R.id.email_editText);
        pass = findViewById(R.id.password_editText);
        forgotPass = findViewById(R.id.forgotPasswordTextView);
        signUp = findViewById(R.id.signUp);
        rememberMe = findViewById(R.id.rememberSwitch);
        loginBtn = findViewById(R.id.login_Btn);
        googleSignInBtn = findViewById(R.id.google_Btn);
        mAuth = FirebaseAuth.getInstance();
        rootNode = FirebaseDatabase.getInstance().getReference();

        db = new DBHelper(this);

        rememberCond = false;

        rememberMe.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rememberCond = isChecked;
            }
        });

        // Retrieve saved credentials if exist
        String[] savedCredentials = db.getUserCredentials();
        if (savedCredentials[0] != null && savedCredentials[1] != null) {
            mail.setText(savedCredentials[0]);
            pass.setText(savedCredentials[1]);
            rememberMe.setChecked(true);
        } else {
            db.clearUserCredentials();
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mail.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(ActivityLogin.this, "Email and password must not be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(ActivityLogin.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        if (user.isEmailVerified()) {
                                            int userid = db.getUserId(email, password);
                                            if (userid == -1) {
                                                // User does not exist in local DB, create an entry
                                                db.insertUsersData(email, user.getDisplayName(), password);
                                                userid = db.getUserId(email, password);
                                            }
                                            Log.d("LoginActivity", "User authentication successful. UserId: " + userid);
                                            syncEventsFromFirebase(userid);
                                            if (rememberCond) {
                                                db.saveUserCredentials(email, password);
                                                SessionManager sessionManager = new SessionManager(ActivityLogin.this);
                                                sessionManager.saveSession(userid);
                                            } else {
                                                db.clearUserCredentials();
                                            }
                                            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
                                            intent.putExtra("userId", userid);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Log.d("LoginActivity", "Email not verified");
                                            Toast.makeText(ActivityLogin.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Log.d("LoginActivity", "Current user is null");
                                        Toast.makeText(ActivityLogin.this, "User is null", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Log.d("LoginActivity", "Authentication failed: " + task.getException().getMessage());
                                    Toast.makeText(ActivityLogin.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle forgot password functionality here
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
                // Handle Google Sign-In functionality here
            }
        });
    }

    //original code
   /* private void syncEventsFromFirebase(int userId) {
        DatabaseReference userEventsRef = rootNode.child("events").child(String.valueOf(userId));

        userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventName = eventSnapshot.getKey(); // Changed line to use event name as key
                    String description = eventSnapshot.child("description").getValue(String.class);
                    String startTime = eventSnapshot.child("startTime").getValue(String.class);
                    String endTime = eventSnapshot.child("endTime").getValue(String.class);
                    String startDate = eventSnapshot.child("startDate").getValue(String.class);
                    String endDate = eventSnapshot.child("endDate").getValue(String.class);

                    // Check if the event already exists in the local database
                    if (!db.isEventExists(userId, eventName, startDate, startTime)) {
                        db.insertEventData(userId, eventName, startDate, endDate, startTime, endTime, description);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving events: " + error.getMessage());
            }
        });
    }*/

    //test code
    private void syncEventsFromFirebase(int userId) {
        DatabaseReference userEventsRef = rootNode.child("events").child(String.valueOf(userId));

        userEventsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                    String eventName = eventSnapshot.getKey(); // Ensure correct key usage
                    String description = eventSnapshot.child("description").getValue(String.class);
                    String startTime = eventSnapshot.child("startTime").getValue(String.class);
                    String endTime = eventSnapshot.child("endTime").getValue(String.class);
                    String startDate = eventSnapshot.child("startDate").getValue(String.class);
                    String endDate = eventSnapshot.child("endDate").getValue(String.class);

                    if (!db.isEventExists(userId, eventName, startDate, startTime)) {
                        db.insertEventData(userId, eventName, startDate, endDate, startTime, endTime, description);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving events: " + error.getMessage());
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

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            int userid = db.getUserId(email, pwd);
            if (rememberCond) {
                SessionManager sessionManager = new SessionManager(ActivityLogin.this);
                sessionManager.saveSession(userid);
            }
            Intent intent = new Intent(ActivityLogin.this, ActivityMain.class);
            intent.putExtra("userId", userid);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(ActivityLogin.this, "User is null", Toast.LENGTH_SHORT).show();
        }
    }
}
