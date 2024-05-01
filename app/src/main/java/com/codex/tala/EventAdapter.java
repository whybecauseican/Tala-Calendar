package com.codex.tala;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView dash = convertView.findViewById(R.id.dash);

        String eventTitle = "(no title)";
        String eventSTTitle = "";
        String eventETTitle = "";

        if (event != null){
            String selectedDate = String.valueOf(CalendarUtils.selectedDate);
            String endDate = event.getEndDate();
            String startDate = event.getStartDate();

            if (!event.getName().isEmpty()){
                eventTitle = event.getName();
            }

            if (selectedDate.equals(startDate) && selectedDate.equals(endDate)) { // One-day event
                eventSTTitle = removeZeroMinutes(event.getStartTime());
                eventETTitle = removeZeroMinutes(event.getEndTime());
                if (eventSTTitle.endsWith(" AM") && eventETTitle.endsWith(" AM") ||
                        eventSTTitle.endsWith(" PM") && eventETTitle.endsWith(" PM")) {
                    eventSTTitle = eventSTTitle.substring(0, eventSTTitle.length() - 3);
                }
                dash.setVisibility(View.VISIBLE);
            } else if (selectedDate.equals(startDate)) { // Start day of multiple occurrence event
                eventSTTitle = removeZeroMinutes(event.getStartTime());
            } else if (!selectedDate.equals(endDate)) { // Multiple occurrence event
                eventSTTitle = "All-day";
            } else if (!selectedDate.equals(event.getStartDate())) { // Last day of event
                eventSTTitle = "Until";
                eventETTitle = removeZeroMinutes(event.getEndTime());
            }
        }

        eventCellTV.setText(eventTitle);
        startTimeCellTV.setText(eventSTTitle);
        endTimeCellTV.setText(eventETTitle);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityEventDetails.class);
                assert event != null;
                intent.putExtra("eventId", event.getEventID());
                intent.putExtra("userId", event.getUserId());
                if (getContext() instanceof Activity) {
                    Activity activity = (Activity) getContext();
                    activity.startActivity(intent);
                    activity.overridePendingTransition(R.anim.slide_up_anim,0);
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getContext().startActivity(intent);
                }
            }
        });

        return convertView;
    }

    private String removeZeroMinutes(String time) {
        if (time.endsWith(":00 AM")) {
            return time.replace(":00 AM", " AM"); // Remove ":00" and retain " AM"
        } else if (time.endsWith(":00 PM")) {
            return time.replace(":00 PM", " PM"); // Remove ":00" and retain " PM"
        }
        return time;
    }

}
