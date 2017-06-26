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

    public abstract String date();
    public abstract String name();
    public abstract String price();

    public static Builder builder() {
        return new AutoValue_Expense.Builder().setDate("").setName("").setPrice("");
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setDate(String value);
        public abstract Builder setName(String value);
        public abstract Builder setPrice(String value);
        public abstract Expense build();
    }

    public Object toFirebaseValue() {
        return new AutoValue_Expense.FirebaseValue(this);
    }
}
