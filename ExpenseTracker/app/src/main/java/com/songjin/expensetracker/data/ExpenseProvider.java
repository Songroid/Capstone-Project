package com.songjin.expensetracker.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


public class ExpenseProvider extends ContentProvider {

    private static final int EXPENSE = 100;
    private static final int EXPENSE_ID = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();
    private ExpenseDBHelper openHelper;

    public static UriMatcher buildUriMatcher() {
        String content = ExpenseContract.CONTENT_AUTHORITY;

        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(content, ExpenseContract.PATH_EXPENSE, EXPENSE);
        matcher.addURI(content, ExpenseContract.PATH_EXPENSE + "/#", EXPENSE_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        openHelper = new ExpenseDBHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        Cursor cursor;

        switch (uriMatcher.match(uri)) {
            case EXPENSE:
                cursor = db.query(ExpenseContract.ExpenseEntry.TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case EXPENSE_ID:
                long _id = ContentUris.parseId(uri);
                cursor = db.query(ExpenseContract.ExpenseEntry.TABLE_NAME, projection,
                        ExpenseContract.ExpenseEntry._ID + " = ?", new String[]{String.valueOf(_id)},
                        null, null, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case EXPENSE:
                return ExpenseContract.ExpenseEntry.CONTENT_TYPE;
            case EXPENSE_ID:
                return ExpenseContract.ExpenseEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        long _id;
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case EXPENSE:
                _id = db.insert(ExpenseContract.ExpenseEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = ExpenseContract.ExpenseEntry.buildExpenseUri(_id);
                } else {
                    throw new UnsupportedOperationException("Unable to insert rows into: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        int rows;

        switch (uriMatcher.match(uri)) {
            case EXPENSE:
                rows = db.delete(ExpenseContract.ExpenseEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (selection == null || rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = openHelper.getWritableDatabase();
        int rows;

        switch (uriMatcher.match(uri)) {
            case EXPENSE:
                rows = db.update(ExpenseContract.ExpenseEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rows;
    }
}
