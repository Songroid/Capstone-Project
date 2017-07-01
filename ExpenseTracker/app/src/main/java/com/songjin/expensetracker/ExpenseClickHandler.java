package com.songjin.expensetracker;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;

import com.songjin.expensetracker.data.Expense;
import com.songjin.expensetracker.fragment.DetailFragment;
import com.songjin.expensetracker.fragment.ExpenseFragment;

public class ExpenseClickHandler {

    public static void onClick(Context context, Expense expense) {
        if (expense != null) {
            AppCompatActivity activity = ((AppCompatActivity)context);
            FragmentManager manager = activity.getSupportFragmentManager();
            FragmentTransaction ft = manager.beginTransaction();

            Fragment detailFragment = manager.findFragmentByTag(DetailFragment.TAG);
            if (detailFragment == null) {
                detailFragment = DetailFragment.newInstance(expense);
            }
            Fade in = new Fade(Fade.IN);
            Fade out = new Fade(Fade.OUT);
            in.setDuration(1000);
            detailFragment.setEnterTransition(in);

            Fragment expenseFragment = manager.findFragmentByTag(ExpenseFragment.TAG);
            if (expenseFragment != null) {
                expenseFragment.setExitTransition(out);
            }

            ft.replace(R.id.fragment_holder, detailFragment, DetailFragment.TAG);
            ft.addToBackStack(null);
            ft.commit();
        }
    }
}
