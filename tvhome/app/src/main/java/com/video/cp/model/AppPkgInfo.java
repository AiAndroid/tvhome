package com.video.cp.model;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by lijianbo1 on 15-3-25.
 */
public class AppPkgInfo extends BaseEpItem {
    private static final String TAG = "AppPkgInfo";

    // define key here
    private static final String KEY_APP_NEED_INSTALL = "need_install";
    private static final String KEY_APP_PACKAGE_NAME = "package_name";
    private static final String KEY_APP_VERSION_CODE = "version_code";
    private static final String KEY_APP_SILENCE_INSTALL = "silence_install";
    private static final String KEY_APP_NEED_UNZIP = "need_unzip";
    private static final String KEY_APP_APK_NAME = "apk_name";
    private static final String KEY_APP_DISPLAY_NAME = "display_name";
    private static final String KEY_APP_CANCELABLE_DOWNLOAD = "cancelable_download";
    private static final String KEY_APP_SHOW_START_DIALOG = "show_start_dialog";
    private static final String KEY_APP_ALWAYS_LAUNCH_APP = "always_launch_app";



    private String Md5;
    private String DownloadUrl;

    public AppPkgInfo(RawCpItem item) {
        copyFrom(item);
        setType(ExternalPackageData.TYPE_APP_APK);
    }

    @Override
    public void copyFrom(RawCpItem item) {
        super.copyFrom(item);
        if (item.plugin != null) {
            Md5 = item.plugin.md5;
            DownloadUrl = item.plugin.url;
        }
    }

    public String getAppPkgName() {
        return Params.get(KEY_APP_PACKAGE_NAME);
    }

    public String getDisplayName() {
        return Params.get(KEY_APP_DISPLAY_NAME);
    }

    public String getAppApkName() {
        return Params.get(KEY_APP_APK_NAME);
    }

    public boolean installSilence(boolean defaultValue) {
        String ret_str = Params.get(KEY_APP_SILENCE_INSTALL);
        if (!TextUtils.isEmpty(ret_str)) {
            return "true".equalsIgnoreCase(ret_str);
        }

        return defaultValue;
    }

    public boolean needUnzip(boolean defaultValue) {
        String ret_str = Params.get(KEY_APP_NEED_UNZIP);
        if (!TextUtils.isEmpty(ret_str)) {
            return "true".equalsIgnoreCase(ret_str);
        }

        return defaultValue;
    }

    public int getAppVersionCode() {
        String ver_str =  Params.get(KEY_APP_VERSION_CODE);
        try {
            if (TextUtils.isEmpty(ver_str)) {
                return 0;
            }

            return Integer.valueOf(ver_str);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Log.d(TAG, "Invalidate version number");
        }
        return 0;
    }

    public boolean needInstall(boolean defaultValue) {
        String str = Params.get(KEY_APP_NEED_INSTALL);
        if (!TextUtils.isEmpty(str)) {
            return "true".equalsIgnoreCase(str);
        }
        return defaultValue;
    }

    public String getMd5() {
        return Md5;
    }

    public String getDownloadUrl() {
        return DownloadUrl;
    }

    public boolean canCancelDownload(boolean defaultValue) {
        String str = Params.get(KEY_APP_CANCELABLE_DOWNLOAD);
        if (!TextUtils.isEmpty(str)) {
            return "true".equalsIgnoreCase(str);
        }
        return defaultValue;
    }

    public boolean showStartDownloadDlg(boolean defaultValue) {
        String str = Params.get(KEY_APP_SHOW_START_DIALOG);
        if (!TextUtils.isEmpty(str)) {
            return "true".equalsIgnoreCase(str);
        }
        return defaultValue;
    }

    public boolean alwaysLaunchApp(boolean defaultValue) {
        String str = Params.get(KEY_APP_ALWAYS_LAUNCH_APP);
        if (!TextUtils.isEmpty(str)) {
            return "true".equalsIgnoreCase(str);
        }
        return defaultValue;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(super.toString())
                .append("download_url: ").append(getDownloadUrl()).append("\n")
                .append("md5: ").append(getMd5()).append("\n")
                .append(KEY_APP_PACKAGE_NAME).append(": ").append(Params.get(KEY_APP_PACKAGE_NAME)).append("\n")
                .append(KEY_APP_DISPLAY_NAME).append(": ").append(Params.get(KEY_APP_DISPLAY_NAME)).append("\n")
                .append(KEY_APP_APK_NAME).append(": ").append(Params.get(KEY_APP_APK_NAME)).append("\n")
                .append(KEY_APP_VERSION_CODE).append(": ").append(Params.get(KEY_APP_VERSION_CODE)).append("\n")
                .append(KEY_APP_NEED_INSTALL).append(": ").append(Params.get(KEY_APP_NEED_INSTALL)).append("\n")
                .append(KEY_APP_SILENCE_INSTALL).append(": ").append(Params.get(KEY_APP_SILENCE_INSTALL)).append("\n")
                .append(KEY_APP_NEED_UNZIP).append(": ").append(Params.get(KEY_APP_NEED_UNZIP)).append("\n")
                .append(KEY_APP_ALWAYS_LAUNCH_APP).append(": ").append(Params.get(KEY_APP_ALWAYS_LAUNCH_APP)).append("\n")
                .append(KEY_APP_CANCELABLE_DOWNLOAD).append(": ").append(Params.get(KEY_APP_CANCELABLE_DOWNLOAD)).append("\n")
                .append(KEY_APP_SHOW_START_DIALOG).append(": ").append(Params.get(KEY_APP_SHOW_START_DIALOG)).append("\n");
        return sb.toString();
    }
}
