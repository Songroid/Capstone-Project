package com.songjin.expensetracker;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.songjin.expensetracker.data.Expense;
import com.songjin.expensetracker.databinding.ExpenseItemBinding;

import java.util.List;

/* package */ public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {

    private List<Expense> data;

    public ExpenseAdapter(List<Expense> data) {
        this.data = data;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ExpenseItemBinding binding = ExpenseItemBinding.inflate(inflater, parent, false);
        return new ExpenseViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int i) {
        final Expense expense = data.get(i);
        holder.bind(expense);
        holder.getView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExpenseClickHandler.onClick(view.getContext(), expense);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
