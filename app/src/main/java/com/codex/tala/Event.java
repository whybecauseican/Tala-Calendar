package com.codex.tala;

import android.database.Cursor;
import android.util.Log;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event {
    public static ArrayList<Event> eventsList = new ArrayList<>();
    public static ArrayList<Event> eventsForDate(DBHelper db, int userId, LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();
        Cursor cursor = db.getEventDataForDate(userId, date);
        if (cursor != null && cursor.moveToFirst()) {
            // TODO: add the other event details in here
            do {
                int eventID = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_TITLE));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_DATE));
                String startTime = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_TIME));
                String endTime = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_TIME));
                events.add(new Event(eventID, userId, name, startDate, endDate, startTime, endTime));
            } while (cursor.moveToNext());

            cursor.close();
        }

        return events;
    }

    public static ArrayList<Event> eventsForDateAndTime(DBHelper db, int userId, LocalDate date, LocalTime time) {
        ArrayList<Event> events = new ArrayList<>();

        Cursor cursor = db.getEventDataForDate(userId, date);
        int cellHour = time.getHour();
        if (cursor != null && cursor.moveToFirst()) {
            // TODO: add the other event details in here
            do {
                int eventID = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_EVENT_TITLE));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_DATE));

                String startTime = CalendarUtils.convert12to24(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_START_TIME)));
                String[] st = startTime.split(":");
                startTime = st[0];

                String endTime = CalendarUtils.convert12to24(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_END_TIME)));
                String[] et = endTime.split(":");
                endTime = et[0];
                if (Integer.parseInt(startTime) == cellHour){
                    events.add(new Event(eventID, userId, name, startDate, endDate, startTime, endTime));
                }
            } while (cursor.moveToNext());

            cursor.close();
        }

        return events;
    }

    private final String name, startDate, endDate, startTime, endTime;
    private final int userId, eventID;

    public Event(int eventID, int userId, String name, String startDate, String endDate, String startTime, String endTime) {
        this.eventID = eventID;
        this.userId = userId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getEventID() {
        return eventID;
    }

    public int getUserId() {
        return userId;
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