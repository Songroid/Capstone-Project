package widget;

import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.songjin.expensetracker.R;
import com.songjin.expensetracker.data.Expense;

import java.util.ArrayList;
import java.util.List;

public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = WidgetDataProvider.class.getSimpleName();

    private Context context;
    private DatabaseReference database;
    private List<Expense> data;

    public WidgetDataProvider(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {
        database = FirebaseDatabase.getInstance().getReference();
        data = new ArrayList<>();
    }

    @Override
    public void onDataSetChanged() {
        updateData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.expense_item);
        Expense expense = data.get(i);
        remoteViews.setTextViewText(R.id.item_name, expense.name());
        remoteViews.setTextViewText(R.id.item_date, expense.date());
        remoteViews.setTextViewText(R.id.item_price, expense.price());
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void updateData() {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for (DataSnapshot snapshot : dataSnapshot.child(Expense.TAG).getChildren()) {
                    Expense expense = Expense.builder().setId(snapshot.getKey())
                            .setDate((String)snapshot.child(Expense.DATE).getValue())
                            .setName((String)snapshot.child(Expense.NAME).getValue())
                            .setPrice((String)snapshot.child(Expense.PRICE).getValue())
                            .setLat((Double)snapshot.child(Expense.LAT).getValue())
                            .setLng((Double)snapshot.child(Expense.LNG).getValue())
                            .build();
                    data.add(expense);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });
    }
}
