package task.sololearn.com.task.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import task.sololearn.com.task.R;
import task.sololearn.com.task.activities.NewsDetailedActivity;
import task.sololearn.com.task.helpers.NetworkHelper;
import task.sololearn.com.task.models.NewsModel;
import task.sololearn.com.task.models.PinnedItem;
import task.sololearn.com.task.utils.Utils;

import static task.sololearn.com.task.utils.Constants.ViewType.NEWS;
import static task.sololearn.com.task.utils.Constants.ViewType.PINNED;

public class NewsFeedAdapter<T extends NewsFeedAdapter.ViewHolder> extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    private List<NewsModel> data;
    private Realm realm;
    private RealmResults<PinnedItem> pinnedItemRealmList;
    private List<PinnedItem> pinnedData = new ArrayList<>();
    private PinnedAdapter pinnedAdapter;

    public NewsFeedAdapter(List<NewsModel> data) {

        this.data = data;
        realm = Realm.getDefaultInstance();
        pinnedItemRealmList = realm.where(PinnedItem.class).findAll();
        pinnedItemRealmList.addChangeListener(new RealmChangeListener<RealmResults<PinnedItem>>() {
            @Override
            public void onChange(RealmResults<PinnedItem> pinnedItems) {
                addPinnedData(pinnedItems);
            }
        });
        addPinnedData(pinnedItemRealmList);
    }

    private void addPinnedData(RealmResults<PinnedItem> pinnedItems) {
        pinnedData.clear();
        for (int i = pinnedItems.size() - 1; i >= 0; i--) {
            PinnedItem itemReal = realm.copyFromRealm(pinnedItems.get(i));
            pinnedData.add(itemReal);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NewsFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case PINNED:
                holder = new ViewPinnedHolder(inflater.inflate(R.layout.item_recycler_pinned, parent, false));
                break;
            case NEWS:
                holder = new ViewNewsHolder(inflater.inflate(R.layout.item_recycler_news_feed, parent, false));
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (pinnedItemRealmList.size() > 0) {
            return position == 0 ? PINNED : NEWS;
        }
        return NEWS;

    }

    public abstract class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void bindData(int position);
    }

    public class ViewNewsHolder extends ViewHolder {
        TextView title, sectionName;
        NetworkImageView image;
        NewsModel model;
        ImageView favImg;


        public ViewNewsHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lblNewsTitle);
            sectionName = itemView.findViewById(R.id.lblAuthor);
            image = itemView.findViewById(R.id.imgNews);
            favImg = itemView.findViewById(R.id.actionFav);
        }

        @Override
        public void bindData(int position) {
            model = data.get(position);
            title.setText(model.getWebTitle());
            sectionName.setText(model.getSectionName());
            image.setImageUrl(model.getFields().getThumbnail(), NetworkHelper.getInst().getImageLoader());
            Utils.setPinnedIcon(realm, favImg, model);
            favImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utils.pinnItem(realm, favImg, model);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsDetailedActivity.openDetailed(v.getContext(), model.getRealmId());
                }
            });
        }


    }

    private class ViewPinnedHolder extends ViewHolder {
        RecyclerView recyclerView;

        public ViewPinnedHolder(View itemView) {
            super(itemView);
            recyclerView = (RecyclerView) itemView;
            pinnedAdapter = new PinnedAdapter(pinnedData);
            recyclerView.setAdapter(pinnedAdapter);
        }

        @Override
        void bindData(int position) {
            pinnedAdapter.notifyDataSetChanged();
        }
    }
}
