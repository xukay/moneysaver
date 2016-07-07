package com.example.psalata.moneysaver.history.date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.DatePicker;

import java.util.Date;

/**
 * Created by Paweł on 07.07.2016.
 */
public class CustomDatePicker extends DatePicker {

    public CustomDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void updateDate(SingleDateRange dateRange) {
        super.updateDate(dateRange.getYear(), dateRange.getMonth(), dateRange.getDay());
    }

    public SingleDateRange getDateRange() {
        return new SingleDateRange(getYear(), getMonth(), getDayOfMonth(), isEnabled());
    }
}


