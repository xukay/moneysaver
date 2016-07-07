package com.example.psalata.moneysaver.history;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psalata.moneysaver.R;
import com.example.psalata.moneysaver.database.DBHelper;
import com.example.psalata.moneysaver.history.date.CustomDatePicker;
import com.example.psalata.moneysaver.history.date.DateRangesFilter;
import com.example.psalata.moneysaver.outcomes.Outcome;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {

    private List<Outcome> outcomes;
    private DBHelper db;

    private RecyclerView recyclerView;

    private DateRangesFilter dateRangesFilter = new DateRangesFilter();
    private CategoriesFilter categoriesFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        recyclerView = (RecyclerView) findViewById(R.id.history_list_of_outcomes);
        LinearLayoutManager llm = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(llm);

        TextView dateRangesFilterButton = (TextView) findViewById(R.id.open_date_picker_button);
        dateRangesFilterButton.setOnClickListener(this);
        TextView categoriesFilterButton = (TextView) findViewById(R.id.open_category_picker_button);
        categoriesFilterButton.setOnClickListener(this);

        db = DBHelper.getInstance(getApplicationContext());
        categoriesFilter = new CategoriesFilter(db.getOutcomeCategories());

        fillOutcomesRV();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillOutcomesRV() {
        outcomes = db.getOutcomes(dateRangesFilter, categoriesFilter);
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

    private void showDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.date_ranges_picker, null);

        final CustomDatePicker startDatePicker = (CustomDatePicker) customView.findViewById(R.id.start_date_picker);
        final CustomDatePicker endDatePicker = (CustomDatePicker) customView.findViewById(R.id.end_date_picker);
        startDatePicker.updateDate(dateRangesFilter.getStartDate());
        startDatePicker.setEnabled(dateRangesFilter.getStartDate().isEnabled());
        endDatePicker.updateDate(dateRangesFilter.getEndDate());
        endDatePicker.setEnabled(dateRangesFilter.getEndDate().isEnabled());

        final CheckBox startDateCheckBox = (CheckBox) customView.findViewById(R.id.start_date_checkbox);
        final CheckBox endDateCheckBox = (CheckBox) customView.findViewById(R.id.end_date_checkbox);
        setCheckboxListener(startDateCheckBox, startDatePicker);
        setCheckboxListener(endDateCheckBox, endDatePicker);

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
                dateRangesFilter.setDateRanges(startDatePicker.getDateRange(), endDatePicker.getDateRange());
                if(!dateRangesFilter.isStartDateBeforeOrEqualEndDate()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.invalid_date_range), Toast.LENGTH_SHORT).show();
                } else {
                    fillOutcomesRV();
                    dialog.dismiss();
                }
            }
        });
    }

    private void setCheckboxListener(CheckBox checkBox, final CustomDatePicker datePicker) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                datePicker.setEnabled(isChecked);
            }
        });
    }

    private void showCategoryPicker() {
        LayoutInflater inflater = (LayoutInflater) getLayoutInflater();
        View customView = inflater.inflate(R.layout.category_picker, null);

        final ListView categories = (ListView) customView.findViewById(R.id.history_filter_categories_list);
        CheckedTextViewAdapter adapter =
                new CheckedTextViewAdapter(this, categoriesFilter.getCategoriesMap());
        categories.setAdapter(adapter);
//        categories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                View v = categories.getChildAt(position);
//                CheckedTextView checkedTextView = (CheckedTextView) v.findViewById(R.id.category_checkedtextview);
//                String categoryName = checkedTextView.getText().toString();
//                if (checkedTextView.isChecked()) {
//                    checkedTextView.setChecked(false);
//                    checkedTextView.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
//                    categoriesFilter.put(categoryName, false);
//                } else {
//                    checkedTextView.setChecked(true);
//                    checkedTextView.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
//                    categoriesFilter.put(categoryName, true);
//                }
//            }
//        });

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
                    fillOutcomesRV();
                    dialog.dismiss();
            }
        });
    }

}

