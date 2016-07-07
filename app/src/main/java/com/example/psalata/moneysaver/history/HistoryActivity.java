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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psalata.moneysaver.R;
import com.example.psalata.moneysaver.database.DBHelper;
import com.example.psalata.moneysaver.outcomes.Outcome;
import com.example.psalata.moneysaver.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Outcome> outcomes;
    private DBHelper db;

    private RecyclerView recyclerView;

    private boolean startDatePickerEnabled;
    private boolean endDatePickerEnabled;

    private Calendar calendar = Calendar.getInstance();
    private int startYear = calendar.get(Calendar.YEAR);
    private int startMonth = calendar.get(Calendar.MONTH);
    private int startDay = calendar.get(Calendar.DAY_OF_MONTH);
    private int endYear = calendar.get(Calendar.YEAR);
    private int endMonth = calendar.get(Calendar.MONTH);
    private int endDay = calendar.get(Calendar.DAY_OF_MONTH);

    private Map<String, Boolean> categoriesFilter = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.history_list_of_outcomes);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(llm);
        TextView dateFilter = (TextView) findViewById(R.id.open_date_picker_button);
        dateFilter.setOnClickListener(this);

        TextView categoryFilterButton = (TextView) findViewById(R.id.open_category_picker_button);
        categoryFilterButton.setOnClickListener(this);

        db = DBHelper.getInstance(getApplicationContext());
        outcomes = db.getOutcomes(null, null, null);
        List<String> categories = db.getOutcomeCategories();

        for(String category : categories) {
            categoriesFilter.put(category, true);
        }

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

    private List<Outcome> getOutcomesFromDB() {
        int startMonthRealNumber = startMonth + 1, endMonthRealNumber = endMonth + 1;
        String startDate = null, endDate = null;
        List<String> enabledCategories = new ArrayList<>();

        if (startDatePickerEnabled) {
            startDate = Utils.formatDateToString(startYear, startMonthRealNumber, startDay);
        }

        if (endDatePickerEnabled) {
            endDate = Utils.formatDateToString(endYear, endMonthRealNumber, endDay);
        }

        for (Map.Entry<String, Boolean> entry : categoriesFilter.entrySet()) {
            if(entry.getValue()) { //if category is enabled
                enabledCategories.add(entry.getKey());
            }
        }

        return db.getOutcomes(startDate, endDate, enabledCategories);
    }

    private void refreshOutcomesRV() {
        outcomes = getOutcomesFromDB();

        RVAdapter adapter = new RVAdapter(outcomes);
        recyclerView.setAdapter(adapter);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_date_picker_button:
                showDatePicker();
                break;
            case R.id.open_category_picker_button:
                showCategoryPicker();
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

    private void showDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.custom_date_picker, null);

        final DatePicker startDatePicker = (DatePicker) customView.findViewById(R.id.start_date_picker);
        final DatePicker endDatePicker = (DatePicker) customView.findViewById(R.id.end_date_picker);

        startDatePicker.updateDate(startYear, startMonth, startDay);
        endDatePicker.updateDate(endYear, endMonth, endDay);

        CheckBox startDateCheckBox = (CheckBox) customView.findViewById(R.id.start_date_checkbox);
        CheckBox endDateCheckBox = (CheckBox) customView.findViewById(R.id.end_date_checkbox);
        startDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                startDatePicker.setEnabled(isChecked);
                startDatePickerEnabled = isChecked;
            }
        });
        endDateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                endDatePicker.setEnabled(isChecked);
                endDatePickerEnabled = isChecked;
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startYear = startDatePicker.getYear();
                startMonth = startDatePicker.getMonth();
                startDay = startDatePicker.getDayOfMonth();
                endYear = endDatePicker.getYear();
                endMonth = endDatePicker.getMonth();
                endDay = endDatePicker.getDayOfMonth();
                if(getDate(startYear, startMonth, startDay).after(getDate(endYear, endMonth, endDay))) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_date_range), Toast.LENGTH_SHORT).show();
                } else {
                    refreshOutcomesRV();
                    dialog.dismiss();
                }
            }
        });
    }

    private Date getDate(int year, int month, int day) {
        return new GregorianCalendar(year, month, day).getTime();
    }

    private void showCategoryPicker() {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.category_picker, null);

        final ListView categories = (ListView) customView.findViewById(R.id.history_filter_categories_list);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, R.layout.category_checkbox_item, db.getOutcomeCategories());
        categories.setAdapter(adapter);
        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = categories.getChildAt(position);
                CheckedTextView checkedTextView = (CheckedTextView) v.findViewById(R.id.category_checkedtextview);
                checkedTextView.setChecked( !checkedTextView.isChecked());
                categoriesFilter.put(checkedTextView.getText().toString(), checkedTextView.isChecked());
            }
        });

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(customView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    refreshOutcomesRV();
                    dialog.dismiss();
            }
        });
    }

}

