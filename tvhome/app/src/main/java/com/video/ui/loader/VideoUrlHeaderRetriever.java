package com.video.ui.loader;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by lijianbo1 on 15-7-10.
 */
public class VideoUrlHeaderRetriever {
    private static final String TAG = "VideoUrlHeaderRetriever";
    private static final boolean DEBUG = false;

    private String mUrl;
    private long mTimeout;

    public VideoUrlHeaderRetriever(String url){
        mUrl = url;
    }

    public VideoUrlHeader get(long timeout){
        mTimeout = timeout;
        FutureTask<VideoUrlHeader> future = new FutureTask<VideoUrlHeader>(mRunner);
        new Thread(future).start();
        VideoUrlHeader r = null;
        try {
            r = future.get(mTimeout, TimeUnit.MILLISECONDS);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    private Callable<VideoUrlHeader> mRunner = new Callable<VideoUrlHeader>() {
        @Override
        public VideoUrlHeader call() throws Exception {
            int retry = 3;
            for(int i = 0; i < retry; i++){
                try {
                    if (DEBUG) Log.d(TAG, "retry count " + i);
                    VideoUrlHeader result = doGet();
                    return result;
                } catch (Exception e) {
                }
            }
            return null;
        }

        private VideoUrlHeader doGet() throws IOException {
            VideoUrlHeader result = new VideoUrlHeader();

            HttpGet httpGet = new HttpGet(mUrl);
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(httpGet);
//            Log.i(TAG, "line: " + response.getStatusLine());

            HttpEntity httpEntity = response.getEntity();
            result.ContentLength = httpEntity.getContentLength();
            result.ContentType = httpEntity.getContentType().getValue();

            if (DEBUG) Log.i(TAG, "size: " + result.ContentLength + " type: " + result.ContentType + " url:" + mUrl);

            return result;
        }
    };

    public static class VideoUrlHeader {
        public long ContentLength;
        public String ContentType;
    }
}
