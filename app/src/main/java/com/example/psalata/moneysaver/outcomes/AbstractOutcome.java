package com.example.psalata.moneysaver.outcomes;

/**
 * Created by Paweł on 2016-02-04.
 */
public abstract class AbstractOutcome {
    private double amount;
    private String date;
    private String category;

    public AbstractOutcome(double amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }


    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

}
