package com.example.psalata.moneysaver.settings;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.psalata.moneysaver.R;

/**
 * Created by Paweł on 06.02.2016.
 */
public class SettingsFragmentTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment_tab, container, false);
    }
}
