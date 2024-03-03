package com.codex.tala;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "session";
    private static final String SESSION_KEY = "session_user";
    private static final String DEFAULT_EMAIL = "";

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveSession(String email) {
        editor.putString(SESSION_KEY, email);
        editor.apply();
    }

    public String getSession() {
        return pref.getString(SESSION_KEY, DEFAULT_EMAIL);
    } //return email else default email

    public boolean isLoggedIn() {
        return !getSession().equals(DEFAULT_EMAIL);
    }

    public void removeSession() {
        editor.putString(SESSION_KEY, DEFAULT_EMAIL);
        editor.apply();
    }
}
