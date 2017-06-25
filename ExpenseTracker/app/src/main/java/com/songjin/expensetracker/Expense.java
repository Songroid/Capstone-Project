package com.songjin.expensetracker;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import me.mattlogan.auto.value.firebase.annotation.FirebaseValue;

@AutoValue @FirebaseValue
public abstract class Expense implements Parcelable {

    static String TAG = Expense.class.getSimpleName();

    public abstract String date();
    public abstract String name();
    public abstract String price();

    static Builder builder() {
        return new AutoValue_Expense.Builder().setDate("").setName("").setPrice("");
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setDate(String value);
        abstract Builder setName(String value);
        abstract Builder setPrice(String value);
        abstract Expense build();
    }

    public Object toFirebaseValue() {
        return new AutoValue_Expense.FirebaseValue(this);
    }
}
