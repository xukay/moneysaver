package com.example.psalata.moneysaver.outcomes;

/**
 * Created by Paweł on 06.02.2016.
 */
public class NormalOutcome extends AbstractOutcome {

    public NormalOutcome(double amount, String date, String category) {
        super(amount, date, category, false);
    }
}
