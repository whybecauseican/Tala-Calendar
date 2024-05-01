package com.codex.tala;

import static com.codex.tala.CalendarUtils.selectedDate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HourAdapter extends ArrayAdapter<HourEvent> {
    public HourAdapter(@NonNull Context context, List<HourEvent> hourEvents)
    {
        super(context, 0, hourEvents);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        HourEvent event = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.hour_cell, parent, false);

        assert event != null;
        setHour(convertView, event.getTime());
        setEvents(convertView, event.getEvents());

        return convertView;
    }

    private void setHour(View convertView, LocalTime time) {
        TextView timeTV = convertView.findViewById(R.id.timeTV);
        timeTV.setText(convertTo12HourFormat(CalendarUtils.formattedShortTime(time)));
    }

    private void setEvents(View convertView, ArrayList<Event> events) {
        TextView event1= convertView.findViewById(R.id.event1);
        TextView event2= convertView.findViewById(R.id.event2);
        TextView event3= convertView.findViewById(R.id.event3);

        if(events.size() == 0){
            hideEvent(event1);
            hideEvent(event2);
            hideEvent(event3);
        } else if(events.size() == 1){
            setEvent(event1, events.get(0));
            hideEvent(event2);
            hideEvent(event3);
        } else if(events.size() == 2){
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            hideEvent(event3);
        } else if(events.size() == 3){
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            setEvent(event3, events.get(2));
        } else {
            setEvent(event1, events.get(0));
            setEvent(event2, events.get(1));
            event3.setVisibility(View.VISIBLE);
            String eventsNotShown = String.valueOf(events.size() - 2);
            eventsNotShown += " More Event/s";
            event3.setText(eventsNotShown);
        }
    }

    private void setEvent(TextView textView, Event event) {
        textView.setText(event.getName().isEmpty() ? "(No title)" : event.getName());
        textView.setVisibility(View.VISIBLE);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityEventDetails.class);
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
    }

    private void hideEvent(TextView tv) {
        tv.setVisibility(View.INVISIBLE);
    }

    public static String convertTo12HourFormat(String time24hr) {
        String[] parts = time24hr.split(":");
        int hour = Integer.parseInt(parts[0]);
        String period = (hour < 12) ? "AM" : "PM";

        // Convert hour to 12-hour format
        hour = (hour == 0 || hour == 12) ? 12 : hour % 12;

        return String.format(Locale.getDefault(), "%d %s", hour, period);
    }

}
