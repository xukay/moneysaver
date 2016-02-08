package com.example.psalata.moneysaver.outcomes;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.psalata.moneysaver.R;
import com.example.psalata.moneysaver.database.DBHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class OutcomeFragmentTab extends Fragment implements View.OnClickListener {
    DBHelper db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        db = new DBHelper(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.outcome_fragment_tab, container, false);
        Button regularOutcomeButton = (Button) view.findViewById(R.id.regular_outcome_button);
        regularOutcomeButton.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.regular_outcome_button:
                addNormalOutcome(v);
                break;
        }
    }

    private void addNormalOutcome(View view) {
        String stringAmount = view.findViewById(R.id.regular_outcome_edit).toString();
        Double amount = Double.parseDouble(stringAmount);
        String date = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        NormalOutcome outcome = new NormalOutcome(amount, date, "TEMPORARY CATEGORY");
        db.addOutcome(outcome);

        TextView amountRemaining = (TextView) view.findViewById(R.id.amount_remaining);
        String amountRemainingText = db.getAmountRemaining().toString();
        amountRemaining.setText(amountRemainingText); //update amount remaining
    }
}
