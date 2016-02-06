package com.example.psalata.moneysaver;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.psalata.moneysaver.incomes.IncomeFragmentTab;
import com.example.psalata.moneysaver.outcomes.OutcomeFragmentTab;

/**
 * Created by Paweł on 2016-02-04.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int numOfTabs;

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new OutcomeFragmentTab();
            case 1:
                return new IncomeFragmentTab();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
