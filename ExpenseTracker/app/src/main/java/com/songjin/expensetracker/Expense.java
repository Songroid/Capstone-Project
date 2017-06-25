package com.songjin.expensetracker;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Expense implements Parcelable {
    abstract String date();
    abstract String name();
    abstract String price();

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
}
