package task.sololearn.com.task.activities;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;
import task.sololearn.com.task.R;
import task.sololearn.com.task.adapters.NewsFeedAdapter;
import task.sololearn.com.task.helpers.NetworkHelper;
import task.sololearn.com.task.models.NewsModel;
import task.sololearn.com.task.widgets.swipyrefreshlayout.SwipyRefreshLayout;
import task.sololearn.com.task.widgets.swipyrefreshlayout.SwipyRefreshLayoutDirection;

import static task.sololearn.com.task.utils.Constants.JsonData.PAGE_SIZE;

public class NewsFeedActivity extends BaseActivity {
    private RecyclerView recyclerView;

    private NewsFeedAdapter adapter;
    private List<NewsModel> data = new ArrayList<>();
    private RealmResults<NewsModel> realmResults;
    private SwipyRefreshLayoutDirection swipeDirection = SwipyRefreshLayoutDirection.TOP;
    private int lastAddedPosition = 0;
    private SwipyRefreshLayout swipy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        setUpRecyclerView();

        findViewById(R.id.btnUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToUp();
            }
        });
        swipy = findViewById(R.id.swipyRefreshLayout);
        swipy.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                swipeDirection = direction;
                swipy.setRefreshing(true);
                refreshFrom();
            }
        });
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.recyclerViewMain);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        realmResults = realm.where(NewsModel.class).sort("publishMillis", Sort.DESCENDING).findAll();
        realmResults.addChangeListener(new RealmChangeListener<RealmResults<NewsModel>>() {
            @Override
            public void onChange(RealmResults<NewsModel> newsModels) {
                swipy.setRefreshing(false);
                if (swipeDirection == SwipyRefreshLayoutDirection.TOP) {
                    addToDataBegin(newsModels);
                } else {
                    addToDataEnd(newsModels);
                }
            }
        });
        adapter = new NewsFeedAdapter(data);
        addToDataBegin(realmResults);
        recyclerView.setAdapter(adapter);
        NetworkHelper.getInst().doRequest(true);
    }

    private void addToDataBegin(RealmResults<NewsModel> realmResults) {
        int count = realmResults.size() < PAGE_SIZE ? realmResults.size() : PAGE_SIZE;
        for (int i = 0; i < count; i++) {
            NewsModel model = realm.copyFromRealm(realmResults.get(i));
            if (!data.contains(model)) {
                data.add(0, model);
            }
        }
        if (lastAddedPosition == 0) {
            lastAddedPosition = count;
        }
        adapter.notifyDataSetChanged();
    }


    private void addToDataEnd(RealmResults<NewsModel> realmResults) {
        int index = data.size();
        int count = realmResults.size() < PAGE_SIZE ? realmResults.size() : (realmResults.size() - lastAddedPosition);
        for (int i = realmResults.size()-1; i>=(realmResults.size() - lastAddedPosition); i--) {
            NewsModel model = realm.copyFromRealm(realmResults.get(i));
            if (!data.contains(model)) {
                data.add(index, model);
            }
        }
        lastAddedPosition += count;
        adapter.notifyDataSetChanged();

        recyclerView.smoothScrollBy(0,300);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        recyclerView.setAdapter(null);
        realmResults.removeAllChangeListeners();
    }

    private void refreshFrom() {
        boolean usePagination = swipeDirection == SwipyRefreshLayoutDirection.BOTTOM;
        NetworkHelper.getInst().doRequest(usePagination);
    }


    private void scrollToUp() {
        recyclerView.smoothScrollToPosition(0);
    }

    public static PendingIntent notificationClickIntent(Context context) {
        Intent notificationIntent = new Intent(context, NewsFeedActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        return intent;
    }
}
