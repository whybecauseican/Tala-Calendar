package com.codex.tala;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter <CalendarViewHolder> {
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private final LocalDate savedDate;

    public CalendarAdapter(LocalDate savedDate, ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
        this.savedDate = savedDate;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.calendar_month_cell, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(days.size() > 15) //month view
            layoutParams.height = (int) (parent.getHeight() * 0.166666666);
        else
            layoutParams.height = (int) parent.getHeight();
        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        final LocalDate date = days.get(position);

//        if (date != null) {
//            LocalDate currentDate = LocalDate.of(savedDate.getYear(), savedDate.getMonth(), date.getDayOfMonth());
//            if (currentDate.equals(LocalDate.now())) {
//                holder.itemView.setBackgroundResource(R.drawable.calendar_today_bg);
//                holder.dayOfMonth.setTextColor(Color.WHITE);
//            } else if (!(currentDate.getMonth() == LocalDate.now().getMonth() &&
//                    currentDate.getYear() == LocalDate.now().getYear()) &&
//                    currentDate.withDayOfMonth(1).equals(currentDate)) {
//                holder.itemView.setBackgroundResource(R.drawable.calendar_selected_bg);
//                holder.dayOfMonth.setTextColor(Color.BLACK);
//            }
//        }

        if (date == null)
            holder.dayOfMonth.setText("");
        else {
            holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
            if (date.equals(CalendarUtils.selectedDate))
                holder.parentView.setBackgroundResource(R.drawable.calendar_selected_bg);
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemclick(int position, LocalDate date);

    }
}
