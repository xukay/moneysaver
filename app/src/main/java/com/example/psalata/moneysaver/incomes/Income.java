package com.example.psalata.moneysaver.incomes;


import com.example.psalata.moneysaver.R;

/**
 * Created by Paweł on 2016-02-04.
 */
public class Income {
    private Double amount;
    private String date;

    public Income(Double amount, String date) {
        this.amount = amount;
        this.date = date;
    }

    public String getIncomeAsString() {
        StringBuilder incomeSB = new StringBuilder();
        incomeSB.append(date).append(" ").append(amount.toString()).append(R.string.zl_prefix);
        return incomeSB.toString();
    }

}
