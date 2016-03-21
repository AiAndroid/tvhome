package com.video.ui.loader.video;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.duokan.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;

/**
 * Created by tv metro on 9/1/14.
 */
public class TabsGsonLoader extends BaseGsonLoader<GenericBlock<DisplayItem>> {
    public static int LOADER_ID = 0x401;
    @Override
    public void setCacheFileName() {
        cacheFileName = "tabs_content.cache";
    }

    @Override
    public void setLoaderURL(DisplayItem item) {
        //only for test
        //calledURL = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/mobile_port.json";
        //calledURL = "https://raw.githubusercontent.com/AiAndroid/tvhome/master/home.json";

//        String homeurl = "https://raw.githubusercontent.com/AiAndroid/tvhome/master/home1.json";
        String homeurl = "http://media.tv.mitvos.com/tv/lean/aio/home?ptf=207&codever=1&deviceid=deb49000000000000000000000000001&opaque=aedf5d2c3d4e03e7841c4faa7210ba74b86c666d";
        String baseURL = CommonUrl.BaseURL;
        if(item != null && item.settings != null && "1".equals(item.settings.get("from_push"))){
            baseURL += "push/";
        }

        //setRawURL(baseURL + "c/home");
        setRawURL(homeurl);

        String url = getRawURL();
        if(item != null && item.settings != null && "1".equals(item.settings.get("from_push"))){
            if(url.indexOf("?")<1)
                url += "?from_push=1";
            else
                url += "&from_push=1";
        }

        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }

    public TabsGsonLoader(Context context, DisplayItem item) {
        super(context, item, 1);
    }

    @Override
    protected void loadDataByGson() {
        RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
        GsonRequest<GenericBlock<DisplayItem>> gsonRequest = new GsonRequest<GenericBlock<DisplayItem>>(calledURL, new TypeToken<GenericBlock<DisplayItem>>(){}.getType(), null, listener, errorListener);
        gsonRequest.setRawURL(getRawURL());
        Log.d("loader", "RawURL " + getRawURL());
        gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName);
        gsonRequest.setShouldCache(false);
        gsonRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
        requestQueue.add(gsonRequest);
    }


}
