package com.songjin.expensetracker.data;

import android.databinding.Bindable;
import android.databinding.Observable;
import android.os.Parcelable;

import io.requery.Entity;
import io.requery.Generated;
import io.requery.Index;
import io.requery.Key;
import io.requery.Persistable;

@Entity
public interface Expense extends Observable, Parcelable, Persistable {

    @Key @Generated
    int getId();

    @Bindable
    @Index(value = "date_index")
    String getDate();

    @Bindable
    String getName();

    @Bindable
    String getPrice();
}
