package com.example.psalata.moneysaver.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * Created by Paweł on 31.05.2016.
 */
public class MySpinner extends Spinner {

    public MySpinner(Context context) {
        super(context);
    }

    public MySpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setSelection(int position, boolean animate) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position, animate);
        if(sameSelected) {
            if(getOnItemSelectedListener() != null) {
                getOnItemSelectedListener()
                        .onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }

    @Override
    public void setSelection(int position) {
        boolean sameSelected = position == getSelectedItemPosition();
        super.setSelection(position);
        if(sameSelected) {
            if(getOnItemSelectedListener() != null) {
                getOnItemSelectedListener()
                        .onItemSelected(this, getSelectedView(), position, getSelectedItemId());
            }
        }
    }
}
