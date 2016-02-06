package com.example.psalata.moneysaver.outcomes;

/**
 * Created by Paweł on 2016-02-04.
 */
public abstract class Outcome {
    private double amount;
    private String date;

    public Outcome(double amount, String date) {
        this.amount = amount;
        this.date = date;
    }


    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
