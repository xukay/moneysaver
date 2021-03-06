package com.example.psalata.moneysaver.outcomes;


import android.content.Context;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.psalata.moneysaver.R;
import com.example.psalata.moneysaver.database.DBHelper;
import com.example.psalata.moneysaver.utils.Utils;

import java.util.List;



public class OutcomeFragmentTab extends Fragment implements View.OnClickListener,
        AdapterView.OnItemSelectedListener{
    DBHelper db;
    EditText outcomeEditText;
    TextView amountRemainingTextView;
    Spinner categorySpinner;
    Utils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DBHelper(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.outcome_fragment_tab, container, false);

        outcomeEditText = (EditText) view.findViewById(R.id.outcome_edit);
        amountRemainingTextView = (TextView) view.findViewById(R.id.amount_remaining);
        categorySpinner = (Spinner) view.findViewById(R.id.spinner_outcome_category);
        Button regularOutcomeButton = (Button) view.findViewById(R.id.outcome_button);
        regularOutcomeButton.setOnClickListener(this);
        categorySpinner.setOnItemSelectedListener(this);
        categorySpinner.setAdapter(createSpinnerWithCategories());

        amountRemainingTextView.setText(db.getAmountRemainingAsString());
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.outcome_button:
                addNormalOutcome();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
        String item = adapterView.getItemAtPosition(position).toString();
        Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private ArrayAdapter<String> createSpinnerWithCategories() {
        List<String> categories = db.getOutcomeCategories();
        categories.add("DUPA");
        categories.add("GOWNO");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>
                (getActivity(), android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return dataAdapter;
    }

    private void addNormalOutcome() {
        View view = getActivity().getCurrentFocus();
        if(view != null) {
            InputMethodManager inputManager = (InputMethodManager)getActivity().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        String stringAmount = outcomeEditText.getText().toString();
        Double amount = Double.parseDouble(stringAmount);
        String date = utils.getCurrentDate();
        String category = categorySpinner.getSelectedItem().toString();
        Outcome outcome = new Outcome(amount, date, category);
        db.addOutcome(outcome);

        outcomeEditText.getText().clear();
        amountRemainingTextView.setText(db.getAmountRemainingAsString());
    }
}
