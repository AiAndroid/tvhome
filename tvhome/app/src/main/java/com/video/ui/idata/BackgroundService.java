package com.video.ui.idata;

import android.app.AlarmManager;
import android.app.DownloadManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.*;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.duokan.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.mothership.tvhome.*;
import com.mothership.tvhome.view.AlertDialogHelper;
import com.mothership.tvhome.view.BaseCardView;
import com.tv.ui.metro.model.AppVersion;
import com.tv.ui.metro.model.Constants;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.XiaomiStatistics;
import com.video.ui.Util;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonBaseUrl;
import com.video.ui.loader.CommonUrl;
import com.video.utils.IPackageManagerConstants;
import com.video.utils.WLReflect;
import com.xiaomi.analytics.Actions;
import com.xiaomi.analytics.Analytics;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.net.URI;
import java.util.HashMap;

import miui.os.BuildV5;

/**
 * Created by liuhuadonbg on 1/31/15.
 */
public class BackgroundService extends IntentService {
    static final String TAG = "BackgroundService";
    public static final String REMOVE_MIPUSH_MESSAGE_ACTION = "com.miui.video_remove_mipush";

    public BackgroundService() {
        super("video_background");
    }

    private static boolean registed_download = false;
    private static HashMap<Long, Long> downloadingTask = new HashMap<Long, Long>();



    private final static String favor_sync_last_time          = "favor_sync_last_time";
    private final static String ads_sync_last_time            = "ads_sync_last_time";
    private final static String cloud_settings_sync_last_time = "cloud_settings_sync_last_time";
    private final static String activate_sync_last_time       = "activate_sync_last_time";
    private static int nErrorCount    = 0;
    private static int nErrorAdsCount = 0;
    private static int nErrorCloudSettingsCount = 0;
    private static int nErrorActivateCount      = 0;
    private static final long LOOP_FAVOR_SYNC         = 2;
    private static final long LOOP_ADS_SYNC           = 4;
    private static final long LOOP_CLOUD_SETTING_SYNC = 1;
    private static final long ACTIVATE_SYNC           = 3;//3 days
    private static final boolean TEST_LOOP = false;
    private static Context mContext;
    public static void startFavorLoopAlarm(Context context){
        long lastsync = iDataORM.getLongValue(context, favor_sync_last_time, 0);

        makeSureHandlerExist(context);
        rescheduleFavorAlarming(context, lastsync<=0?true:false);
    }

    private static void makeSureHandlerExist(Context context){
        if(mHandler == null)
            mHandler = new BackHandler();

        if(mContext == null)
            mContext = context.getApplicationContext();
    }

    public static void FavorAarmingComing(final Context context){

        Log.d(TAG, "FavorAarmingComing ");
        makeSureHandlerExist(context);
        mHandler.obtainMessage(FAVOR_FETCH).sendToTarget();


        //delay 10 seconds to let the fetch finish
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rescheduleFavorAlarming(context, false);
            }
        }, 10000);

    }

    public static void startAdsLoopAlarm(Context context){
        long lastsync = iDataORM.getLongValue(context, ads_sync_last_time, 0);

        makeSureHandlerExist(context);

        rescheduleAdsAlarming(context, lastsync <= 0 ? true : false);
    }

    public static void rescheduleAdsAlarming(final Context context){

        Log.d(TAG, "rescheduleAdsAlarming :"+System.currentTimeMillis());
        makeSureHandlerExist(context);

        if(iDataORM.enabledAds(context)) {
            mHandler.obtainMessage(ADS_FETCH).sendToTarget();
        }

        //delay 10 seconds to let the fetch finish
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rescheduleAdsAlarming(context, false);
            }
        }, 10000);
    }

    public static void startActivateLoopAlarm(Context context){
        long lastsync = iDataORM.getLongValue(context, activate_sync_last_time, 0);

        makeSureHandlerExist(context);
        rescheduleActivateAlarming(context, lastsync <= 0 ? true : false);
    }
    public static void startCloudConfigurationLoopAlarm(Context context){
        long lastsync = iDataORM.getLongValue(context, cloud_settings_sync_last_time, 0);

        if(mContext == null){
            mContext = context.getApplicationContext();
        }
        rescheduleCloudSettingsAlarming(context, lastsync <= 0 ? true : false);
    }

    public static void CloudSettingsAarmingComing(final Context context){

        Log.d(TAG, "CloudSettingsAarmingComing ");
        makeSureHandlerExist(context);
        mHandler.obtainMessage(CLOUD_SETTINGS_FETCH).sendToTarget();

        //delay 10 seconds to let the fetch finish
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rescheduleCloudSettingsAlarming(context, false);
            }
        }, 10000);
    }

    public static void activateAarmingComing(final Context context){

        Log.d(TAG, "activateAarmingComing ");
        makeSureHandlerExist(context);
        mHandler.obtainMessage(ACTIVATE_FETCH).sendToTarget();

        //delay 10 seconds to let the fetch finish
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rescheduleActivateAlarming(context, false);
            }
        }, 10000);
    }

    private static final int FAVOR_FETCH          = 100;
    private static final int CLOUD_SETTINGS_FETCH = 101;
    private static final int ADS_FETCH            = 102;
    private static final int ACTIVATE_FETCH       = 103;
    private static Handler mHandler;
    private static class  BackHandler extends Handler{
        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case FAVOR_FETCH: {
                    SyncServiceHelper.addAllBookMark(mContext, false, new SyncServiceHelper.Callback() {
                        @Override
                        public void onResult(boolean suc, Object ids) {
                            Log.d(TAG, "success add bookmarks ids:" + ids);
                            if (suc) {
                                iDataORM.addSetting(mContext, favor_sync_last_time, String.valueOf(System.currentTimeMillis()));
                                nErrorCount = 0;
                            } else {
                                nErrorCount++;
                            }
                        }
                    });
                    break;
                }
                case CLOUD_SETTINGS_FETCH: {
                    SyncServiceHelper.syncSettings(mContext, new SyncServiceHelper.Callback() {
                        @Override
                        public void onResult(boolean suc, Object settings) {
                            Log.d(TAG, "success get configuration ids:" + settings);
                            if (suc) {
                                iDataORM.addSetting(mContext, cloud_settings_sync_last_time, String.valueOf(System.currentTimeMillis()));
                                nErrorCloudSettingsCount = 0;
                            } else {
                                nErrorCloudSettingsCount++;
                            }
                        }
                    });
                    break;
                }
                case ADS_FETCH:{
                    SyncServiceHelper.fetchAds(mContext, new SyncServiceHelper.Callback() {
                        @Override
                        public void onResult(boolean suc, Object ids) {
                            Log.d(TAG, "success fetchAds ids:");
                            if (suc) {
                                iDataORM.addSetting(mContext, ads_sync_last_time, String.valueOf(System.currentTimeMillis()));
                                nErrorAdsCount = 0;
                            } else {
                                nErrorAdsCount++;
                            }
                        }
                    });
                    break;
                }
                case ACTIVATE_FETCH:{
                    SyncServiceHelper.activateOperate(mContext, new SyncServiceHelper.Callback() {
                        @Override
                        public void onResult(boolean suc, Object settings) {
                            Log.d(TAG, "success get activateOperate ids:" + settings);
                            if (suc) {
                                iDataORM.addSetting(mContext, activate_sync_last_time, String.valueOf(System.currentTimeMillis()));
                                nErrorActivateCount = 0;
                            } else {
                                nErrorActivateCount++;
                            }
                        }
                    });
                    break;
                }

            }
        }
    };

    public static final String REQUEST_ADS_SYNC_ACTION = "com.miui.video_ads_sync";
    public static void rescheduleAdsAlarming(Context context, boolean force){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long nextTime = getNextTime(context, nErrorAdsCount, ads_sync_last_time, iDataORM.LOOP_ADS_SYNC, LOOP_ADS_SYNC, force, 20, 30, 1);
        Intent i = new Intent(REQUEST_ADS_SYNC_ACTION);
        PendingIntent intent = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.set(AlarmManager.RTC, nextTime, intent);
    }

    public static void rescheduleAdsAlarming(Context context, boolean force, int timeout){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long nextTime = getNextTime(context, nErrorAdsCount, ads_sync_last_time, iDataORM.LOOP_ADS_SYNC, LOOP_ADS_SYNC, force, 20, timeout, 1);
        Intent i = new Intent(REQUEST_ADS_SYNC_ACTION);
        PendingIntent intent = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.set(AlarmManager.RTC, nextTime, intent);
    }

    public static final String REQUEST_FAVOR_SYNC_ACTION = "com.miui.video_favor_sync";
    public static void rescheduleFavorAlarming(Context context, boolean force){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long nextTime = getNextTime(context, nErrorCount, favor_sync_last_time, iDataORM.LOOP_FAVOR_SYNC, LOOP_FAVOR_SYNC, force, 20, 5, 24);
        Intent i = new Intent(REQUEST_FAVOR_SYNC_ACTION);
        PendingIntent intent = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.set(AlarmManager.RTC, nextTime, intent);
    }

    public static final String REQUEST_CLOUD_SETTINGS_SYNC_ACTION = "com.miui.video_cloud_settings_sync";
    public static void rescheduleCloudSettingsAlarming(Context context, boolean force){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long nextTime = getNextTime(context, nErrorCloudSettingsCount, cloud_settings_sync_last_time, iDataORM.LOOP_CLOUD_SETTING_SYNC, LOOP_CLOUD_SETTING_SYNC, force, 20, 20, 12);
        Intent i = new Intent(REQUEST_CLOUD_SETTINGS_SYNC_ACTION);
        PendingIntent intent = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.set(AlarmManager.RTC, nextTime, intent);
    }

    public static final String REQUEST_ACTIVATE_ACTION = "com.miui.video_activate_sync";
    public static void rescheduleActivateAlarming(Context context, boolean force){
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long nextTime = getNextTime(context, nErrorActivateCount, activate_sync_last_time, iDataORM.ACTIVATE_SYNC, ACTIVATE_SYNC, force, 20, 20, 24);
        Intent i = new Intent(REQUEST_ACTIVATE_ACTION);
        PendingIntent intent = PendingIntent.getService(context.getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmMgr.set(AlarmManager.RTC, nextTime, intent);
    }

    private static long getNextTime(Context context, int errorCount, String recordLastTime, String recordType, long loop, boolean force, int loop_delay, int delay, int hours){
        long nexttime;
        long current_time = System.currentTimeMillis();
        long last_update_time = iDataORM.getLongValue(context, recordLastTime, 0);
        long donespan  = (current_time - last_update_time);
        long left_time = iDataORM.getLongValue(context, recordType, loop) * (hours * 60 * 60 * 1000) - donespan;
        if (donespan < 0 || left_time <= 0) {
            long waittime = 1;
            for (int i = 0; i < errorCount && i < 10; i++) {
                waittime = waittime * 2;
            }
            nexttime = System.currentTimeMillis() + loop_delay * 1000 * waittime;
        } else {
            nexttime = System.currentTimeMillis() + left_time;
        }
        if (force) {
            nexttime = System.currentTimeMillis() + delay * 1000;
        }

        if (TEST_LOOP) {
            nexttime = System.currentTimeMillis() + 2 * 60 * 1000;
        }

        //only in debug mode
        if(iDataORM.getBooleanValue(context, iDataORM.debug_mode, iDataORM.default_debug_mode)){
            nexttime = System.currentTimeMillis() + 20 * 1000;
        }

        return nexttime;
    }

    public static void startDownloadAPK(final Context context, String apk_url, String title, String desc, boolean show_hint, final DisplayItem item){
        final Intent intent = new Intent(REQUEST_APK_DOWNLOAD);
        intent.putExtra("apk_url", apk_url);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        intent.putExtra("item", item);

        if(show_hint){
            String hint_download = "下载提示";//context.getString(R.string.hint_apk_download);
            if(!TextUtils.isEmpty(item.target.params.app_name())){
                hint_download = String.format("下载啥呢 %1$s", item.target.params.app_name());
            }

            String app_icon = "";
            if(!TextUtils.isEmpty(item.target.params.app_icon())){
                app_icon = item.target.params.app_icon();
            }

            AlertDialogHelper.showDiaload(context, title, hint_download, app_icon, "安装APK", new AlertDialogHelper.DialogCallBack() {
                @Override
                public void onPositivePressed() {
                    if (item != null) {
                        BaseCardView.uploadSecondAction(context, item);
                    }
                    context.startService(intent);
                }

                @Override
                public void onNagtivePressed() {
                }
            });
        }else {
            context.startService(intent);
        }

    }

    public static void startVideoDownloadAPK(final Context context, String apk_url, String title, String desc, boolean show_hint, final DisplayItem item, boolean showDownloadNotify){
        final Intent intent = new Intent(REQUEST_APK_DOWNLOAD);
        intent.putExtra("apk_url", apk_url);
        intent.putExtra("title", title);
        intent.putExtra("desc", desc);
        intent.putExtra("item", item);
        intent.putExtra("component", context.getPackageName());
        intent.putExtra("notify", showDownloadNotify);

        if(show_hint){
            String hint_download = "下载APK";//context.getString(R.string.hint_apk_download);
            if(!TextUtils.isEmpty(item.target.params.app_name())){
                hint_download = String.format("下载啥 %1$s", item.target.params.app_name());
            }

            String app_icon = "";
            if(!TextUtils.isEmpty(item.target.params.app_icon())){
                app_icon = item.target.params.app_icon();
            }

            AlertDialogHelper.showDiaload(context, title, hint_download, app_icon, "下载APK",  new AlertDialogHelper.DialogCallBack() {
                @Override
                public void onPositivePressed() {
                    if(item != null){
                        BaseCardView.uploadSecondAction(context, item);
                    }
                    context.startService(intent);
                }

                @Override
                public void onNagtivePressed() {}
            });
        }else {
            context.startService(intent);
        }

    }

    public static void startDownloadAPK(final Context context, String apk_url, String title, String desc){
        startDownloadAPK(context, apk_url, title, desc, false, null);
    }


    public static void startVideoDownloadAPK(final Context context, String apk_url, String title, String desc, boolean showDownloadNotify){
        startVideoDownloadAPK(context, apk_url, title, desc, false, null, showDownloadNotify);
    }

    public static void startDownloadAPK(final Context context, String apk_url, String title, String desc, DisplayItem item){
        startDownloadAPK(context, apk_url, title, desc, false, item);
    }

    private static final String apk_mime = "application/vnd.android.package-archive";
    private static long requestDownload(Context context, String apk_url, String title, String desc, Intent intent, boolean visible){
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apk_url));
        request.setMimeType(apk_mime);
        //request.setMimeType(VideoUtils.getMimeType(apk_url));
        request.setTitle(title);
        request.setDescription(desc);
        request.setVisibleInDownloadsUi(true);
        int downloadFlag = DownloadManager.Request.NETWORK_WIFI;
        downloadFlag |=DownloadManager.Request.NETWORK_MOBILE;
        request.setAllowedNetworkTypes(downloadFlag);
        request.allowScanningByMediaScanner();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "." + MimeTypeMap.getFileExtensionFromUrl(apk_url);
        request.setDestinationUri(Uri.fromFile(new File(path)));

        if(visible) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE | DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }else {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
        }

        long download_id = dm.enqueue(request);
        registerDownloadMoniter(context);
        downloadingTask.put(download_id, download_id);
        return download_id;
    }

    private static long requestDownload(Context context, String apk_url, String title, String desc, Intent intent){
        return requestDownload(context, apk_url, title, desc, intent, true);
    }

    private static long requestDownload(Context context, String apk_url, String title, String desc){
        return requestDownload(context, apk_url, title, desc, null);
    }

    public static void registerDownloadMoniter(Context context){
        if(registed_download == false) {
            IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            filter.addAction(DownloadManager.ACTION_NOTIFICATION_CLICKED);
            context.registerReceiver(receiver, filter);
            registed_download = true;
        }
    }
    public static void unRegisterDownloadApkMonitor(Context context){
        try {
            registed_download = false;
            context.unregisterReceiver(receiver);
        }catch (Exception ne){}
    }
    public static final String REQUEST_APK_DOWNLOAD = "com.miui.video_apk_download";
    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent == null )
            return;

        if(REQUEST_APK_DOWNLOAD.equals(intent.getAction())){
            final String apk_url = intent.getStringExtra("apk_url");
            final String title   = intent.getStringExtra("title");
            final String desc    = intent.getStringExtra("desc");
            final String component = intent.getStringExtra("component");
            final DisplayItem item = (DisplayItem) intent.getSerializableExtra("item");

            //build intent
            Intent intent_launch = BaseCardView.buildIntent(getApplicationContext(), item);
            long download_id = requestDownload(getApplicationContext(), apk_url, title, desc, intent_launch, intent.getBooleanExtra("notify", true));
            if(!TextUtils.isEmpty(component) && component.equals(getApplicationContext().getPackageName())){
                iDataORM.addSettingSync(getApplicationContext(), "com.miui.video_down_id", String.valueOf(download_id));
            }
        }else if(REQUEST_FAVOR_SYNC_ACTION.equals(intent.getAction())){
            FavorAarmingComing(getApplicationContext());
        }else if(REQUEST_CLOUD_SETTINGS_SYNC_ACTION.equals(intent.getAction())){
            CloudSettingsAarmingComing(getApplicationContext());
        }else if(REQUEST_ADS_SYNC_ACTION.equals(intent.getAction())){
            rescheduleAdsAlarming(getApplicationContext());
        }else if(Constants.EXPIRED_BROADCAST.equals(intent.getAction())){
            Log.d(TAG, "login expired, need re-login");
            //TODO
            //LoginManager.LoginRequest.doLogin(getApplicationContext(), LoginManager.getInstance(getApplicationContext()));
        }else if(Constants.EXPIRED_UPGRADE_BROADCAST.equals(intent.getAction())){
            checkVerison(getApplicationContext(), intent.getBooleanExtra("force", false), intent.getBooleanExtra("show_whatsnew", false), false);
        }else if(REQUEST_ACTIVATE_ACTION.equals(intent.getAction())){
            activateAarmingComing(getApplicationContext());
        }else if(REMOVE_MIPUSH_MESSAGE_ACTION.equals(intent.getAction())){
//            APIActivity.sendTraceLogIfMipush(intent);
        }
    }

    public static String getDownloadFile(Cursor c){
        String localString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
        Log.d(TAG, "local_filename: "+localString);

        if (localString!= null ) {
            if(localString.startsWith("file://")) {
                localString = localString.substring(7);
            }else {
                if (localString.startsWith("file:///") == false) {
                    localString = "file://" + localString;
                }
            }
        }

        String uriString = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
        String filePath = getFilePathFromUri(mContext, Uri.parse(uriString));

        String apkURL = TextUtils.isEmpty(localString)?filePath:localString;

        Log.d(TAG, "local_filename: "+apkURL + " local_uri:"+uriString + " filePath:"+filePath);
        return apkURL;
    }

    public static String getFilePathFromUri(Context c, Uri uri) {
        String filePath = null;
        if ("content".equals(uri.getScheme())) {
            String[] filePathColumn = { MediaStore.MediaColumns.DATA };
            ContentResolver contentResolver = c.getContentResolver();

            Cursor cursor = contentResolver.query(uri, filePathColumn, null,
                    null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();
        } else if ("file".equals(uri.getScheme())) {
            filePath = new File(uri.getPath()).getAbsolutePath();
        }
        return filePath;
    }

    static BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            DownloadManager dm = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                //ignore for plug-in download
                if(AlertDialogHelper.isForPlugIn(downloadId)){
                    Log.d(TAG, "install for plugin apk");
                }

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor c = dm.query(query);
                if(c != null) {
                    if (c.moveToFirst()) {
                        int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                        if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                            AlertDialogHelper.reportAppDownloadSuccess(context, downloadId);

                            String mime   = c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
                            String apkURL = getDownloadFile(c);

                            Log.d(TAG, "new downloaded" + apkURL);
                            if (apk_mime.equals(mime) || apkURL.endsWith(".apk")) {
                                PackageInstallObserver pi = new PackageInstallObserver();
                                pi.download_id = downloadId;
                                pi.context = context.getApplicationContext();
                                BackgroundService.installApk(context, apkURL, pi);
                            }

                            /*
                            if (apk_mime.equals(mime) ) {
                                PackageInstallObserver pi = new PackageInstallObserver();
                                pi.download_id = downloadId;
                                pi.context = context.getApplicationContext();
                                BackgroundService.installApk(context, uriString, pi);
                                downloadingTask.remove(downloadId);
                            }*/
                        }
                    }

                    c.close();
                }
            } else if (DownloadManager.ACTION_NOTIFICATION_CLICKED.equals(action)) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO) {
                    long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                    Log.d(TAG, "new download complete=" + downloadId);
                    DownloadManager.Query query = new DownloadManager.Query();
                    query.setFilterById(downloadId);
                    Cursor c = dm.query(query);
                    if(c != null) {
                        if (c.moveToFirst()) {
                            int columnIndex = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                            if (DownloadManager.STATUS_SUCCESSFUL == c.getInt(columnIndex)) {

                                String apkURL = getDownloadFile(c);
                                String mime   = c.getString(c.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));

                                Log.d(TAG, "new downloaded" + apkURL);
                                if (apk_mime.equals(mime) || apkURL.endsWith(".apk")) {
                                    PackageInstallObserver pi = new PackageInstallObserver();
                                    pi.download_id = downloadId;
                                    pi.context = context.getApplicationContext();
                                    BackgroundService.installApk(context, apkURL, pi);
                                } else {
                                    openDownloadsPage(context);
                                }
                            } else if (DownloadManager.STATUS_FAILED == c.getInt(columnIndex)) {
                                Log.d(TAG, "new download fail=" + downloadId);
                                openDownloadsPage(context);
                            }
                        }

                        c.close();
                    }
                } else {
                    openDownloadsPage(context);
                }
            }
        }
    };


    public static void openDownloadsPage(Context context) {
        try {
            Intent pageView = new Intent(DownloadManager.ACTION_VIEW_DOWNLOADS);
            pageView.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(pageView);
        }catch (Exception ne){
            ne.printStackTrace();
        }
    }

    public static void installApk(Context context, String uriString, PackageInstallObserver pi){
        try {
            context.enforceCallingOrSelfPermission(android.Manifest.permission.INSTALL_PACKAGES, null);
        }catch (Exception ne){}

        try {
            Log.d(TAG, "installApk :"+uriString);
            pi.uriString = uriString;
            WLReflect.installPackage(context.getPackageManager(), Uri.parse(uriString), pi, IPackageManagerConstants.INSTALL_REPLACE_EXISTING, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();

            installManual(context, uriString);
        }

    }


    public static void unInstallApk(Context context, String packageName) {
        try {
            context.enforceCallingOrSelfPermission(android.Manifest.permission.INSTALL_PACKAGES, null);
        } catch (Exception ne) {
        }

        try {
            PackageUNInstallObserver pi = new PackageUNInstallObserver();
            WLReflect.unInstallPackage(context.getPackageManager(), pi, 0x00000001, context.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();

            try {
                Intent actionIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
                actionIntent.setDataAndType(Uri.parse(packageName), "application/vnd.android.package-archive");
                actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(actionIntent);
            } catch (Exception ne) {
                ne.printStackTrace();
            }
        }
    }


    private static void installManual(Context context, String uriString){
        try {
            Intent actionIntent = new Intent(Intent.ACTION_INSTALL_PACKAGE);
            actionIntent.setDataAndType(Uri.parse(uriString), "application/vnd.android.package-archive");
            actionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(actionIntent);
        }catch (Exception ne){
            ne.printStackTrace();
        }
    }

    public interface InstallCallback{
        void onResult(boolean result);
    }

    public static class PackageInstallObserver extends IPackageInstallObserver.Stub{
        public String uriString;
        public String pkgName;
        public int returnCode = Integer.MIN_VALUE;

        //use to do install callback
        public  InstallCallback installCallback;

        private static HashMap<String, String> map = new HashMap<String, String>();
        public long download_id;
        public Context context;

        @Override
        public void packageInstalled(String packageName,  final int retCode)throws RemoteException {
            pkgName = packageName;
            returnCode = retCode;
            //返回的结果不是我们想要的结果
            if(retCode == IPackageManagerConstants.INSTALL_SUCCEEDED){
                //Toast.makeText(mContext, "Install application successfully", Toast.LENGTH_SHORT).show();

                //add statistics
                map.clear();
                map.put("package", packageName);
                if(mContext != null) {
                    try {
                        String name = mContext.getPackageManager().getPackageInfo(packageName, 0).applicationInfo.name;
                        map.put("app_name", name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                BaseCardView.formartDeviceMap(map);
                if(XiaomiStatistics.initialed)MiStatInterface.recordCalculateEvent(XiaomiStatistics.app_install, "install_suc", 1, map);

                if(installCallback != null){
                    installCallback.onResult(true);
                }

                removeDownloadFile(context, download_id);
            }else {

                recordFirstLaunch(context, packageName, "install_fail_code_"+retCode);

                Log.e(TAG, "install error:"+retCode);

                if(installCallback != null){
                    installCallback.onResult(false);
                }
                installManual(mContext, uriString);
            }

            //release download id
            AlertDialogHelper.releaseDownload(download_id);
        }
    }

    public static void removeDownloadFile(Context context, long download_id) {

        try{
            //remove download file
            DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = dm.getUriForDownloadedFile(download_id);
            if (uri != null && !TextUtils.isEmpty(uri.toString())) {
                new File(URI.create(uri.toString())).delete();
            }
            dm.remove(download_id);

            Log.d(TAG, "remove the download file: "+uri);
        }catch (Exception ne){
            ne.printStackTrace();
        }
    }

    private static String build_in_package = "com.hunantv.imgo.activity,";
    public static void recordFirstLaunch(Context context, String packageName, String eventName)
    {
        if(XiaomiStatistics.initialed) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("package", packageName);
            BaseCardView.formartDeviceMap(map);


            //TODO
            //map.put("total_size", Util.convertToFormateSize100M(Utils.getSDAllSize()));
            //map.put("avail_size", Util.convertToFormateSize100M(Utils.getSDAvailaleSize()));

            MiStatInterface.recordCalculateEvent("app_launch", eventName, 1, map);
            Analytics.getInstance(context).getTracker("video_adevent").track(Actions.newEventAction(eventName));

            String record = iDataORM.getStringValue(context, "record_launch_package", build_in_package);
            if(record.contains(packageName)){
                iDataORM.addSetting(context, packageName, "1");
            }
        }
    }

    public static class PackageUNInstallObserver extends IPackageDeleteObserver.Stub{

        @Override
        public void packageDeleted(String packageName, int returnCode) throws RemoteException {

        }
    }

    public static void checkVerison(final Context context, final boolean force, final boolean showWhatsNew, final boolean showInfoInLatestVersion){
        checkVerison(context, force,showWhatsNew, showInfoInLatestVersion, true);
    }
    public static void checkVerison(final Context context, final boolean force, final boolean showWhatsNew, final boolean showInfoInLatestVersion, final boolean showDownloadNotify){
        String miui = WLReflect.getSystemProperties("ro.miui.ui.version.name");
        String version_str = iDataORM.getStringValue(context, iDataORM.version_type, iDataORM.version_stable);
        if(iDataORM.version_alpha.equals(version_str) && "V5".equals(miui)){
            if(showWhatsNew) {
                Toast.makeText(context, "V5 不支持内测版本升级， 麻烦升级你的手机到最新MIUI V6版本", Toast.LENGTH_SHORT).show();
            }

            //rollback
            iDataORM.addSettingSync(context, iDataORM.version_type, iDataORM.version_stable);
            iDataORM.application_type = iDataORM.version_stable;
            iDataORM.addSettingSync(context, iDataORM.enable_switch_server, "0");
            return;
        }

        if("V6".equals(miui) || true) {
            final String appversion = CommonBaseUrl.BaseURL + "meta/app?miui="+miui;
            final String appCheck = new CommonUrl(context).addCommonParams(appversion);
            long lastCheckTime = iDataORM.getLongValue(context, iDataORM.app_update_ignore_date, 0);
            if(System.currentTimeMillis() - lastCheckTime > 24*60*60*1000L || force) {

                checkVersionUpgradePolicy(context, new CanUpgradeVersion() {
                    @Override
                    public void onResult(boolean canUpgrade) {
                        if (canUpgrade || force) {
                            Response.Listener<AppVersion> listener = new Response.Listener<AppVersion>() {
                                @Override
                                public void onResponse(final AppVersion response) {
                                    PackageManager pm = context.getPackageManager();
                                    try {
                                        int versionCode = CommonBaseUrl.versionCode;
                                        if (versionCode < 0) {
                                            versionCode = pm.getPackageInfo(context.getPackageName(), 0).versionCode;
                                        }

                                        StringReader sr = new StringReader(response.recent_change());
                                        BufferedReader br = new BufferedReader(sr);
                                        String firstLine = br.readLine();
                                        sr.close();
                                        br.close();
                                        String release_notes = TextUtils.isEmpty(response.release_notes())?firstLine:response.release_notes();

                                        iDataORM.addSettingSync(context, "release_notes", release_notes);

                                        if (response.version_code() > versionCode) {
                                            Log.d(TAG, "add to download apk :" + appCheck);
                                            if(!showWhatsNew){
                                                BackgroundService.startVideoDownloadAPK(context, response.apk_url(), response.version_name(), response.released_by() + " @" + response.release_date(), showDownloadNotify);
                                            }
                                            else {
                                                AlertDialogHelper.showDialoadOrig(context, "应用更新",
                                                        response.recent_change() + "\nVersion Code:" +
                                                                response.version_code() + "\n" +
                                                                response.release_date() + "\n" +
                                                                response.released_by() + "\n",
                                                        "",
                                                        "更新",
                                                        new AlertDialogHelper.DialogCallBack() {
                                                            @Override
                                                            public void onPositivePressed() {
                                                                BackgroundService.startVideoDownloadAPK(context, response.apk_url(), response.version_name(), response.released_by() + " @" + response.release_date(), showDownloadNotify);
                                                            }

                                                            @Override
                                                            public void onNagtivePressed() {
                                                                iDataORM.addSetting(context, iDataORM.app_update_ignore_date, String.valueOf(System.currentTimeMillis()));
                                                            }
                                                        });
                                            }
                                        } else {
                                            //show dialog
                                            if (showInfoInLatestVersion) {
                                                AlertDialogHelper.showInfomationDiaload(context, "应用最新版本",
                                                        response.version_name() + "\n" +
                                                                response.recent_change() + "\n\nVersion Code:" +
                                                                response.version_code() + "\n" +
                                                                response.release_date() + "@" +
                                                                response.released_by() + "\n");

                                                //Toast.makeText(context, R.string.app_latest_version, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (Exception e) {
                                        try{
                                            if (response!= null) {
                                                BackgroundService.startVideoDownloadAPK(context, response.apk_url(), response.version_name(), response.released_by() + " @" + response.release_date(), showDownloadNotify);
                                            }
                                        }catch (Exception ne){ne.printStackTrace();}
                                        e.printStackTrace();
                                    }
                                }
                            };

                            Response.ErrorListener errorListener = new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            };

                            RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
                            BaseGsonLoader.GsonRequest<AppVersion> gsonRequest = new BaseGsonLoader.GsonRequest<AppVersion>(appCheck, new TypeToken<AppVersion>() {
                            }.getType(), null, listener, errorListener);
                            gsonRequest.setCacheNeed(context.getCacheDir() + "/app_version.cache");
                            gsonRequest.setRawURL(appversion);
                            gsonRequest.setShouldCache(true);
                            requestQueue.add(gsonRequest);
                        }
                    }
                });
            }
        }
    }

    public interface CanUpgradeVersion{
        void onResult(boolean canUpgrade);
    }

    private static void checkVersionUpgradePolicy(final Context context, final CanUpgradeVersion canUpgradeVersion ){

        if(canUpgradeVersion == null){
            Log.e(TAG, "why null object for CanUpgradeVersion");
            return;
        }

        //temp close client version check
        if(!iDataORM.getBooleanValue(context, "open_client_upgrade_check", false)){
            canUpgradeVersion.onResult(true);
            return;
        }

        //for mivideo alpha user, just let them go
        String version_str = iDataORM.getStringValue(context, iDataORM.version_type, iDataORM.version_stable);
        if(iDataORM.version_alpha.equals(version_str)){
            canUpgradeVersion.onResult(true);
            return;
        }

        //get app upgrade from github
        //https://raw.githubusercontent.com/AiAndroid/stream/master/tv/game/version_upgrade.json
        String version_url = iDataORM.getStringValue(context, iDataORM.version_upgrade_url, CommonBaseUrl.BaseURL + "meta/upgradepolicy");
        final String appUpgradeURL = new CommonUrl(context).addCommonParams(version_url);

        Response.Listener<AppVersion.VersionUpgradePolicy> listener = new Response.Listener<AppVersion.VersionUpgradePolicy>() {
            @Override
            public void onResponse(final AppVersion.VersionUpgradePolicy response) {
                Log.d(TAG, ""+response);

                boolean needCheckUpgrade = false;
                try {
                    String mImeiId = AccountUtils.getImeiId(context);

                    //tester account
                    if (!TextUtils.isEmpty(response.test_accounts)) {
                        if (response.test_accounts.contains(mImeiId)) {
                            canUpgradeVersion.onResult(true);
                            return;
                        }
                    }

                    //token
                    if(!TextUtils.isEmpty(response.token) && !TextUtils.isEmpty(iDataORM._op_value)){
                        if(response.token.equals(iDataORM._op_value)){
                            canUpgradeVersion.onResult(true);
                            return;
                        }
                    }

                    if (TextUtils.isEmpty(mImeiId)) {
                        if ("1".equals(response.upgrade_direct_noimie)) {
                            canUpgradeVersion.onResult(true);
                        }
                        return;
                    }

                    //to version + imei code
                    String to_version = response.to_version;
                    if(TextUtils.isEmpty(to_version)){
                        to_version = String.valueOf(CommonBaseUrl.versionCode);
                    }
                    int position = Math.abs((to_version.hashCode() + mImeiId.hashCode()) % 1000);


                    //check alpha
                    if (BuildV5.IS_ALPHA_BUILD) {
                        AppVersion.VersionUpgradePolicy.MIUI miui = response.miui.get(AppVersion.VersionUpgradePolicy.MIUI.alpha);
                        if (miui != null) {
                            String percent = miui.percent();
                            try {
                                int percentInt = Integer.parseInt(percent);
                                if (position <= percentInt) {
                                    needCheckUpgrade = true;
                                }
                            } catch (Exception ne) {
                            }
                        }
                    }

                    if (needCheckUpgrade) {
                        canUpgradeVersion.onResult(true);
                        return;
                    }

                    //check dev
                    if (BuildV5.IS_DEVELOPMENT_VERSION) {
                        AppVersion.VersionUpgradePolicy.MIUI miui = response.miui.get(AppVersion.VersionUpgradePolicy.MIUI.dev);
                        if (miui != null) {
                            String percent = miui.percent();
                            try {
                                int percentInt = Integer.parseInt(percent);
                                if (position <= percentInt) {
                                    needCheckUpgrade = true;
                                }
                            } catch (Exception ne) {
                            }
                        }
                    }
                    if (needCheckUpgrade) {
                        canUpgradeVersion.onResult(true);
                        return;
                    }

                    //check stable
                    if (BuildV5.IS_STABLE_VERSION) {
                        AppVersion.VersionUpgradePolicy.MIUI miui = response.miui.get(AppVersion.VersionUpgradePolicy.MIUI.stable);
                        if (miui != null) {
                            String percent = miui.percent();
                            try {
                                int percentInt = Integer.parseInt(percent);
                                if (position <= percentInt) {
                                    needCheckUpgrade = true;
                                }
                            } catch (Exception ne) {
                            }
                        }
                    }
                    if (needCheckUpgrade) {
                        if (canUpgradeVersion != null) {
                            canUpgradeVersion.onResult(true);
                        }
                        return;
                    }

                    //check excludes
                    String device = Build.MODEL.toLowerCase();
                    if (response.includes != null && response.includes.toLowerCase().contains(device)) {
                        needCheckUpgrade = true;
                    }

                    if (response.excludes != null && response.excludes.toLowerCase().contains(device)) {
                        needCheckUpgrade = false;
                        if (canUpgradeVersion != null) {
                            canUpgradeVersion.onResult(false);
                        }
                        return;
                    }

                    //check miui version v4, v5, v6, v7 ...
                    if(!TextUtils.isEmpty(response.include_miuis)) {
                        String miui = WLReflect.getSystemProperties("ro.miui.ui.version.name");
                        if (!TextUtils.isEmpty(miui)) {
                            if(!response.include_miuis.toLowerCase().contains(miui.toLowerCase())){

                                //not in defined miui v5, v6, v7
                                if (canUpgradeVersion != null) {
                                    canUpgradeVersion.onResult(false);
                                }
                                return;
                            }
                        }
                    }


                    //check miui version, v4, v5, v6, v7 ...
                    if(!TextUtils.isEmpty(response.exclude_miuis)) {
                        String miui = WLReflect.getSystemProperties("ro.miui.ui.version.name");
                        if (!TextUtils.isEmpty(miui)) {
                            if(response.exclude_miuis.toLowerCase().contains(miui.toLowerCase())){

                                //not in defined miui v5, v6, v7
                                if (canUpgradeVersion != null) {
                                    canUpgradeVersion.onResult(false);
                                }
                                return;
                            }
                        }
                    }

                    //check android version
                    int android_sdk = Build.VERSION.SDK_INT;
                    if(!TextUtils.isEmpty(response.min_android)) {

                        //min android
                        try {
                            int min_android = Integer.parseInt(response.min_android);
                            if(android_sdk<min_android){
                                //less than the android version
                                if (canUpgradeVersion != null) {
                                    canUpgradeVersion.onResult(false);
                                }
                                return;
                            }
                        }catch (Exception ne){}
                    }

                    //max android
                    if(!TextUtils.isEmpty(response.max_android)) {
                        try {
                            int max_android = Integer.parseInt(response.max_android);
                            if (android_sdk > max_android) {
                                //less than the android version
                                if (canUpgradeVersion != null) {
                                    canUpgradeVersion.onResult(false);
                                }
                                return;
                            }
                        } catch (Exception ne) {
                        }
                    }

                    //gray
                    String gray = response.gray;
                    try {
                        int grayInt = Integer.parseInt(gray);
                        if (position <= grayInt) {
                            needCheckUpgrade = true;
                        }
                    } catch (Exception ne) {
                    }
                }catch (Exception ne){
                    Log.d(TAG, ""+ne.getMessage());

                    needCheckUpgrade = true;
                }

                if(canUpgradeVersion != null){
                    canUpgradeVersion.onResult(needCheckUpgrade);
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(error.networkResponse != null && error.networkResponse.statusCode >= 400){
                    if(canUpgradeVersion != null){
                        canUpgradeVersion.onResult(true);
                    }
                }
            }
        };

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<AppVersion.VersionUpgradePolicy> gsonRequest = new BaseGsonLoader.GsonRequest<AppVersion.VersionUpgradePolicy>(appUpgradeURL, new TypeToken<AppVersion.VersionUpgradePolicy>() {
        }.getType(), null, listener, errorListener);
        gsonRequest.setCacheNeed(context.getCacheDir() + "/version_policy.cache");
        gsonRequest.setShouldCache(false);
        gsonRequest.setRetryPolicy(new DefaultRetryPolicy(DEFAULT_TIMEOUT_MS, IMAGE_MAX_RETRIES, IMAGE_BACKOFF_MULT));
        requestQueue.add(gsonRequest);
    }

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 2500;
    /** Default number of retries for image requests */
    public static final int IMAGE_MAX_RETRIES = 3;
    /** Default backoff multiplier for image requests */
    public static final float IMAGE_BACKOFF_MULT = 2f;
}
