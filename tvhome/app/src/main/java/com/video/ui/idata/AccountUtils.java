package com.video.ui.idata;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import android.text.TextUtils;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.video.utils.WLReflect;

public class AccountUtils {

	public static String getInternalFilesDir(Context context) {
		File file = context.getFilesDir();
		if(file != null) {
			return file.getAbsolutePath();
		}
		return null;
	}
	

    public static String getAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);    	
    }

    private static String imeiid = "";
    public static String getImeiId(Context context){

        return getDeviceId(context);

        /*
        if(TextUtils.isEmpty(imeiid)) {
            TelephonyManager tm = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                try {
                    imeiid = tm.getDeviceId(); // get imei
                    return imeiid;
                } catch (Exception e) {
                    Log.d("AccountUtils", e.getLocalizedMessage());
                }
            }
        }
        return imeiid;
        */
    }

    private static final String PERSIST_RADIO_MEID = "persist.radio.meid";
    private static final String PERSIST_RADIO_IMEI2 = "persist.radio.imei2";
    private static final String PERSIST_RADIO_IMEI1 = "persist.radio.imei1";
    private static final String PERSIST_RADIO_IMEI = "persist.radio.imei";

    public static ArrayList<String> getIMEI(Context context) {
        ArrayList<String> results = new ArrayList<String>();
        String property;

        if (!TextUtils.isEmpty(property =  WLReflect.getSystemProperties(PERSIST_RADIO_IMEI))) {
            results.add(property);
            return results;
        }

        if (!TextUtils.isEmpty(property =  WLReflect.getSystemProperties(PERSIST_RADIO_IMEI1))) {
            results.add(property);
            return results;
        }

        if (!TextUtils.isEmpty(property =  WLReflect.getSystemProperties(PERSIST_RADIO_IMEI2))) {
            results.add(property);
            return results;
        }


        if (!TextUtils.isEmpty(property = WLReflect.getSystemProperties(PERSIST_RADIO_MEID))) {
            results.add(property);
            return results;
        }

        if (!TextUtils.isEmpty(property =  WLReflect.getSystemProperties("ro.ril.miui.imei"))) {
            results.add(property);
            return results;
        }

        if (!TextUtils.isEmpty(property = WLReflect.getSystemProperties("ro.ril.miui.imei.0"))) {
            results.add(property);
            return results;
        }
        if (!TextUtils.isEmpty(property =  WLReflect.getSystemProperties("ro.ril.miui.imei.1"))) {
            results.add(property);
            return results;
        }

        if (!TextUtils.isEmpty(property =  WLReflect.getSystemProperties("ro.ril.oem.imei"))) {
            results.add(property);
            return results;
        }

        if (!TextUtils.isEmpty(property =  WLReflect.getSystemProperties("ro.ril.oem.imei1"))) {
            results.add(property);
            return results;
        }

        return results;
    }

    public static String getDeviceId(Context context) {
        String newId = "";//TelephonyManager.getDefault().getDeviceId();

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null) {
            try {
                //long pre = System.currentTimeMillis();
                newId = tm.getDeviceId();
                //Log.d("getDeviceId", "time:"+(System.currentTimeMillis() - pre));
            } catch (Exception e) {
                Log.d("AccountUtils", e.getLocalizedMessage());
            }
        }

        if(TextUtils.isEmpty(newId) && !TextUtils.isEmpty(imeiid)) {
            return imeiid;
        } else if(!TextUtils.isEmpty(newId) && TextUtils.equals(newId, imeiid)) {
            return imeiid;
        } else {
            String oldId = null;
            if(context != null) {
                try {
                    oldId = Settings.System.getString(context.getContentResolver(), "last_valid_device_id");
                } catch (Exception var5) {
                    ;
                }
            }

            if(TextUtils.isEmpty(newId)) {
                newId = oldId;
            } else if(!TextUtils.equals(newId, oldId)) {
                try {
                    Settings.System.putString(context.getContentResolver(), "last_valid_device_id", newId);
                } catch (Exception var4) {
                    ;
                }
            }

            imeiid = newId;

            if(TextUtils.isEmpty(imeiid)){
                ArrayList<String> ss = getIMEI(context);
                if(ss.size() > 0){
                    imeiid = ss.get(0);

                    try {
                        Settings.System.putString(context.getContentResolver(), "last_valid_device_id", newId);
                    } catch (Exception var4) {
                        ;
                    }
                }
            }
            return imeiid;
        }
    }

    private static String macAddress = "";
    public static String getMacAddress(Context context){
        if(TextUtils.isEmpty(macAddress)) {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo != null) {
                macAddress = wifiInfo.getMacAddress();
                return macAddress;
            }
        }
        return macAddress;
    }

    public static String getUid(Context context){
        String imei = getImeiId(context);
    	if(!TextUtils.isEmpty(imei)){
    		return imei;
    	}else if(!TextUtils.isEmpty(getMacAddress(context))){
    		return getMacAddress(context);
    	}else if(!TextUtils.isEmpty(getAndroidId(context))){
    		return getAndroidId(context);
    	}
    	return null;
    }
}
