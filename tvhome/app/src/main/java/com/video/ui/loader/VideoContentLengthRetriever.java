package com.video.ui.loader;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 3/19/15.
 */
public class VideoContentLengthRetriever {
    private static final String TAG = "ContentLengthRetriever";

    private String mUrl;
    private long mTimeout;

    public VideoContentLengthRetriever(String url){
        mUrl = url;
    }

    public int get(long timeout){
        mTimeout = timeout;
        FutureTask<Integer> future = new FutureTask<Integer>(mRunner);
        new Thread(future).start();
        int size = 0;
        try {
            size = future.get(mTimeout, TimeUnit.MILLISECONDS);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    private Callable<Integer> mRunner = new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
            int retry = 3;
            for(int i = 0; i < retry; i++){
                try {
                    Log.d(TAG, "retry count " + i);
                    int size = doGet();
                    return size;
                } catch (Exception e) {
                }
            }
            return 0;
        }

        private int doGet() throws IOException {
            HttpURLConnection connection = null;
            int size = 0;
            try{
                URL url = new URL(mUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout((int)mTimeout);
                size = connection.getContentLength();
                String type = connection.getContentType();

                Log.d(TAG, "url: " + mUrl + ", size is " + size + " type:"+type);
                //for none-video, return the max len to let download
                if(!TextUtils.isEmpty(type) && !type.contains("video") && !type.equals("application/octet-stream")){
                    return Integer.MAX_VALUE;
                }
            }finally{
                if(connection != null){
                    connection.disconnect();
                }
            }
            return size;
        }
    };
}
