package com.example.psalata.moneysaver.outcomes;

/**
 * Created by Paweł on 2016-02-04.
 */
public abstract class AbstractOutcome {
    private Double amount;
    private String date;
    private String category;
    private Boolean isMonthly;

    public AbstractOutcome(double amount, String date, String category, Boolean isMonthly) {
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.isMonthly = isMonthly;
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

    public Boolean getIsMonthly() {
        return isMonthly;
    }
}
