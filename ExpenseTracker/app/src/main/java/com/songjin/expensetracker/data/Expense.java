package com.songjin.expensetracker.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Expense {
    public abstract String date();
    public abstract String name();
    public abstract String note();
    public abstract String price();

    public static Expense create(String date, String name, String note, String price) {
        return new AutoValue_Expense(date, name, note, price);
    }
}
