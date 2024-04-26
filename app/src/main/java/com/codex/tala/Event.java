package com.codex.tala;

import android.database.Cursor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>();
    public static ArrayList<Event> eventsForDate(DBHelper db, int userId, LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = db.getEventDataForDate(userId, date);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_TITLE));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_DATE));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_TIME));

                events.add(new Event(name, startDate, endDate, startTime, endTime));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return events;
    }

    private final String name, startDate, endDate, startTime, endTime;

    public Event(String name, String startDate, String endDate, String startTime, String endTime) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}