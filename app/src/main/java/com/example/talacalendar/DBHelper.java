package com.example.talacalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Tala.db";
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_EMAIL + " TEXT PRIMARY KEY UNIQUE,"
                    + COLUMN_USER_NAME + " TEXT,"
                    + COLUMN_PASSWORD + " TEXT"
                    + ")";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertData( String email, String username, String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("email", email);
        cv.put("username", username);
        cv.put("password", pass);
        long result = db.insert("users", null, cv);
        if(result==-1)
            return false;
        else
            return true;
    }

    public Boolean checkemail(String email){ //checks if email exists in the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email = ?",new String[] {email});
        if(cursor.getCount() > 0) {
            return true;
        }else
            return false;
    }

    public Boolean checkemailpass(String email, String password){ //checks if email and password combination exists in the database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE email = ? AND password = ?", new String[]{email, password});
        if(cursor.getCount() > 0)
            return true;
        else
            return false;
    }
}
