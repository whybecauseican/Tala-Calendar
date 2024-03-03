package com.codex.tala;

import static com.codex.tala.CalendarUtils.daysInWeekArray;
import static com.codex.tala.CalendarUtils.monthYearFromDate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class WeekFragment extends Fragment implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        initWidgets(view);
        setBtn(view);
        CalendarUtils.selectedDate = (LocalDate) LocalDate.now();
        setWeekView();
        return view;
    }

    private void initWidgets(View view)
    {
        calendarRecyclerView = (RecyclerView) view.findViewById(R.id.calendarRecyclerView);
        monthYearText = (TextView) view.findViewById(R.id.tv_monthYear);
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(CalendarUtils.selectedDate, days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarRecyclerView.setLayoutManager(layoutManager);
//        setEventAdapter();
    }

    public void setBtn(View view) {
        Button nextBTN = view.findViewById(R.id.nextWeekAction);
        Button prevBTN = view.findViewById(R.id.prevWeekAction);

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousWeekAction();
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextWeekAction();
            }
        });

    }

    public void previousWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }
    @Override
    public void onItemclick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        setEventAdapter();
//    }
//
//    private void setEventAdapter() {
//        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
//        EventAdapter eventAdapter = new EventAdapter(getContext().getApplicationContext(), dailyEvents);
//        eventListView.setAdapter(eventAdapter);
//    }
}