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
    protected void onCreate(Bundle savedInstanceState) {
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

        setupRealTimeListener(); // Call setupRealTimeListener() in onCreate() or a similar lifecycle method
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

        delete_event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the event listener before performing delete
                DatabaseReference userEventsRef = rootNode.child("events").child(String.valueOf(userid));
                userEventsRef.removeEventListener(eventListener);

                builder.setTitle("")
                        .setMessage("Are you sure you want to delete this event?")
                        .setCancelable(true)
                        .setPositiveButton("Delete Event", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEvent(eventid);
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

    // Add this method to set up the real-time listener
    private void setupRealTimeListener() {
        DatabaseReference userEventsRef = rootNode.child("events").child(String.valueOf(userid));
        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Handle the event data changes
                if (dataSnapshot.exists()) {
                    // Update UI with the latest data
                    // This will ensure that any changes are reflected in real-time
                } else {
                    // Handle the case when data is deleted
                    // Maybe update the UI accordingly
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle possible errors
            }
        };
        userEventsRef.addValueEventListener(eventListener);
    }

    private void deleteEvent(int eventId) {
        // Remove from local database
        boolean isDeleted = db.deleteEventData(userid, eventId);

        if (isDeleted) {
            // Remove from Firebase
            DatabaseReference userEventsRef = rootNode.child("events").child(String.valueOf(userid)).child(String.valueOf(eventId));
            userEventsRef.removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ActivityEventDetails.this, "Event deleted successfully", Toast.LENGTH_SHORT).show();
                            // Finish the activity
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
