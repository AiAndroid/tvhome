package com.video.ads;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.tv.ui.metro.model.Constants;
import com.tv.ui.metro.model.DisplayItem;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.CommonBaseUrl;
import com.xiaomi.analytics.Actions;
import com.xiaomi.analytics.AdAction;
import com.xiaomi.analytics.Analytics;
import miui.os.BuildV5;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhuadonbg on 11/26/15.
 */
public class AdsReport {

    final static String TAG = AdsReport.class.getName();

    public static boolean isNeedPresentReport(DisplayItem item){
        return (!TextUtils.isEmpty(item.target.params.present_url()) || !TextUtils.isEmpty(item.target.params.miui_ads()));
    }

    public static void uploadTickAction(Context context, DisplayItem item){
        if(item == null || item.target == null)
            return;

        List<String> urls = new ArrayList<String>();

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.tick_url())){
            urls.add(item.target.params.tick_url());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.tick_url_1())){
            urls.add(item.target.params.tick_url_1());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.tick_url_2())){
            urls.add(item.target.params.tick_url_2());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.tick_url_3())){
            urls.add(item.target.params.tick_url_3());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.tick_url_4())){
            urls.add(item.target.params.tick_url_4());
        }

        realUploadAction(context, urls, item, "CLICK");
    }

    public static void realUploadAction(Context context, List<String> urls, DisplayItem item, String event){
        try {
            Analytics analytics = Analytics.getInstance(context);
            AdAction adActioin = Actions.newAdAction(event.toLowerCase());
            if (urls != null && urls.size() > 0) {
                adActioin.addAdMonitor(urls);
            }

            String payload = "";
            boolean havePost = false;
            try {
                if (item != null && item.target != null &&
                        item.target.params != null &&
                        !TextUtils.isEmpty(item.target.params.miui_ads())) {

                    payload = Uri.parse(item.target.params.miui_ads()).getQueryParameter("payload");
                    //String mimarket =  Uri.parse(item.target.params.miui_ads()).getQueryParameter("mimarket");
                    if (!TextUtils.isEmpty(payload)) {
                        adActioin.addParam("v", "sdk_1.0").addParam("e", event).addParam("t", System.currentTimeMillis());
                        adActioin.addParam("ex", payload);
                        havePost = true;
                    }
                }
            } catch (Exception ne) {
                Log.d(TAG, "no payload for " + event + ":" + ne.getMessage());
            }

            if (Constants.DEBUG && urls != null) {
                Log.d(TAG, event + ":" + urls.size() + " 0:" + (urls.size() > 0 ? urls.get(0) : ""));
            }

            //this is to do ad test.
            if (havePost || (urls != null && urls.size() > 0)) {
                if ("adlogstaging".equals(iDataORM._op_value) && havePost)
                    analytics.getTracker("video_adlogstaging").track(adActioin);
                else {
                    //new config key
                    if (havePost) {
                        analytics.getTracker("video_adlog").track(adActioin);
                    } else if (urls != null && urls.size() > 0) {
                        analytics.getTracker("video_adevent").track(adActioin);
                    }
                }
            }
        }catch (Exception ne){
            Log.e(TAG, ""+ne.getMessage());
            ne.printStackTrace();
        }
    }

    public static void uploadPresentAction(Context context, DisplayItem item){
        if(item == null || item.target == null)
            return;

        List<String> urls = new ArrayList<String>();

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.present_url())){
            urls.add(item.target.params.present_url());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.present_url_1())){
            urls.add(item.target.params.present_url_1());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.present_url_2())){
            urls.add(item.target.params.present_url_2());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.present_url_3())){
            urls.add(item.target.params.present_url_3());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.present_url_4())){
            urls.add(item.target.params.present_url_4());
        }

        realUploadAction(context, urls, item, "VIEW");
    }

    public static void uploadSecondAction(Context context, DisplayItem item){
        if(item == null || item.target == null)
            return;

        List<String> urls = new ArrayList<String>();

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.action_url())){
            urls.add(item.target.params.action_url());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.action_url_1())){
            urls.add(item.target.params.action_url_1());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.action_url_2())){
            urls.add(item.target.params.action_url_2());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.action_url_3())){
            urls.add(item.target.params.action_url_3());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.action_url_4())){
            urls.add(item.target.params.action_url_4());
        }

        boolean needUploadStardDownload = true;
        String miui = CommonBaseUrl.getMIUIVersion();
        if(!"V5".equals(miui) && !"V6".equals(miui) && !TextUtils.isEmpty(miui)) {
            try {
                String miuiVersion = Build.VERSION.INCREMENTAL;
                if (BuildV5.IS_ALPHA_BUILD || BuildV5.IS_DEVELOPMENT_VERSION ||
                        (miuiVersion != null && miuiVersion.startsWith("V7") && !miuiVersion.startsWith("V7.0.")) || (miuiVersion != null && miuiVersion.startsWith("V8"))) {
                    if (item != null && item.target != null && item.target.params != null && !TextUtils.isEmpty(item.target.params.miui_ads())) {
                        Uri uri = Uri.parse(item.target.params.miui_ads());
                        String mimarket = uri.getQueryParameter("mimarket");

                        if ("2".equals(mimarket)) {
                            needUploadStardDownload = false;
                        }
                    }
                }
            } catch (Exception ne) {
            }
        }

        if(needUploadStardDownload){
            realUploadAction(context, urls, item, "APP_START_DOWNLOAD");
        }
    }

    public static void uploadInstallAction(Context context, DisplayItem item){
        if(item == null || item.target == null)
            return;

        List<String> urls = new ArrayList<String>();

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.install_url())){
            urls.add(item.target.params.install_url());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.install_url_1())){
            urls.add(item.target.params.install_url_1());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.install_url_2())){
            urls.add(item.target.params.install_url_2());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.install_url_3())){
            urls.add(item.target.params.install_url_3());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.install_url_4())){
            urls.add(item.target.params.install_url_4());
        }

        realUploadAction(context, urls, item, "APP_INSTALL_SUCCESS");
    }


    public static void uploadLaunchAction(Context context, DisplayItem item, boolean suc){
        if(item == null || item.target == null)
            return;

        List<String> urls = new ArrayList<String>();

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.launch_url())){
            urls.add(item.target.params.launch_url());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.launch_url_1())){
            urls.add(item.target.params.launch_url_1());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.launch_url_2())){
            urls.add(item.target.params.launch_url_2());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.launch_url_3())){
            urls.add(item.target.params.launch_url_3());
        }

        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.launch_url_4())){
            urls.add(item.target.params.launch_url_4());
        }

        realUploadAction(context, urls, item, suc?"APP_LAUNCH_SUCCESS":"APP_LAUNCH_FAIL");
    }
}
