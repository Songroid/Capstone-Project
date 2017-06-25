package com.songjin.expensetracker;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.songjin.expensetracker.databinding.ExpenseItemBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/* package */ public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseViewHolder> implements View.OnClickListener {
    
    private List<Expense> data;

    private Context context;

    public ExpenseAdapter(Context context, List<Expense> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public ExpenseViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new ExpenseViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ExpenseViewHolder holder, int i) {
        final Expense expense = data.get(i);

        holder.binding.setExpense(expense);
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onClick(View v) {
        ExpenseItemBinding binding = (ExpenseItemBinding) v.getTag();
        if (binding != null) {
            EventBus.getDefault().post(new ExpenseClickEvent(binding.getExpense()));
        }
    }
}
