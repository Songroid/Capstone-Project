package com.songjin.expensetracker.fragment;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.songjin.expensetracker.R;
import com.songjin.expensetracker.data.Expense;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends DialogFragment {

    public static final String TAG = DetailFragment.class.getSimpleName();
    private static final String KEY_EXPENSE = "keyExpense";

    private Expense expense;

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    public static DetailFragment newInstance(Expense expense) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_EXPENSE, expense);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        expense = getArguments().getParcelable(KEY_EXPENSE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);

        // toolbar setup
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        Drawable naviIcon = new IconDrawable(getContext(), Iconify.IconValue.zmdi_arrow_back)
                .colorRes(android.R.color.white)
                .actionBarSize();
        toolbar.setNavigationIcon(naviIcon);
        toolbar.setTitle(expense.getName());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        Toast.makeText(getContext(), expense.toString(), Toast.LENGTH_SHORT).show();

        return view;
    }

}
