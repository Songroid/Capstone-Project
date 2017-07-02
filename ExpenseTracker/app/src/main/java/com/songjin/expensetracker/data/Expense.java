package com.songjin.expensetracker.data;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class Expense implements Parcelable {

    public static final String TAG = Expense.class.getSimpleName();

    public static final String DATE = "date";
    public static final String NAME = "name";
    public static final String PRICE = "price";
    public static final String LAT = "lat";
    public static final String LNG = "lng";

    public static final double USA_LAT = 37.0902;
    public static final double USA_LNG = 95.7129;

    public abstract String id();
    public abstract String date();
    public abstract String name();
    public abstract String price();
    public abstract double lat();
    public abstract double lng();

    public static Builder builder() {
        return new AutoValue_Expense.Builder().setId("").setDate("").setName("").setPrice("")
                .setLat(37.090).setLng(95.712);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setId(String value);
        public abstract Builder setDate(String value);
        public abstract Builder setName(String value);
        public abstract Builder setPrice(String value);
        public abstract Builder setLat(double value);
        public abstract Builder setLng(double value);
        public abstract Expense build();
    }

    public Object toFirebaseValue() {
        return new AutoValue_Expense.FirebaseValue(this);
    }
}
