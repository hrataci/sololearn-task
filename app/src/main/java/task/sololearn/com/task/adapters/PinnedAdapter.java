package task.sololearn.com.task.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import task.sololearn.com.task.R;
import task.sololearn.com.task.activities.NewsDetailedActivity;
import task.sololearn.com.task.helpers.NetworkHelper;
import task.sololearn.com.task.models.NewsModel;
import task.sololearn.com.task.models.PinnedItem;

public class PinnedAdapter extends RecyclerView.Adapter<PinnedAdapter.ViewHolder> {
    private List<PinnedItem> data;

    public PinnedAdapter(List<PinnedItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PinnedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_pinned, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PinnedAdapter.ViewHolder viewHolder, int position) {
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
            image = itemView.findViewById(R.id.imgNews);
        }

        public void bindData(int position) {
            model = data.get(position).getModel();
            title.setText(model.getWebTitle());
            sectionName.setText(model.getSectionName());
            image.setImageUrl(model.getFields().getThumbnail(), NetworkHelper.getInst().getImageLoader());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsDetailedActivity.openDetailed(v.getContext(), model.getRealmId());
                }
            });
        }
    }
}
