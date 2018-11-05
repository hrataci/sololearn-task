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

import task.sololearn.com.task.TaskApplication;
import task.sololearn.com.task.models.NewsModel;
import task.sololearn.com.task.utils.Constants;

import static task.sololearn.com.task.utils.Constants.JsonData.*;

public class NetworkHelper {
    private static NetworkHelper helper;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private DateFormat df = new SimpleDateFormat(Constants.Date.PATTERN, Locale.US);
    private NetworkHelper() {
        requestQueue = getRequestQueue();
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

    public ImageLoader getImageLoader(){
        return imageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


    public void doRequest(){
        addToRequestQueue(new JsonObjectRequest(Request.Method.GET, Constants.Connection.URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        List<NewsModel> modelList = new ArrayList<>();
                        try {
                            JSONObject responseObject = response.getJSONObject("response");
                            if(isOk(responseObject.getString(STATUS))){
                                JSONArray results = responseObject.getJSONArray(RESULTS);
                                Gson gson = new Gson();
                                for (int i = 0; i < results.length(); i++) {
                                    NewsModel model =  gson.fromJson(results.getString(i),NewsModel.class);
                                    model.setPublishMillis(df.parse(model.getWebPublicationDate()).getTime());
                                    modelList.add(model);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {



                        }
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


    public  boolean isOk(String status){
        return STATUS_OK.equals(status);
    }




}
