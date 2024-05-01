package com.codex.tala;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.util.ArrayList;

public class CalendarAdapter extends RecyclerView.Adapter <CalendarViewHolder> {
    private final Context context;
    private final ArrayList<LocalDate> days;
    private final OnItemListener onItemListener;
    private final DBHelper db;
    private final int userId;

    public CalendarAdapter(Context context, int userId, ArrayList<LocalDate> days, OnItemListener onItemListener) {
        this.days = days;
        this.onItemListener = onItemListener;
        this.context = context;
        this.db = new DBHelper(context);
        this.userId = userId;
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
            layoutParams.height = parent.getHeight();
        return new CalendarViewHolder(view, onItemListener, days);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {//looping
        final LocalDate date = days.get(position);
        holder.dayOfMonth.setText(String.valueOf(date.getDayOfMonth()));
        boolean eventExists = db.checkCalendarEventExists(userId, date);

        if (date.equals(CalendarUtils.selectedDate)) {
            if (date.equals(LocalDate.now())) {
                holder.itemView.setBackgroundResource(R.drawable.calendar_today_bg);
                holder.dayOfMonth.setTextColor(Color.WHITE);
            } else {
                holder.parentView.setBackgroundResource(R.drawable.calendar_selected_bg);
            }
        } else if (date.equals(LocalDate.now())) {
            int colorMainDarkCyan = ContextCompat.getColor(context, R.color.color_main_darkcyan);
            holder.dayOfMonth.setTextColor(colorMainDarkCyan);
        }

        if (!date.getMonth().equals(CalendarUtils.selectedDate.getMonth())) {
            holder.dayOfMonth.setTextColor(Color.LTGRAY);
        }

        if (eventExists && date.getMonth().equals(CalendarUtils.selectedDate.getMonth()) &&
                !date.equals(CalendarUtils.selectedDate)) {
            holder.cellDotView.setVisibility(View.VISIBLE);
        } else {
            holder.cellDotView.setVisibility(View.GONE);
        }
        db.close();
    }


    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface OnItemListener {
        void onItemclick(int position, LocalDate date);

    }
}
