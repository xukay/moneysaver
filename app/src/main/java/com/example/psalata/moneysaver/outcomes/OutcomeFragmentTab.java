package com.example.psalata.moneysaver.outcomes;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psalata.moneysaver.R;
import com.example.psalata.moneysaver.database.DBHelper;
import com.example.psalata.moneysaver.history.HistoryActivity;
import com.example.psalata.moneysaver.utils.Utils;

import java.math.BigDecimal;
import java.util.List;



public class OutcomeFragmentTab extends Fragment implements View.OnClickListener{
    DBHelper db;
    EditText outcomeEditText;
    TextView amountRemainingTextView;
    Spinner categorySpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = DBHelper.getInstance(getActivity());
        View view = inflater.inflate(R.layout.outcome_fragment_tab, container, false);

        outcomeEditText = (EditText) view.findViewById(R.id.outcome_edit);
        amountRemainingTextView = (TextView) view.findViewById(R.id.amount_remaining);
        categorySpinner = (Spinner) view.findViewById(R.id.spinner_outcome_category);
        Button regularOutcomeButton = (Button) view.findViewById(R.id.outcome_button);
        regularOutcomeButton.setOnClickListener(this);
        Button historyButton = (Button) view.findViewById(R.id.history_button);
        historyButton.setOnClickListener(this);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = categorySpinner.getSelectedItem().toString();

                if(item.equals(getActivity().getString(R.string.add_new_category))) {
                    displayAddCategoryDialogAndSaveToDB();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        categorySpinner.setAdapter(createSpinnerWithCategories());
        amountRemainingTextView.setText(db.getAmountRemainingAsString());
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.outcome_button:
                addOutcome();
                break;
            case R.id.history_button:
                Intent intent = new Intent(getActivity(), HistoryActivity.class);
                getActivity().startActivity(intent);
                break;
        }
    }



    private void displayAddCategoryDialogAndSaveToDB() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle(this.getString(R.string.add_new_category));
        dialog.setMessage("");

        final EditText editText = new EditText(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        editText.setLayoutParams(lp);
        dialog.setView(editText);

        dialog.setPositiveButton(getResources().getString(R.string.button_add),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String category = editText.getText().toString();
                        if (!category.equals("")) {
                            Toast.makeText(getContext(),
                                    getResources().getString(R.string.new_cateogry_added),
                                    Toast.LENGTH_SHORT).show();
                            db.insertOutcomeCategory(category);
                            categorySpinner.setAdapter(createSpinnerWithCategories());
                            dialog.dismiss();
                        } else {
                            Toast.makeText(getContext(),
                                    getResources().getString(R.string.catggory_not_added),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        dialog.setNegativeButton(getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.show();
    }

    private SpinnerAdapter createSpinnerWithCategories() {
        List<String> categories = db.getOutcomeCategories();
        categories.add(this.getString(R.string.add_new_category));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }

    private void addOutcome() {
        View view = getActivity().getCurrentFocus();
        if(view != null) {
            InputMethodManager inputManager = (InputMethodManager)getActivity().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        String stringAmount = outcomeEditText.getText().toString();
        String category = categorySpinner.getSelectedItem().toString();
        if(category.equals(getActivity().getString(R.string.add_new_category))) {
            Toast.makeText(getContext(),
                    getActivity().getString(R.string.choose_category), Toast.LENGTH_SHORT).show();
        } else if(stringAmount.equals("") || (Double.parseDouble(stringAmount) < 0.01)) {
            Toast.makeText(getContext(),
                    getActivity().getString(R.string.invalid_amount), Toast.LENGTH_SHORT).show();
        } else {
            BigDecimal amount = new BigDecimal(stringAmount);
            String date = Utils.getCurrentDate();

            db.insertOutcome(new Outcome(amount, date, category));

            outcomeEditText.getText().clear();
            amountRemainingTextView.setText(db.getAmountRemainingAsString());
        }
    }
}
