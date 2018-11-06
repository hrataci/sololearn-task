package task.sololearn.com.task.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import task.sololearn.com.task.R;
import task.sololearn.com.task.helpers.NetworkHelper;
import task.sololearn.com.task.models.NewsModel;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {
    private List<NewsModel> data;


    public NewsFeedAdapter(List<NewsModel> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public NewsFeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder((LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_news_feed, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsFeedAdapter.ViewHolder viewHolder, int position) {
        viewHolder.bindData(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, sectionName;
        NetworkImageView image;
        NewsModel model;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.lblNewsTitle);
            sectionName = itemView.findViewById(R.id.lblAuthor);
            image =  itemView.findViewById(R.id.imgNews);
        }

        public void bindData(int position) {
            model = data.get(position);
            title.setText(model.getWebTitle());
            sectionName.setText(model.getSectionName());
            image.setImageUrl(model.getFields().getThumbnail(),NetworkHelper.getInst().getImageLoader());
        }
    }
}
