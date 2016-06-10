package com.example.psalata.moneysaver.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.psalata.moneysaver.R;
import com.example.psalata.moneysaver.database.DBHelper;
import com.example.psalata.moneysaver.outcomes.Outcome;
import com.example.psalata.moneysaver.utils.Utils;

import java.util.Calendar;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Outcome> outcomes;
    private DBHelper db;

    private TextView dateFilter;
    private RecyclerView recyclerView;

    private Calendar calendar = Calendar.getInstance();
    private int startYear = calendar.get(Calendar.YEAR);
    private int startMonth = calendar.get(Calendar.MONTH);
    private int startDay = calendar.get(Calendar.DAY_OF_MONTH);
    private int endYear = calendar.get(Calendar.YEAR);
    private int endMonth = calendar.get(Calendar.MONTH);
    private int endDay = calendar.get(Calendar.DAY_OF_MONTH);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.history_recycler_view);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(llm);
        dateFilter = (TextView) findViewById(R.id.history_date_filter);
        dateFilter.setOnClickListener(this);

        db = DBHelper.getInstance(getApplicationContext());
        downloadDataFromDB();

        RVAdapter adapter = new RVAdapter(outcomes);
        recyclerView.setAdapter(adapter);

        setStartDateAMonthBack();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadDataFromDB() {
        outcomes = db.getOutcomes();
    }



    private void downloadOutcomesWithDateRange(boolean startDatePickerEnabled, boolean endDatePickerEnabled) {
        String startDate, endDate;

        if(startDatePickerEnabled) {
            int startMonthRealNumber = startMonth + 1;
            startDate = Utils.formatDateToString(startYear, startMonthRealNumber, startDay);
        } else {

        }
        if(endDatePickerEnabled) {
            int endMonthRealNumber = endMonth + 1;
            endDate = Utils.formatDateToString(endYear, endMonthRealNumber, endDay);
        } else {

        }


        outcomes = db.getOutcomesWithDateRange(startDate, endDate);
    }

    private void refreshOutcomesRV(boolean startDatePickerEnabled, boolean endDatePickerEnabled) {
        downloadOutcomesWithDateRange(startDatePickerEnabled, endDatePickerEnabled);

        RVAdapter adapter = new RVAdapter(outcomes);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.history_date_filter:
                showDatePicker();
                break;
        }
    }


    private void setStartDateAMonthBack() {
        if(startMonth != 1) {
            startMonth -= 1;
        } else {
            startMonth = 12;
            startYear -= 1;
        }
    }

    private void checkDateRange() {

    }

    private void showDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_date_picker, null);

        final DatePicker startDatePicker = (DatePicker) customView.findViewById(R.id.start_date_picker);
        final DatePicker endDatePicker = (DatePicker) customView.findViewById(R.id.end_date_picker);

        startDatePicker.updateDate(startYear, startMonth, startDay);
        endDatePicker.updateDate(endYear, endMonth, endDay);

        CheckBox startDateCheckBox = (CheckBox) customView.findViewById(R.id.start_date_checkbox);
        final CheckBox endDateCheckBox = (CheckBox) customView.findViewById(R.id.end_date_checkbox);
        startDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startDatePicker.setEnabled(isChecked);
            }
        });
        endDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                endDatePicker.setEnabled(isChecked);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startYear = startDatePicker.getYear();
                startMonth = startDatePicker.getMonth();
                startDay = startDatePicker.getDayOfMonth();
                endYear = endDatePicker.getYear();
                endMonth = endDatePicker.getMonth();
                endDay = endDatePicker.getDayOfMonth();
                refreshOutcomesRV(startDatePicker.isEnabled(), endDatePicker.isEnabled());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }

}
