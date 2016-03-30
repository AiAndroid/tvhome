package com.video.search.access;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyHelper {

    private static VolleyHelper mInstance;
    private RequestQueue mAPIRequestQueue;

    private Context mContext;

    public static synchronized VolleyHelper getInstance(Context aCt) {
        if (mInstance == null) {
            mInstance = new VolleyHelper(aCt.getApplicationContext());
        }
        return mInstance;
    }

    private VolleyHelper(Context context) {
        mContext = context;
        mAPIRequestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        return mAPIRequestQueue != null ? mAPIRequestQueue : Volley.newRequestQueue(mContext);
    }
}
