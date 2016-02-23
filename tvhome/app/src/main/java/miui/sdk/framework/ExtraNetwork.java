package miui.sdk.framework;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Network & Data Usage related
 */
public final class ExtraNetwork {

    /**
     * Action to request network diagnosis when network is blocked
     * 
     * @permission Need permission miui.permission.EXTRA_NETWORK
     */
    public static final String ACTION_NETWORK_BLOCKED = "miui.intent.action.NETWORK_BLOCKED";
    /**
     * Action to request network diagnosis when network is connected
     * Need permission miui.permission.EXTRA_NETWORK
     */
    public static final String ACTION_NETWORK_CONNECTED = "miui.intent.action.NETWORK_CONNECTED";

    /* Constants for firewall seting and checking */
    private static final String FIREWALL_URI_STR = "content://com.miui.networkassistant.provider/firewall/%s";
    private static final String FIREWALL_PACKAGE_NAME = "package_name";
    private static final String FIREWALL_MOBILE_RULE = "mobile_rule";
    private static final String FIREWALL_WIFI_RULE = "wifi_rule";

    /* Constants for traffic pruchase */
    private static final String TRAFFIC_PURCHASE_STATUS_URI_STR = "content://com.miui.networkassistant.provider/na_traffic_purchase";
    private static final String TRAFFIC_PURCHASE_STATUS_URI_STR_ISMI = "content://com.miui.networkassistant.provider/na_traffic_purchase/slotId/%d";
    private static final String NETWORKASSISTANT_PURCHASE_ACTION = "miui.intent.action.NETWORKASSISTANT_TRAFFIC_PURCHASE";
    private static final String TRAFFIC_PURCHASE_ENABLED = "traffic_purchase_enabled";
    private static final String BUNDLE_KEY_SLOTID = "bundle_key_slotid";
    private static final String BUNDLE_KEY_COMMON = "bundle_key_com";
    private static final String BUNDLE_KEY_PURCHASE_FROM = "bundle_key_purchase_from";

    /* Constants for datausage status query */
    private static final String URI_NETWORK_TRAFFIC_INFO = "content://com.miui.networkassistant.provider/datausage_status";
    private static final String COLUMN_NAME_TOTAL_LIMIT = "total_limit";
    private static final String COLUMN_NAME_MONTH_USED = "month_used";
    private static final String COLUMN_NAME_TODAY_USED = "today_used";
    private static final String COLUMN_NAME_MONTH_WARNING = "month_warning";

    /*
     * the constant is used by status bar when it uses navigateToTrafficPurchasePage() interface ,for sourceFrom param
     */
    public static final String TRACK_PURCHASE_FROM_STATUS_BAR = "100003";

    /*
     * the constant is used by sercurity center when it uses navigateToTrafficPurchasePage() interface, for sourceFrom
     * param
     */
    public static final String TRACK_PURCHASE_FROM_SERCURITY_CENTER_EXAM = "100008";

    /**
     * Register firewall content provider observer
     * 
     * @param context
     * @param observer
     */
    public static void registerFirewallContentObserver(Context context, ContentObserver observer) {
        context.getContentResolver().registerContentObserver(Uri.parse(String.format(FIREWALL_URI_STR, "")), true,
                observer);
    }

    /**
     * Unregister firewall content provider observer
     * 
     * @param context
     * @param observer
     */
    public static void unRegisterFirewallContentObserver(Context context, ContentObserver observer) {
        context.getContentResolver().unregisterContentObserver(observer);
    }

    /**
     * Set mobile network firewall rule.
     * 
     * @permission Need permission miui.permission.EXTRA_NETWORK
     * @param context
     * @param pkgName
     * @param isRestrict
     * @return true if set succeed
     */
    public static boolean setMobileRestrict(Context context, String pkgName, boolean isRestrict) {
        return setNetworkRestrict(context, pkgName, FIREWALL_MOBILE_RULE, isRestrict);
    }

    /**
     * Set Wifi network firewall rule.
     * 
     * @permission Need permission miui.permission.EXTRA_NETWORK
     * @param context
     * @param pkgName
     * @param isRestrict
     * @return true if set succeed
     */
    public static boolean setWifiRestrict(Context context, String pkgName, boolean isRestrict) {
        return setNetworkRestrict(context, pkgName, FIREWALL_WIFI_RULE, isRestrict);
    }

    private static boolean setNetworkRestrict(Context context, String pkgName, String networkType, boolean isRestrict) {
        try {
            Uri uri = Uri.parse(String.format(FIREWALL_URI_STR, pkgName));
            if (uri != null) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues params = new ContentValues();
                params.put(networkType, !isRestrict);
                return (resolver.update(uri, params, null, null) == 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check whether mobile network connection is restrict by package name
     * 
     * @permission Need permission miui.permission.EXTRA_NETWORK
     * @param context
     * @param pkgName
     * @return true if query provicer successful and get restrict result
     */
    public static boolean isMobileRestrict(Context context, String pkgName) {
        return isNetworkRestrict(context, pkgName, FIREWALL_MOBILE_RULE);
    }

    /**
     * Check whether WIFI network connection is restrict by package name
     * 
     * @permission Need permission miui.permission.EXTRA_NETWORK
     * @param context
     * @param pkgName
     * @return true if query provicer successful and get restrict result
     */
    public static boolean isWifiRestrict(Context context, String pkgName) {
        return isNetworkRestrict(context, pkgName, FIREWALL_WIFI_RULE);
    }

    private static boolean isNetworkRestrict(Context context, String pkgName, String networkType) {
        if (context == null || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        Cursor cursor = null;
        try {
            final Uri uri = Uri.parse(String.format(FIREWALL_URI_STR, pkgName));
            if (uri != null) {
                ContentResolver resolver = context.getContentResolver();
                cursor = resolver.query(uri, null, null, new String[] { pkgName }, null);
                if (cursor != null && cursor.moveToFirst()) {
                    String packageName = cursor.getString(cursor.getColumnIndex(FIREWALL_PACKAGE_NAME));
                    if (pkgName.equals(packageName)) {
                        final int rule = cursor.getInt(cursor.getColumnIndex(networkType));
                        return rule == 1;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    /**
     * check traffic purchase supported to other APPs Need permission miui.permission.EXTRA_NETWORK
     * 
     * @param context
     * @return true if purchase enabled
     */
    public static boolean isTrafficPurchaseSupported(Context context) {
        if (context != null) {
            try {
                final Uri uri = Uri.parse(TRAFFIC_PURCHASE_STATUS_URI_STR);
                return queryTrafficPurchaseStatus(context, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * check traffic purchase supported to other APPs Need permission miui.permission.EXTRA_NETWORK
     * 
     * @param context
     * @param slotId
     * @return true if purchase enabled
     */
    public static boolean isTrafficPurchaseSupported(Context context, int slotId) {
        if (context != null && slotId >= 0 && slotId < 2) {
            try {
                final Uri uri = Uri.parse(String.format(TRAFFIC_PURCHASE_STATUS_URI_STR_ISMI, slotId));
                return queryTrafficPurchaseStatus(context, uri);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static boolean queryTrafficPurchaseStatus(Context context, Uri uri) {
        Cursor cursor = null;
        boolean result = false;
        try {
            if (uri != null) {
                ContentResolver resolver = context.getContentResolver();
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    result = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(TRAFFIC_PURCHASE_ENABLED)));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * navigate traffic purchase activity to other APPs Need permission miui.permission.EXTRA_NETWORK
     * 
     * @param context
     */
    public static void navigateToTrafficPurchasePage(Context context) {
        Intent intent = new Intent(NETWORKASSISTANT_PURCHASE_ACTION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * navigate traffic purchase activity to other APPs Need permission miui.permission.EXTRA_NETWORK
     * 
     * @param context
     * @param sourceFrom
     *            track apps used this interface
     */
    public static void navigateToTrafficPurchasePage(Context context, String sourceFrom) {
        Intent intent = new Intent(NETWORKASSISTANT_PURCHASE_ACTION);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PURCHASE_FROM, sourceFrom);
        intent.putExtra(BUNDLE_KEY_COMMON, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * navigate traffic purchase activity to other APPs Need permission miui.permission.EXTRA_NETWORK
     * 
     * @param context
     * @param slotId
     */
    public static void navigateToTrafficPurchasePage(Context context, int slotId) {
        Intent intent = new Intent(NETWORKASSISTANT_PURCHASE_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_SLOTID, slotId);
        intent.putExtra(BUNDLE_KEY_COMMON, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * navigate traffic purchase activity to other APPs Need permission miui.permission.EXTRA_NETWORK
     * 
     * @param context
     * @param slotId
     * @param sourceFrom
     *            track apps used this interface
     */
    public static void navigateToTrafficPurchasePage(Context context, int slotId, String sourceFrom) {
        Intent intent = new Intent(NETWORKASSISTANT_PURCHASE_ACTION);
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY_SLOTID, slotId);
        bundle.putString(BUNDLE_KEY_PURCHASE_FROM, sourceFrom);
        intent.putExtra(BUNDLE_KEY_COMMON, bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /*
     * Data usage detail entity
     */
    public static final class DataUsageDetail {
        public long monthTotal;
        public long monthUsed;
        public long monthWarning;
        public long todayUsed;

        public DataUsageDetail(long monthTotal, long monthUsed, long monthWarning, long todayUsed) {
            this.monthTotal = monthTotal;
            this.monthUsed = monthUsed;
            this.monthWarning = monthWarning;
            this.todayUsed = todayUsed;
        }

        @Override
        public String toString() {
            return String.format("monthTotal:%s, monthUsed:%s, monthWarning:%s, todayUsed:%s", monthTotal, monthUsed,
                    monthWarning, todayUsed);
        }
    }

    /**
     * get data usage detail of user by sim slot.
     * 
     * @permission Need permission miui.permission.EXTRA_NETWORK
     * @return {@link DataUsageDetail} or null
     */
    public static DataUsageDetail getUserDataUsageDetail(Context context) {
        if (context != null) {
            Cursor cursor = null;
            try {
                Uri uri = Uri.parse(URI_NETWORK_TRAFFIC_INFO);
                cursor = context.getContentResolver().query(uri, null, null, null, null);
                boolean dataGetted = false;
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        long monthTotal = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TOTAL_LIMIT));
                        long monthUsed = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_MONTH_USED));
                        long monthWarning = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_MONTH_WARNING));
                        long todayUsed = cursor.getLong(cursor.getColumnIndex(COLUMN_NAME_TODAY_USED));
                        return new DataUsageDetail(monthTotal, monthUsed, monthWarning, todayUsed);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                    cursor = null;
                }
            }
        }
        return null;
    }
}
