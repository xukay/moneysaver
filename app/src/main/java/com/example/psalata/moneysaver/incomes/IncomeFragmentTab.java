package com.example.psalata.moneysaver.incomes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.psalata.moneysaver.R;

public class IncomeFragmentTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.income_fragment_tab, container, false);
    }

}
