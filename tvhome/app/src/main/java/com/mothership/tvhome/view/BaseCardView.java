package com.mothership.tvhome.view;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.Browser;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.duokan.VolleyHelper;
import com.google.gson.reflect.TypeToken;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Transformation;
import com.tv.ui.metro.model.Constants;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.Image;
import com.tv.ui.metro.model.ImageGroup;
import com.tv.ui.metro.model.PlaySource;
import com.tv.ui.metro.model.XiaomiStatistics;
import com.video.ads.AdsReport;
import com.video.cp.model.AppPkgInfo;
import com.video.ui.idata.BackgroundService;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.AppGson;
import com.video.ui.loader.CommonBaseUrl;
import com.video.ui.loader.CommonUrl;
import com.video.ui.loader.Utils;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by liuhuadong on 11/19/14.
 */
public abstract class BaseCardView  extends RelativeLayout {
    private static final String TAG = "BaseCardView";
    public BaseCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if(media_item_padding == -1){
            media_item_padding = 30;//getResources().getDimensionPixelSize(R.dimen.ITEM_DIVIDE_SIZE);
            media_vertical_item_padding = 30;//getResources().getDimensionPixelSize(R.dimen.ITEM_VERTICAL_DIVIDE_SIZE);

            fontScale = getResources().getConfiguration().fontScale;

            iDataORM.picture_quality = iDataORM.getBooleanValue(context, iDataORM.good_picture_priority, false)?1:0;
        }
    }

    public static int getPictureQuality(Context context){
        if(iDataORM.picture_quality == -1){
            iDataORM.picture_quality = iDataORM.getBooleanValue(context, iDataORM.good_picture_priority, false)?1:0;
        }

        return iDataORM.picture_quality;
    }

    protected static float fontScale;
    public static void benchmark(long pre, String name){
        if(Constants.DEBUG)
            Log.d(Constants.BENCHMARK, name + " benchmark:" + (System.currentTimeMillis() - pre));
    }

    public static Picasso getSinglePicasso(Context context){
        return Picasso.with(context);

        /*
        if(picassoInstance == null){
            LruCache cache = new LruCache(context);
            picassoInstance = new Picasso.Builder(context).executor(pt).memoryCache(cache).build();
        }
        return picassoInstance;
        */
    }

    protected DisplayItem item;
    public DisplayItem getMediaContent(){
        return item;
    }

    public int dpToPx(float dp) {
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public Object getTag(int key) {
        Object obj = super.getTag(key);
        if(obj == null){
            return new Integer(-1);
        }

        return obj;
    }

    public static String trimString(String str){
        try {
            if (str != null) {
                if (str.length() > 60) {
                    str = str.substring(0, 60);
                }
            }
        }catch (Exception ne){}
        return str;
    }
    public static void formartShowInfo(DisplayItem item, HashMap<String, String>map){
        map.put("id-title", trimString(item.id) + "--" + item.title);
        map.put("id", trimString(item.id));
        if (item.media != null && !TextUtils.isEmpty(item.media.category)){
            map.put("cate", item.media.category);
            map.put("cate-title-id", item.media.category + "-" + item.title + "-"+trimString(item.id));
        }
    }

    private static int fetched_device_info = -1;
    private static boolean add_device_info = false;
    public static void formartDeviceMap(HashMap<String, String> map){
        map.put("miui",                CommonBaseUrl.getMIUIVersion());
        map.put("version",             String.valueOf(CommonBaseUrl.versionCode));
        map.put("version.incremental", Build.VERSION.INCREMENTAL);
        map.put("version.release",     Build.VERSION.RELEASE + " : " +Build.VERSION.SDK_INT);
        map.put("device",              Build.MODEL);
    }

    public static void uploadAdsEffect(Context context, String url){

        if(TextUtils.isEmpty(url))
            return;

        Response.ErrorListener rl = new Response.ErrorListener() {
            @Override public void onErrorResponse(VolleyError error) {}
        };

        //send feed back to ad publisher
        try {
            String calledURL = url;

            if(fetched_device_info == -1){
                add_device_info = iDataORM.getBooleanValue(context, "add_device_info", false);
                fetched_device_info = 0;
            }

            if(add_device_info) {
                calledURL = new CommonUrl(context).addCommonParams(url);
            }
            Request req = new Request(Request.Method.GET, calledURL, rl) {
                @Override protected Response parseNetworkResponse(NetworkResponse response) { return Response.success("successful", null); }
                @Override protected void deliverResponse(Object response) {}
            };
            req.setShouldCache(false);
            VolleyHelper.getInstance(context).addToAPIRequestQueue(req);

            if(XiaomiStatistics.initialed){
                HashMap<String, String>map = new HashMap<String, String>();
                map.put("miui_record_url",         calledURL);
                MiStatInterface.recordCountEvent(XiaomiStatistics.ads_record, XiaomiStatistics.ads_record, map);
            }
        }catch (Exception ne){}
    }

    public static void uploadTickAction(Context context, DisplayItem item){
        AdsReport.uploadTickAction(context, item);
    }

    public static void realUploadAction(Context context, List<String> urls, DisplayItem item, String event){
        AdsReport.realUploadAction(context, urls, item, event);
    }

    public static void uploadPresentAction(Context context, DisplayItem item){
        AdsReport.uploadPresentAction(context, item);
    }

    public static void uploadSecondAction(Context context, DisplayItem item){
        AdsReport.uploadSecondAction(context, item);
    }

    public static void uploadInstallAction(Context context, DisplayItem item){
        AdsReport.uploadInstallAction(context, item);
    }

    public static void uploadLaunchAction(Context context, DisplayItem item, boolean suc){
        AdsReport.uploadLaunchAction(context, item, suc);
    }

    protected static int media_item_padding = -1;
    protected static int media_vertical_item_padding = -1;
    private static HashMap<String, String> map = new HashMap<String, String>();
    public static void launcherAction(final Context context, DisplayItem item){

        if(item != null && item.settings != null && item.settings.get("version") != null ){
            try {
                String version = item.settings.get("version");
                int toVersion = Integer.parseInt(version);
                int versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;

                if(toVersion > versionCode){
                    AlertDialogHelper.showDiaload(context,"需要更新", "需要更新summary", "", "更新BUTTON", new AlertDialogHelper.DialogCallBack() {
                        @Override
                        public void onPositivePressed() {
                            BackgroundService.checkVerison(context, true, false, false);

                            if (XiaomiStatistics.initialed){
                                HashMap<String, String>mp = new HashMap<String, String>();
                                mp.put("package", context.getPackageName());
                                MiStatInterface.recordCalculateEvent(XiaomiStatistics.VersionUpgrade, "ok", 1, mp);
                            }
                        }

                        @Override
                        public void onNagtivePressed() {
                            if (XiaomiStatistics.initialed){
                                HashMap<String, String>mp = new HashMap<String, String>();
                                mp.put("package", context.getPackageName());
                                MiStatInterface.recordCalculateEvent(XiaomiStatistics.VersionUpgrade, "cancel", 1, mp);
                            }

                        }
                    });
                }

                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        int flag = 0;
        if (XiaomiStatistics.initialed) {
            map.clear();
            formartShowInfo(item, map);
            formartDeviceMap(map);

            if (item.target != null && !TextUtils.isEmpty(item.target.entity))
                MiStatInterface.recordCalculateEvent(XiaomiStatistics.action_click, item.target.entity, 1, map);
            else {
                MiStatInterface.recordCalculateEvent(XiaomiStatistics.action_click, "unknown", 1, map);
            }
        }

        uploadTickAction(context, item);

        if(Constants.DEBUG)
            Log.d("click action", "item =" + item);
        //Toast.makeText(context, "prepare to launch="+item.title + "/" +item.id + "/" + item.target + "/"+item.ns+ item.ui_type, Toast.LENGTH_SHORT).show();

        boolean go_play = false;
        if(item.target != null && item.target.entity != null) {

            if(Constants.Entiry_Long_Video.equals(item.target.entity)) {
                if((iDataORM.version_alpha.equals(iDataORM.application_type) && iDataORM.getBooleanValue(context, iDataORM.debug_mode, iDataORM.default_debug_mode))
                        ||
                        iDataORM.getBooleanValue(context, "simple_detail_ui", false) ||
                        (item.target.params != null && "oitem".equals(item.target.params.get("ui_type")))){
                    item.type = "oitem";
                }else {
                    flag = Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP;
                    item.type = "item";
                }
            }else if(Constants.Entity_APP_UPGRADE.equals(item.target.entity)){
                //force update app fast
                BackgroundService.checkVerison(context, true, false, false);
                Toast.makeText(context, "应用更新", Toast.LENGTH_SHORT).show();
                return;
            }
            else if(Constants.Entity_LONG_Video_PLAY.equals(item.target.entity)){
                if((iDataORM.version_alpha.equals(iDataORM.application_type) && iDataORM.getBooleanValue(context, iDataORM.debug_mode, iDataORM.default_debug_mode)) ||
                        iDataORM.getBooleanValue(context, "simple_detail_ui", false)){
                    item.type = "oitem";
                    flag = Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP;
                }else {
                    item.type = "item";
                    flag = Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP;
                }
                go_play = true;
            } else if (Constants.Entity_People.equals(item.target.entity)) {
                item.type = "people";
            }
            else if(Constants.Entity_Short_Video.equals(item.target.entity) ||
                    Constants.Entity_Album_Collection.equals(item.target.entity) ||
                    Constants.Entity_Album.equals(item.target.entity)) {

                item.type = "album";

            }else if(Constants.Entity_Search_Video.equals(item.target.entity) ||
                    Constants.Entity_Search_Result_Video.equals(item.target.entity)) {
                item.type = "search";
            } else if (Constants.Entity_Download.equals(item.target.entity)) {
                //check if the app already installed
                launchDownload(context, item);
                return;
            } else if (Constants.Entity_Intent.equals(item.target.entity)) {
                launchDefault(context, item);
                return;
            }else if(Constants.Entity_Browser.equals(item.target.entity)){
                launchBrowser(context, item);
                return;
            }else if(Constants.Entity_Local_Video.equals(item.target.entity)){
                //Toast.makeText(context, "Open local video", Toast.LENGTH_SHORT).show();
                launcherLocalVideo(context, item);
                return;
            } else if (Constants.Entity_Local_offline_Video.equals(item.target.entity)) {
                launcherLocalOfflineVideo(context, item);
                return;
            } else if (Constants.Entity_Local_dir_Video.equals(item.target.entity)) {
                launcherLocalDirVideo(context, item);
                return;
            }else if(Constants.Entity_Play.equals(item.target.entity)){
                launcherDirectPlay(context, item);
                return;
            }else if(Constants.Entity_Play_Url.equals(item.target.entity)){
                launcherH5URLPlay(context, item);
                return;
            }else if(Constants.DLNA_DIR.equals(item.target.entity)){
                //TODO
//                Intent local = new Intent(context, AlbumActivity.class);
//                local.putExtra(Constants.DLNA_DIR, true);
//                local.putExtra("item", item);
//                context.startActivity(local);
                return;
            }else if(Constants.Entity_DLNA_DIR_VIDEO.equals(item.target.entity) || Constants.Entity_Http_Play_Source.equals(item.target.entity)){
                String url = item.target.url;
                if (url.startsWith("file:///") == false && url.startsWith("http://") == false) {
                    url = "file://" + url;
                }
                addPlayHistory(context, item);
                if (Constants.Entity_DLNA_DIR_VIDEO.equals(item.target.entity) && url.startsWith("file:///")){
                    launcherLocalVideo(context, item);
                }else {
                    //TODO
                    //Player.play(context, Uri.parse(url), item.title);
                }
                return;
            }
            else if(Constants.Entity_Normal.equals(item.target.entity)){
                Log.d(TAG, "do nothing for normal");
            }else {
                launchDefault(context, item);
                return;
            }
        }else {
            item.type = "album";
            if(item.target != null && TextUtils.isEmpty(item.target.url) && TextUtils.isEmpty(item.id)){
                Log.d(TAG, "target is null and url is null");
                return;
            }
            //launchDefault(context, item);
            //return;
        }

        if(item.id.endsWith(Constants.Video_ID_History) || item.id.endsWith(Constants.Video_ID_Offline)){
            if (XiaomiStatistics.initialed)MiStatInterface.recordCalculateEvent("local", item.id, 1);
            item.type = "local_album";
        }

        if(item.id.endsWith(Constants.Video_ID_Favor)){
            AccountManager mAccountManager = AccountManager.get(context);
            final Account[] account = mAccountManager.getAccountsByType("com.xiaomi");
            if(account.length > 0 ) {
                //if not get account information, re-fetch account info in background again
                //TODO
                //UserManager.makeSureAccountDataFetched(context);

                //if login, show favor data from server
                item.target.entity = "album";
                item.target.url = "c/bookmark";
                item.id = Constants.VIDEO_BOOKMARK_ID;
            }else {
                if (XiaomiStatistics.initialed)MiStatInterface.recordCalculateEvent("local-favor", item.id, 1);
                item.type = "local_album";
            }
        }

        if(item.id.endsWith(Constants.Video_ID_Offline) ){
            item.type = "play_offline";
        }

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if(TextUtils.isEmpty(item.ns)){
                item.ns = "video";
            }
            intent.setData(Uri.parse("mvschema://" + item.ns + "/" + item.type + "?rid=" + item.id));
            intent.putExtra("item", item);
            intent.putExtra("go_play", go_play);
            if (item.settings != null && "1".equals(item.settings.get("from_api_activity"))){
                intent.setFlags(flag);
            }else {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|flag);
            }
            context.startActivity(intent);
        } catch (Exception ne) {
            ne.printStackTrace();
        }
    }

    public static void launcherLocalVideo(Context context, String url, String title){
        if (url.startsWith("file:///") == false) {
            url = "file://" + url;
        }
//        Player.playVideoUrls(context, 0, new String[]{url}, title==null?null:new String[]{title});
    }

    public static void launcherDirectPlay(Context context, DisplayItem item){
//        try {
//            String viid = item.target.params.viid();
//            String cp = item.target.params.cp();
//            int offset = item.target.params.offset();
//            boolean remove_ad = item.target.params.remove_ad();
//            String play_entity = item.target.params.entity();
//
//            if ("tvlive".equals(play_entity)) {
//                TvPlayManager.playChannel(context, viid);
//            } else {
//                Map<String,String> extras = new HashMap<String, String>();
//                extras.put(Player.PLAY_INFO_EXTRA_KEY_OFFSET, String.valueOf(offset));
//                extras.put(Player.PLAY_INFO_EXTRA_KEY_NOAD, String.valueOf(remove_ad));
//                if(item.settings!=null) extras.putAll(item.settings);
//                playByVid(context, viid, cp, item.title, extras);
//            }
//        }catch (Exception ne){
//            Toast.makeText(context, ne.getMessage() + " launcherDirectPlay "+item, Toast.LENGTH_SHORT).show();
//        }
    }

    //play the external h5
    public static void launcherH5URLPlay(Context context, DisplayItem item){
        String cp = item.target.params.get("cp");
//        EpisodePlayAdapter.play3rdH5(context, cp, item.target.url, item.target.params.get("title"));
    }

    private static void playByVid(Context context, String vId, String cp, final String title, final Map<String,String> extras){
//        final Context app = context.getApplicationContext();
//        Response.Listener<PlaySource> listener = new Response.Listener<PlaySource>() {
//            @Override
//            public void onResponse(PlaySource response) {
//                if (Utils.isHtml5Source(response)) {
//                    EpisodePlayAdapter.playOnlineH5(app, response, title, null);
//                } else {
//                    EpisodePlayAdapter.playOnlineBySDK(app, response, title, null, extras);
//                }
//            }
//        };
//        Response.ErrorListener errorListener = new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        };
//
//        EpisodePlayAdapter.fetchPlaySource(context, cp, vId, listener, errorListener);
    }

    public static void addPlayHistory(Context context, DisplayItem local){
        DisplayItem item = new DisplayItem();
        item.id     = local.id;
        item.target = new DisplayItem.Target();
        item.target.entity = Constants.Entity_DLNA_DIR_VIDEO;
        item.target.url = local.target.url;
        item.title      = local.title;
        item.hint   = new DisplayItem.Hint();
        item.images = new ImageGroup();
        Image image = new Image();
        image.url   = local.target.url;
        item.images.put("poster", image);
        iDataORM.addFavor(context, "video", iDataORM.HistoryAction, item.id, item);
    }

    public static void launcherLocalOfflineVideo(Context context, DisplayItem item) {
//        String vendorName = "";
//        String episodeIndex = "";
//        if (item.target != null && item.target.params != null && item.media != null) {
//            vendorName = item.target.params.get("vendorName");
//            episodeIndex = item.media.episode_index;
//        } else {
//            return;
//        }
//
//        if (TextUtils.isEmpty(episodeIndex)) return;
//        if (TextUtils.isEmpty(vendorName)) return;
//
//
//        Player.PlayInfo.OfflineVideo video = new Player.PlayInfo.OfflineVideo();
//        video.title = item.title;
//        video.vendorName = vendorName;
//        video.localPath = item.target.url;
//
//        // find episode according to episode index
//        boolean epsFound = false;
//        for (DisplayItem.Media.Episode episode : item.media.items) {
//            if (episodeIndex.equals(episode.id)) {
//                video.episode = episode;
//                epsFound = true;
//                break;
//            }
//        }
//
////        video.episode = item.media.items.get(0);
//
//        if (epsFound) {
//            Player.playOfflineVideo(context, item, 0, new Player.PlayInfo.OfflineVideo[]{video});
//        }
    }

    public static void launcherLocalVideo(Context context, DisplayItem item){
//        try {
//            addPlayHistory(context, item);
//
//            if (item.settings == null || item.settings.get("uri_list") == null) {
//                launcherLocalVideo(context, item.target.url, item.title);
//            } else {
//                String index = item.settings.get("play_index");
//
//                ArrayList<String> uri_list = AppGson.get().fromJson(item.settings.get("uri_list"), new TypeToken<ArrayList<String>>(){}.getType());
//                ArrayList<String> title_list = AppGson.get().fromJson(item.settings.get("title_list"), new TypeToken<ArrayList<String>>(){}.getType());
//                Player.playVideoUrls(context, Integer.valueOf(index), uri_list.toArray(new String[0]), title_list.toArray(new String[0]));
//            }
//        }catch (Exception ne){
//            Toast.makeText(context, ne.getMessage() + " "+item, Toast.LENGTH_SHORT).show();
//        }
    }

    public static void launcherLocalDirVideo(Context context, DisplayItem item){

//        //if just one video, play directly
//        if(item instanceof LocalMediaLoader.LocalMediaList){
//            if(((LocalMediaLoader.LocalMediaList)item).localMediaList != null){
//                if(((LocalMediaLoader.LocalMediaList)item).localMediaList.size() == 1){
//                    //play directly
//                    launcherLocalVideo(context, ((LocalMediaLoader.LocalMediaList)item).localMediaList.get(0));
//                    return;
//                }
//            }
//        }
//        Intent local = new Intent(context, AlbumActivity.class);
//        local.putExtra(Constants.Local_Dir_Video, true);
//        local.putExtra("item", item);
//        context.startActivity(local);
    }

    public static Intent buildIntent(Context context, DisplayItem item){
        Intent intent = null;

        if(item == null)
            return intent;

        if (item.target != null && TextUtils.isEmpty(item.target.params.android_action()) == false)
            if("ACTION_VIEW".equals(item.target.params.android_action())){
                intent = new Intent(Intent.ACTION_VIEW);
            }else {
                intent = new Intent(item.target.params.android_action());
            }
        else
            intent = new Intent(Intent.ACTION_VIEW);


        if(item.target != null && item.target.params != null && !TextUtils.isEmpty(item.target.params.android_component())) {
            intent.setPackage(item.target.params.android_component());

            if(!TextUtils.isEmpty(item.target.params.android_activty())) {
                intent.setClassName(item.target.params.android_component(), item.target.params.android_activty());
            }
        }

        //just intent mode set the data from target url
        if(item.target != null && !TextUtils.isEmpty(item.target.url ) && Constants.Entity_Intent.equals(item.target.entity)) {
            try {
                intent.setData(Uri.parse(item.target.url));
            }catch (Exception ne){}
        }

        if (item.target != null && item.target.params != null && !TextUtils.isEmpty(item.target.params.android_extra())) {
            try {
                intent.setData(Uri.parse(item.target.params.android_extra()));
            }catch (Exception ne){}
        }


        if(intent.getData() == null && !TextUtils.isEmpty(intent.getPackage()) && intent.getComponent() == null){
            //find activity in package manager
            try {
                if(context.getPackageManager().getPackageInfo(intent.getPackage(), 0) != null) {
                    Intent tmp = intent;
                    intent = context.getPackageManager().getLaunchIntentForPackage(intent.getPackage());

                    if(intent == null){
                        intent = tmp;
                    }
                }
            }catch (Exception ne){ne.printStackTrace();}
        }

        if (item.target != null && item.target.params != null && !TextUtils.isEmpty(item.target.params.android_mime())) {
            intent.setType(item.target.params.android_mime());
        }

        if(item.target != null && item.target.params != null && item.target.params.new_task())
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        return intent;
    }

    private static void launchBrowser(Context context, DisplayItem item) {
        if (item != null && !TextUtils.isEmpty(item.title)){
            try{

                Intent launchUriIntent = null;

                String query = item.title;
                if (!TextUtils.isEmpty(item.sub_title)) {
                    query = item.sub_title;
                }

                if(iDataORM.getBooleanValue(context, "baidu_link_mode", true)){
                    String searchUrl = "https://m.baidu.com/s?from=1012852t&word={searchTerms}"; // 搜索链接，包含渠道号，需张靖提供
                    String url = searchUrl.replace("{searchTerms}", URLEncoder.encode(query, "UTF-8"));   // 真正的搜索链接

                    launchUriIntent = new Intent(Intent.ACTION_VIEW);
                    launchUriIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    launchUriIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                    launchUriIntent.setData(Uri.parse(url));
                }else {
                    launchUriIntent = new Intent(Intent.ACTION_SEARCH);
                    launchUriIntent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    launchUriIntent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                }

                if (item.settings != null && !TextUtils.isEmpty(item.settings.get("vendor"))) {
                    launchUriIntent.putExtra("vendor", item.settings.get("vendor"));
                }

                if (item.settings != null && !TextUtils.isEmpty(item.settings.get("sniff"))) {
                    launchUriIntent.putExtra("sniff", item.settings.get("sniff"));
                }


                launchUriIntent.putExtra(SearchManager.QUERY, query);
                launchUriIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchUriIntent);
            }catch (Exception ne){
                ne.printStackTrace();
            }
        }
    }

    private static void launchDownload(Context context, DisplayItem item){
        if(item.target.params != null && !TextUtils.isEmpty(item.target.params.android_component())){
            String download_url = item.target.url;
            if(item.target.params != null && !TextUtils.isEmpty(item.target.params.apk_url())){
                download_url = item.target.params.apk_url();
            }

            if(TextUtils.isEmpty(download_url)){
                Log.e(TAG, "wrong url to download");
                return;
            }

            try {
                ApplicationInfo aio = context.getPackageManager().getApplicationInfo(item.target.params.android_component(), 0);
                if(aio != null){
                    //launch app
                    Intent intent = buildIntent(context, item);

                    int versionCode = context.getPackageManager().getPackageInfo(item.target.params.android_component(), 0).versionCode;
                    if ((versionCode < item.target.params.apk_version())) {
                        //download
                        if(item.target == null){
                            item.target = new DisplayItem.Target();
                        }
                        if(item.target.params != null){
                            item.target.params = new DisplayItem.Target.Params();
                        }
                        item.target.params.put( DisplayItem.Target.Params.prompt, "false");
                        AlertDialogHelper.showLoadingDialog(context, download_url, item);
                    }

                    try {
                        context.startActivity(intent);
                    }catch (Exception ne){
                        ne.printStackTrace();

                        try {
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }catch (Exception le){le.printStackTrace();}
                    }
                }
            } catch (Exception e) {
                //download
                AlertDialogHelper.showLoadingDialog(context, download_url, item);

                //BackgroundService.startDownloadAPK(context, item.target.url, item.title, context.getString(R.string.app_name), item.target.params.prompt(), item);
            }
        }else {
            //download
            String download_url = item.target.url;
            if(item.target.params != null && !TextUtils.isEmpty(item.target.params.apk_url())){
                download_url = item.target.params.apk_url();
            }
            AlertDialogHelper.showLoadingDialog(context, download_url, item);
            //BackgroundService.startDownloadAPK(context, item.target.url, item.title, context.getString(R.string.app_name), item.target.params.prompt(), item);
        }
    }

    private static void launchDefault(Context context, DisplayItem item){
        Intent intent = null;
        AppPkgInfo api = null;

        try {

            if (item.target != null && item.target.url!=null && item.target.params!= null && item.target.params.inner_html()){
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("mivideo://video/internal?url=" + item.target.url));
                intent.putExtra("action_url", item.target.params.action_url());
                context.startActivity(intent);
                return;
            }

            intent = buildIntent(context, item);

            //trying to loading apk
            if (api == null && !TextUtils.isEmpty(item.target.params.android_component())) {
                //check app is exist
                try {
                    ApplicationInfo aio = context.getPackageManager().getApplicationInfo(item.target.params.android_component(), 0);
                    int versionCode = context.getPackageManager().getPackageInfo(item.target.params.android_component(), 0).versionCode;
                    if (aio == null || (!TextUtils.isEmpty(item.target.params.apk_url()) && versionCode < item.target.params.apk_version())) {
                        //download
                        AlertDialogHelper.showLoadingDialog(context, item.target.params.apk_url(), item);

                        //BackgroundService.startDownloadAPK(context, item.target.params.apk_url(), item.title, context.getString(R.string.app_name), true);
                    }
                } catch (Exception ne) {
                    //download
                    if (!TextUtils.isEmpty(item.target.params.apk_url())) {
                        AlertDialogHelper.showLoadingDialog(context, item.target.params.apk_url(), item);
                        //BackgroundService.startDownloadAPK(context, item.target.params.apk_url(), item.title, context.getString(R.string.app_name), true);
                    }
                }
            }

            if (api == null) {
                context.startActivity(intent);
            } else {
                makeHideAppApkReady(context, intent, api);
            }
        }catch (Exception ne){
            ne.printStackTrace();

            try {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }catch (Exception le){
                le.printStackTrace();

                //uninstall app
                //BackgroundService.unInstallApk(context, intent.getPackage());
            }
        }
    }

    private static void makeHideAppApkReady(final Context context, final Intent intent, final AppPkgInfo api) {

    }

    private static void goDownloadApk(final Context context, final Intent intent, final AppPkgInfo api, PackageInfo pi) {

    }

    public static Bitmap getRoundedCornerBitmap(int color, int width, int height, float round) {
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, round, round, paint);
        return output;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float round) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float Px = round;

        final Rect bottomRect = new Rect(0, bitmap.getHeight(), bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, Px, Px, paint);
        // Fill in upper right corner
        // canvas.drawRect(topRightRect, paint);
        // Fill in bottom corners
        canvas.drawRect(bottomRect, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        if (bitmap != output) {
            bitmap.recycle();
        }
        return output;
    }

    public static class BackgroundTarget implements Target {
        ImageView mView;
        public BackgroundTarget(ImageView v){
            mView = v;
        }
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
            mView.setBackground(new BitmapDrawable(mView.getResources(),bitmap));

        }
        public void onBitmapFailed(android.graphics.drawable.Drawable drawable){
            //mView.setImageResource(R.drawable.list_selector_bg);
        }

        public void onPrepareLoad(android.graphics.drawable.Drawable drawable){
            //mView.setImageResource(R.drawable.list_selector_bg);
        }
    }

    public static class Round_Corners implements Transformation {
        private int Round;
        private boolean justTopEffect;
        private boolean isCircle;
        public Round_Corners(Context context, int margin, int Round, boolean justTop) {
            this.Round = dpToPx(context, Round);
            justTopEffect = justTop;
        }

        public Round_Corners(Context context, boolean circle) {
            this.Round = dpToPx(context, Round);
            isCircle = circle;
        }

        public int dpToPx(Context context, int dp) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int px = Math.round(dp* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

        @Override
        public String key() {
            return "Round" + Round;
        }

        @Override
        public Bitmap transform(Bitmap arg0) {
            return getRoundedTopLeftCornerBitmap(arg0);
        }

        public Bitmap getRoundedTopLeftCornerBitmap(Bitmap bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            float Px = Round;
            if(isCircle){
                Px = bitmap.getWidth()/2;
                justTopEffect = false;
            }

            final Rect bottomRect = new Rect(0, bitmap.getHeight()/(justTopEffect?2:1), bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, Px, Px, paint);
            // Fill in upper right corner
            // canvas.drawRect(topRightRect, paint);
            // Fill in bottom corners
            canvas.drawRect(bottomRect, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            if (bitmap != output) {
                bitmap.recycle();
            }
            return output;
        }
    }
}
