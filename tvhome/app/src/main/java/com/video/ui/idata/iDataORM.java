package com.video.ui.idata;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.ComponentName;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.duokan.VolleyHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.TokenInfo;
import com.tv.ui.metro.model.VideoItem;
import com.tv.ui.metro.model.XiaomiStatistics;
import com.video.ui.loader.AppGson;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonBaseUrl;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * Created by tv metro on 7/7/14.
 *
 */
public class iDataORM {
    public static final String AUTHORITY                 = "com.mothership.video.mobile";
    public static final String AUTHORITY_MIBROWSER                 = "com.mothership.browser.video";
    public static final Uri SETTINGS_CONTENT_URI         = Uri.parse("content://" + AUTHORITY + "/settings");
    public static final Uri ALBUM_CONTENT_URI            = Uri.parse("content://" + AUTHORITY + "/local_album");
    public static final Uri DOWNLOAD_CONTENT_URI         = Uri.parse("content://" + AUTHORITY + "/download");
    public static final Uri DOWNLOAD_GROUP_CONTENT_URI   = Uri.parse("content://" + AUTHORITY + "/downloadgroup");
    public static final Uri SEARCH_CONTENT_URI           = Uri.parse("content://" + AUTHORITY + "/search");
    public static final Uri MIBROWSER_ALBUM_CONTENT_URI            = Uri.parse("content://" + AUTHORITY_MIBROWSER + "/history");

    private static  String TAG = "iDataORM";

    public static String FavorAction             = "favor";
    public static String HistoryAction           = "play_history";

    public static String Max_Show_Search         = "Max_Show_Search";
    public static String LOOP_FAVOR_SYNC         = "LOOP_FAVOR_SYNC";
    public static String LOOP_ADS_SYNC           = "LOOP_ADS_SYNC";
    public static String LOOP_CLOUD_SETTING_SYNC = "LOOP_CLOUD_SETTING_SYNC";
    public static String ACTIVATE_SYNC           = "ACTIVATE_SYNC";
    public static String KEY_PREFERENCE_SOURCE   = "prefer_source_cp";
    public static String gridview_ui             = "gridview_ui";

    public static final String KEY_ALERT_NETWORK  = "alert_network";
    public static final String open_customization = "open_customization";
	public static final String account_token      = "account_token";
    public static final String debug_mode               = "debug_mode";
    public static final String base_url                 = "base_url";
    public static final String max_search_hint_count    = "max_search_hint_count";
    public static final String enable_debug_cache       = "enable_debug_cache";
    public static final String latest_epg_fetch_date    = "latest_epg_fetch_date";
    public static final String loop_epg_keep_alive      = "loop_epg_keep_alive";
    public static final String device_id_md5            = "device_id_md5";
    public static final String device_id                = "device_id";
    public static final String app_update_ignore_date   = "app_update_ignore_date";
    public static final String JS_GET_URL               = "JS_GET_URL";
    public static final String use_volley_load_ads      = "use_volley_load_ads";
    public static final String show_first_ads           = "show_first_ads";
    public static final String startup_ads              = "startup_ads";
    public static final String startup_ads_loop         = "startup_ads_loop";
    public static final String not_move_task_to_back    = "not_move_task_to_back";
    public static final String _op                      = "_op";
    public static final String sys_init                 = "sys_init";
    public static final String  default_version         = "default_version";
    public static final String data_usage_hint_always_on = "data_usage_hint_always_on";
    public static final String running_type              = "running_type";
    public static final String good_picture_priority     = "good_picture_priority";
    public static final String activate_date             = "activate_date";
    public static final String activate_wait_days        = "activate_wait_days";
    public static final String  allow_everyone_join      = "allow_everyone_join";
    public static final String enable_switch_server      = "enable_switch_server";
    public static final String enable_router_feature     = "enable_router_feature";
    public static final String enable_crash_handler      = "enable_crash_handler";
    public static final String mi_router_versioncode     = "mi_router_versioncode";
    public static final String upgrade_mirouter_by_market= "upgrade_mirouter_by_market";
    public static final String enable_mothership_ads           = "enable_mothership_ads";
    public static final String cps_supported_download    = "downloadable_cps";
    public static final String banned_media_type         = "banned_media";
    public static final String enable_switch_source_on_ad = "enable_switch_source_on_ad";

    public static final String version_upgrade_url       = "version_upgrade_url";


    //key definition
    public static final String version_type             = "version_type";

    public static String device_id_inmem                = "";

    public static Boolean upgrade_mirouter_by_market_default = false;
    public static final boolean default_debug_mode = false;
    public static final boolean default_show_first_ads = true;
    public static final boolean default_data_usage_hint_always_on = false;


    public static int     performance_type       = -1;//performance priority, 1 memory priority
    public static int     picture_quality        = -1;

    public static final  int memory_priority    = 0;
    public static final  int performance_priority = 1;

    private static iDataORM _instance;
    private static HandlerThread ht;
    private static Handler       mBackHandler;

    private static int MAX_STORE_COUNT = 80;

    //memory data
    public static TokenInfo mTokenInfo;
    public static String    _op_value;
    public static String application_type="";

    public static String  version_stable  = "90";
    public static String  version_dev     = "10";
    public static String  version_alpha   = "00";
    public static String  version_default = "90";

    private static boolean is_show_image_indicator;
    public static boolean isShowIndicator(){
        return is_show_image_indicator;
    }

    public static void setShowIndicatorWithImage(boolean show){
        is_show_image_indicator = show;
    }

    public static iDataORM getInstance(Context con){
        if(_instance == null){
            _instance = new iDataORM(con);

            MAX_STORE_COUNT = 80;//_instance.getIntValue("max_storage_count", 80);
        }

        return _instance;
    }


    public static Handler getWorkThreadHandler(){
        if(mBackHandler == null){

            ht = new HandlerThread("idate_bg_thread");
            ht.start();

            mBackHandler = new Handler(ht.getLooper());
            MAX_STORE_COUNT = _instance.getIntValue("max_storage_count", 80);
            return new Handler();
        }

        return mBackHandler;
    }

    private Context mContext;
    private iDataORM(Context con){
        mContext = con.getApplicationContext();
    }

    public static String[]settingsProject =  new String[]{
            "_id",
            "name",
            "application",
            "value",
    };

    public static String[] actionProject =  new String[]{
            "_id",
            "res_id",
            "ns",
            "value",
            "action",
            "uploaded",
            "offset",
            "date_time",
            "date_int"
    };

    public static String[] downloadProject =  new String[]{
            "_id",
            "res_id",
            "ns",
            "value",

            "download_url",

            "sub_id",
            "sub_value",

            "uploaded",

            "date_time",
            "date_int",

            "download_status",
            "download_path",

            "totalsizebytes",
            "downloadbytes",

            " vendor_download_id",     //id in dex vendor
            " vendor_name",     // dex vendor name
            "download_id"
    };

    public static String[]searchProject =  new String[]{
            "_id",
            "key",
            "date_time",
            "date_int",
    };

    public static boolean enabledAds(Context context)
    {
        return (iDataORM.getBooleanValue(context, iDataORM.debug_mode, iDataORM.default_debug_mode) || iDataORM.getBooleanValue(context, iDataORM.show_first_ads, iDataORM.default_show_first_ads));
    }
//    public static boolean isPriorityStorage(Context context) {
//        return getBooleanValue(context, "priority_storage", true);
//    }

    public static boolean allowPlayByCellular(Context context) {
        return getBooleanValue(context, "play_by_cell_network", false);
    }

    public static boolean isOpenCellularOfflineHint(Context context) {
        return getBooleanValue(context, "cell_network_offline_hint", true);
    }

    public static boolean isMiPushOn(Context context) {
        return getBooleanValue(context, "mi_push_on", true);
    }

//    public static void setPriorityStorage(Context context, boolean priorityStorage) {
//        addSetting(context, "priority_storage", priorityStorage?"1":"0");
//    }

    public static void setAllowPlayByCellular(Context context, boolean checked) {
        addSetting(context, "play_by_cell_network", checked?"1":"0");
    }

    public static void setOpenCellularOfflineHint(Context context, boolean checked) {
        addSetting(context, "cell_network_offline_hint", checked ? "1" : "0");
    }

    public static void setMiPushOn(Context context, boolean miPushOn) {
        addSetting(context, "mi_push_on", miPushOn?"1":"0");
    }

    public static void setOpen_customizationOn(Context baseContext, boolean checked) {
        iDataORM.addSetting(baseContext, open_customization, checked?"1":"0");
    }

    public static boolean isOpen_customizationOn(Context context) {
        return iDataORM.getBooleanValue(context, open_customization, false);
    }

    static boolean oneTimeAcceptNetworkUsage = false;
    public static void setOneTimeAccept(boolean accept) {
        oneTimeAcceptNetworkUsage = accept;
    }
    public static boolean isAlertNetworkOn(Context context){
        return iDataORM.getBooleanValue(context, KEY_ALERT_NETWORK, true) && !oneTimeAcceptNetworkUsage;
    }

    public static void setAlertNetworkOn(Context context, boolean enable){
        iDataORM.addSetting(context, KEY_ALERT_NETWORK, enable?"1":"0");
    }

    public static class VendorDownload {
        public int                      id;
        public String                   res_id;
        public String                   sub_id;
        public String                   vendor_download_id;
        public String                   vendor_name;
        public String                   download_path;
        public int                      download_status;
        public long                     totalsizebytes;
        public long                     downloadbytes;
    }

    public static String[] vendor_download_project = new String [] {
            "_id",
            "res_id",
//            "ns",
//            "value",
//            "download_url",
            "sub_id",
//            "sub_value",
//            "uploaded",
//            "date_time",
//            "date_int",
            "download_status",
            "download_path",
            "totalsizebytes",
            "downloadbytes",
            "vendor_download_id",     //id in dex vendor
            "vendor_name",     // dex vendor name
            "download_id"
    };

    public static class PendingDownload{
        public int                       id;
        public String                    res_id;
        public String                    sub_id;
        public String                    sub_value;
        public String                    value;
        public String                    cp;

        public VideoItem                 video_obj;
        public DisplayItem.Media.CP      cp_obj;
        public DisplayItem.Media.Episode episode_obj;
    }

    public static String[] download_pending_Project =  new String[]{
            "_id",
            "res_id",
            "value",
            "cp",
            "sub_id",
            "sub_value",
            "date_time",
            "date_int"
    };

    public static List<Pair<String, Integer>> getDownloadableCps(Context context) {
        String downloadable_cps = "";

        if ((!TextUtils.isEmpty(CommonBaseUrl.getMIUIVersion()) &&  !"V5".equalsIgnoreCase(CommonBaseUrl.getMIUIVersion()))) {
            // V6 version
            downloadable_cps = getStringValue(context, cps_supported_download, "");
        } else {
            // V5 version
            downloadable_cps = getStringValue(context, "downloadable_cps_v5", "");
        }

//        downloadable_cps = "iqiyi20151111=88";

        List<Pair<String, Integer>> results = new ArrayList<Pair<String, Integer>>();

        if (TextUtils.isEmpty(downloadable_cps)) return results;

        // String should be like : "iqiyi=15&funshion=12&sohu=10&youku=9", number means rank, larger means higher
        String[] cps = downloadable_cps.split("&");

        for (String cp : cps) {
            try {
                String[] cp_factor = cp.split("=");
                String cpName = cp_factor[0];

                // hardcode to enable iqiyi offline in new app version
                if ("iqiyi20151111".equals(cpName)) {
                    cpName = "iqiyi";
                }

                Pair<String, Integer> cp_item = new Pair<String, Integer>(cpName, Integer.valueOf(cp_factor[1]));
                results.add(cp_item);
            } catch (Exception e) {
                continue;
            }
        }

        return results;
    }

    public static PendingDownload getNextPendingDownload(Context context, ArrayList<String> epids){
        PendingDownload item = null;
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, download_pending_Project, ColumsCol.DOWNLOAD_ID + "="+ DOWN_ID_PENDING, null, " date_int desc");
        if(cursor != null ){
            if(cursor.moveToFirst()) {
                do {
                    String sub_id = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
                    if(epids.contains(sub_id)) {
                        if(!cursor.moveToNext()){
                            break;
                        }

                        continue;
                    }

                    item = new PendingDownload();
                    item.sub_value = sub_id;
                    item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                    item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                    item.cp = cursor.getString(cursor.getColumnIndex(ColumsCol.CP));
                    item.value = cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE));
                    item.sub_value = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_VALUE));
                    break;
                }while (cursor.moveToNext());

            }
            cursor.close();
        }
        return item;
    }

    public static void updateVendorDownload(Context context, List<VendorDownload> downloads) {
        if (downloads == null || downloads.size() <= 0) return;

        for (VendorDownload download : downloads) {
            ContentValues values = new ContentValues();
            values.put(ColumsCol.DOWNLOAD_STATUS, download.download_status);
            values.put(ColumsCol.DOWNLOADED_SIZE, download.downloadbytes);
            values.put(ColumsCol.TOTAL_SIZE, download.totalsizebytes);

            String where = ColumsCol.VENDOR_NAME + "=? and " + ColumsCol.VENDOR_DOWNLOAD_ID + "=?";
            context.getContentResolver().update(DOWNLOAD_CONTENT_URI, values, where, new String[]{download.vendor_name, download.vendor_download_id});
        }
    }

    public static void removeVendorDownload(Context context, List<VendorDownload> downloads) {
        if (downloads == null || downloads.size() <= 0) return;

        for (VendorDownload download : downloads) {
            String where = ColumsCol.VENDOR_NAME + "=? and " + ColumsCol.VENDOR_DOWNLOAD_ID + "=?";
            context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, new String[]{download.vendor_name, download.vendor_download_id});
        }
    }

    public static List<VendorDownload> getAllVendorDownload(Context context) {
        String where =  ColumsCol.DOWNLOAD_ID + "=?";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, vendor_download_project, where, new String[] {String.valueOf(DOWN_ID_DEX_VENDOR)}, " date_int desc");

        List<VendorDownload> result = new ArrayList<VendorDownload>();

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    VendorDownload item = new VendorDownload();
                    item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                    item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                    item.sub_id = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
                    item.download_status = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_STATUS));
                    item.vendor_download_id = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_DOWNLOAD_ID));
                    item.vendor_name = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_NAME));
                    item.downloadbytes = cursor.getLong(cursor.getColumnIndex(ColumsCol.DOWNLOADED_SIZE));
                    item.totalsizebytes = cursor.getLong(cursor.getColumnIndex(ColumsCol.TOTAL_SIZE));
                    result.add(item);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }
        return result;
    }

    public static Cursor getAllUncompletedDownloads(Context context) {
        String where =  ColumsCol.DOWNLOAD_STATUS + "!=" + DOWNLOAD_STATUS_FINISHED;
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, vendor_download_project, where, null, " date_int desc");

        return cursor;
    }

    public static Cursor getDownloadByVendorNameAndDownloadId(Context context, String vendorName, String downloadId) {
        String where =  ColumsCol.VENDOR_DOWNLOAD_ID + "=? and " + ColumsCol.VENDOR_NAME + "=?";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, where, new String[]{downloadId, vendorName}, " date_int desc");

        return cursor;
    }

    public static void syncDMInfoToLocalDB(final Context context, Cursor cursor) {
        List<VendorDownload> dataInLocalDB = getSysDMDownloads(context);
        HashMap<String, String> removedDownloadId = new HashMap<String, String>();
        if (dataInLocalDB != null && dataInLocalDB.size() >0) {
            for (VendorDownload download : dataInLocalDB) {
                removedDownloadId.put(download.vendor_download_id, download.vendor_download_id);
            }
        }

        List<ContentValues> valuesList = new ArrayList<ContentValues>();

        if( cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ContentValues values = new ContentValues();
                String vendorDownloadId = cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));
                values.put(ColumsCol.VENDOR_DOWNLOAD_ID, vendorDownloadId);
                values.put(ColumsCol.VENDOR_NAME, "system");
                values.put(ColumsCol.DOWNLOADED_SIZE, cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)));
                values.put(ColumsCol.TOTAL_SIZE, cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)));
                values.put(ColumsCol.DOWNLOAD_ID, Integer.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_ID))));
                values.put(ColumsCol.DOWNLOAD_PATH, cursor.getString(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI)));
                removedDownloadId.remove(vendorDownloadId);

                int _stat = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                int status = -1;
                switch (_stat) {
                    case DownloadManager.STATUS_PENDING:
                    case DownloadManager.STATUS_RUNNING:
                        status = iDataORM.DOWNLOAD_STATUS_RUNNING;
                        break;

                    case DownloadManager.STATUS_PAUSED:
                        status = iDataORM.DOWNLOAD_STATUS_PAUSE;
                        break;

                    case DownloadManager.STATUS_SUCCESSFUL:
                        status = iDataORM.DOWNLOAD_STATUS_FINISHED;
                        break;

                    case DownloadManager.STATUS_FAILED:
                        status = iDataORM.DOWNLOAD_STATUS_FAILED;
                        break;

                    default:
                        Log.e(TAG, "Wrong download status from DM");
                }

                values.put(ColumsCol.DOWNLOAD_STATUS, status);
                valuesList.add(values);
                cursor.moveToNext();
            }
        }

        if (valuesList.size() > 0) {
            iDataORM.batchUpdateDownloads(context, valuesList);
        }

        if (removedDownloadId.size() > 0) {
            List<Pair<String, String>> toRemoveId = new ArrayList<Pair<String, String>>();
            for (String id : removedDownloadId.keySet()) {
                toRemoveId.add(new Pair<String, String>("system", id));
            }
            iDataORM.removeDownloadsInLocalDb(context, toRemoveId);
        }
    }

    public static List<VendorDownload> getSysDMDownloads(Context context) {
        String where = ColumsCol.DOWNLOAD_ID + "!=? and " + ColumsCol.DOWNLOAD_ID + "!=? and " + ColumsCol.VENDOR_NAME + "=\'system\'";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, vendor_download_project, where, new String[] {String.valueOf(DOWN_ID_PENDING), String.valueOf(DOWN_ID_RESTORE)}, " date_int desc");

        List<VendorDownload> result = new ArrayList<VendorDownload>();

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    VendorDownload item = new VendorDownload();
                    item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                    item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                    item.sub_id = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
                    item.download_status = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_STATUS));
                    item.vendor_download_id = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_DOWNLOAD_ID));
                    item.vendor_name = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_NAME));
                    result.add(item);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return result;

    }
    public static List<VendorDownload> getVendorDownloadByVendorName(Context context, String vendorName) {
        String where =  ColumsCol.DOWNLOAD_ID + "=? and " + ColumsCol.VENDOR_NAME + "=?";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, vendor_download_project, where, new String[]{String.valueOf(DOWN_ID_DEX_VENDOR), vendorName}, " date_int desc");

        List<VendorDownload> result = new ArrayList<VendorDownload>();

        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                while (!cursor.isAfterLast()) {
                    VendorDownload item = new VendorDownload();
                    item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                    item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                    item.sub_id = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
                    item.download_status = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_STATUS));
                    item.vendor_download_id = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_DOWNLOAD_ID));
                    item.vendor_name = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_NAME));
                    result.add(item);
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return result;
    }

    public static PendingDownload getPendingDownload(Context context, String episode_id){
        PendingDownload item = new PendingDownload();
        String where =  ColumsCol.DOWNLOAD_ID + "=? and " + ColumsCol.SUB_ID + "=?";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, download_pending_Project, where, new String[]{String.valueOf(DOWN_ID_PENDING), episode_id}, " date_int desc");
        if(cursor != null ){
            if(cursor.moveToFirst()) {
                item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                item.cp = cursor.getString(cursor.getColumnIndex(ColumsCol.CP));
                item.value = cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE));
                item.sub_id = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
                item.sub_value = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_VALUE));

            }
            cursor.close();
        }
        return item;
    }
    public static boolean existInPendingTask(Context context, String episode_id){
        boolean exist = false;
        String where = ColumsCol.SUB_ID + " =?" ;
        Cursor cursor;
        /*
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_PENDING_CONTENT_URI, new String[]{ColumsCol.ID}, where, new String[]{episode_id}, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
        }*/

        if(!exist){
            cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID}, where, new String[]{episode_id}, null);
            if(cursor != null ){
                if(cursor.getCount() > 0){
                    exist = true;
                }
                cursor.close();
            }
        }
        return exist;
    }

    public static final int DOWN_ID_RESTORE = -100;
    public static final int DOWN_ID_PENDING = -200;
    public static final int DOWN_ID_DEX_VENDOR = -300;

    public static void addPendingDownloadTask(final Context context, final VideoItem item, final DisplayItem.Media.CP cp, final DisplayItem.Media.Episode episode) {
        addDownload(context, item.target.url, DOWN_ID_PENDING, "", item, episode, cp, "", "", "");
    }

    public static class ColumsCol {
        public static final String ID         = "_id";
        public static final String RES_ID     = "res_id";
        public static final String NS         = "ns";
        public static final String VALUE      = "value";
        public static final String KEY        = "key";
        public static final String Action     = "action";
        public static final String Uploaded   = "uploaded";
        public static final String ChangeDate = "date_time";
        public static final String ChangeLong = "date_int";

        public static final String SUB_ID           = "sub_id";
        public static final String SUB_VALUE        = "sub_value";
        public static final String CP               = "cp";
        public static final String DOWNLOAD_ID      = "download_id";
        public static final String DOWNLOAD_STATUS  = "download_status";
        public static final String DOWNLOAD_PATH    = "download_path";
        public static final String DOWNLOAD_URL     = "download_url";
        public static final String OFFSET           = "offset";

        public static final String TOTAL_SIZE       = "totalsizebytes";
        public static final String DOWNLOADED_SIZE  = "downloadbytes";

        public static final String VENDOR_DOWNLOAD_ID = "vendor_download_id";     //id in dex vendor
        public static final String VENDOR_NAME = "vendor_name";     // dex vendor name
    }

    public static class ActionRecord<T>{
        public int    id;
        public String res_id;
        public String ns;
        public String json;
        public String action;
        public int    uploaded;
        public Object object;
        public String date;
        public long   dateInt;

        //just for download
        public int    download_id;
        public String download_url;
        public String download_path;
        public int    download_status;
        public String download_vendor_name;
        public String vendor_download_id;
        public String sub_id;
        public String sub_value;
        public String cp;
        public int    offset;//just for play history
        public long   downloadbytes;
        public long   totalsizebytes;

        public static <T> T parseJson(Gson gson, String json, Type type){
            return gson.fromJson(json, type);
        }
    }

    static Gson gson = AppGson.get();

    public static void addFavor(final Context context, final ArrayList<DisplayItem>alls, final boolean uploaded){
        if(alls == null)
            return;

        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                for (DisplayItem item : alls) {
                    if (existFavor(context, "video", FavorAction, item.id)) {
                        ops.add(ContentProviderOperation.newUpdate(ALBUM_CONTENT_URI).withSelection(ColumsCol.RES_ID + " LIKE ? and " + ColumsCol.Action + "=?", new String[]{"%" + item.id, FavorAction}).withValue(ColumsCol.Uploaded, uploaded ? 1 : 0).build());
                    } else {
                        ContentValues ct = new ContentValues();
                        ct.put(ColumsCol.RES_ID, getVideoID(item.id));
                        ct.put(ColumsCol.NS, "video");
                        ct.put(ColumsCol.VALUE, gson.toJson(item));
                        ct.put(ColumsCol.Action, FavorAction);
                        ct.put(ColumsCol.Uploaded, uploaded);
                        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
                        ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());
                        ops.add(ContentProviderOperation.newInsert(ALBUM_CONTENT_URI).withValues(ct).build());
                    }
                }

                try {
                    context.getContentResolver().applyBatch(AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void addFavor(final Context context, final ArrayList<DisplayItem>alls, final boolean uploaded, final long recordDate){
        if(alls == null)
            return;

        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                for (DisplayItem item : alls) {
                    if (existFavor(context, "video", FavorAction, item.id)) {
                        ops.add(ContentProviderOperation.newUpdate(ALBUM_CONTENT_URI).withSelection(ColumsCol.RES_ID + " LIKE ? and " + ColumsCol.Action + "=?", new String[]{"%" + getVideoID(item.id), FavorAction}).withValue(ColumsCol.Uploaded, uploaded ? 1 : 0).build());
                    } else {
                        ContentValues ct = new ContentValues();
                        ct.put(ColumsCol.RES_ID, getVideoID(item.id));
                        ct.put(ColumsCol.NS, "video");
                        ct.put(ColumsCol.VALUE, gson.toJson(item));
                        ct.put(ColumsCol.Action, FavorAction);
                        ct.put(ColumsCol.Uploaded, uploaded);
                        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
                        ct.put(ColumsCol.ChangeLong, String.valueOf(recordDate));
                        ops.add(ContentProviderOperation.newInsert(ALBUM_CONTENT_URI).withValues(ct).build());
                    }
                }

                try {
                    context.getContentResolver().applyBatch(AUTHORITY, ops);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public static void addFavor(Context context, String ns, String action,String res_id,  DisplayItem item){
        res_id = getVideoID(res_id);
        addFavor(context, ns, action, res_id, gson.toJson(item));
    }

    public static void addFavorSync(Context context, String ns, String action,String res_id,  DisplayItem item){
        res_id = getVideoID(res_id);
        addFavorSync(context, ns, action, res_id, gson.toJson(item));
    }

    public static void addFavor(final Context context, final String ns, final String action,final String res_id,  final String json){
        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {

                String where = ColumsCol.NS + "='" + ns + "' and action='" + action + "'";
                Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, " date_int asc");
                if (cursor != null) {
                    if (cursor.getCount() >= MAX_STORE_COUNT && cursor.moveToFirst()) {
                        int step = 0;
                        StringBuilder sb = new StringBuilder();
                        sb.append(ColumsCol.ID);
                        sb.append(" in (");
                        do {
                            if (step > 0) {
                                sb.append(",");
                            }
                            sb.append(cursor.getInt(cursor.getColumnIndex(ColumsCol.ID)));

                            step++;
                        } while (cursor.moveToNext() && step <= 10);
                        sb.append(" )");

                        int lens = context.getContentResolver().delete(ALBUM_CONTENT_URI, sb.toString(), null);
                        Log.d(TAG, "remove latest 10 items:" + lens);
                    }
                    cursor.close();
                }

                ContentValues ct = new ContentValues();
                ct.put(ColumsCol.RES_ID, getVideoID(res_id));
                ct.put(ColumsCol.NS, ns);
                ct.put(ColumsCol.VALUE, json);
                ct.put(ColumsCol.Action, action);
                ct.put(ColumsCol.Uploaded, 0);
                ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
                ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());
                //if exist, update
                if (existFavor(context, ns, action, res_id)) {
                    updateFavor(context, ct);
                } else {
                    context.getContentResolver().insert(ALBUM_CONTENT_URI, ct);
                }
            }
        });

    }

    public static void addFavorToBrowser(final Context context, final String url, final String title, final String subtitle, final String currenttime, final String duration){
        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                addFavorToBrowserSync(context, url, title, subtitle, currenttime, duration);
            }
        });
    }

    public static void addFavorSync(final Context context, final String ns, final String action,final String res_id,  final String json){
        String where = ColumsCol.NS + "='" + ns + "' and action='" + action + "'";
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, " date_int asc");
        if (cursor != null) {
            if (cursor.getCount() >= MAX_STORE_COUNT && cursor.moveToFirst()) {
                int step = 0;
                StringBuilder sb = new StringBuilder();
                sb.append(ColumsCol.ID);
                sb.append(" in (");
                do {
                    if (step > 0) {
                        sb.append(",");
                    }
                    sb.append(cursor.getInt(cursor.getColumnIndex(ColumsCol.ID)));

                    step++;
                } while (cursor.moveToNext() && step <= 10);
                sb.append(" )");

                int lens = context.getContentResolver().delete(ALBUM_CONTENT_URI, sb.toString(), null);
                Log.d(TAG, "remove latest 10 items:" + lens);
            }
            cursor.close();
        }

        ContentValues ct = new ContentValues();
        ct.put(ColumsCol.RES_ID, getVideoID(res_id));
        ct.put(ColumsCol.NS, ns);
        ct.put(ColumsCol.VALUE, json);
        ct.put(ColumsCol.Action, action);
        ct.put(ColumsCol.Uploaded, 0);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
        ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());
        //if exist, update
        if (existFavor(context, ns, action, res_id)) {
            updateFavor(context, ct);
        } else {
            context.getContentResolver().insert(ALBUM_CONTENT_URI, ct);
        }

    }

    public static void addFavorToBrowserSync(final Context context, final String url, final String title, final String subtitle, final String currenttime, final String duration){
        if(TextUtils.isEmpty(url) || TextUtils.isEmpty(title)){
            return;
        }

        ContentValues ct = new ContentValues();
        ct.put("url", url);
        ct.put("title", title);
        if (!TextUtils.isEmpty(subtitle)){
            ct.put("subtitle", subtitle);
        }

        if (!TextUtils.isEmpty(currenttime)){
            ct.put("currenttime", currenttime);
        }

        if (!TextUtils.isEmpty(duration)){
            ct.put("duration", duration);
        }
        try {
            context.getPackageManager().getProviderInfo(new ComponentName("com.android.browser", "com.android.browser.provider.VideoProvider"), PackageManager.GET_META_DATA);
            context.getContentResolver().insert(MIBROWSER_ALBUM_CONTENT_URI, ct);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static boolean updateFavor(final Context context, final ContentValues ct) {
        boolean ret = false;
        String where = " ns = ? and res_id LIKE ? and action=?";//, ct.get(ColumsCol.NS), ct.get(ColumsCol.RES_ID), ct.get(ColumsCol.Action));
        if(context.getContentResolver().update(ALBUM_CONTENT_URI, ct, where, new String[]{(String) ct.get(ColumsCol.NS), "%"+getVideoID((String) ct.get(ColumsCol.RES_ID)), (String) ct.get(ColumsCol.Action)}) > 0){
            ret = true;
        }
        return ret;
    }

    public static boolean existFavor(Context context, String ns, String action, String res_id){
        res_id = getVideoID(res_id);
        boolean exist = false;
        String where = ColumsCol.NS +"=? and " + ColumsCol.RES_ID + " LIKE ? and action=?";
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, new String[]{"_id"}, where, new String[]{ns, "%" + getVideoID(res_id),action}, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
        }
        return exist;
    }

    public static int removeFavor(Context context, String ns, String action, String res_id){
        res_id = getVideoID(res_id);
        String where = ColumsCol.NS +"=? and " + ColumsCol.RES_ID + " LIKE ? and action=?";
        return context.getContentResolver().delete(ALBUM_CONTENT_URI, where, new String[]{ns, "%"+getVideoID(res_id), action});
    }

    public static int getFavoritesCount(Context context, String ns, String action){
        int count = 0;
        String where = ColumsCol.NS +"='"+ns + "' and action='" + action + "'";
        if (context != null){
            Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, new String[]{"_id"}, where, null, null);
            if(cursor != null ){
                count = cursor.getCount();
                cursor.close();
            }
        }
        return count;
    }

    public static ArrayList<ActionRecord> getFavorites(Context context, String ns, String action, int before_date){
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
        String where = ColumsCol.NS +"='"+ns + "' and action='" + action + "' and date_int >= "+before_date;
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, actionProject, where, null, " date_int desc");
        if(cursor != null ){
            if(cursor.moveToFirst()) {
                do {
                    ActionRecord item = new ActionRecord();
                    item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                    item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                    item.ns = cursor.getString(cursor.getColumnIndex(ColumsCol.NS));
                    item.json = cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE));
                    item.action = cursor.getString(cursor.getColumnIndex(ColumsCol.Action));
                    item.uploaded = cursor.getInt(cursor.getColumnIndex(ColumsCol.Uploaded));
                    item.date = cursor.getString(cursor.getColumnIndex(ColumsCol.ChangeDate));
                    item.dateInt = cursor.getLong(cursor.getColumnIndex(ColumsCol.ChangeLong));
                    item.offset  = cursor.getInt(cursor.getColumnIndex(ColumsCol.OFFSET));

                    actionRecords.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return actionRecords;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void FavorSynced(Context context, String ids){

        ArrayList<ContentProviderOperation>ops = new ArrayList<ContentProviderOperation>();
        String[] arr = ids.split(",");
        for(String res_id:arr) {
            ops.add(ContentProviderOperation.newUpdate(ALBUM_CONTENT_URI).withSelection(ColumsCol.RES_ID + " LIKE ? and " + ColumsCol.Action + "=?", new String[]{"%" + getVideoID(res_id), FavorAction}).withValue(ColumsCol.Uploaded, 1).build());
        }
        try {
            context.getContentResolver().applyBatch(AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    public static void  removeFavorIDS(Context context, String ids){
        ArrayList<ContentProviderOperation>ops = new ArrayList<ContentProviderOperation>();
        String[] arr = ids.split(",");
        for(String res_id:arr) {
            ops.add(ContentProviderOperation.newDelete(ALBUM_CONTENT_URI).withSelection(ColumsCol.RES_ID + " LIKE ? and " + ColumsCol.Action + "=?", new String[]{"%" + getVideoID(res_id), FavorAction}).build());
        }
        try {
            context.getContentResolver().applyBatch(AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONObject getFavoritesIDS(Context context, String ns){
        JSONObject data = new JSONObject();
        String where = ColumsCol.NS +"='"+ns + "' and action='" + FavorAction + "' and " + ColumsCol.Uploaded + " = 0";
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.RES_ID, ColumsCol.ChangeLong}, where, null, " date_int desc");

        if(cursor != null ){
            if(cursor.moveToFirst()) {
                do {
                    String res_id    = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));
                    int    date_long = cursor.getInt(cursor.getColumnIndex(ColumsCol.ChangeLong));
                    try {
                        data.put(getVideoID(res_id), date_long);
                    } catch (JSONException e) {}
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return data;
    }

    public static String getVideoID(String id){
        return id.substring(id.indexOf('/', 0) + 1);
    }

    public static Cursor getFavoritesCusor(Context context, String ns, String action, int before_date){
        String where = ColumsCol.NS +"='"+ns + "' and action='" + action + "' and date_int >= "+before_date;
        return context.getContentResolver().query(ALBUM_CONTENT_URI, actionProject, where, null, " date_int desc");
    }

    public static ArrayList<ActionRecord> getFavorites(Context context, String ns, String action){
        return getFavorites(context, ns, action, 0);
    }

    public static DisplayItem  getFavorite(Context context, String ns, String action, String res_id){
        res_id = getVideoID(res_id);
        DisplayItem item = null;
        String where = ColumsCol.NS +"=? and "+ColumsCol.Action + "=?  and "+ColumsCol.RES_ID + " LIKE ?";
        Cursor cursor = context.getContentResolver().query(ALBUM_CONTENT_URI, actionProject, where, new String[]{ns, action, "%"+res_id}, " date_int desc");
        if(cursor != null ){
            if(cursor.moveToFirst()) {
                item =  AppGson.get().fromJson(cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE)), DisplayItem.class);
            }
            cursor.close();
        }
        return item;
    }

    public static String formartEpisodeName(Context context, String title, int episode){
        return title + episode;
    }


    public static void  updatePlayStats(final Context context, final String res_id, final String ep_id, final Bundle map){
        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                final String new_id;
                if (TextUtils.isEmpty(res_id)) {
                    new_id = ep_id;
                } else {
                    new_id = getVideoID(res_id);
                }

                DisplayItem item = getFavorite(context, "video", HistoryAction, new_id);
                if (item != null) {

                    if (item.media != null && item.media.items != null && item.media.items.size() > 1) {
                        int index = 1;
                        for (DisplayItem.Media.Episode episode : item.media.items) {
                            if (episode.id.equals(ep_id)) {
                                break;
                            }
                            index++;
                        }
                        item.title = formartEpisodeName(context, item.media.name, index);
                    }
                    addVideoPlaylClick(item);
                } else {
                    fetchVideoItemWithout(context, new_id, ep_id, map);
                }
            }
        });
    }

    public static void  updatePlayHistory(final Context context, final String res_id, final String ep_id){
        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                final String new_id;
                if (TextUtils.isEmpty(res_id)) {
                    new_id = ep_id;
                } else {
                    new_id = getVideoID(res_id);
                }

                DisplayItem item = getFavorite(context, "video", HistoryAction, new_id);
                if (item != null) {
                    if (item.settings == null) {
                        item.settings = new DisplayItem.Settings();
                    }
                    item.settings.put(DisplayItem.Settings.play_id, ep_id);
                    if (item.media != null && item.media.items != null && item.media.items.size() > 1) {
                        int index = 1;
                        for (DisplayItem.Media.Episode episode : item.media.items) {
                            if (episode.id.equals(ep_id)) {
                                break;
                            }
                            index++;
                        }

                        item.title = formartEpisodeName(context, item.media.name, index);
                    }
                    addFavor(context, "video", HistoryAction, item.id, item);

                } else {
                    fetchVideoItemWithout(context, new_id, ep_id, null);
                }
            }
        });
    }

    public static void addVideoPlaylClick(DisplayItem item){
        addStatisticsClick(item, XiaomiStatistics.episode_play);
    }

    public static void addStatisticsClick(DisplayItem item, String event){
        if (XiaomiStatistics.initialed && item != null) {
            try {
                HashMap<String, String> map = new HashMap<String, String>();

                formartShowInfo(item, map);
                formartDeviceMap(map);

                if (item.target != null && !TextUtils.isEmpty(item.target.entity))
                    MiStatInterface.recordCalculateEvent(event, item.target.entity, 1, map);
                else {
                    MiStatInterface.recordCalculateEvent(event, "unknown", 1, map);
                }
            }catch (Exception ne){
                ne.printStackTrace();
            }
        }
    }

    public static void formartShowInfo(DisplayItem item, HashMap<String, String>map){
        map.put("id-title", item.id + "--" + item.title);
        map.put("id", item.id);
        if (item.media != null && !TextUtils.isEmpty(item.media.category)){
            map.put("cate", item.media.category);
            map.put("cate-title-id", item.media.category + "-" + item.title + "-"+item.id);

            if(item.media.cps != null && item.media.cps.size() > 0){
                map.put("cp", item.media.cps.get(0).cp());
            }
        }
    }

    public static void formartDeviceMap(HashMap<String, String> map){
        map.put("mothership",                CommonBaseUrl.getMIUIVersion());
        map.put("version",             String.valueOf(CommonBaseUrl.versionCode));
        map.put("version.incremental", Build.VERSION.INCREMENTAL);
        map.put("version.release",     Build.VERSION.RELEASE);
        map.put("device", Build.MODEL);
    }

    public static void fetchVideoItemWithout(final Context context, final String id, final String ep_id, final Bundle map){

        Response.Listener<DisplayItem.Media> listener = new Response.Listener<DisplayItem.Media>() {
            @Override
            public void onResponse(DisplayItem.Media response) {
                VideoItem videoItem = new VideoItem();

                videoItem.media = response;
                videoItem.title = response.name;
                videoItem.id    = id;
                videoItem.target.entity = "play_stats";

                if (videoItem.settings == null) {
                    videoItem.settings = new DisplayItem.Settings();
                }
                videoItem.settings.put(DisplayItem.Settings.play_id, ep_id);
                if(videoItem.media != null && videoItem.media.items != null && videoItem.media.items.size() > 1) {
                    int index = 1;
                    for (DisplayItem.Media.Episode episode : videoItem.media.items) {
                        if (episode.id.equals(ep_id)) {
                            break;
                        }
                        index++;
                    }

                    videoItem.title = formartEpisodeName(context, videoItem.media.name, index);
                }


                if(map == null) {
                    iDataORM.addFavor(context, "video", iDataORM.HistoryAction, videoItem.id, videoItem);
                }

                if(map != null && map.getBoolean("new_ep_play", false)) {
                    addVideoPlaylClick(videoItem);
                }

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {}
        };

        fetchVideoMedia(context, id, listener, errorListener);
    }

    public static void fetchVideoMedia(final Context context, String id, Response.Listener<DisplayItem.Media> listener, Response.ErrorListener errorListener){

        String url = CommonBaseUrl.BaseURL + "data/video?id=" + getVideoID(id) + "&none_ui=1";
        String calledURL = new CommonBaseUrl(context) {
            @Override
            protected void getBaseURLFromLoacalSetting() {}
        }.addCommonParams(url);

        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<DisplayItem.Media> gsonRequest = new BaseGsonLoader.GsonRequest<DisplayItem.Media>(calledURL, new TypeToken<DisplayItem.Media>(){}.getType(), null, listener, errorListener);
        gsonRequest.setShouldCache(false);
        requestQueue.add(gsonRequest);
    }

    /*
    * download begin
    */

//    public static int addDownload(final Context context, final String res_id, final long download_id, final String download_url, final DisplayItem item, final DisplayItem.Media.Episode episode){
//        return addDownload(context, res_id, download_id, download_url, item, episode, null, "", "");
//    }

    public static int addDownload(final Context context, final String res_id, final long download_id, final String download_url, final DisplayItem item, final DisplayItem.Media.Episode episode, final DisplayItem.Media.CP cp, final String vendorName, final String vendorDownloadId, final String localPath){
        try {
            String json = gson.toJson(item, VideoItem.class);
            ContentValues ct = new ContentValues();
            ct.put(ColumsCol.RES_ID,    getVideoID(res_id));
            ct.put(ColumsCol.VALUE,     json);
            ct.put(ColumsCol.SUB_ID,    episode.id);
            ct.put(ColumsCol.SUB_VALUE, gson.toJson(episode, DisplayItem.Media.Episode.class));
            ct.put(ColumsCol.NS, "video"); //TODO need add ns to download apk
            ct.put(ColumsCol.DOWNLOAD_URL, download_url);
            ct.put(ColumsCol.DOWNLOAD_ID, download_id);
            if (download_id == DOWN_ID_RESTORE) {
                ct.put(ColumsCol.DOWNLOAD_STATUS, DOWNLOAD_STATUS_FINISHED);
            }
            ct.put(ColumsCol.Uploaded, 0);
            ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
            ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());

            if (!TextUtils.isEmpty(vendorName)) {
                ct.put(ColumsCol.VENDOR_NAME, vendorName);
            }
            if (!TextUtils.isEmpty(vendorDownloadId)) {
                ct.put(ColumsCol.VENDOR_DOWNLOAD_ID, vendorDownloadId);
            }
            if (!TextUtils.isEmpty(localPath)) {
                ct.put(ColumsCol.DOWNLOAD_PATH, localPath);
            }

            if(cp != null){
                ct.put(ColumsCol.CP,         gson.toJson(cp, DisplayItem.Media.CP.class));
            }
            //if exist, update
            if (existDownload(context, episode.id)) {
                if(download_id != DOWN_ID_RESTORE) {
                    updateDownload(context, ct);
                    Log.d(TAG, "addDownload updateDownload"+item);
                }
            } else {
                context.getContentResolver().insert(DOWNLOAD_CONTENT_URI, ct);
                Log.d(TAG, "addDownload:" + item);
            }

            return  1;
        }catch (Exception ne){
            Log.e(TAG, "addDownload: "+ne);
            ne.printStackTrace();

            return  0;
        }
    }

    public static boolean touchPendingDownloadTask(Context context, String sub_id) {
        ContentValues values = new ContentValues();
        values.put(SettingsCol.ChangeDate, dateToString(new Date()));
        values.put(ColumsCol.ChangeLong, System.currentTimeMillis());

        boolean ret = false;
        String where = ColumsCol.SUB_ID+ "=? and " + ColumsCol.DOWNLOAD_ID + "=?";
        if(context.getContentResolver().update(DOWNLOAD_CONTENT_URI, values, where, new String[]{sub_id, String.valueOf(DOWN_ID_PENDING)}) > 0){
            ret = true;
        }
        return ret;
    }

    public static final int DOWNLOAD_STATUS_FAILED = 703;
    public static final int DOWNLOAD_STATUS_PAUSE = 702;
    public static final int DOWNLOAD_STATUS_FINISHED = 1;
    public static final int DOWNLOAD_STATUS_RUNNING = 0;

//    public static void downloadFinished(final Context context, int download_id){
//        final ActionRecord ar = getDowndloadByDID(context, download_id);
//        if(ar != null) {
//            getWorkThreadHandler().post(new Runnable() {
//                @Override
//                public void run() {
//                    ar.download_status = DOWNLOAD_STATUS_FINISHED;
//                    updateDownload(context, actionRecordToContentValues(ar));
//
//                }
//            });
//        }
//    }

//    public static void downloadFinished(final Context context, final ArrayList<Integer> ids){
//        ArrayList<ContentProviderOperation>ops = new ArrayList<ContentProviderOperation>();
//        for(Integer down_id:ids) {
//            ops.add(ContentProviderOperation.newUpdate(DOWNLOAD_CONTENT_URI).withSelection(ColumsCol.DOWNLOAD_ID + "=? ", new String[]{String.valueOf(down_id)}).withValue(ColumsCol.DOWNLOAD_STATUS, DOWNLOAD_STATUS_FINISHED).build());
//        }
//        try {
//            ContentProviderResult[] res = context.getContentResolver().applyBatch(AUTHORITY, ops);
//            if(Constants.DEBUG) {
//                Log.d(TAG, "downloadFinished: ids:" + ids);
//                Log.d(TAG, ""+res);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        //remove
//    }

    public static void updateVendorDownloadId(Context context, String oldId, String newId, String vendorName) {
        if (TextUtils.isEmpty(oldId) || TextUtils.isEmpty(newId)) return;

        String where = ColumsCol.VENDOR_DOWNLOAD_ID + "=? and " + ColumsCol.VENDOR_NAME + "=?";
        ContentValues values = new ContentValues();
        values.put(ColumsCol.VENDOR_DOWNLOAD_ID, newId);

        context.getContentResolver().update(iDataORM.DOWNLOAD_CONTENT_URI, values, where, new String[]{oldId, vendorName});
    }

    public static void removeFailedSubmitDownload(Context context, String downloadId, String vendorName) {
        if (TextUtils.isEmpty(downloadId)) return;
        String where = ColumsCol.VENDOR_DOWNLOAD_ID + "=? and " + ColumsCol.VENDOR_NAME + "=?";

        context.getContentResolver().delete(iDataORM.DOWNLOAD_CONTENT_URI, where, new String[]{downloadId, vendorName});
    }

    public static void removeAllVendorDownloadByVendorNameInLocalDb(Context context, String vendorName) {
        if (TextUtils.isEmpty(vendorName)) return;

        String where = ColumsCol.VENDOR_NAME + "=?";
        context.getContentResolver().delete(iDataORM.DOWNLOAD_CONTENT_URI, where, new String[] {vendorName});
    }

    public static void removeDownloadsInLocalDbByFileList(Context context, List<String> fileLists) {
        if (fileLists == null || fileLists.size() <=0 ) return;

        String where = ColumsCol.DOWNLOAD_PATH + "=?";
        ArrayList<ContentProviderOperation> opsList = new ArrayList<ContentProviderOperation>();
        for (String filePath : fileLists) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(iDataORM.DOWNLOAD_CONTENT_URI).withSelection(where, new String[]{filePath});
            opsList.add(builder.build());
        }

        try {
            context.getContentResolver().applyBatch(AUTHORITY, opsList);
        } catch (OperationApplicationException oae) {
            oae.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        }
    }

    public static void removeDownloadsInLocalDb(Context context, List<Pair<String, String>> downloadIds) {
        if (downloadIds == null || downloadIds.size() <= 0 ) return;

        String where = ColumsCol.VENDOR_NAME + "=? and " + ColumsCol.VENDOR_DOWNLOAD_ID + "=?";
        ArrayList<ContentProviderOperation> opsList = new ArrayList<ContentProviderOperation>();
        for (Pair<String, String> download : downloadIds) {
            ContentProviderOperation.Builder builder = ContentProviderOperation.newDelete(iDataORM.DOWNLOAD_CONTENT_URI).withSelection(where, new String[]{download.first, download.second});
            opsList.add(builder.build());
        }

        try {
            context.getContentResolver().applyBatch(AUTHORITY, opsList);
        } catch (OperationApplicationException oae) {
            oae.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        }
    }

    public static void batchUpdateDownloads(Context context, List<ContentValues> valuesList) {
        if (valuesList == null || valuesList.size() <=0) return;

        String selectStr = ColumsCol.VENDOR_NAME + "=? and " + ColumsCol.VENDOR_DOWNLOAD_ID + "=?";
        ArrayList<ContentProviderOperation> opsList = new ArrayList<ContentProviderOperation>();

        for (ContentValues values : valuesList) {
            String vendorDownloadId = values.getAsString(ColumsCol.VENDOR_DOWNLOAD_ID);
            String vendorName = values.getAsString(ColumsCol.VENDOR_NAME);
            values.remove(ColumsCol.VENDOR_DOWNLOAD_ID);
            values.remove(ColumsCol.VENDOR_NAME);

            ContentProviderOperation.Builder builder = ContentProviderOperation.newUpdate(iDataORM.DOWNLOAD_CONTENT_URI).withSelection(selectStr, new String[]{vendorName, vendorDownloadId});
            builder.withValues(values);

            opsList.add(builder.build());
        }

        try {
            context.getContentResolver().applyBatch(AUTHORITY, opsList);
        } catch (OperationApplicationException oae) {
            oae.printStackTrace();
        } catch (RemoteException re) {
            re.printStackTrace();
        }
    }

//    private static ArrayList<String> getAllDownloadSubIDS(Context context, ArrayList<Integer>downs){
//        ArrayList<String> subIDS = new ArrayList<String>();
//        StringBuilder sb = new StringBuilder();
//        sb.append(ColumsCol.DOWNLOAD_ID);
//        sb.append(" in (");
//        boolean needDouhao = false;
//        for(Integer item:downs){
//            if(needDouhao){
//                sb.append(",");
//            }
//            sb.append(item);
//
//            needDouhao = true;
//        }
//        sb.append(" )");
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.SUB_ID}, sb.toString(), null, null);
//        if(cursor != null){
//            if(cursor.moveToFirst()){
//                do{
//                    subIDS.add(cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID)));
//                }while (cursor.moveToNext());
//            }
//
//            cursor.close();
//        }
//        return subIDS;
//    }

//    public static ActionRecord getDowndloadByDID(Context context, int download_id){
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, " download_id = " + download_id, null, " date_int desc");
//        if(cursor != null ){
//            if(cursor.moveToFirst()) {
//                do {
//                    return formatActionRecord(cursor);
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        }
//        return null;
//    }

    public static ActionRecord formatActionRecord(Cursor cursor){
        ActionRecord item = new ActionRecord();
        try {
            if (!cursor.isNull(cursor.getColumnIndex(ColumsCol.ID))) {
                item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                item.res_id = cursor.getString(cursor.getColumnIndex(ColumsCol.RES_ID));

                item.json = cursor.getString(cursor.getColumnIndex(ColumsCol.VALUE));
                item.sub_id = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
                item.sub_value = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_VALUE));

                item.uploaded = cursor.getInt(cursor.getColumnIndex(ColumsCol.Uploaded));
                item.date = cursor.getString(cursor.getColumnIndex(ColumsCol.ChangeDate));
                item.dateInt = cursor.getLong(cursor.getColumnIndex(ColumsCol.ChangeLong));
                item.download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
                item.download_url = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
                item.download_status = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_STATUS));
                item.download_path = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_PATH));
                item.download_vendor_name = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_NAME));
                item.vendor_download_id = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_DOWNLOAD_ID));
                item.downloadbytes = cursor.getLong(cursor.getColumnIndex(ColumsCol.DOWNLOADED_SIZE));
                item.totalsizebytes = cursor.getLong(cursor.getColumnIndex(ColumsCol.TOTAL_SIZE));
            }
        }catch (Exception ne){
            ne.printStackTrace();
        }
        return  item;
    }

    private static ContentValues actionRecordToContentValues(ActionRecord ar){
        ContentValues ct = new ContentValues();
        ct.put(ColumsCol.ID,        ar.id);
        ct.put(ColumsCol.RES_ID,    ar.res_id);
        ct.put(ColumsCol.VALUE,     ar.json );
        ct.put(ColumsCol.SUB_ID,    ar.sub_id);
        ct.put(ColumsCol.SUB_VALUE, ar.sub_value);
        ct.put(ColumsCol.NS,        ar.ns); //TODO need add ns to download apk
        ct.put(ColumsCol.DOWNLOAD_URL, ar.download_url);
        ct.put(ColumsCol.DOWNLOAD_ID,  ar.download_id);
        ct.put(ColumsCol.Uploaded,     ar.uploaded);
        ct.put(SettingsCol.ChangeDate, ar.date);
        ct.put(ColumsCol.ChangeLong,   ar.dateInt);

        ct.put(ColumsCol.DOWNLOAD_PATH,   ar.download_path);
        ct.put(ColumsCol.DOWNLOAD_STATUS, ar.download_status);
        ct.put(ColumsCol.VENDOR_NAME, ar.download_vendor_name);
        ct.put(ColumsCol.VENDOR_DOWNLOAD_ID, ar.vendor_download_id);
        ct.put(ColumsCol.TOTAL_SIZE, ar.totalsizebytes);
        ct.put(ColumsCol.DOWNLOADED_SIZE, ar.downloadbytes);

        return  ct;
    }

    public static boolean updateDownload(Context context, ContentValues ct) {
        boolean ret = false;
        String where = " res_id LIKE ? and sub_id=?";
        if(context.getContentResolver().update(DOWNLOAD_CONTENT_URI, ct, where, new String[]{"%"+getVideoID(ct.getAsString(ColumsCol.RES_ID)), ""+ct.get(ColumsCol.SUB_ID)}) > 0){
            ret = true;
        }
        return ret;
    }

    public static boolean updateDownloadProgress(Context context, String vendorName, String vendorDownloadId, long currentBytes, long totalBytes) {
        boolean ret = false;
        ContentValues values = new ContentValues();
        values.put(SettingsCol.ChangeDate, dateToString(new Date()));
        values.put(ColumsCol.ChangeLong, System.currentTimeMillis());

        values.put(ColumsCol.DOWNLOADED_SIZE, currentBytes);
        values.put(ColumsCol.TOTAL_SIZE, totalBytes);

        if (currentBytes == totalBytes && totalBytes > 0) {
            values.put(ColumsCol.DOWNLOAD_STATUS, DOWNLOAD_STATUS_FINISHED);
        }

        String where = ColumsCol.VENDOR_DOWNLOAD_ID + "=? and " + ColumsCol.VENDOR_NAME + "=?";
        if(context.getContentResolver().update(DOWNLOAD_CONTENT_URI, values, where, new String[]{vendorDownloadId, vendorName,}) > 0){
            ret = true;
        }

        return ret;
    }

    // keep these definition same as those defined in IOfflineVendor.java
    public static final int STATUS_COMPLETED = 0;
    public static final int STATUS_UNCOMPLETED_STOP = 1;
    public static final int STATUS_UNCOMPLETED_WAIT = 2;
    public static final int STATUS_UNCOMPLETED_GOING = 3;
    public static final int STATUS_UNCOMPLETED_FAILED = 4;

    public static final int STATUS_UNCOMPLETED_STARTING = 5;
    public static final int STATUS_UNCOMPLETED_DEFAULT = 6;

    public static boolean updateDownloadStatus(Context context, String vendorName, String vendorDownloadId, int status) {
        boolean ret = false;

        int result_status = -1;
        switch (status) {
            case STATUS_COMPLETED:
                result_status = DOWNLOAD_STATUS_FINISHED;
                break;

            case STATUS_UNCOMPLETED_GOING:
            case STATUS_UNCOMPLETED_STARTING:
            case STATUS_UNCOMPLETED_DEFAULT:
                result_status = DOWNLOAD_STATUS_RUNNING;
                break;

            case STATUS_UNCOMPLETED_STOP:
            case STATUS_UNCOMPLETED_WAIT:
                // just update status to pause even if it is really a failed task
                // because we cannot known what is real reason ( user pause or failed);
                result_status = DOWNLOAD_STATUS_PAUSE;
                break;

            case  STATUS_UNCOMPLETED_FAILED:
                result_status = DOWNLOAD_STATUS_FAILED;
                break;

            default:
                // never be here
                Log.e(TAG, "Unknown download status in vendor downloading");
                return false;
        }

        String where = ColumsCol.VENDOR_DOWNLOAD_ID + "=? and " + ColumsCol.VENDOR_NAME + "=?";

        ContentValues values = new ContentValues();
        values.put(SettingsCol.ChangeDate, dateToString(new Date()));
        values.put(ColumsCol.ChangeLong, System.currentTimeMillis());
        values.put(ColumsCol.DOWNLOAD_STATUS, result_status);
        if(context.getContentResolver().update(DOWNLOAD_CONTENT_URI, values, where, new String[]{vendorDownloadId, vendorName,}) > 0){
            ret = true;
        }
        return ret;
    }

    public static int getDownloadID(Context context, String res_id, String sub_id){
        int download_id = -1;
        String where = /*ColumsCol.RES_ID + " LIKE ?  and " +*/ ColumsCol.SUB_ID + " =?";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_ID}, where, new String[]{/*"%"+getVideoID(res_id),*/ sub_id}, null);
        if(cursor != null ){
            if(cursor.getCount() > 0 && cursor.moveToFirst()){
                download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
            }
            cursor.close();
        }
        return download_id;
    }

    public static Pair<String, String> getVendorDownloadId(Context context, String res_id, String sub_id) {
        String vendorDownloadId = "";
        String vendorName = "";
        int download_id = -1;

        String where = ColumsCol.SUB_ID + " =?";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_ID,ColumsCol.VENDOR_DOWNLOAD_ID, ColumsCol.VENDOR_NAME}, where, new String[]{sub_id}, null);
        if(cursor != null ){
            if(cursor.getCount() > 0 && cursor.moveToFirst()){
                download_id = cursor.getInt(cursor.getColumnIndexOrThrow(ColumsCol.DOWNLOAD_ID));
                if (download_id != DOWN_ID_PENDING && download_id != DOWN_ID_RESTORE) {
                    vendorDownloadId = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_DOWNLOAD_ID));
                    vendorName = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_NAME));
                }
            }
            cursor.close();
        }

        if (download_id != DOWN_ID_PENDING && download_id != DOWN_ID_RESTORE && download_id != -1) {
            return new Pair<String, String>(vendorName, vendorDownloadId);
        } else {
            return null;
        }
    }

//    public static String getDownloadLocalURI(Context context, String res_id, String sub_id){
//        String local_path = "";
//        String where = ColumsCol.RES_ID + "  LIKE ? and " + ColumsCol.SUB_ID + " = ?";
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_URL}, where, new String[]{"%"+getVideoID(res_id), sub_id}, null);
//        if(cursor != null ){
//            if(cursor.getCount() > 0 && cursor.moveToFirst()){
//                local_path = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
//            }
//            cursor.close();
//        }
//        return local_path;
//    }

    public static Pair<String, String> getCompletedDownloadLocalUri(Context context, String sub_id) {
        if(TextUtils.isEmpty(sub_id))
            return null;

        int download_id;
        String localPath = "";
        String vendorName = "";

        String where = ColumsCol.SUB_ID + " =? and " + ColumsCol.DOWNLOAD_STATUS + "=" + iDataORM.DOWNLOAD_STATUS_FINISHED;
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.DOWNLOAD_URL, ColumsCol.DOWNLOAD_ID, ColumsCol.VENDOR_NAME, ColumsCol.DOWNLOAD_PATH}, where, new String[]{sub_id}, null);

        if(cursor != null ){
            if(cursor.getCount() > 0 && cursor.moveToFirst()){
                download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
                localPath = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_PATH));
                vendorName = cursor.getString(cursor.getColumnIndex(ColumsCol.VENDOR_NAME));

                if (download_id == iDataORM.DOWN_ID_RESTORE && TextUtils.isEmpty(localPath)) {
                    // backward compatible for old version DB
                    localPath = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));;
                }
            }
            cursor.close();
        }

        if (TextUtils.isEmpty(vendorName))
            vendorName = "system";

        if (TextUtils.isEmpty(localPath)) return null;

        return new Pair<String, String>(vendorName, localPath);
    }

//    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
//    public static String getDownloadLocalUri(Context context, String sub_id){
//        String localPath = "";
//        if(TextUtils.isEmpty(sub_id))
//            return localPath;
//
//        int download_id = -1;
//        String local_url = "";
//        String where = ColumsCol.SUB_ID + " =? ";
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_URL, ColumsCol.DOWNLOAD_ID}, where, new String[]{sub_id}, null);
//        if(cursor != null ){
//            if(cursor.getCount() > 0 && cursor.moveToFirst()){
//                download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
//                local_url   = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
//            }
//            cursor.close();
//        }
//
//        if(download_id == DOWN_ID_RESTORE)
//            return local_url;
//
//        if(download_id != -1){
//            if(dm == null) {
//                dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            }
//
//            DownloadManager.Query query = new DownloadManager.Query();
//            query = query.setFilterById(download_id);
//            Cursor currentUI = dm.query(query);
//            if (currentUI != null){
//                if(currentUI.moveToFirst()) {
//                    if(DownloadManager.STATUS_SUCCESSFUL == currentUI.getInt(currentUI.getColumnIndex(DownloadManager.COLUMN_STATUS))){
//                        localPath = currentUI.getString(currentUI.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//                    }
//                }
//                currentUI.close();
//            }
//        }
//        return localPath;
//    }

    public static boolean existDownload(Context context, String sub_id){
        boolean exist = false;
        String where = ColumsCol.SUB_ID + " ='"+sub_id + "'";
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
        }
        return exist;
    }

//    public static boolean existDownloadURLTask(Context context, String sub_id, String download_url){
//        boolean exist = false;
//        String where = ColumsCol.SUB_ID + " ='"+sub_id + "' and "+ColumsCol.DOWNLOAD_URL + " ='"+download_url+"'";
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, null);
//        if(cursor != null ){
//            if(cursor.getCount() > 0){
//                exist = true;
//            }
//            cursor.close();
//        }
//        return exist;
//    }

//    public static boolean existOffinedDownload(Context context, String sub_id, String download_url){
//        //if downloaded
//        boolean exist = hasFinishedDownload(context, sub_id);
//
//        //if in downloading task
//        if(exist == false){
//           exist = existDownloadURLTask(context, sub_id, download_url);
//        }
//
//        //
//        //this is wrong status
//        //
//        //change episode download task status
//        if(exist == true) {
//            long download_id = iDataORM.getDownloadID(context, "", sub_id);
//            if (download_id == iDataORM.DOWN_ID_PENDING) {
//                //remove the pending task
//                iDataORM.removePendingTask(context, sub_id);
//            }
//        }
//
//        return exist;
//    }

//    public static  boolean hasFinishedDownload(Context context, String sub_id){
//        boolean exist = false;
//        //if downloaded
//        String where = ColumsCol.SUB_ID + " ='"+sub_id + "' and "+ColumsCol.DOWNLOAD_STATUS + " ="+ DOWNLOAD_STATUS_FINISHED;
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, null);
//        if(cursor != null ){
//            if(cursor.getCount() > 0){
//                exist = true;
//            }
//            cursor.close();
//        }
//
//        return exist;
//    }

//    public static void removePendingTask(Context context, String sub_id){
//        String where = ColumsCol.SUB_ID  + " ='"+sub_id+"'";//+"' and " + ColumsCol.DOWNLOAD_ID + " = "+DOWN_ID_PENDING;
//        context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, null);
//    }

//    public static void removeDownloadEpisodes(final Context context, final ArrayList<String> sub_ids){
//        getWorkThreadHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
//                for (String ep_id : sub_ids) {
//                    ops.add(ContentProviderOperation.newDelete(DOWNLOAD_CONTENT_URI).withSelection(ColumsCol.SUB_ID + "=? ", new String[]{String.valueOf(ep_id)}).build());
//                }
//
//                try {
//                    ContentProviderResult[] results = context.getContentResolver().applyBatch(AUTHORITY, ops);
//                    if (results != null) {
//                        for (ContentProviderResult result : results) {
//                            if (default_debug_mode)
//                                Log.d(TAG, "releaseDownloadTask: count:" + result.count + " result:" + result.toString());
//                        }
//
//                        Log.d(TAG, "removeDownload: ids:" + sub_ids + " lens:" + results.length);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public static void removeDownload(final Context context, final String sub_id){
        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                String where = ColumsCol.SUB_ID + " LIKE ?";
                int count = context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, new String[]{"%" + getVideoID(sub_id)});
                Log.d(TAG, "remove download sub_id: " + sub_id + " result: " + count);
            }
        });
    }

//    public static void removeFinishedDownload(final Context context, final String res_id){
//        getWorkThreadHandler().post(new Runnable() {
//            @Override
//            public void run() {
//
//                String where = ColumsCol.RES_ID + " LIKE ? and " + ColumsCol.DOWNLOAD_STATUS + " = 1";
//                Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, where, new String[]{"%" + getVideoID(res_id)}, null);
//                if(cursor != null) {
//                    if (cursor.moveToFirst()) {
//                        do {
//                            try {
//                                int down_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
//                                if (down_id == DOWN_ID_RESTORE) {
//                                    String local = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
//                                    try {
//                                        new File(local).delete();
//                                    } catch (Exception ne) {
//                                        ne.printStackTrace();
//                                    }
//                                } else if (down_id > 0) {
//                                    try {
//                                        if(dm == null) {
//                                            dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                                        }
//
//                                        Uri uri = dm.getUriForDownloadedFile(down_id);
//                                        new File(URI.create(uri.toString())).delete();
//                                        dm.remove(down_id);
//                                    } catch (Exception ne) {
//                                        ne.printStackTrace();
//                                    }
//                                }
//                            } catch (Exception ne) {
//                            }
//
//                        } while (cursor.moveToNext());
//                    }
//
//                    cursor.close();
//                    context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, new String[]{"%" + getVideoID(res_id)});
//                }
//            }
//        });
//    }

//    public static void removeDownloadFile(final Context context, final String sub_id){
//        getWorkThreadHandler().post(new Runnable() {
//            @Override
//            public void run() {
//                String where = ColumsCol.SUB_ID + " ='" + sub_id + "'";
//                String localPath = "";
//
//                Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.DOWNLOAD_URL}, where, null, null);
//                if (cursor != null) {
//                    if (cursor.moveToFirst()) {
//                        localPath = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
//                        boolean deleted = new File(localPath).delete();
//                        Log.d(TAG, "remove local file:" + localPath + " result:" + deleted);
//                    }
//                    cursor.close();
//                }
//
//                context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, null);
//            }
//        });
//    }

//    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
//    public static void removeDownloadsByEpisodes(Context context, String viid, ArrayList<String> epsisode_ids) {
//        ArrayList<EpisodeDown> eps = getFinishedEpisodeIDSDowns(context, viid);
//
//        ArrayList<String> ep_ids = new ArrayList<String>();
//        ArrayList<String> localFiles = new ArrayList<String>();
//        ArrayList<Integer>ids = new ArrayList<Integer>();
//        for(EpisodeDown item:eps){
//            if(epsisode_ids.contains(item.episode)){
//                if( item.down_id == iDataORM.DOWN_ID_RESTORE) {
//                    ep_ids.add(item.episode);
//                    localFiles.add(item.localURL);
//                }else {
//                    ids.add(item.down_id);
//                }
//            }
//        }
//
//        long []lids = new long[ids.size()];
//        for(int i=0;i<ids.size();i++){
//            lids[i] = ids.get(i);
//        }
//
//
//        if(localFiles.size() > 0){
//            for(String file: localFiles){
//                try {
//                    boolean res = new File(file).delete();
//                    Log.d(TAG, "remove file:"+file + " result:"+res);
//                }catch (Exception ne){ne.printStackTrace();}
//            }
//        }
//
//        removeDownload(context, ids);
//        removeDownloadEpisodes(context, epsisode_ids);
//
//        if(lids.length > 0) {
//            if(dm == null) {
//                dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//            }
//            dm.remove(lids);
//        }
//    }

//    public static int removeDownload(Context context, ArrayList<Integer> ids){
//        if(ids == null || ids.size() == 0)
//            return 0;
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(ColumsCol.DOWNLOAD_ID);
//        sb.append(" in (");
//        boolean needDouhao = false;
//        for(Integer item:ids){
//            if(needDouhao){
//                sb.append(",");
//            }
//            sb.append(item);
//
//            needDouhao = true;
//        }
//        sb.append(" )");
//        return context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, sb.toString(), null);
//    }

    public static int getDownloadCount(Context context){
        if (context == null){
            return 0;
        }
//        clearDownloadNotInSystemDownload(context);
        int count = 0;
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{"_id"}, null, null, null);
        if(cursor != null ){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

//    public static ArrayList<ActionRecord> getDownloads(Context context){
//       return getDownloads(context, 0);
//    }

    //need keep the pending download
//    public static int clearDownloadNotInSystemDownload(Context context){
//        ArrayList<Integer> removes = new ArrayList<Integer>();
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_ID}, ColumsCol.DOWNLOAD_ID + "!=" + DOWN_ID_RESTORE, null, " date_int desc");
//        if(cursor != null ){
//            if( cursor.moveToFirst()) {
//                do {
//                    int download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
//                    if (!existInDownloadManager(context, download_id) && download_id != DOWN_ID_RESTORE && download_id != DOWN_ID_PENDING) {
//                        removes.add(download_id);
//                    }
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        }
//
//        return  removeDownload(context, removes);
//    }


    public static int getFinishedEpisodeCount(Context context, String res_id){
        int episode = 0;
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID}, " res_id LIKE ? and download_status=" + DOWNLOAD_STATUS_FINISHED, new String[]{"%"+getVideoID(res_id)}, null);
        if(cursor != null ){
            episode = cursor.getCount();

            cursor.close();

        }
        return episode;
    }

//    public static ArrayList<Integer> getFinishedEpisodeDowns(Context context, String res_id){
//        ArrayList<Integer> downs = new ArrayList<Integer>();
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_ID}, " res_id LIKE ?  and download_status = 1",  new String[]{"%"+getVideoID(res_id)}, null);
//        if(cursor != null ){
//            if(cursor.moveToFirst()) {
//                do {
//                    downs.add(cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID)));
//                }while (cursor.moveToNext());
//            }
//            cursor.close();
//
//        }
//        return downs;
//    }

//    public static class EpisodeDown{
//        public String episode;
//        public String localURL;//when restore from phone, this is local url, not download url
//        public int    down_id;
//        public int    _id;
//    }
//    public static ArrayList<EpisodeDown> getFinishedEpisodeIDSDowns(Context context, String res_id){
//        ArrayList<EpisodeDown> downs = new ArrayList<EpisodeDown>();
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_ID, ColumsCol.DOWNLOAD_URL, ColumsCol.SUB_ID}, " res_id LIKE ? and download_status = 1",  new String[]{"%"+getVideoID(res_id)}, null);
//        if(cursor != null ){
//            if(cursor.moveToFirst()) {
//                do {
//                    EpisodeDown item = new EpisodeDown();
//                    item._id     = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
//                    item.down_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
//                    item.episode = cursor.getString(cursor.getColumnIndex(ColumsCol.SUB_ID));
//                    //when restore from phone, this is local url, not download url
//                    item.localURL = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
//                    downs.add(item);
//                }while (cursor.moveToNext());
//            }
//            cursor.close();
//
//        }
//        return downs;
//    }

    public static ArrayList<ActionRecord> getDownloadsByResId(Context context, String res_id) {
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, " res_id LIKE ?", new String[]{"%"+getVideoID(res_id)}, " date_int desc");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ActionRecord item = formatActionRecord(cursor);
                    actionRecords.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return actionRecords;
    }

    public static ArrayList<ActionRecord> getFinishedDownloads(Context context, String res_id) {
        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();

        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, " res_id LIKE ? and download_status=" + DOWNLOAD_STATUS_FINISHED, new String[]{"%"+getVideoID(res_id)}, " date_int desc");

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    ActionRecord item = formatActionRecord(cursor);
                    actionRecords.add(item);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return actionRecords;
    }

//    public static ArrayList<ActionRecord> getFinishedDownloads(Context context, String res_id){
//        ArrayList<Integer> removes = new ArrayList<Integer>();
//        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, " res_id LIKE ? and download_status = " + DOWNLOAD_STATUS_FINISHED, new String[]{"%"+getVideoID(res_id)}, " date_int desc");
//        if(cursor != null ){
//            if(cursor.moveToFirst()){
//                do {
//                    //if exist in download manager task
//                    int downid = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
//                    if (existInDownloadManager(context, downid) || downid == DOWN_ID_RESTORE) {
//                        ActionRecord item = formatActionRecord(cursor);
//
//                        actionRecords.add(item);
//                    } else {
//                        removes.add(downid);
//                    }
//                }while(cursor.moveToNext());
//            }
//            cursor.close();
//
//            //
//            removeDownload(context, removes);
//        }
//        return actionRecords;
//    }


//    public static ArrayList<ActionRecord> getDownloads(Context context, int before_date){
//        ArrayList<Integer> removes = new ArrayList<Integer>();
//        ArrayList<ActionRecord> actionRecords = new ArrayList<ActionRecord>();
//        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, " date_int >= " + before_date + " and download_status != 1", null, " date_int desc");
//        if(cursor != null ){
//            if(cursor.moveToFirst()){
//                do {
//                    //if exist in download manager task
//                    int downid = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
//                    if (existInDownloadManager(context, downid) || downid == DOWN_ID_RESTORE) {
//                        ActionRecord item = formatActionRecord(cursor);
//                        actionRecords.add(item);
//                    } else {
//                        removes.add(downid);
//                    }
//                }while(cursor.moveToNext());
//            }
//            cursor.close();
//
//            //
//            removeDownload(context, removes);
//        }
//        return actionRecords;
//    }

    private static String decode(String localPath){
        String str = "";
        try {
            str = URLDecoder.decode(localPath, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    public static List<ActionRecord> getDownloadByPath(Context context, String path) {
        if (TextUtils.isEmpty(path)) return null;

        List<ActionRecord> result = new ArrayList<ActionRecord>();

        String where = ColumsCol.DOWNLOAD_PATH + "=" + path;

        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, where, null, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            while (cursor.isAfterLast()) {
                result.add(formatActionRecord(cursor));
                cursor.moveToNext();
            }
        }

        if (cursor != null) {
            cursor.close();
        }

        return result;
    }

//    public static void  removeDownloadByPath(Context context, String path){
//          Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, downloadProject, null, null, null);
//          if (cursor != null){
//              if (cursor.moveToFirst()){
//                  do {
//                      int downloadid = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
//                      if (downloadid == DOWN_ID_RESTORE){
//                          String local_url   = cursor.getString(cursor.getColumnIndex(ColumsCol.DOWNLOAD_URL));
//                          if (path.equalsIgnoreCase(local_url)){
//                              String where = ColumsCol.DOWNLOAD_URL + " =? ";
//                              context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, new String[]{local_url});
//                              break;
//                          }
//                      }else {
//                          if(dm == null) {
//                              dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//                          }
//
//                          DownloadManager.Query query = new DownloadManager.Query();
//                          query = query.setFilterById(downloadid);
//                          Cursor currentUI = dm.query(query);
//                          if (currentUI != null){
//                              if(currentUI.moveToFirst()) {
//                                  if(DownloadManager.STATUS_SUCCESSFUL == currentUI.getInt(currentUI.getColumnIndex(DownloadManager.COLUMN_STATUS))){
//                                      String localPath = currentUI.getString(currentUI.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
//                                      if (localPath.startsWith("file:///") == true) {
//                                          localPath = localPath.substring("file:///".length() - 1);
//                                      }
//                                      if (path.equalsIgnoreCase(decode(localPath))){
//                                          dm.remove(downloadid);
//                                          //delete in database
//                                          String where = ColumsCol.DOWNLOAD_ID + " =? ";
//                                          context.getContentResolver().delete(DOWNLOAD_CONTENT_URI, where, new String[]{String.valueOf(downloadid)});
//                                          break;
//                                      }
//                                  }
//                              }
//                              currentUI.close();
//                          }
//
//                      }
//                  }while (cursor.moveToNext());
//              }
//              cursor.close();
//          }
//    }

    private static  DownloadManager dm;
//    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
//    public static boolean existInDownloadManager(Context context, int download_id){
//        boolean existInSystemDownloadQueue = false;
//        if(dm == null) {
//            dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
//        }
//
//        DownloadManager.Query query = new DownloadManager.Query();
//        query = query.setFilterById(download_id);
//        Cursor currentUI = dm.query(query);
//        if (currentUI != null){
//            if(currentUI.getCount() > 0 && currentUI.moveToFirst()) {
//                existInSystemDownloadQueue = true;
//
//                int downloadId = currentUI.getInt(currentUI.getColumnIndexOrThrow(DownloadManager.COLUMN_ID));
//                int _stat = currentUI.getInt(currentUI.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
//                if(_stat == DownloadManager.STATUS_SUCCESSFUL){
//                    Uri uri = dm.getUriForDownloadedFile(downloadId);
//                    if(!new File(URI.create(uri.toString())).exists()) {
//                        //ArrayList<Integer> removeIDS = new ArrayList<Integer>();
//                        //iDataORM.removeDownload(context, removeIDS);
//                        existInSystemDownloadQueue = false;
//                    }
//                }
//            }
//
//            currentUI.close();
//        }
//        return existInSystemDownloadQueue;
//    }

    public static ArrayList<Long> getDownloadIDs(Context context, int before_date){
        ArrayList<Long> actionRecords = new ArrayList<Long>();
        Cursor cursor = context.getContentResolver().query(DOWNLOAD_CONTENT_URI, new String[]{ColumsCol.ID, ColumsCol.DOWNLOAD_ID}, " date_int >= "+before_date + " and download_status != 1", null, " date_int desc");
        if(cursor != null){
            if( cursor.moveToFirst()) {
                do {
                    long download_id = cursor.getInt(cursor.getColumnIndex(ColumsCol.DOWNLOAD_ID));
                    actionRecords.add(download_id);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return actionRecords;
    }

    /*
    *download end
    */

    /*
     * begin search history
     */
    public static class SearchHistoryItem{
        public int    id         ;
        public String key        ;
        public String date       ;
        public long   date_int   ;
    }

    public static boolean hasSearchHistory(Context context) {
        boolean exist = false;
        Cursor cursor = context.getContentResolver().query(SEARCH_CONTENT_URI, new String[]{ColumsCol.ID}, null, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
        }
        return exist;
    }

    public static int removeSearchHistory(Context context, String key) {
        int lines;
        //remove all
        if(TextUtils.isEmpty(key)){
            lines = context.getContentResolver().delete(SEARCH_CONTENT_URI, null, null);
        }else{
            String where = ColumsCol.KEY + " ='" + key + "'";
            lines = context.getContentResolver().delete(SEARCH_CONTENT_URI, where, null);
        }

        return lines;
    }

    public static Uri addSearchHistory(Context context, String key) {

        Uri ret = null;
        ContentValues ct = new ContentValues();
        ct.put(ColumsCol.KEY, key);
        ct.put(ColumsCol.ChangeDate, dateToString(new Date()));
        ct.put(ColumsCol.ChangeLong, System.currentTimeMillis());
        //if exist, update
        if(existSearch(context, key)){
            updateSearch(context, ct);
        }else{
            ret = context.getContentResolver().insert(SEARCH_CONTENT_URI, ct);
        }
        return ret;

    }

    public static boolean updateSearch(Context context, ContentValues ct) {
        boolean ret = false;
        String where = String.format(" key = \'%1$s\' ", ct.get(ColumsCol.KEY));
        if(context.getContentResolver().update(SEARCH_CONTENT_URI, ct, where, null) > 0){
            ret = true;
        }
        return ret;
    }

    public static boolean existSearch(Context context, String key){
        boolean exist = false;
        String where = ColumsCol.KEY + " ='" + key +  "' " ;
        Cursor cursor = context.getContentResolver().query(SEARCH_CONTENT_URI, new String[]{ColumsCol.ID}, where, null, null);
        if(cursor != null ){
            if(cursor.getCount() > 0){
                exist = true;
            }
            cursor.close();
        }
        return exist;
    }

    public static ArrayList<SearchHistoryItem> getSearchHistory(Context con, int count){
        ArrayList<SearchHistoryItem> actionRecords = new ArrayList<SearchHistoryItem>();
        Cursor cursor = con.getContentResolver().query(SEARCH_CONTENT_URI, searchProject, null, null, " date_int desc");
        if(cursor != null ){
            if(cursor.moveToFirst()) {
                do {
                    SearchHistoryItem item = new SearchHistoryItem();
                    item.id = cursor.getInt(cursor.getColumnIndex(ColumsCol.ID));
                    item.key = cursor.getString(cursor.getColumnIndex(ColumsCol.KEY));
                    item.date = cursor.getString(cursor.getColumnIndex(ColumsCol.ChangeDate));
                    item.date_int = cursor.getLong(cursor.getColumnIndex(ColumsCol.ChangeLong));

                    actionRecords.add(item);

                }while (cursor.moveToNext() && actionRecords.size() < count);
            }
            cursor.close();
        }

        return actionRecords;
    }
    /*
     * end search history
     */

    public static int getIntValue(Context con, String name, int defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        int valueB = defaultValue;
        try{
            if(value != null){
                valueB = Integer.valueOf(value);
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }

    public static long getLongValue(Context con, String name, long defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        long valueB = defaultValue;
        try{
            if(value != null){
                valueB = Long.valueOf(value);
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }


    public static boolean getBooleanValue(Context con, String name, boolean defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        boolean valueB = defaultValue;
        try{
            if(value != null){
                valueB = value.equals("1");
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }


    public static String getStringValue(Context con, String name, String defaultValue) {
        String value = getSettingValue(con, SETTINGS_CONTENT_URI, name);
        String valueB = defaultValue;
        try{
            if(value != null && value.length() > 0){
                valueB = value;
            }
        }catch(Exception ne){ne.printStackTrace();}

        return valueB;
    }

    public static class SettingsCol{
        public static final String ID         = "_id";
        public static final String Name       = "name";
        public static final String Value      = "value";
        public static final String Application= "application";
        public static final String ChangeDate = "date_time";
        //public static final String ChangeLong = "date_long";
    }



    //settings
    public String getSettingValue(String name) {
        String va = null;
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
        }
        return va;
    }

    public int getIntValue(String name, int defaultV) {
        String va = String.valueOf(defaultV);
        String where = SettingsCol.Name + "='"+name+"'";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, null, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
            }
            cursor.close();
        }
        return  Integer.valueOf(va);
    }

    public boolean getBooleanValue(String name, boolean defaultV) {
        Boolean va = defaultV;
        String where = SettingsCol.Name + "=?";
        Cursor cursor = mContext.getContentResolver().query(SETTINGS_CONTENT_URI,settingsProject,where, new String[]{name}, null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value)).equals("1");
            }
            cursor.close();
        }
        return  va;
    }

    public void addSetting(String name, String value) {
        addSetting(mContext, name, value);
    }

    public static String getSystemProperties(String key){
        try {
            Class osSystem = Class.forName("android.os.SystemProperties");
            Method getInvoke = osSystem.getMethod("get", new Class[]{String.class});
            return  (String) getInvoke.invoke(osSystem,  new Object[]{key});
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return  "";
    }

    public static void addSetting(final Context context, final HashMap<String, String> value) {
        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                Set<String> sets = value.keySet();
                for (String key : sets) {
                    String va = value.get(key);
                    if (open_customization.equals(key) && !"cm".equals(getSystemProperties("ro.carrier.name"))) {
                        //if I am not cm phone, ignore the setting
                        continue;
                    }
                    addSettingSync(context, key, va);
                }
            }
        });
    }

    public static void clearSetting(Context context){
        context.getContentResolver().delete(SETTINGS_CONTENT_URI, null, null);
    }

    public static void addSetting(final Context context, final String name, final String value) {
        getWorkThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                addSetting(context, SETTINGS_CONTENT_URI, name, value);
            }
        });
    }

    public static void addSettingSync(final Context context, final String name, final String value) {
        addSetting(context, SETTINGS_CONTENT_URI, name, value);
    }

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static String getSettingValue(Context con, Uri settingUri, String name) {
        if (con == null || con.getContentResolver() == null) {
            Log.d(TAG, "con == null || con.getContentResolver() == null");
            return null;
        }
        String va = null;
        try {
            String where = SettingsCol.Name + "=?";
            Cursor cursor = con.getContentResolver().query(settingUri, settingsProject, where, new String[]{name}, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    va = cursor.getString(cursor.getColumnIndex(SettingsCol.Value));
                }
                cursor.close();
            }
        }catch (Exception ne){}
        return va;
    }

    private static boolean isSettingKeyExist(Context con, Uri settingUri, String key) {
        if (con == null || con.getContentResolver() == null || settingUri == null || TextUtils.isEmpty(key)) {
            Log.d(TAG, "con == null || con.getContentResolver() == null");
            return false;
        }
        String where = SettingsCol.Name +"=?";
        Cursor cursor = con.getContentResolver().query(settingUri, settingsProject, where, new String[]{key}, null);

        boolean ret = false;
        if(cursor != null && cursor.getCount() > 0){
            ret = true;
        }

        if (cursor != null) {
            cursor.close();
        }

        return ret;
    }

    public static String dateToString(Date time){
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }

    private static Uri addSetting(Context context, Uri settingUri, String name, String value) {
        Uri ret = null;
        ContentValues ct = new ContentValues();
        ct.put(SettingsCol.Name, name);
        ct.put(SettingsCol.Value, value);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));
        //if exist, update
        if(isSettingKeyExist(context, settingUri, name)){
            updateSetting(context, settingUri, name, value);
        }
        else{
            ret = context.getContentResolver().insert(settingUri, ct);
        }

        return ret;
    }

    public static boolean updateSetting(Context context, Uri settingUri, String name, String value) {
        boolean ret = false;
        String where = "name = ?";
        ContentValues ct = new ContentValues();
        ct.put(SettingsCol.Value, value);
        ct.put(SettingsCol.ChangeDate, dateToString(new Date()));

        if(context.getContentResolver().update(settingUri, ct, where, new String[]{name}) > 0)
        {
            ret = true;
        }
        return ret;
    }

}
