package com.video.search.access;

import android.util.Log;

import com.video.search.Config;
import com.video.search.Constants;
import com.video.utils.Tools;

import java.net.MalformedURLException;
import java.net.URL;

public class CommonUrl {

    private static final String TAG = "CommonUrl";
    private CommonUrlBuilder mUrlBuilder;


    public CommonUrl(String url) {
        mUrlBuilder = new CommonUrlBuilder(Config.HOST + url);
    }

    public CommonUrl(String host, String url) {
        mUrlBuilder = new CommonUrlBuilder(host + url);
    }

    public void putParam(String key, String value) {
        mUrlBuilder.put(key, value);
    }

    public String build() {
        String res = build(Constants.API_TOKEN, Constants.API_SECRET_KEY);
        Log.d(TAG, "build " + res);
        return res;
    }

    public String build(String token, String key) {
        //TODO:use real dev info ptf=207&codever=1&deviceid=deb49000000000000000000000000001
        mUrlBuilder.put("ptf", "207");
        mUrlBuilder.put("codever", "15");
        mUrlBuilder.put("deviceid", "deb49000000000000000000000000001");

        String url = mUrlBuilder.toUrl();
        String path;
        try {
            path = new URL(url).getPath();
        } catch (MalformedURLException e) {
            return url;
        }

        int indexOfPath = url.indexOf(path);
        String strForSign = url.substring(indexOfPath);
        String sign = Tools.genSignature(strForSign, token, key);
        mUrlBuilder.put("opaque", sign);

        return mUrlBuilder.toUrl();
    }

}
