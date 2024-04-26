package com.codex.tala;

import static com.codex.tala.CalendarUtils.daysInMonthArray;
import static com.codex.tala.CalendarUtils.monthYearFromDate;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MonthFragment extends Fragment implements CalendarAdapter.OnItemListener{

    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private DBHelper db;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_month, container, false);
        initWidgets(view);
        setBtn(view);
        CalendarUtils.selectedDate = (LocalDate) LocalDate.now();
        setMonthView();

        return view;
    }

    public MonthFragment(int userId){
        this.userId = userId;
    }

    private void initWidgets(View view)
    {
        calendarRecyclerView = (RecyclerView) view.findViewById(R.id.calendarRecyclerView);
        monthYearText = (TextView) view.findViewById(R.id.tv_monthYear);
        eventListView = (ListView) view.findViewById(R.id.eventListView);
    }

    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(getContext(), userId, daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 7);
        calendarRecyclerView.setAdapter(calendarAdapter);
        calendarRecyclerView.setLayoutManager(layoutManager);
        setEventAdapter();
    }

    public void setBtn(View view) {
        Button nextBTN = view.findViewById(R.id.nextMonthAction);
        Button prevBTN = view.findViewById(R.id.prevMonthAction);

        prevBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                previousMonthAction();
            }
        });

        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextMonthAction();
            }
        });

    }

    public void previousMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    public void nextMonthAction() {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    @Override
    public void onItemclick(int position, LocalDate date) {
        if (date != null){
            CalendarUtils.selectedDate = date;
            setMonthView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setEventAdapter();
    }

    private void setEventAdapter() {
        db = new DBHelper(getContext());
        ArrayList<Event> dailyEvents = Event.eventsForDate(db,userId,CalendarUtils.selectedDate); //all events that occured on the selectedDate
        EventAdapter eventAdapter = new EventAdapter(getContext().getApplicationContext(), dailyEvents); //function to populate the listview with the events
        eventListView.setAdapter(eventAdapter);
    }
}