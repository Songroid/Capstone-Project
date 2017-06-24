package com.songjin.expensetracker.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.songjin.expensetracker.ExpenseAdapter;
import com.songjin.expensetracker.ExpenseApplication;
import com.songjin.expensetracker.R;
import com.songjin.expensetracker.data.Expense;
import com.songjin.expensetracker.data.ExpenseEntity;
import com.songjin.expensetracker.event.ExpenseClickEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.requery.Persistable;
import io.requery.reactivex.ReactiveEntityStore;


public class ExpenseFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener,
        PlaceSelectionListener {

    private static final String PLACE_FRAGMENT_TAG = "placeFragment";

    private Unbinder unbinder;

    private BottomSheetBehavior behavior;
    private DatePickerDialog.OnDateSetListener dateListener;
    private Calendar calendar;

    private ExpenseAdapter adapter;
    private ExecutorService executor;

    private InputMethodManager imm;

    private ReactiveEntityStore<Persistable> data;

    @BindView(R.id.main_toolbar) Toolbar toolbar;
    @BindView(R.id.addExpenseBottomSheet) FrameLayout bottomSheet;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.expenseListView) RecyclerView listView;

    @BindView(R.id.edittext_date) EditText editTextDate;
    @BindView(R.id.edittext_expense) EditText editTextExpense;

    @BindString(R.string.app_name) String appName;

    public static ExpenseFragment newInstance() {
        return new ExpenseFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        imm = (InputMethodManager) getActivity().getSystemService(
                Activity.INPUT_METHOD_SERVICE);

        data = ((ExpenseApplication) getActivity().getApplication()).getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        // toolbar setup
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
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

        // list setup
        executor = Executors.newSingleThreadExecutor();
        adapter = new ExpenseAdapter(getContext());
        adapter.setExecutor(executor);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(getContext()));

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

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager fm = getChildFragmentManager();
        SupportPlaceAutocompleteFragment placeFragment = getAutoCompleteFrag();

        if (placeFragment == null) {
            placeFragment = new SupportPlaceAutocompleteFragment();
            placeFragment.setOnPlaceSelectedListener(this);
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.placeFragmentHolder, placeFragment, PLACE_FRAGMENT_TAG);
            ft.commit();
            fm.executePendingTransactions();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onResume() {
        adapter.queryAsync();
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.share).setIcon(new IconDrawable(getContext(), Iconify.IconValue.zmdi_share)
                    .colorRes(android.R.color.white)
                    .actionBarSize());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        executor.shutdown();
        adapter.close();
        super.onDestroy();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.bottom_sheet_ok)
    public void onOkClicked() {
        boolean isAutoCompleteEmpty = true;
        EditText searchInput =((EditText)getAutoCompleteFrag().getView()
                .findViewById(R.id.place_autocomplete_search_input));
        if (searchInput != null) {
            isAutoCompleteEmpty = searchInput.getText().toString().isEmpty();
        }
        boolean isDateEmpty = editTextDate.getText().toString().isEmpty();
        boolean isExpenseEmpty = editTextExpense.getText().toString().isEmpty();

        if (isAutoCompleteEmpty || isDateEmpty || isExpenseEmpty) {
            // require no empty fields
            Snackbar.make(getView().findViewById(R.id.container), "Please fill in all the fields",
                    Snackbar.LENGTH_SHORT).show();
        } else {
            ExpenseEntity expense = new ExpenseEntity();
            expense.setDate(editTextDate.getText().toString());
            expense.setName(searchInput.getText().toString());
            expense.setPrice("$" + editTextExpense.getText().toString());

            // save the expense
            Single<ExpenseEntity> single = data.insert(expense);
            single.subscribeOn(Schedulers.single())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Expense>() {
                        @Override
                        public void accept(Expense expense) throws Exception {
                            onDismissClicked();
                            clearFields();
                        }
                    });
            adapter.queryAsync();
        }
    }

    @OnClick(R.id.bottom_sheet_dismiss)
    public void onDismissClicked() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        if (editTextExpense.isFocused()) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            editTextExpense.clearFocus();
        }
    }

    @OnClick(R.id.edittext_date)
    public void onDateClicked() {
        new DatePickerDialog(getContext(), dateListener, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onExpenseClicked(ExpenseClickEvent event) {
        Expense expense = event.getExpense();
        if (expense != null) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

            Fragment detailFragment = getActivity().getSupportFragmentManager()
                    .findFragmentByTag(DetailFragment.TAG);
            if (detailFragment == null) {
                detailFragment = DetailFragment.newInstance(expense);
            }
            Slide slideRight = new Slide(Gravity.RIGHT);
            detailFragment.setEnterTransition(slideRight);
            detailFragment.setExitTransition(slideRight);

            ft.replace(R.id.fragment_holder, detailFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMotionEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (behavior.getState()==BottomSheetBehavior.STATE_EXPANDED) {

                Rect outRect = new Rect();
                bottomSheet.getGlobalVisibleRect(outRect);

                if(!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    onDismissClicked();
                }
            }
        }
    }

    @Nullable
    private SupportPlaceAutocompleteFragment getAutoCompleteFrag() {
        return (SupportPlaceAutocompleteFragment) getChildFragmentManager().
                findFragmentByTag(PLACE_FRAGMENT_TAG);
    }

    private void clearFields() {
        if (getAutoCompleteFrag() != null) {
            getAutoCompleteFrag().setText("");
        }
        editTextDate.setText("");
        editTextExpense.setText("");
    }

    @Override
    public void onPlaceSelected(Place place) {
    }

    @Override
    public void onError(Status status) {

    }
}
