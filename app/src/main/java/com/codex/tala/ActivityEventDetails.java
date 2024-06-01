package com.codex.tala;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ActivityEventDetails extends AppCompatActivity {
    private TextView title, description, startDate, endDate, startTime, endTime, monthTv;
    private DBHelper db;
    private LinearLayout linearLayout;
    private Button edit_event_btn, delete_event_btn;
    private int userid, eventid;
    private DatabaseReference rootNode;
    private ValueEventListener eventListener; // Declare the ValueEventListener

    AlertDialog.Builder builder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        rootNode = FirebaseDatabase.getInstance().getReference();

        userid = getIntent().getIntExtra("userId", -1);
        eventid = getIntent().getIntExtra("eventId", -1);

        linearLayout = findViewById(R.id.lin_back_btn);
        monthTv = findViewById(R.id.ed_month_btn);
        edit_event_btn = findViewById(R.id.edit_event_btn);
        delete_event_btn = findViewById(R.id.delete_event_btn);
        builder = new AlertDialog.Builder(this);
        title = findViewById(R.id.ED_event_name);
        description = findViewById(R.id.descriptionTV);
        startDate = findViewById(R.id.ED_dateStartTv);
        endDate = findViewById(R.id.ED_dateEndTv);
        startTime = findViewById(R.id.ED_timeStartTv);
        endTime = findViewById(R.id.ED_timeEndTv);

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
                overridePendingTransition(0, R.anim.slide_down_anim);
            }
        });

        edit_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ActivityEventDetails.this, "edit event clicked", Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference userEventsRef = rootNode.child("events").child(String.valueOf(userid));

        eventListener = userEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Handle data changes here
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors in retrieving data
            }
        });

        delete_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEventsRef.removeEventListener(eventListener); // Remove the event listener

                builder.setTitle("")
                        .setMessage("Are you sure you want to delete this event?")
                        .setCancelable(true)
                        .setPositiveButton("Delete Event", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean isDeleted = db.deleteEventData(userid, eventid);
                                if (isDeleted) {
                                    DatabaseReference eventRef = rootNode.child("events").child(String.valueOf(userid)).child(String.valueOf(eventid));
                                    eventRef.removeValue()
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(ActivityEventDetails.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    overridePendingTransition(0, R.anim.slide_down_anim);
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(ActivityEventDetails.this, "Failed to delete event from Firebase", Toast.LENGTH_SHORT).show();
                                                }
                                            });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the event listener when the activity is destroyed
        if (eventListener != null) {
            DatabaseReference userEventsRef = rootNode.child("events").child(String.valueOf(userid));
            userEventsRef.removeEventListener(eventListener);
        }
    }

    private void setEventDetails() {
        Cursor cursor = db.getEventData(userid, eventid);
        if (cursor != null && cursor.moveToFirst()) {
            String titleCheck = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_TITLE));
            if (titleCheck.isEmpty()) {
                titleCheck = "(No title)";
            }
            title.setText(titleCheck);

            String descCheck = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_DESCRIPTION));
            if (descCheck.isEmpty()) {
                descCheck = "Add description";
            }
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
