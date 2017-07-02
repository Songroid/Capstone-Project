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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.songjin.expensetracker.R;
import com.songjin.expensetracker.data.Expense;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailFragment extends DialogFragment implements OnMapReadyCallback {

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
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        Drawable naviIcon = new IconDrawable(getContext(), Iconify.IconValue.zmdi_arrow_back)
                .colorRes(android.R.color.white)
                .actionBarSize();
        toolbar.setNavigationIcon(naviIcon);
        toolbar.setTitle(expense.name());
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng place = new LatLng(expense.lat(), expense.lng());
        MarkerOptions options = new MarkerOptions().position(place)
                .title(expense.name())
                .snippet(expense.price() + " on " + expense.date());
        googleMap.addMarker(options).showInfoWindow();
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 14.0f));
    }
}
