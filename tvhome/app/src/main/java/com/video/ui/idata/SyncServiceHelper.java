package com.video.ui.idata;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.duokan.VolleyHelper;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.Constants;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.loader.AppGson;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonBaseUrl;
import com.video.ui.loader.CommonUrl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by liuhuadonbg on 3/5/15.
 */
public class SyncServiceHelper {
    private static final String BookMarkTAG = "Bookmark";

    public interface Callback {
        public void onResult(boolean suc, Object ids);
    }

    public static void addAllBookMark(final Context context, final boolean remove, final Callback bcb){

        JSONObject map = iDataORM.getFavoritesIDS(context, "video");
        addBookMarks(context, remove, map, bcb);
    }

    public static void addBookMarks(final Context context, final boolean remove, final JSONObject data , final Callback bcb){
        if(data == null || data.names() ==null || data.names().length() == 0) {
            bcb.onResult(true, null);
            return;
        }

        AccountManager mAccountManager = AccountManager.get(context);
        final Account[] account = mAccountManager.getAccountsByType("com.xiaomi");
        if(account.length > 0) {

            Log.d(BookMarkTAG, "xiaomi account: " + account[0].toString());
            final String orcommentURL;
            if(remove){
                orcommentURL = CommonBaseUrl.BaseURL + "action/unbookmark";
            }else {
                orcommentURL = CommonBaseUrl.BaseURL + "action/bookmark";
            }
            final String callUrl;
            callUrl = new CommonUrl(context).addCommonParams(orcommentURL);

            Response.Listener<Boolean> listener = new Response.Listener<Boolean>() {
                @Override
                public void onResponse(Boolean response) {
                    JSONArray jsonArray = data.names();
                    if(jsonArray != null) {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (sb.length() > 0)
                                sb.append(",");

                            try {
                                sb.append(jsonArray.get(i).toString());
                            } catch (JSONException e) {
                            }
                        }

                        if (remove == false) {
                            iDataORM.FavorSynced(context, sb.toString());
                        } else {
                            iDataORM.removeFavorIDS(context, sb.toString());
                        }
                    }
                    Log.d(BookMarkTAG, "add bookmark: success remove:"+remove + " synced favor to server:"+data);
                    if(bcb != null){
                        bcb.onResult(true, data);
                    }
                }
            };

            Response.ErrorListener errorListener = new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(BookMarkTAG, "fail to bookmark:" + account[0] + " ids:"+data);
                    if(bcb != null){
                        bcb.onResult(false, data);
                    }
                }
            };


            RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
            BaseGsonLoader.PostRequest<Boolean> gsonRequest = null;
            try {
                gsonRequest = new BaseGsonLoader.PostRequest<Boolean>(callUrl, null, data.toString().getBytes("utf-8"), listener, errorListener);
                requestQueue.add(gsonRequest);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else {
            Log.d(BookMarkTAG, "not logined account, no need to sync favor to server");
            if(bcb != null)
                bcb.onResult(false,"not logined account, no need to sync favor to server" );
        }
    }

    public static void removeBookMark(final Context context, final JSONObject ids, final Callback bcb) {
        addBookMarks(context, true, ids, bcb);
    }

    private static final String CloudTAG = "CloudConfiguration";
    public static void syncSettings(final Context context, final Callback callback) {
        final String callUrl;
        String orgcallUrl = CommonBaseUrl.BaseURL + "sys/start";
        callUrl = new CommonUrl(context).addCommonParams(orgcallUrl);
        Response.Listener<CloudSettings> listener = new Response.Listener<CloudSettings>() {
            @Override
            public void onResponse(CloudSettings response) {

                if(response !=null && response.settings != null) {
                    iDataORM.addSetting(context, response.settings);
                }
                Log.d(CloudTAG, "cloud settings: "+response);
                if(callback != null){
                    callback.onResult(true, response);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(CloudTAG, "fail to sync cloud settings");
                if(callback != null){
                    callback.onResult(false, "");
                }
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<CloudSettings> gsonRequest = new BaseGsonLoader.GsonRequest<CloudSettings>(callUrl, new TypeToken<CloudSettings>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(context.getCacheDir() + "/cloud_settings.cache");
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);

    }

    public static class CloudSettings implements Serializable {
        private static final long serialVersionUID = 1L;
        public HashMap<String, String> settings;

        public String toString(){
            return settings.toString();
        }
    }

    public static void activateOperate(final Context context, final Callback callback) {
        final String callUrl;
        String orgcallUrl = CommonBaseUrl.BaseURL + "sys/activate";
        callUrl = new CommonUrl(context).addCommonParams(orgcallUrl);
        Response.Listener<CloudSettings> listener = new Response.Listener<CloudSettings>() {
            @Override
            public void onResponse(CloudSettings response) {

                if(response != null && response.settings != null) {
                    iDataORM.addSetting(context, response.settings);
                }
                if(Constants.DEBUG) {
                    Log.d(CloudTAG, "cloud activate: " + response);
                }
                if(callback != null){
                    callback.onResult(true, response);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(CloudTAG, "fail to cloud activate ");
                if(callback != null){
                    callback.onResult(false, "");
                }
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<CloudSettings> gsonRequest = new BaseGsonLoader.GsonRequest<CloudSettings>(callUrl, new TypeToken<CloudSettings>(){}.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(context.getCacheDir() + "/activate.cache");
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);

    }

    public static void appInitOperate(final Context context, final Callback callback) {
        if(iDataORM.getBooleanValue(context, iDataORM.sys_init, false))
            return;

        final String callUrl;
        String orgcallUrl = CommonBaseUrl.BaseURL + "sys/init";
        callUrl = new CommonUrl(context).addCommonParams(orgcallUrl);
        Response.Listener<JsonObject> listener = new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                iDataORM.addSetting(context, iDataORM.sys_init, "1");
                Log.d(CloudTAG, "sys/init: "+response);
                if(callback != null){
                    callback.onResult(true, response);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(CloudTAG, "fail to cloud activate ");
                if(callback != null){
                    callback.onResult(false, "");
                }
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<JsonObject> gsonRequest = new BaseGsonLoader.GsonRequest<JsonObject>(callUrl, new TypeToken<JsonObject>(){}.getType(), null, listener, errorListener);
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);

    }

    //to let adview load data fast
    public static volatile Block<DisplayItem> ads_object;
    private static final String ADSTAG = "ads";
    public static  void fetchAds(final Context context, final Callback callback){
        final String callUrl;
        String orgcallUrl = CommonBaseUrl.BaseURL + "c/ads.r";
        if(iDataORM.getBooleanValue(context, iDataORM.debug_mode, iDataORM.default_debug_mode)) {
            //"http://image.box.xiaomi.com/mfsv2/download/s010/p01FWl8FrDEX/8TlNb4UY2YAhIoB.js";
            orgcallUrl = CommonBaseUrl.BaseURL + "c/ads.r";
        }
        callUrl = new CommonUrl(context).addCommonParams(orgcallUrl);
        Response.Listener<Block<DisplayItem>> listener = new Response.Listener<Block<DisplayItem>>() {
            @Override
            public void onResponse(Block<DisplayItem> response) {
                if(Constants.DEBUG)
                    Log.d(ADSTAG, "suc to sync ads :"+response);

                if(callback != null){
                    callback.onResult(true, response);
                }

                ads_object = response;
                if(ads_object.times == null){
                    ads_object.times = new DisplayItem.Times();
                }
                ads_object.times.updated = System.currentTimeMillis();

                Log.d(ADSTAG, "reset ads_object:"+System.currentTimeMillis());

                //fetch ads image
                if(response != null && response.blocks != null && response.blocks.size() > 0) {
                    Block<DisplayItem> block = response.blocks.get(0);
                    if(block != null) {


                        if (block.blocks.size() > 0 && block.blocks.get(0).images != null && block.blocks.get(0).images.poster() != null) {
                            //we just load it into disk
                            final String adsurl = block.blocks.get(0).images.poster().url;

                            //v5 need load the ads
                            boolean avaliableData = true;
//                            try {
//                                ExtraNetwork.DataUsageDetail dataUsageDetail = ExtraNetwork.getUserDataUsageDetail(context);
//                                Log.d(ADSTAG, ""+dataUsageDetail);
//                            }catch (Exception ne){
//                            }

                            Picasso.with(context).load(adsurl).fetch();


                            //fetch all ads picture
                            for(int step = 1;step< block.blocks.size();step++) {
                                Block<DisplayItem> item = block.blocks.get(step);

                                if(item.images != null && item.images.poster() != null && !TextUtils.isEmpty(item.images.poster().url) ) {
                                    final String ads_url = item.images.poster().url;

                                    Picasso.with(context).load(ads_url).fetch();
                                }
                            }

                        }

                        if(block.settings != null ){
                            try {
                                if(block.settings.get("disable_uep") != null) {
                                    iDataORM.addSettingSync(context, "disable_uep", block.settings.get("disable_uep"));
                                }

                                iDataORM.addSetting(context, block.settings);
                            }catch (Exception ne){
                                ne.printStackTrace();
                            }
                        }
                    }
                }

                try {
                    iDataORM.addSetting(context, iDataORM.startup_ads, AppGson.get().toJson(response, new TypeToken<Block<DisplayItem>>() {
                    }.getType()));

                    if(Constants.DEBUG)
                        Log.d(ADSTAG, "ads settings: " + response);
                }catch (Exception e){
                    if(Constants.DEBUG)
                        Log.d(ADSTAG, "ads settings error: " + response);

                    e.printStackTrace();
                }
            }
        };


        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(ADSTAG, "fail to sync ads settings");
                if(callback != null){
                    callback.onResult(false, "");
                }
            }
        };

        try{
            RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
            BaseGsonLoader.GsonRequest<Block<DisplayItem>> gsonRequest = new BaseGsonLoader.GsonRequest<Block<DisplayItem>>(callUrl, new TypeToken<Block<DisplayItem>>(){}.getType(), null, listener, errorListener);
            gsonRequest.setCacheNeed(context.getCacheDir() + "/ads.cache");
            gsonRequest.setRawURL(orgcallUrl);
            gsonRequest.setShouldCache(true);
            gsonRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
            requestQueue.add(gsonRequest);
        }catch (Exception ne){
            ne.printStackTrace();
        }
    }

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 4000;
    /** Default number of retries for image requests */
    public static final int IMAGE_MAX_RETRIES = 0;
    /** Default backoff multiplier for image requests */
    public static final float IMAGE_BACKOFF_MULT = 2f;
}
