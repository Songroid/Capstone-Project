package com.songjin.expensetracker;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import com.songjin.expensetracker.event.OnBackPressedEvent;

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

    @Override
    public void onBackPressed() {
        // dismiss the bottom sheet first
        if (!findViewById(R.id.fab).isShown()) {
            EventBus.getDefault().post(new OnBackPressedEvent());
        } else {
            super.onBackPressed();
        }
    }
}
