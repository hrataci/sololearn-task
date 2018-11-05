package task.sololearn.com.task.helpers;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import task.sololearn.com.task.TaskApplication;

public class NetworkHelper {
    private static NetworkHelper helper;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
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





}
