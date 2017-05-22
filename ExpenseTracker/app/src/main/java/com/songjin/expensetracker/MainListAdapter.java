package com.songjin.expensetracker;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.songjin.expensetracker.data.ExpenseEntity;
import com.songjin.expensetracker.databinding.ListItemBinding;

import io.requery.Persistable;
import io.requery.android.QueryRecyclerAdapter;
import io.requery.query.Result;
import io.requery.reactivex.ReactiveEntityStore;

/* package */ class MainListAdapter extends QueryRecyclerAdapter<ExpenseEntity, BindingHolder<ListItemBinding>>
        implements View.OnClickListener {

    private ReactiveEntityStore<Persistable> data;

    private Context context;

    /* package */ MainListAdapter(Context context, ReactiveEntityStore<Persistable> data) {
        super(ExpenseEntity.$TYPE);
        this.context = context;
        this.data = data;
    }

    @Override
    public Result<ExpenseEntity> performQuery() {
        return data.select(ExpenseEntity.class).orderBy(ExpenseEntity.DATE.lower()).get();
    }

    @Override
    public BindingHolder<ListItemBinding> onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding binding = ListItemBinding.inflate(LayoutInflater.from(parent.getContext()));
        binding.getRoot().setTag(binding);
        binding.getRoot().setOnClickListener(this);
        return new BindingHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(ExpenseEntity item, BindingHolder<ListItemBinding> holder, int position) {
        holder.binding.setExpense(item);
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show();
    }
}
