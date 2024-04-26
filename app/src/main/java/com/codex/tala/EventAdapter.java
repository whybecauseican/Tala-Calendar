package com.codex.tala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(@NonNull Context context, List<Event> events)
    {
        super(context, 0, events);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        Event event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);

        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);
        TextView startTimeCellTV = convertView.findViewById(R.id.startTimeCellTV);
        TextView endTimeCellTV = convertView.findViewById(R.id.endTimeCellTV);

        String eventTitle = event.getName();
        String eventSTTitle = event.getStartTime();
        String eventETTitle;
        if (event.getEndDate().equals(String.valueOf(CalendarUtils.selectedDate))) {
            eventETTitle = event.getEndTime();
        }else {
            eventETTitle = "";
        }
        eventCellTV.setText(eventTitle);
        startTimeCellTV.setText(eventSTTitle);
        endTimeCellTV.setText(eventETTitle);
        return convertView;
    }
}
