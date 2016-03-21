package com.video.ui.loader;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.tv.ui.metro.model.Constants;
import com.tv.ui.metro.model.TokenInfo;
import com.video.cp.model.VideoPlayerConstant;
import com.video.ui.idata.AccountUtils;
import com.video.ui.idata.iDataORM;
import com.video.utils.WLReflect;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Random;

import miui.os.BuildV5;

public abstract class CommonBaseUrl {
	protected Context mAppContext;
    public static final String API_KEY          = "x457_VKnEUiNinik5bQUnw==";
    public static final String API_TOKEN        = "C8u617rK4WUQyyv1rZPT3B6gfDDndZudprPrRerZy6gUA=";

    public static String BaseHost = "media.tv.mitvos.com";
    public static String BaseURL  = "http://"+BaseHost +"";
    public static String BaseURLHttps  = "https://"+BaseHost +"";
    public static String LoginURL = "http://"+BaseHost +"/api/";

    public static void setBaseURL(String host){
        BaseHost = host;
        BaseURL  = "http://"+BaseHost +"/api/a1/";
        BaseURLHttps  = "https://"+BaseHost +"/api/a1/";
        LoginURL = "http://"+BaseHost +"/api/";
    }

    public static String miui_version = "";
    public static String version_type = "";
	public static int fetchedBaseUrl = -1;
    public static int versionCode    = -1;
    public static String versionName="";
	public CommonBaseUrl(Context appContext) {
		mAppContext = appContext.getApplicationContext();
        try {
            if(versionCode == -1) {
                versionCode = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(), 0).versionCode;
                if(TextUtils.isEmpty(iDataORM.application_type)) {
                    VersionSwitch(appContext);
                }
                versionName = mAppContext.getPackageManager().getPackageInfo(mAppContext.getPackageName(), 0).versionName;

                miui_version = WLReflect.getSystemProperties("ro.miui.ui.version.name");

                version_type = "stable";
                if(BuildV5.IS_ALPHA_BUILD){
                    version_type = "alpha";
                }else if(BuildV5.IS_DEVELOPMENT_VERSION){
                    version_type = "dev";
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception ne){
            ne.printStackTrace();
        }
    }

    public static String getMIUIVersion(){
        if(TextUtils.isEmpty(miui_version)){
            miui_version = WLReflect.getSystemProperties("ro.miui.ui.version.name");
        }

        return miui_version;
    }

    public static String getMIUIVersionType(){
        if(TextUtils.isEmpty(version_type)) {
            if(BuildV5.IS_STABLE_VERSION){
                version_type = "stable";
            }
            else if (BuildV5.IS_ALPHA_BUILD) {
                version_type = "alpha";
            } else if (BuildV5.IS_DEVELOPMENT_VERSION) {
                version_type = "dev";
            }
        }

        return version_type;
    }

    public static void VersionSwitch(Context context){
        iDataORM.application_type = iDataORM.getStringValue(context, iDataORM.version_type, iDataORM.version_default);

        try {
            if(versionCode == -1) {
                versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;

                miui_version = WLReflect.getSystemProperties("ro.miui.ui.version.name");

                if(BuildV5.IS_ALPHA_BUILD){
                    version_type = "alpha";
                }else if(BuildV5.IS_DEVELOPMENT_VERSION){
                    version_type = "dev";
                }
            }

            //ignore dev type
            if(iDataORM.version_dev.equals(iDataORM.application_type)) {
                iDataORM.application_type = iDataORM.version_stable;
                iDataORM.addSetting(context, iDataORM.version_type, iDataORM.version_stable);
            }

            //TODO
            /*
            int lastTwoVersion = Integer.parseInt(iDataORM.application_type);
            if((versionCode%100 - lastTwoVersion) <= 9 && (versionCode%100 - lastTwoVersion) >=0){
                versionCode = 100 * (versionCode / 100) + versionCode%100;
            }else {
                versionCode = 100 * (versionCode / 100) + lastTwoVersion;
            }*/
        }catch (Exception ne){}
    }

	protected abstract void getBaseURLFromLoacalSetting();

	public String addCommonParams(String url) {

        long now = System.currentTimeMillis();
        CommonUrlBuilder urlBuilder = new CommonUrlBuilder(url);

        //for OTT
        urlBuilder.put("ptf", "207");
        urlBuilder.put("codever", "1");
        urlBuilder.put("deviceid", getMacMd5Id(mAppContext));

        urlBuilder.put("_locale", getLocale());
        urlBuilder.put("_res", getResolution(mAppContext));
        urlBuilder.put("_devid", getDeviceMd5Id(mAppContext));
        urlBuilder.put("_md5", iDataORM.getStringValue(mAppContext, iDataORM.device_id_md5, ""));
        urlBuilder.put("_model", Build.DEVICE);
        urlBuilder.put("_miuiver", Build.VERSION.INCREMENTAL);
        urlBuilder.put("_model", Build.MODEL);
        urlBuilder.put("_andver", String.valueOf(Build.VERSION.SDK_INT));
        urlBuilder.put("_nonce", nonce());
        urlBuilder.put("_appver", String.valueOf(versionCode));
        urlBuilder.put("_ts", String.valueOf(System.currentTimeMillis() / 1000));
        urlBuilder.put("_ver", versionName);
        urlBuilder.put("_devtype", "1");
        urlBuilder.put("_eimi", com.video.ui.Util.getMD5(AccountUtils.getImeiId(mAppContext)));
        urlBuilder.put("_cam", com.video.ui.Util.getMD5(AccountUtils.getMacAddress(mAppContext)));
        urlBuilder.put("_diordna", com.video.ui.Util.getMD5(AccountUtils.getAndroidId(mAppContext)));
        urlBuilder.put("_android", AccountUtils.getAndroidId(mAppContext));
        urlBuilder.put("_miui", getMIUIVersion());
        urlBuilder.put("_version", getMIUIVersionType());
        urlBuilder.put("_plyver", VideoPlayerConstant.VERSION);
        if (url.contains("/login")) {
            urlBuilder.put("token", AccountToken());
            if (!TextUtils.isEmpty(accountName())) {
                urlBuilder.put("userid", accountName());
            }
        } else {
            urlBuilder.put("token", AccessToken());
        }

        //add op
        if (TextUtils.isEmpty(iDataORM._op_value)) {
            iDataORM._op_value = iDataORM.getStringValue(mAppContext, iDataORM._op, "");
            if (TextUtils.isEmpty(iDataORM._op_value)) {
                iDataORM._op_value = "000";
            }
        }
        if (!"000".equals(iDataORM._op_value))
            urlBuilder.put(iDataORM._op, iDataORM._op_value);

        String tmpUrl = urlBuilder.toUrl();
        String path;
        try {
            path = new URL(tmpUrl).getPath();
        } catch (MalformedURLException e) {
            return tmpUrl;
        }

        int indexOfPath = tmpUrl.indexOf(path);
        String strForSign = tmpUrl.substring(indexOfPath);
        if (Constants.DEBUG)
            Log.d("xxx", "strForSign " + strForSign);
        String sign = genSignature(strForSign);
        Log.d("xxx", "sign " + sign);

        urlBuilder.put("opaque", sign);

        if (Constants.DEBUG)
            Log.d("benchmark", "benchmark url:" + (System.currentTimeMillis() - now));

        return urlBuilder.toUrl();

	}

    protected String accountName(){
        if(iDataORM.mTokenInfo != null && iDataORM.mTokenInfo.userAccount != null){
            return iDataORM.mTokenInfo.userAccount.accountName;
        }else {
            String token = iDataORM.getInstance(mAppContext).getSettingValue(iDataORM.account_token);
            TokenInfo tokenInfo = AppGson.get().fromJson(token, TokenInfo.class);
            if (tokenInfo != null && tokenInfo.userAccount != null && !TextUtils.isEmpty(tokenInfo.userAccount.accountName)){
                iDataORM.mTokenInfo = tokenInfo;
                return iDataORM.mTokenInfo.userAccount.accountName;
            }
        }
        return null;
    }

    protected String AccessToken(){
        if(iDataORM.mTokenInfo != null && iDataORM.mTokenInfo.loginData != null
                && !TextUtils.isEmpty(iDataORM.mTokenInfo.loginData.access_token)){
            return iDataORM.mTokenInfo.loginData.access_token;
        }else {
            String token = iDataORM.getInstance(mAppContext).getSettingValue(iDataORM.account_token);
            TokenInfo tokenInfo = AppGson.get().fromJson(token, TokenInfo.class);
            if (tokenInfo != null && tokenInfo.loginData != null && !TextUtils.isEmpty(tokenInfo.loginData.access_token)){
                iDataORM.mTokenInfo = tokenInfo;
                return iDataORM.mTokenInfo.loginData.access_token;
            }
        }
        return API_TOKEN;
    }

    protected String AccessKey(){
        if(iDataORM.mTokenInfo != null && iDataORM.mTokenInfo.loginData != null
                && !TextUtils.isEmpty(iDataORM.mTokenInfo.loginData.access_key)){
            return iDataORM.mTokenInfo.loginData.access_key;
        }else {
            String token = iDataORM.getInstance(mAppContext).getSettingValue(iDataORM.account_token);
            TokenInfo tokenInfo = AppGson.get().fromJson(token, TokenInfo.class);
            if (tokenInfo != null && tokenInfo.loginData  != null && !TextUtils.isEmpty(tokenInfo.loginData.access_key)){
                iDataORM.mTokenInfo = tokenInfo;
                return iDataORM.mTokenInfo.loginData.access_key;
            }
        }
        return API_KEY;
    }

    protected String AccountToken(){
        if(iDataORM.mTokenInfo != null && iDataORM.mTokenInfo.userAccount != null && !TextUtils.isEmpty(iDataORM.mTokenInfo.userAccount.authToken)){
            return iDataORM.mTokenInfo.userAccount.authToken;
        }else {
            String token = iDataORM.getInstance(mAppContext).getSettingValue(iDataORM.account_token);
            TokenInfo tokenInfo = AppGson.get().fromJson(token, TokenInfo.class);
            if (tokenInfo != null && tokenInfo.userAccount != null && !TextUtils.isEmpty(tokenInfo.userAccount.authToken)){
                iDataORM.mTokenInfo = tokenInfo;
                return iDataORM.mTokenInfo.userAccount.authToken;
            }
        }
        return API_TOKEN;
    }

    protected String AccountKey(){
        if(iDataORM.mTokenInfo != null && iDataORM.mTokenInfo.userAccount != null && !TextUtils.isEmpty(iDataORM.mTokenInfo.userAccount.ssec)){
            return iDataORM.mTokenInfo.userAccount.ssec;
        }else {
            String token = iDataORM.getInstance(mAppContext).getSettingValue(iDataORM.account_token);
            TokenInfo tokenInfo = AppGson.get().fromJson(token, TokenInfo.class);
            if (tokenInfo != null && tokenInfo.userAccount != null && !TextUtils.isEmpty(tokenInfo.userAccount.ssec)){
                iDataORM.mTokenInfo = tokenInfo;
                return iDataORM.mTokenInfo.userAccount.ssec;
            }
        }
        return API_KEY;
    }


    private String nonce() {
        StringBuilder strBuilder = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());
        strBuilder.append(random.nextInt());
        return strBuilder.toString();
    }

	protected String getLocale() {
		Locale locale = mAppContext.getResources().getConfiguration().locale;
		return locale.getLanguage() + "_" + locale.getCountry();
	}

    public static final String screen_2k = "hd1440";
    public static String resolution = "";
	public static String getResolution(Context context) {
        if(TextUtils.isEmpty(resolution)) {
            DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
            if (displaymetrics.widthPixels == 720) {
                resolution = "hd720";
            } else if (displaymetrics.widthPixels == 1080) {
                resolution = "hd1080";
            } else if (displaymetrics.widthPixels == 1440) {
                resolution = "hd1440";
            } else if (displaymetrics.widthPixels == 2160) {
                resolution = "hd2160";
            } else {
                return displaymetrics.widthPixels + "x" + displaymetrics.heightPixels;
            }
        }

        return resolution;
	}

    public static String getImageScaleParam(Context context){
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();
        if (displaymetrics.widthPixels == 720) {
            return "!m";
        } else if (displaymetrics.widthPixels == 1080) {
            return "";
        }else if (displaymetrics.widthPixels == 1440) {
            return "";
        }
        else if (displaymetrics.widthPixels == 2160) {
            return "";
        } else {
            return "";
        }
    }

	public static String sDeviceMD5Id;

	protected static String getDeviceId(Context con) {
		return AccountUtils.getUid(con);
	}

    public static String getDeviceMd5Id(Context con) {
        String deviceID = AccountUtils.getUid(con);
        if (!TextUtils.isEmpty(deviceID)) {
            sDeviceMD5Id = com.video.ui.Util.getMD5(deviceID);
        }

        if("002".equals(iDataORM._op_value)) {
            return "11111940";
        }
        return sDeviceMD5Id;
    }

    public static String getMacMd5Id(Context con) {
        String deviceID = AccountUtils.getMacAddress(con);
        if (!TextUtils.isEmpty(deviceID)) {
            sDeviceMD5Id = com.video.ui.Util.getMD5(deviceID);
        }

        if("002".equals(iDataORM._op_value)) {
            return "11111940";
        }
        return sDeviceMD5Id;
    }


    @TargetApi(Build.VERSION_CODES.ECLAIR)
    protected static String getUserID(Context context){
        AccountManager mAccountManager = AccountManager.get(context);
        final Account[] account = mAccountManager.getAccountsByType("com.xiaomi");
        if(account.length > 0) {
            return account[0].toString();
        }
        return "";
    }

	protected String genSignature(String str) {
		String opaque = null;
		try {
            //for login account, we need account ssec
            //for video sign, we need use the login return key
            if(str.toLowerCase().contains("/login"))
			    opaque = Utils.getSignature(str.getBytes(), AccountKey().getBytes());
            else
                opaque = Utils.getSignature(str.getBytes(), AccessKey().getBytes());
		} catch (InvalidKeyException e) {
			Log.e("InvalidKeyException", "InvalidKeyException");
		} catch (NoSuchAlgorithmException e) {
			Log.e("Exception", "NoSuchAlgorithmException");
		}
		return opaque;
	}
}
