package com.codex.tala;

import java.time.LocalTime;
import java.util.ArrayList;

public class HourEvent {
    private LocalTime time;
    private ArrayList<Event> events;
    private int userId;

    public HourEvent(int userId, LocalTime time, ArrayList<Event> events){
        this.userId = userId;
        this.time = time;
        this.events = events;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }
}
