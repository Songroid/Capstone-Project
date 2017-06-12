package com.songjin.expensetracker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.songjin.expensetracker.data.ExpenseEntity;
import com.songjin.expensetracker.databinding.ExpenseItemBinding;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveEntityStore;

/* package */ public class ExpenseAdapter extends QueryRecyclerAdapter<ExpenseEntity, BindingHolder<ExpenseItemBinding>>
        implements View.OnClickListener {

    private ReactiveEntityStore<Persistable> data;

    private Context context;

    /* package */
    public ExpenseAdapter(Context context) {
        super(ExpenseEntity.$TYPE);
        this.context = context;
        data = ((ExpenseApplication) context.getApplicationContext()).getData();
    }

    @Override
    public Result<ExpenseEntity> performQuery() {
        return data.select(ExpenseEntity.class).orderBy(ExpenseEntity.DATE.lower()).get();
    }

    @Override
    public BindingHolder<ExpenseItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ExpenseItemBinding binding = ExpenseItemBinding.inflate(inflater);
        binding.getRoot().setTag(binding);
        binding.getRoot().setOnClickListener(this);
        return new BindingHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(ExpenseEntity item, BindingHolder<ExpenseItemBinding> holder, int position) {
        holder.binding.setExpense(item);
    }

    @Override
    public void onClick(View v) {
        ExpenseItemBinding binding = (ExpenseItemBinding) v.getTag();
        if (binding != null) {

        }
    }
}
