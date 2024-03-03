package com.codex.tala;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final String PREF_NAME = "session";
    private static final String SESSION_KEY = "session_user";
    private static final int DEFAULT_USER_ID = -1;

    public SessionManager(Context context) {
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void saveSession(int userId) {
        editor.putInt(SESSION_KEY, userId);
        editor.apply();
    }

    public int getSession() {
        return pref.getInt(SESSION_KEY, DEFAULT_USER_ID);
    }

    public boolean isLoggedIn() {
        return getSession() != DEFAULT_USER_ID;
    }

    public void removeSession() {
        editor.putInt(SESSION_KEY, DEFAULT_USER_ID);
        editor.apply();
    }
}
