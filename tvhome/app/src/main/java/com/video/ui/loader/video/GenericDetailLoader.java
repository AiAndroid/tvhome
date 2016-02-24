package com.video.ui.loader.video;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.duokan.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.VideoBlocks;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonUrl;

import java.net.URLEncoder;

/**
 * Created by liuhuadong on 9/10/14.
 */
public abstract class GenericDetailLoader<T> extends BaseGsonLoader<VideoBlocks<T>> {
    public static int VIDEO_LOADER_ID = 0x702;
    public static int VIDEO_LOADER_PLAY_ID=0x703;

    public GenericDetailLoader(Context con, DisplayItem item){
        super(con, item, 1);
    }

    public static GenericDetailLoader<VideoItem> generateVideotLoader(Context con, DisplayItem item){
        GenericDetailLoader<VideoItem> loader = new GenericDetailLoader<VideoItem>(con, item){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_item_";
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<VideoBlocks<VideoItem>> gsonRequest = new GsonRequest<VideoBlocks<VideoItem>>(calledURL, new TypeToken<VideoBlocks<VideoItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }

    public static BaseGsonLoader<PlaySource> generateVideoPlayerSourceLoader(Context con, DisplayItem item){
        BaseGsonLoader<PlaySource> loader = new BaseGsonLoader<PlaySource>(con, item, 1){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_source_";
            }

            @Override
            public void setLoaderURL(DisplayItem _item) {
                String id = _item.media.items.get(0).id;

                String baseURL = CommonUrl.BaseURL;
                if(_item != null && _item.settings != null && "1".equals(_item.settings.get("from_push"))){
                    baseURL += "push/";
                }


                String url = baseURL + "play?id=" +id.substring(id.indexOf('/', 0) + 1) + "&cp="+_item.media.cps.get(0).cp();

                url =  processParamsQuery(getContext(), url, _item);

                calledURL = new CommonUrl(getContext()).addCommonParams(url);
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<PlaySource> gsonRequest = new GsonRequest<PlaySource>(calledURL, new TypeToken<PlaySource>(){}.getType(), null, listener, errorListener);
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }

    private static String processParamsQuery(Context context, String url, DisplayItem _item){
        if(_item != null && _item.settings != null && "1".equals(_item.settings.get("from_push"))){
            if(url.indexOf("?")<1)
                url += "?from_push=1";
            else
                url += "&from_push=1";
        }


        if(_item != null && _item.settings != null && !TextUtils.isEmpty(_item.settings.get("ref"))){
            if(url.indexOf("?")<1)
                url += "?ref="+ URLEncoder.encode(_item.settings.get("ref"));
            else
                url += "&ref="+URLEncoder.encode(_item.settings.get("ref"));
        }

        if(_item != null && _item.target != null && _item.target.params != null && !TextUtils.isEmpty(_item.target.params.android_component())){
            String component = _item.target.params.android_component();
            if(iDataORM.getBooleanValue(context, component, false)){
                if(url.indexOf("?")<1)
                    url += "?header_ads=1";
                else
                    url += "&header_ads=1";
            }
        }

        return url;
    }


    @Override
    public void setLoaderURL(DisplayItem _item) {

        String baseURL = CommonUrl.BaseURL;
        if(_item != null && _item.settings != null && "1".equals(_item.settings.get("from_push"))){
            baseURL += "push/";
        }

        String url = baseURL + _item.target.url;

        url =  processParamsQuery(getContext(), url, _item);

        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }

    public GenericDetailLoader(Context context) {
        super(context);
    }
}
