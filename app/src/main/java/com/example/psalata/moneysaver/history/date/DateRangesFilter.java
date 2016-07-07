package com.example.psalata.moneysaver.history.date;

import com.example.psalata.moneysaver.database.DBHelper;
import com.example.psalata.moneysaver.history.FilterOperations;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Paweł on 07.07.2016.
 */
public class DateRangesFilter implements FilterOperations {

    private SingleDateRange startDate, endDate;

    public DateRangesFilter() {
        Calendar calendar = Calendar.getInstance();
        startDate = new SingleDateRange(calendar, true);
        startDate.setDateAMonthBack();
        endDate = new SingleDateRange(calendar, true);
    }

    public boolean isStartDateBeforeOrEqualEndDate() {
        Date start = startDate.getGregorianCalendarDate(), end = endDate.getGregorianCalendarDate();
        return start.before(end) || start.equals(end);
    }

    public SingleDateRange getStartDate() {
        return startDate;
    }

    public SingleDateRange getEndDate() {
        return endDate;
    }

    public void setDateRanges(SingleDateRange startDate, SingleDateRange endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public boolean areAnyEnabled() {
        return startDate.isEnabled() || endDate.isEnabled();
    }

    @Override
    public String getDBQueryPart() {
        if (!startDate.isEnabled() && !endDate.isEnabled()) {
            return "";
        }
        if (!startDate.isEnabled()) {
            return " WHERE " + DBHelper.KEY_DATE + " <= '" + endDate.toString() + "'";
        }
        if (!endDate.isEnabled()) {
            return " WHERE " + DBHelper.KEY_DATE + "  >= '" + startDate.toString() + "'";
        }

        return " WHERE " + DBHelper.KEY_DATE + "  >= '" + startDate.toString() + "' AND "
                + DBHelper.KEY_DATE + " <= '" + endDate.toString() + "'";
    }


}
