package com.example.psalata.moneysaver.history.date;

import com.example.psalata.moneysaver.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Paweł on 07.07.2016.
 */
public class SingleDateRange {
    private int year, month, day;
    private boolean enabled;

    public SingleDateRange(Calendar calendar, boolean enabled) {
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
        this.enabled = enabled;
    }

    public SingleDateRange(int year, int month, int day, boolean enabled) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.enabled = enabled;
    }

    public Date getGregorianCalendarDate() {
        return new GregorianCalendar(year, month, day).getTime();
    }

    void setDateAMonthBack() {
        if(month > 0) {
            month -= 1;
        } else {
            month = 11;
            year -= 1;
        }
    }

    public String toString() {
        return Utils.formatDateToString(year, month + 1, day);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
