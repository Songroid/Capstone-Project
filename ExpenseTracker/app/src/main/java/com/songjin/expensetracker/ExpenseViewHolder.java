package com.songjin.expensetracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.songjin.expensetracker.data.Expense;
import com.songjin.expensetracker.databinding.ExpenseItemBinding;


/* package */ class ExpenseViewHolder extends RecyclerView.ViewHolder {
    final ExpenseItemBinding binding;

    /* package */ ExpenseViewHolder(ExpenseItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    /* package */ void bind(Expense expense) {
        binding.setExpense(expense);
        binding.executePendingBindings();
    }

    View getView() {
        return binding.getRoot();
    }
}
