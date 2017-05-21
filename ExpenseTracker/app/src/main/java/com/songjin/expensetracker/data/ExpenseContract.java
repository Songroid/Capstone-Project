package com.songjin.expensetracker.data;


import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class ExpenseContract {

    public static final String CONTENT_AUTHORITY = "com.songjin.expensetracker";
    public static final String PATH_EXPENSE = "expense";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public static final class ExpenseEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_EXPENSE).build();

        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_URI  + "/" + PATH_EXPENSE;
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_URI + "/" + PATH_EXPENSE;

        public static final String TABLE_NAME = "expenseTable";
        public static final String COLUMN_NAME = "expenseName";
        public static final String COLUMN_DATE = "expenseDate";
        public static final String COLUMN_ADDRESS = "expenseAddress";
        public static final String COLUMN_NOTE = "expenseNote";
        public static final String COLUMN_PRICE = "expensePrice";

        public static Uri buildExpenseUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
