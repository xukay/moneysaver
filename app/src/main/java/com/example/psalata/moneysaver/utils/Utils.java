package com.example.psalata.moneysaver.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Paweł on 13.02.2016.
 */
public class Utils {
    public final static String displayDateFormat = "dd-MM-yyyy";
    public final static String databaseDateFormat = "yyyy-MM-dd";

    public static String getCurrentDate(){
        return new SimpleDateFormat(databaseDateFormat, Locale.getDefault()).format(new Date());
    }

    public static String formatDateToString(Integer year, Integer month, Integer day) {
        String sDay, sMonth, sYear;
        sYear = year.toString();
        if(day < 10) {
            sDay = String.format(Locale.getDefault(), "%02d", day);
        } else {
            sDay = day.toString();
        }
        if(month < 10) {
            sMonth = String.format(Locale.getDefault(), "%02d", month);
        } else {
            sMonth = month.toString();
        }
        return (sYear + "-" + sMonth + "-" + sDay);
    }

    public static String changeDateFormat(String fromFormat, String toFormat, String date) {
        SimpleDateFormat df_input = new SimpleDateFormat(fromFormat, Locale.getDefault());
        SimpleDateFormat df_output = new SimpleDateFormat(toFormat, Locale.getDefault());

        Date parsed;
        String finalDate = "";

        try {
            parsed = df_input.parse(date);
            finalDate = df_output.format(parsed);
        } catch (ParseException e) {
            Log.e("Utils", "Error parsing date from format "
                    + fromFormat + " to format " + toFormat);
        }

        return finalDate;
    }


}
