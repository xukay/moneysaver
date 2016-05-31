package com.example.psalata.moneysaver.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.psalata.moneysaver.R;
import com.example.psalata.moneysaver.outcomes.Outcome;

import java.util.List;

/**
 * Created by Paweł on 31.05.2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.OutcomeViewHolder> {

    List<Outcome> outcomes;

    public RVAdapter(List<Outcome> outcomes) {
        this.outcomes = outcomes;
    }

    public static class OutcomeViewHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView amount;
        TextView category;

        public OutcomeViewHolder(View itemView) {
            super(itemView);

            date = (TextView)itemView.findViewById(R.id.history_outcome_date);
            amount = (TextView) itemView.findViewById(R.id.history_outcome_amount);
            category = (TextView) itemView.findViewById(R.id.history_outcome_category);
        }
    }

    @Override
    public OutcomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.outcome_history_item, parent, false);
        OutcomeViewHolder ovh = new OutcomeViewHolder(v);
        return ovh;
    }

    @Override
    public void onBindViewHolder(OutcomeViewHolder holder, int position) {
        holder.date.setText(outcomes.get(position).getDate());
        holder.amount.setText(outcomes.get(position).getAmount().toString());
        holder.category.setText(outcomes.get(position).getCategory());
    }

    @Override
    public int getItemCount() {
        return outcomes.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
