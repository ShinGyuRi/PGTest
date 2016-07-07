package com.volley.network;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by preparkha on 15. 5. 29..
 */
public class NetworkController {

    public static final String TAG = NetworkController.class.getSimpleName();

    private Context context;

    private static NetworkController mNetworkController;

    private static RequestQueue mRequestQueue;
    private static ImageLoader mImageLoader;
    
//    private NetworkController(Context context) {
//        this.context = context;
//    }
    public NetworkController() {
	}
    
    public static void init(Context context) {
    	mRequestQueue = Volley.newRequestQueue(context);
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // Use 1/8th of the available memory for this memory cache.
        int cacheSize = 1024 * 1024 * memClass / 8;
        mImageLoader = new ImageLoader(mRequestQueue, new NetworkLruBitmapCache(cacheSize));
    }

//    public static synchronized NetworkController getInstance(Context context) {
//        if (mNetworkController == null)
//            mNetworkController = new NetworkController(context);
//        return mNetworkController;
//    }

    public static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("RequestQueue not initialized");
        }
    }
    
//    public RequestQueue getRequestQueue() {
//        if (mRequestQueue == null)
//            mRequestQueue = Volley.newRequestQueue(this.context);
//        return mRequestQueue;
//    }
    
    public static ImageLoader getImageLoader() {
        if (mImageLoader != null) {
            return mImageLoader;
        } else {
            throw new IllegalStateException("ImageLoader not initialized");
        }
    }

//    public ImageLoader getImageLoader() {
//        getRequestQueue();
//        if (mImageLoader == null) {
//            mImageLoader = new ImageLoader(this.mRequestQueue, new NetworkLruBitmapCache());
//        }
//        return this.mImageLoader;
//    }

    public static <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public static<T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public static void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
