package com.video.ui.loader;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.Constants;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.VideoBlocks;
import com.tv.ui.metro.model.VideoItem;

import java.io.*;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by tv metro on 9/1/14.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public abstract  class BaseGsonLoader<T> extends Loader<T> {
    private final  static String TAG = "BaseGsonLoader";

    protected       int page      = 1;
    protected final int page_size = 20;
    protected          T       mResult;
    protected volatile boolean mIsLoading;
    private ProgressNotifiable mProgressNotifiable;
    private boolean            mHasDeliveredResult;

    protected String cacheFileName = "";
    public abstract void setCacheFileName();

    private static boolean mEnableCache = false;
    public static void enableCache(boolean _enable){
        mEnableCache = _enable;
    }
    
    protected String calledURL = "";
    public abstract void setLoaderURL(DisplayItem obj);

    protected String rawURL = "";
    public void setRawURL(String url){
        rawURL = url;
    }
    public String getRawURL(){
        return rawURL;
    }
    private static Context mStaticContext;
    public BaseGsonLoader(Context context) {
        super(context);
        mStaticContext = context.getApplicationContext();
        init(null);
    }

    protected DisplayItem mItem;
    private void init(DisplayItem item){
        mIsLoading = false;
        mHasDeliveredResult = false;
        mItem = item; 
        setCacheFileName();
        setLoaderURL(item);
    }

    public BaseGsonLoader(Context context, DisplayItem item, int setPage) {
        super(context);
        page = setPage;
        init(item);
    }

    public void setCurrentPage(int setPage){
        page = setPage;
    }

    public int getCurrentPage(){
        return page;
    }
    public void setProgressNotifiable(ProgressNotifiable progressNotifiable) {
        this.mProgressNotifiable = progressNotifiable;
        if (progressNotifiable != null) {
            progressNotifiable.init(dataExists(), mIsLoading);
        }
    }

    protected boolean dataExists() {
        // data exist and delivered to UI
        return mResult != null && mHasDeliveredResult;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    @Override
    protected void onStartLoading() {
        if(mResult != null){
            deliverResult(mResult);
        }

        if (!mIsLoading && (mResult == null || takeContentChanged())) {
            forceLoad();
        }
    }

    boolean show_loading_ui = true;
    public void forceLoad(boolean showUI){

        show_loading_ui = showUI;
        super.forceLoad();
    }

    @Override
    protected void onForceLoad() {

        //load from server
        mIsLoading = true;
        if (mProgressNotifiable != null && page == 1) {
            if(show_loading_ui == true) {
                mProgressNotifiable.startLoading(dataExists());
            }
        }
        loadDataByGson();
    }


    protected Response.Listener<T> listener = new Response.Listener<T>() {
        @Override
        public void onResponse(T response) {
            mResult = response;
            deliverResult(response);
            mHasDeliveredResult = true;
            mIsLoading = false;

            if (mProgressNotifiable != null) {
                mProgressNotifiable.stopLoading(dataExists(), false);
            }
        }
    };

    protected Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d(TAG, "onErrorResponse error:" + error.toString());
            //means load from next page
            if(page > 1){
                page--;
            }

            if(error.networkResponse != null && error.networkResponse.statusCode == Constants.MIVIDEO_SESSION_EXPIRED){
                //do login
                mProgressNotifiable.loginExpired();
            }

            mIsLoading = false;
            if (mProgressNotifiable != null) {
                mProgressNotifiable.stopLoading(dataExists(), false);
            }

            deliverResult(null);
        }
    };

    protected abstract void loadDataByGson();

    protected String mKeyword;
    public void setSearchKeyword(String key) {
        mKeyword = key;
    }
    public static class GsonRequest<TV> extends Request<TV> {
        private final Gson gson = AppGson.get();
        private final Type type;
        private final Map<String, String> headers;
        private final Response.Listener<TV> listener;
        private String cacheFile;

        @Override
        public String getCacheKey() {
            if(TextUtils.isEmpty(rawURL))
                return getUrl();

            return rawURL;
        }

        private String rawURL = "";
        public void setRawURL(String url){
            rawURL = url;
        }

        public void setCacheNeed(String _cacheFile){
            cacheFile = _cacheFile;
        }

        public GsonRequest(String url, Type type, Map<String, String> headers,
                           Response.Listener<TV> listener, Response.ErrorListener errorListener) {
            super(Request.Method.GET, url, errorListener);
            this.type = type;
            this.headers = headers;
            this.listener = listener;
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected void deliverResponse(TV response) {
            listener.onResponse(response);
        }

        Type tt = new TypeToken<VideoBlocks<VideoItem>>(){}.getType();
        @Override
        protected Response<TV> parseNetworkResponse(NetworkResponse response) {

            //for session expired
            if(response != null && response.statusCode == Constants.MIVIDEO_SESSION_EXPIRED){
                if(mStaticContext != null){
                    try {
                        Intent broadcast = new Intent(Constants.EXPIRED_BROADCAST);
                        mStaticContext.sendBroadcast(broadcast);
                    }catch (Exception ne){}
                }
            }

            if(response != null && response.statusCode == Constants.MIVIDEO_FORCE_UPGRADE){
                if(mStaticContext != null){
                    try {
                        Intent broadcast = new Intent(Constants.EXPIRED_UPGRADE_BROADCAST);
                        broadcast.putExtra("force", true);
                        broadcast.putExtra("show_whatsnew", false);
                        mStaticContext.sendBroadcast(broadcast);
                    }catch (Exception ne){}
                }
            }

            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                if(Constants.DEBUG)
                    Log.d(TAG, "response json:" + json);
                long timeStart = System.currentTimeMillis();
                TV fromJson = gson.fromJson(json, type);
                String episode = response.headers.get("X-viid");

                if(episode != null && type instanceof  ParameterizedType){
                    ParameterizedType pt = (ParameterizedType)type;
                    if(pt.getRawType().toString().contains("VideoBlocks"))
                        ((VideoBlocks<VideoItem>)fromJson).blocks.get(0).media.episode_index = episode;
                }
                long timeEnd = System.currentTimeMillis();
                Log.d(TAG, "fromJson take time in ms: " + (timeEnd - timeStart));
                Response<TV> res =  Response.success(fromJson, HttpHeaderParser.parseCacheHeaders(response));

                if(mEnableCache && cacheFile != null && cacheFile.length() > 0){
                    //save to files
                    updateToFile(cacheFile, json);
                }
                return  res;
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }

    public static class PostRequest<TV> extends Request<TV> {
        private final Map<String, String> headers;
        private final byte []             body;
        private final Response.Listener<TV> listener;
        private Type type;
        public PostRequest(String url, Map<String, String> headers, byte[] body,
                           Response.Listener<TV> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            this.headers = headers;
            this.listener = listener;
            this.body = body;
        }

        public PostRequest(String url, Type type, Map<String, String> headers, byte[] body,
                           Response.Listener<TV> listener, Response.ErrorListener errorListener) {
            super(Method.POST, url, errorListener);
            this.headers = headers;
            this.listener = listener;
            this.body = body;
            this.type = type;
        }

        @Override
        public byte[] getBody() throws AuthFailureError {
            return body != null ? body : super.getBody();
        }

        @Override
        public Map<String, String> getHeaders() throws AuthFailureError {
            return headers != null ? headers : super.getHeaders();
        }

        @Override
        protected void deliverResponse(TV response) {
            listener.onResponse(response);
        }

        @Override
        protected Response<TV> parseNetworkResponse(NetworkResponse response) {
            try {
                String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                if(Constants.DEBUG)
                    Log.d(TAG, "response json:" + json);
                TV fromJson = null;
                if (type != null){
                    fromJson = AppGson.get().fromJson(json, type);
                }
                Response<TV> res =  Response.success(fromJson, HttpHeaderParser.parseCacheHeaders(response));
                return  res;
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JsonSyntaxException e) {
                return Response.error(new ParseError(e));
            }
        }
    }



    public static void createDir(String filename){
        new File(filename).mkdirs();
    }

    public static void updateToFile(String fileName, String response){
        if(null == response || TextUtils.isEmpty(fileName)){
            return;
        }
        createDir(fileName);

        File f = new File(fileName);
        if(f.exists()){
            f.delete();
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);
            fos.write(response.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null){
                try {
                    fos.close();
                    fos = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static StringBuilder readCacheFromFile(String filePath){
        StringBuilder sb = new StringBuilder();
        File f = new File(filePath);
        if(f.exists() == false){
            return  sb;
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(new File(filePath));
            byte []buffer = new byte[4096*2];
            int len = -1;
            while((len = fin.read(buffer, 0, 4096*2)) > 0){
                sb.append(buffer);
            }
            buffer = null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fin != null){
                try {
                    fin.close();
                    fin = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb;
    }

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 4500;
    /** Default number of retries for image requests */
    public static final int IMAGE_MAX_RETRIES = 0;
    /** Default backoff multiplier for image requests */
    public static final float IMAGE_BACKOFF_MULT = 2f;
}
