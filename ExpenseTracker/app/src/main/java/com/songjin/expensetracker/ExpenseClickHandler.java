package com.songjin.expensetracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;

import com.songjin.expensetracker.data.Expense;
import com.songjin.expensetracker.fragment.DetailFragment;

public class ExpenseClickHandler {

    public static void onClick(Context context, Expense expense) {
        if (expense != null) {
            AppCompatActivity activity = ((AppCompatActivity)context);
            FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();

            Fragment detailFragment = activity.getSupportFragmentManager()
                    .findFragmentByTag(DetailFragment.TAG);
            if (detailFragment == null) {
                detailFragment = DetailFragment.newInstance(expense);
            }
            Fade in = new Fade(Fade.IN);
            in.setDuration(1000);
            detailFragment.setEnterTransition(in);

            ft.replace(R.id.fragment_holder, detailFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
