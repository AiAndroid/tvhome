package com.mothership.tvhome.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.style.ClickableSpan;

import android.view.View;
import com.tv.ui.metro.model.DisplayItem;


import java.util.HashMap;
import java.util.Set;

/**
 * Created by liuhuadonbg on 3/5/15.
 */
public class AlertDialogHelper {
    final static String TAG = "AlertDialogHelper";
    public interface DialogCallBack{
        void onPositivePressed();
        void onNagtivePressed();
    }

    private static class DeclarationURLSpan extends ClickableSpan {
        private String mUrl;
        public DeclarationURLSpan(String url) {
            mUrl = url;
        }

        @Override
        public void onClick(View widget) {
            try{
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mivideo://video/internal?url=" + mUrl));
                widget.getContext().startActivity(intent);
            }catch (Exception ne){
                ne.printStackTrace();
            }
        }
    }

    public static void showDiaload(Context context, String title, String message, String app_icon, final DialogCallBack callBack){
        String positiveStr = context.getResources().getString(android.R.string.ok);
        showDiaload(context, title, message, app_icon, positiveStr, callBack);
    }

    public static void showDiaload(final Context context, String title, String message, String app_icon, String positiveStr,  final DialogCallBack callBack){

    }


    public static void showDialoadOrig(Context context, String title, String message, String summary, String positiveStr,  final DialogCallBack callBack){

    }

    public static volatile HashMap<String, Long> downloading = new HashMap<String, Long>();
    public static volatile HashMap<String, DisplayItem> downloadingDisplayItem = new HashMap<String, DisplayItem>();

    public static boolean isForPlugIn(long download_id){
        Set<String> names = downloading.keySet();
        for(String name:names){
            if(downloading.get(name) == download_id){
                return true;
            }
        }
        return false;
    }

    public static void releaseDownload(long download_id){
        synchronized (downloading) {
            Set<String> names = downloading.keySet();
            for (String name : names) {
                if (downloading.get(name) == download_id) {
                    downloading.remove(name);
                    break;
                }
            }
        }
    }

    public static HashMap<String, Intent> installedApplication = new HashMap<String, Intent>();

    private static void registerPackageLauncher(final Context launcher, final Intent intent, String hint_title, final DisplayItem item){

    }

    public static void showLoadingDialog(final Context launcher, final String apk_url, final DisplayItem item){

    }



    public static void reportAppDownloadSuccess(Context context, long download_id){

    }
    public static void showInfomationDiaload(Context context, String title, String message){

    }

    public static void showDataUsageDiaload(Context context, final DialogCallBack callBack){

    }


    public static void showAlhpaVersionJoinDiaload(final Context context, final DialogCallBack callBack){

    }

    public static void showPlayDataUsageDiaload(Context context, final DialogCallBack callBack){

    }
}
