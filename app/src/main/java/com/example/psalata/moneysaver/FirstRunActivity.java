package com.example.psalata.moneysaver;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.psalata.moneysaver.database.DBHelper;

import java.math.BigDecimal;

public class FirstRunActivity extends AppCompatActivity{
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_run);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(getApplicationContext());
    }

//    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.beginning_amount_button:
                addBeginningAmount(v);
                break;
        }
    }

    private void addBeginningAmount(View view) {
        EditText beginningAmountEditText = (EditText) findViewById(R.id.beginning_amount_edit);
        String beginningAmountString = beginningAmountEditText.getText().toString();
        BigDecimal beginningAmount = new BigDecimal(beginningAmountString);
        db.addAmountRemaining(beginningAmount);

        finish();//stops the activity after saving beginning amount
    }
}
