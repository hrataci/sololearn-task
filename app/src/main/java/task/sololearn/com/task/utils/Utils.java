package task.sololearn.com.task.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;

import io.realm.Realm;
import task.sololearn.com.task.R;
import task.sololearn.com.task.models.NewsModel;
import task.sololearn.com.task.models.PinnedItem;

public class Utils {
    public  static  void pinnItem(Realm realm,final ImageView favImg, final NewsModel model) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                PinnedItem sPinned = realm.where(PinnedItem.class).equalTo("id", model.getRealmId()).findFirst();
                if (sPinned == null) {
                    final PinnedItem pinnedItem = new PinnedItem(model);
                    realm.insertOrUpdate(pinnedItem);
                    favImg.setImageResource(R.drawable.ic_fav_selected);
                } else {
                    sPinned.deleteFromRealm();
                    favImg.setImageResource(R.drawable.icon_fav);
                }

            }
        });
    }

    public static void setPinnedIcon(Realm realm,final ImageView favImg,NewsModel model) {
        PinnedItem sPinned = realm.where(PinnedItem.class).equalTo("id", model.getRealmId()).findFirst();
        if (sPinned != null) {
            favImg.setImageResource(R.drawable.ic_fav_selected);
        } else {
            favImg.setImageResource(R.drawable.icon_fav);
        }
    }
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
