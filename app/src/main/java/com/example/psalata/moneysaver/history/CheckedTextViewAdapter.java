package com.example.psalata.moneysaver.history;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;

import com.example.psalata.moneysaver.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Paweł on 07.07.2016.
 */
public class CheckedTextViewAdapter extends BaseAdapter implements View.OnClickListener {

    private Map<String,Boolean> categoriesFilter = new HashMap<>();

    private static LayoutInflater inflater = null;

    public CheckedTextViewAdapter(Context context, Map<String, Boolean> categoriesFilter) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categoriesFilter = categoriesFilter;
    }

    @Override
    public int getCount() {
        return categoriesFilter.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.category_checkbox_item, null);
            CheckedTextView checkedTextView = (CheckedTextView)view.findViewById(R.id.category_checkedtextview);

            checkedTextView.setChecked(categoriesFilter.get(checkedTextView.getText().toString()));
            checkedTextView.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onClick(View view) {
        CheckedTextView checkedTextView = (CheckedTextView)view.findViewById(R.id.category_checkedtextview);
        String categoryName = checkedTextView.getText().toString();
        if (checkedTextView.isChecked()) {
            checkedTextView.setChecked(false);
            checkedTextView.setCheckMarkDrawable(android.R.drawable.checkbox_off_background);
            categoriesFilter.put(categoryName, false);
        } else {
            checkedTextView.setChecked(true);
            checkedTextView.setCheckMarkDrawable(android.R.drawable.checkbox_on_background);
            categoriesFilter.put(categoryName, true);
        }
    }
}
