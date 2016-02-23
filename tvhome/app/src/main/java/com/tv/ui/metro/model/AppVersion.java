package com.tv.ui.metro.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by liuhuadonbg on 2/14/15.
 */
public class AppVersion implements Serializable {
    private static final long serialVersionUID = 2L;

    public HashMap<String, String> data = new HashMap<String, String>();
    public int    version_code() {if(data == null) return 0; return getInt(data.get("version_code"), 0);}
    public String recent_change(){if(data == null) return ""; return  data.get("recent_change");}
    public String version_name() {if(data == null) return ""; return  data.get("version_name");}
    public String apk_url()      {if(data == null) return ""; return  data.get("apk_url");}
    public String apk_md5()      {if(data == null) return ""; return  data.get("apk_md5");}
    public String release_date() {if(data == null) return ""; return  data.get("release_date");}
    public String release_notes() {if(data == null) return ""; return  data.get("release_notes");}
    public String released_by()  {if(data == null || TextUtils.isEmpty(data.get("released_by"))) return "小米视频"; return  data.get("released_by");}

    public static int getInt(String str, int def){
        if(TextUtils.isEmpty(str)){
            return def;
        }

        int res = def;
        try{
            res = Integer.parseInt(str);
        }catch (Exception ne){}
        return res;
    }

    public static long getLong(String str, long def){
        if(TextUtils.isEmpty(str)){
            return def;
        }

        long res = def;
        try{
            res = Long.parseLong(str);
        }catch (Exception ne){}
        return res;
    }

    public static boolean getBoolean(String str, boolean def){
        if(TextUtils.isEmpty(str)){
            return def;
        }

        boolean res = def;
        try{
            res = Boolean.parseBoolean(str);
        }catch (Exception ne){}
        return res;
    }

    public static class VersionUpgradePolicy implements Serializable {
        private static final long serialVersionUID = 1L;

        public HashMap<String, MIUI> miui;

        public static class MIUI extends  HashMap<String, String> implements Serializable {
            private static final long serialVersionUID = 1L;

            public static final String alpha  = "alpha";
            public static final String dev    = "dev";
            public static final String stable = "stable";
            public String percent(){return get("percent");}
        }

        public String upgrade_direct_noimie;
        public String includes;//device
        public String excludes;
        public String test_accounts;

        public String token;

        public String min_miui;
        public String max_miui;

        public String min_android;
        public String max_android;

        public String include_miuis;
        public String exclude_miuis;

        public String to_version;
        public String gray;

        public HashMap<String, String>extra;
    }
}
