package task.sololearn.com.task.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import io.realm.Realm;
import task.sololearn.com.task.R;
import task.sololearn.com.task.helpers.NetworkHelper;
import task.sololearn.com.task.models.NewsModel;
import task.sololearn.com.task.utils.Utils;

import static task.sololearn.com.task.utils.Constants.Intent.ID;

public class NewsDetailedActivity extends BaseActivity {
    private NewsModel model;
    TextView titleLine, sectionName, headLine,body,url;
    NetworkImageView image;
    ImageView favImg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detailed);
        enableHomeButton();
        init();
    }

    private void init() {
        int id = getIntent().getIntExtra(ID, -1);
        model = realm.where(NewsModel.class).equalTo("realmId",id).findFirst();
        if(model == null){
            finish();
            return;
        }
        titleLine =  findViewById(R.id.lblNewsTitle);
        sectionName =  findViewById(R.id.lblAuthor);
        image = findViewById(R.id.imgNews);
        favImg =  findViewById(R.id.actionFav);

        headLine = findViewById(R.id.lblHeadLine);
        body = findViewById(R.id.lblBody);
        url = findViewById(R.id.lblUrl);

        titleLine.setText(model.getWebTitle());
        sectionName.setText(model.getSectionName());
        image.setImageUrl(model.getFields().getThumbnail(), NetworkHelper.getInst().getImageLoader());
        Utils.setPinnedIcon(realm,favImg,model);
        favImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.pinnItem(realm,favImg,model);
            }
        });
        headLine.setText(model.getFields().getHeadline());
        body.setText(model.getFields().getBodyText());
        url.setText(model.getFields().getShortUrl());
        url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(model.getFields().getShortUrl()));
                startActivity(i);
            }
        });
    }

    public static void openDetailed(Context ctx, int newsRealmId) {
        Intent intent = new Intent(ctx, NewsDetailedActivity.class);
        intent.putExtra(ID, newsRealmId);
        ctx.startActivity(intent);
    }
}
