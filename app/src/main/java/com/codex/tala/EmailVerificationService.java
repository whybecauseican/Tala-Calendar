package com.codex.tala;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationService extends IntentService {

    private static final String TAG = "EmailVerificationServi";
    private FirebaseAuth mAuth;

    public EmailVerificationService() {
        super("EmailVerificationService");
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email verification sent to " + user.getEmail());
                        } else {
                            Log.e(TAG, "Failed to send email verification", task.getException());
                        }
                    });
        } else {
            Log.e(TAG, "User is null");
        }
    }
}
