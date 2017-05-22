package com.songjin.expensetracker;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;


public class BindingHolder<B extends ViewDataBinding> extends RecyclerView.ViewHolder {

    protected final B binding;

    public BindingHolder(B binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
