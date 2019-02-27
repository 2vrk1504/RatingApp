package com.projnibbas.ratingapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton ourInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    public static VolleySingleton getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new VolleySingleton(context);
        return ourInstance;
    }

    private VolleySingleton(Context context) {
        mCtx = context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
