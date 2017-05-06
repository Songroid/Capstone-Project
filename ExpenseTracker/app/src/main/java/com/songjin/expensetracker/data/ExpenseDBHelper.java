package com.songjin.expensetracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class ExpenseDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "expense.db";

    public ExpenseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        addExpenseTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExpenseContract.ExpenseEntry.TABLE_NAME);
        onCreate(db);
    }

    private void addExpenseTable(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + ExpenseContract.ExpenseEntry.TABLE_NAME + " (" +
                        ExpenseContract.ExpenseEntry._ID + " INTEGER PRIMARY KEY, " +
                        ExpenseContract.ExpenseEntry.COLUMN_NAME + " TEXT UNIQUE NOT NULL);"
        );
    }
}
