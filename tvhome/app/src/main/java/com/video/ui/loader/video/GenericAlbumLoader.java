package com.video.ui.loader.video;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.duokan.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.tv.ui.metro.model.VideoItem;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonBaseUrl;
import com.video.ui.loader.CommonUrl;

import java.net.URLEncoder;

/**
 * Created by liuhuadong on 9/16/14.
 */
public abstract class GenericAlbumLoader<T> extends BaseGsonLoader<GenericBlock<T>> {
    public static int VIDEO_ALBUM_LOADER_ID   = 0x901;
    public static int VIDEO_SUBJECT_SEARCH_LOADER_ID   = 0x902;
    public static int VIDEO_STREAM_LOADER_ID = 0x3001;
    public GenericAlbumLoader(Context con, DisplayItem item, int setPage){
        super(con, item, setPage);
    }

    public GenericAlbumLoader(Context con, DisplayItem item){
        super(con, item, 1);
    }

    public static GenericAlbumLoader<DisplayItem> generateTabsLoader(final Context con, DisplayItem item){
        GenericAlbumLoader<DisplayItem> loader = new GenericAlbumLoader<DisplayItem>(con, item){

            @Override
            public void setCacheFileName() {
                cacheFileName = "tabs_album_";
            }

            @Override
            public void setLoaderURL(DisplayItem _item) {
                mItem = _item;

                String url = "";
            if(_item.ns!=null&&_item.ns.equals("home")) {
                    String baseURL = "";//CommonUrl.BaseURL;
                    if(_item != null && _item.settings != null && "1".equals(_item.settings.get("from_push"))){
                        baseURL += "push/";
                    }

                    //url = baseURL + "c/home";
                    url = "http://media.tv.mitvos.com/tv/lean/aio/home?ptf=207&codever=1&deviceid=deb49000000000000000000000000001&opaque=aedf5d2c3d4e03e7841c4faa7210ba74b86c666d";

                    setRawURL(url);

                    url =  processParamsQuery(getContext(), url, _item);

                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }else if(_item.ns!=null&&_item.ns.equals("search")) {
                    if(_item.id.endsWith("search.choice")) {
                        url = CommonUrl.BaseURL + "c/search";
                        //url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/mobile_search_choice.json";
                    }else {
                        url = CommonBaseUrl.BaseURL + _item.target.url;
                        //url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_one_list.json";
                    }

                    setRawURL(url);
                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
                else {
                    String baseURL = CommonUrl.BaseURL;
                    if(_item != null && _item.settings != null && "1".equals(_item.settings.get("from_push"))){
                        baseURL += "push/";
                    }

                    url = baseURL + encode(_item.target.url);
                    setRawURL(url);

                    url =  processParamsQuery(getContext(), url, _item);

                    calledURL = new CommonUrl(getContext()).addCommonParams(url);
                }
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<DisplayItem>> gsonRequest = new GsonRequest<GenericBlock<DisplayItem>>(calledURL, new TypeToken<GenericBlock<DisplayItem>>(){}.getType(), null, listener, errorListener);
                gsonRequest.setRawURL(getRawURL());
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");

                gsonRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
                //if for search no cache
                if((mItem.ns!=null&&mItem.ns.equals("search")) || calledURL.contains("search?kw=") || calledURL.contains("bookmark")) {
                    gsonRequest.setShouldCache(false);
                }

                //gsonRequest.setShouldCache(true);
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }


    public static GenericAlbumLoader<VideoItem> generateVideoAlbumLoader(Context con, DisplayItem item, int setPage){
        GenericAlbumLoader<VideoItem> loader = new GenericAlbumLoader<VideoItem>(con, item, setPage){
            @Override
            public void setCacheFileName() {
                cacheFileName = "video_album_";
            }

            @Override
            protected void loadDataByGson() {
                RequestQueue requestQueue = VolleyHelper.getInstance(getContext().getApplicationContext()).getAPIRequestQueue();
                GsonRequest<GenericBlock<VideoItem>> gsonRequest = new GsonRequest<GenericBlock<VideoItem>>(calledURL, new TypeToken<GenericBlock<VideoItem>>(){}.getType(), null, listener, errorListener);

                gsonRequest.setRawURL(getRawURL());
                gsonRequest.setCacheNeed(getContext().getCacheDir() + "/" + cacheFileName + mItem.id + ".cache");
                gsonRequest.setShouldCache(true);
                gsonRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
                requestQueue.add(gsonRequest);
            }
        };
        return  loader;
    }

    @Override
    public void setLoaderURL(DisplayItem _item) {
        mItem = _item;

        String url = getURL(mItem.target.url, page, _item);
        setRawURL(url);

        url =  processParamsQuery(getContext(), url, mItem);

        calledURL = new CommonUrl(getContext()).addCommonParams(url);
    }

    private static String processParamsQuery(Context context, String url, DisplayItem _item){
        if(_item != null && _item.settings != null && "1".equals(_item.settings.get("from_push"))){
            if(url.indexOf("?")<1)
                url += "?from_push=1";
            else
                url += "&from_push=1";
        }


        if (_item != null && _item.settings != null && !TextUtils.isEmpty(_item.settings.get("ref"))){
            if(url.indexOf("?")<1)
                url += "?ref="+ URLEncoder.encode(_item.settings.get("ref"));
            else
                url += "&ref="+URLEncoder.encode(_item.settings.get("ref"));
        }

        return url;
    }

    private String getURL(String target_url, int page, DisplayItem item){

        String baseURL = CommonUrl.BaseURL;
        if(item != null && item.settings != null && "1".equals(item.settings.get("from_push"))){
            baseURL += "push/";
        }

        String url = baseURL + encode(target_url) + "?page=" + page;
        try {
            if (Uri.parse(target_url).getQueryParameterNames() != null && Uri.parse(target_url).getQueryParameterNames().size() > 0) {
                url = baseURL + encode(target_url) + "&page=" + (page);
            }
        }catch (Exception ne){}

        return url;
    }

    protected static String encode(String name){
       return name;
    }

    public boolean hasMoreData() {
        //TODO
        if(mResult != null && mResult.blocks != null && mResult.blocks.size() > 0 //tab
                && mResult.blocks.get(0).blocks != null && mResult.blocks.get(0).blocks.size() > 0 && //blocks
                mResult.blocks.get(0).blocks.get(0).items != null && mResult.blocks.get(0).blocks.get(0).items.size() >= 5/*page_size*/){
            return  true;
        }
        return false;
    }


    public void nextPage() {
        nextPage(++page);
    }

    public void nextPage(int specificPage) {
        page = specificPage;
        //load from server
        mIsLoading = true;
        String url = getURL(mItem.target.url, page, mItem);

        setRawURL(url);
        //TODO, just return test data list
        //String url = "https://raw.githubusercontent.com/AiAndroid/mobilevideo/master/channel_one_list.json";
        Log.d("nextpage", "page="+(page));
        calledURL = new CommonUrl(getContext()).addCommonParams(url);
        loadDataByGson();
    }
}
