package com.example.psalata.moneysaver.outcomes;

/**
 * Created by Paweł on 06.02.2016.
 */
public class Outcome {
    private Double amount;
    private String date;
    private String category;

    Outcome(double amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}
