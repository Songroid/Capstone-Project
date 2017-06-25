package com.songjin.expensetracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.songjin.expensetracker.databinding.ExpenseItemBinding;


/* package */ class ExpenseViewHolder extends RecyclerView.ViewHolder {
    final ExpenseItemBinding binding;

    /* package */ ExpenseViewHolder(View rootView) {
        super(rootView);
        binding = ExpenseItemBinding.bind(rootView);
    }
}
