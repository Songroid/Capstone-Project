package com.songjin.expensetracker;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import org.greenrobot.eventbus.EventBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_holder, MainFragment.newInstance());
        ft.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        EventBus.getDefault().post(event);
        return super.dispatchTouchEvent(event);
    }
}
