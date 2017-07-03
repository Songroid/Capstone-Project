package com.songjin.expensetracker;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class ExpenseWidget extends AppWidgetProvider {

    private static final int MAIN_LAUNCH_PI = 100;
    private static final int ADD_LAUNCH_PI = 200;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.expense_widget);

        // Pressing the header opens the main activity
        Intent launch = new Intent(context, MainActivity.class);
        views.setOnClickPendingIntent(R.id.widget_header, getPendingIntent(context, MAIN_LAUNCH_PI, launch, false));
        // Pressing the header opens the main activity with add fragment shown
        views.setOnClickPendingIntent(R.id.widget_plus, getPendingIntent(context, ADD_LAUNCH_PI, launch, true));

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static PendingIntent getPendingIntent(Context context, int id, Intent intent, boolean isAddShown) {
        intent.putExtra(MainActivity.IS_ADD_SHOWN_TAG, isAddShown);
        return PendingIntent.getActivity(context, id, intent, 0);
    }
}

