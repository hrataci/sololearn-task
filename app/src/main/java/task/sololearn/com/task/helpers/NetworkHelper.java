package task.sololearn.com.task.helpers;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmResults;
import task.sololearn.com.task.TaskApplication;
import task.sololearn.com.task.models.NewsModel;
import task.sololearn.com.task.utils.Constants;

import static task.sololearn.com.task.utils.Constants.Connection.PAGE;
import static task.sololearn.com.task.utils.Constants.Connection.URL;
import static task.sololearn.com.task.utils.Constants.JsonData.CURRENT_PAGE;
import static task.sololearn.com.task.utils.Constants.JsonData.PAGE_SIZE;
import static task.sololearn.com.task.utils.Constants.JsonData.RESPONSE;
import static task.sololearn.com.task.utils.Constants.JsonData.RESULTS;
import static task.sololearn.com.task.utils.Constants.JsonData.STATUS;
import static task.sololearn.com.task.utils.Constants.JsonData.STATUS_OK;

public class NetworkHelper {
    private static NetworkHelper helper;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private DateFormat df = new SimpleDateFormat(Constants.Date.PATTERN, Locale.US);
    private int page = 1;

    private NetworkHelper() {
        requestQueue = getRequestQueue();
        RealmResults<NewsModel> realmResults = Realm.getDefaultInstance().where(NewsModel.class).findAll();
        if (realmResults.size() > 0)
            page = realmResults.size() / PAGE_SIZE;
        imageLoader = new ImageLoader(requestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap>
                            cache = new LruCache<String, Bitmap>(200);

                    @Override
                    public Bitmap getBitmap(String url) {
                        return cache.get(url);
                    }

                    @Override
                    public void putBitmap(String url, Bitmap bitmap) {
                        cache.put(url, bitmap);
                    }
                });
    }

    public static synchronized void init() {
        helper = new NetworkHelper();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(TaskApplication.getContext());
        }
        return requestQueue;
    }

    public static NetworkHelper getInst() {
        if (helper == null) {
            throw new RuntimeException("NetworkHelper is not initialized");
        }
        return helper;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void doRequest(final boolean usePagination) {
        String url = null;
        if (usePagination) {
            url = URL + PAGE + page;
        } else {
            url = URL;
        }
        addToRequestQueue(new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        final List<NewsModel> modelList = new ArrayList<>();
                        try {
                            JSONObject responseObject = response.getJSONObject(RESPONSE);
                            if (isOk(responseObject.getString(STATUS))) {
                                if (usePagination)
                                    page = responseObject.getInt(CURRENT_PAGE) + 1;
                                JSONArray results = responseObject.getJSONArray(RESULTS);
                                Gson gson = new Gson();
                                for (int i = 0; i < results.length(); i++) {
                                    NewsModel model = gson.fromJson(results.getString(i), NewsModel.class);
                                    try {
                                        model.setPublishMillis(df.parse(model.getWebPublicationDate()).getTime());
                                        model.setRealmId();
                                        modelList.add(model);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.insertOrUpdate(modelList);
                            }
                        });
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.getMessage());
                    }
                }
        ));
    }


    private boolean isOk(String status) {
        return STATUS_OK.equals(status);
    }


}
