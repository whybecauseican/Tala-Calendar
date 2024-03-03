package com.codex.tala;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class EventAddActivity extends AppCompatActivity {
    private EditText eventNameET;
    private TextView dateStartTv, dateEndTv, timeStartTv, timeEndTv;
    private DatePickerDialog.OnDateSetListener mStartDateSetListener, mEndDateSetListener;
    private Calendar cal;
    private Button cancelBtn, addBtn;
    private DBHelper db;
    private int year, month, day, userId;


    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

        userId = getIntent().getIntExtra("userId", -1);

        cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH);
        day = cal.get(Calendar.DAY_OF_MONTH);

        dateStartTv = (TextView) findViewById(R.id.dateStartTv);
        dateEndTv = (TextView) findViewById(R.id.dateEndTv);
        timeStartTv = (TextView) findViewById(R.id.timeStartTv);
        timeEndTv = (TextView) findViewById(R.id.timeEndTv);

        cancelBtn = (Button) findViewById(R.id.cancel_btn);
        addBtn = (Button) findViewById(R.id.add_btn_event);

        settextDate();
        setTextStart();
        setTextEnd();
        startDate();
        endDate();
        startTime();
        endTime();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = eventNameET.getText().toString();
                String startDate = dateStartTv.getText().toString();
                String endDate = dateEndTv.getText().toString();
                String startTime = timeStartTv.getText().toString();
                String endTime = timeEndTv.getText().toString();

                Boolean insert = db.insertEventData(userId, eventName,startDate, endDate, startTime, endTime); //runs the inserteventdata function in the dbhelper class
                if (insert == true) {
                    finish();
                    overridePendingTransition(R.anim.to_bottom_anim,0);
                } else {
                    Toast.makeText(EventAddActivity.this, "Something went wrong. Please try again later", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.to_bottom_anim,0);
            }
        });
    }

    private void startDate(){
        dateStartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        EventAddActivity.this,
                        mStartDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mStartDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("EEEE, MMM d", Locale.getDefault());

                String formattedDate = format.format(calendar.getTime());

                dateStartTv.setText(formattedDate);
            }
        };
    }

    private void endDate(){
        dateEndTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(
                        EventAddActivity.this,
                        mEndDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mEndDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());

                String formattedDate = format.format(calendar.getTime());

                dateEndTv.setText(formattedDate);
            }
        };
    }

    private void startTime(){
        timeStartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int hourForDialog = currentHour + 1;
                TimePickerDialog dialog = new TimePickerDialog(EventAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hours, int minutes) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hours);
                        calendar.set(Calendar.MINUTE, minutes);

                        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        String formattedTime = format.format(calendar.getTime());

                        timeStartTv.setText(formattedTime);
                    }
                }, hourForDialog, 0, false);
                dialog.show();
            }
        });
    }

    private void endTime() {
        timeEndTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);
                int hourForDialog = currentHour + 2;
                TimePickerDialog dialog = new TimePickerDialog(EventAddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hours, int minutes) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, hours);
                        calendar.set(Calendar.MINUTE, minutes);

                        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.getDefault());
                        String formattedTime = format.format(calendar.getTime());

                        timeEndTv.setText(formattedTime);
                    }
                }, hourForDialog, 0, false);
                dialog.show();
            }
        });
    }

    private void setTextStart(){
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int startHour = currentHour + 1;

        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
        startCalendar.set(Calendar.MINUTE, 0); // Assuming minutes are always 0 for the start time

        String formattedStartTime = format.format(startCalendar.getTime());

        timeStartTv.setText(formattedStartTime);
    }

    private void setTextEnd() {
        int currentHour = cal.get(Calendar.HOUR_OF_DAY);
        int endHour = currentHour +2;

        SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.getDefault());
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        startCalendar.set(Calendar.MINUTE, 0); // Assuming minutes are always 0 for the start time

        String formattedStartTime = format.format(startCalendar.getTime());

        timeEndTv.setText(formattedStartTime);
    }

    private void settextDate() {
        LocalDate selectedDate = CalendarUtils.selectedDate;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault());
        String formattedDate = selectedDate.format(formatter);

        dateStartTv.setText(formattedDate);
        dateEndTv.setText(formattedDate);
    }

}
