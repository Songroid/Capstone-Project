package com.songjin.expensetracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private BottomSheetBehavior behavior;
    private DatePickerDialog.OnDateSetListener dateListener;
    private Calendar calendar;

    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.addExpenseBottomSheet) FrameLayout bottomSheet;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.edittext_date) EditText editTextDate;

    @BindString(R.string.app_name) String appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // toolbar setup
        setSupportActionBar(toolbar);
        toolbar.setTitle(appName);

        // bottom layout setup
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    fab.show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        // fab setup
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    fab.hide();
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

        // date picker setup
        calendar = Calendar.getInstance();

        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // update label
                String myFormat = "MM/dd/yy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                editTextDate.setText(sdf.format(calendar.getTime()));
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottomSheet.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    onDismissClicked(getWindow().getDecorView().getRootView());
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void onOkClicked(View v) {
        onDismissClicked(v);
    }

    public void onDismissClicked(View v) {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public void onDateClicked(View v) {
        new DatePickerDialog(MainActivity.this, dateListener, calendar.get(Calendar.YEAR),
                             calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                            .show();
    }
}
