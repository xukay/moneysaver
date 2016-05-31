package com.example.psalata.moneysaver.outcomes;

import java.math.BigDecimal;

/**
 * Created by Paweł on 06.02.2016.
 */
public class Outcome {
    private BigDecimal amount;
    private String date;
    private String category;

    public Outcome(BigDecimal amount, String date, String category) {
        this.amount = amount;
        this.date = date;
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }
}
