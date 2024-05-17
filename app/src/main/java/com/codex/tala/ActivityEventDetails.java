package com.codex.tala;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class ActivityEventDetails extends AppCompatActivity {
    private TextView title, description, startDate, endDate, startTime, endTime, monthTv;
    private DBHelper db;
    private LinearLayout linearLayout;
    private Button edit_event_btn, delete_event_btn;
    private int userid, eventid;


    AlertDialog.Builder builder;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        userid = getIntent().getIntExtra("userId", -1);
        eventid = getIntent().getIntExtra("eventId", -1);

        linearLayout = (LinearLayout) findViewById(R.id.lin_back_btn);
        monthTv = (TextView) findViewById(R.id.ed_month_btn);
        edit_event_btn = (Button) findViewById(R.id.edit_event_btn);
        delete_event_btn = (Button) findViewById(R.id.delete_event_btn);
        builder = new AlertDialog.Builder(this);
        // TODO: add the other event details in here
        title = (TextView) findViewById(R.id.ED_event_name);
        description = (TextView) findViewById(R.id.descriptionTV);
        startDate = (TextView) findViewById(R.id.ED_dateStartTv);
        endDate = (TextView) findViewById(R.id.ED_dateEndTv);
        startTime = (TextView) findViewById(R.id.ED_timeStartTv);
        endTime = (TextView) findViewById(R.id.ED_timeEndTv);



        db = new DBHelper(this);
        setEventDetails();
        setupBtnClickListeners();
        db.close();
    }

    private void setupBtnClickListeners() {
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                finish();
                overridePendingTransition(0,R.anim.slide_down_anim);
            }
        });

        edit_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityEventDetails.this, "edit event clicked", Toast.LENGTH_SHORT).show();
            }
        });

        delete_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setTitle("")
                        .setMessage("Are you sure you want to delete this event?")
                        .setCancelable(true)
                        .setPositiveButton("Delete Event", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean isDeleted = db.deleteEventData(userid, eventid);
                                if (isDeleted) {
                                    Toast.makeText(ActivityEventDetails.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    overridePendingTransition(0,R.anim.slide_down_anim);
                                } else {
                                    Toast.makeText(ActivityEventDetails.this, "Failed to delete event", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();

            }
        });
    }

    private void setEventDetails() {
        Cursor cursor = db.getEventData(userid, eventid);
        if (cursor != null && cursor.moveToFirst()) {
            String titleCheck = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_TITLE));
            if (titleCheck.isEmpty())
                titleCheck = "(No title)";
            title.setText(titleCheck);

            String descCheck = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DESCRIPTION));
            if (descCheck.isEmpty())
                descCheck = "Add description";
            description.setText(descCheck);

            String startDateStr = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_DATE));
            String formattedStartDate = CalendarUtils.convertDateFormat(startDateStr);
            startDate.setText(formattedStartDate);

            String endDateStr = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_DATE));
            String formattedEndDate = CalendarUtils.convertDateFormat(endDateStr);
            endDate.setText(formattedEndDate);

            startTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_TIME)));
            endTime.setText(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_TIME)));
            monthTv.setText(CalendarUtils.monthFromDate(startDateStr));
            cursor.close();
        }
    }
}
