package com.google.maps.android.utils.demo.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by Tejas Sherdiwala on 3/22/2017.
 * &copy; Knoxpo
 */

public class VolleyHelper {
    private static final int CACHE_SIZE = 16 * 1024 * 1024; // 16MiB

    private static VolleyHelper sVolleySingleton;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mAppContext;

    private VolleyHelper(Context appContext){

        mAppContext = appContext;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue,
                new ImageLoader.ImageCache() {
                    private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(CACHE_SIZE);

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

    public static synchronized VolleyHelper getInstance(Context context) {
        if (sVolleySingleton == null) {
            sVolleySingleton = new VolleyHelper(context.getApplicationContext());
        }
        return sVolleySingleton;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mAppContext);
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader(){
        return mImageLoader;
    }

    public void loadImage(String imageUrl, ImageLoader.ImageListener listener){
        if(imageUrl==null){
            return;
        }
        mImageLoader.get(imageUrl, listener);
    }
}
