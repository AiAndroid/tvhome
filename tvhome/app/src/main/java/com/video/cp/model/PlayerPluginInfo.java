package com.video.cp.model;

import android.text.TextUtils;

/**
 * Created by lijianbo1 on 15-3-25.
 */
public class PlayerPluginInfo extends BaseEpItem {
    public static final String PLUGIN_TYPE_DEX = "apk";
    public static final String PLUGIN_TYPE_SO = "so";
    public static final String PLUGIN_TYPE_H5 = "h5";
    public static final String PLUGIN_TYPE_APP = "app";
    public static final String PLUGIN_TYPE_NONE = "none";

    // plugin common param
    private static final String KEY_PLUGIN_TYPE = "plugin_type";
    private static final String KEY_PLUGIN_HAS_H5_PARAM = "has_h5_param";
    private static final String KEY_PLUGIN_JS_URL_RETRIEVE = "js_url_retrieve"; // old version js to retrieve url
    private static final String KEY_PLUGIN_URL_RETRIEVE_UA = "customized_ua";
    private static final String KEY_PLUGIN_URL_RETRIEVE_JS_LIB = "js_lib_url_retrieve"; // new version of js function lib to retrieve url

    // dex or app plugin param
    private static final String KEY_PLUGIN_APK_NAME = "apk_name";
    private static final String KEY_PLUGIN_APP_PKG_NAME = "package_name";
    private static final String KEY_PLUGIN_APP_NEED_INSTALL = "need_install";
    private static final String KEY_PLUGIN_APP_NEED_UNZIP = "need_unzip";
    private static final String KEY_PLUGIN_APP_SILENCE_INSTALL ="silence_install";

    // dex plugin param
    private static final String KEY_PLUGIN_DEX_CLASS_NAME = "class_name";
    private static final String KEY_PLUGIN_DEX_OFFLINE = "dex_offline"; // true/false
    private static final String KEY_PLUGIN_OFFLINE_CLASS_NAME = "offline_class_name";

    // h5 plugin param
    private static final String KEY_PLUGIN_H5_JS_SETUP = "js_setup";
    private static final String KEY_PLUGIN_H5_JS_TEARDOWN = "js_teardown";
    private static final String KEY_PLUGIN_H5_JS_AUTO_PLAY = "js_auto_play";
    private static final String KEY_PLUGIN_H5_FULL_SCREEN_AUTO_PLAY = "full_screen_play";
    private static final String KEY_PLUGIN_H5_USE_LOCAL_PLAYER = "use_local_player";


    private String Md5;
    private String PluginDownloadUrl;

    public PlayerPluginInfo(RawCpItem item) {
        copyFrom(item);
        setType(ExternalPackageData.TYPE_PLAYER_PLUGIN);
    }

    @Override
    public void copyFrom(RawCpItem item) {
        if (item != null) {
            super.copyFrom(item);
            if (item.plugin != null) {
                Md5 = item.plugin.md5;
                PluginDownloadUrl = item.plugin.url;
            }
        }
    }

    public String getCp() {
        return getEpId();
    }

    public String getMd5() {
        return Md5;
    }

    public String getPluginDownloadUrl() {
        return PluginDownloadUrl;
    }

    //only used on TEST_MODE
    public void setPluginDownloadUrl(String url){
        PluginDownloadUrl = url;
    }

    public String getPluginType() {
        return Params.get(KEY_PLUGIN_TYPE);
    }

    public boolean hasH5Param() {
        return "true".equals(Params.get(KEY_PLUGIN_HAS_H5_PARAM))
                || PLUGIN_TYPE_H5.equals(getPluginType());
    }

    public String getPluginApkName() {
        String plugin_type = getPluginType();
        if (PLUGIN_TYPE_DEX.equals(plugin_type) || PLUGIN_TYPE_APP.equals(plugin_type)) {
            return Params.get(KEY_PLUGIN_APK_NAME);
        }
        return null;
    }

    public String getPluginClassName() {
        String plugin_type = getPluginType();
        if (!TextUtils.isEmpty(plugin_type) && PLUGIN_TYPE_DEX.equals(plugin_type)) {
            return Params.get(KEY_PLUGIN_DEX_CLASS_NAME);
        }
        return null;
    }

    public String getOfflineDexClassName() {
        if (canDexOffline()) {
            return getParams(KEY_PLUGIN_OFFLINE_CLASS_NAME);
        }
        return null;
    }

    public boolean canDexOffline() {
        String plugin_type = getPluginType();
        if (!TextUtils.isEmpty(plugin_type) && PLUGIN_TYPE_DEX.equals(plugin_type)) {
            return "true".equals(getParams(KEY_PLUGIN_DEX_OFFLINE));
        }
        return false;
    }

    public String getPluginPkgName() {
        String plugin_type = getPluginType();
        if (PLUGIN_TYPE_APP.equals(plugin_type) || PLUGIN_TYPE_DEX.equals(plugin_type)) {
            return Params.get(KEY_PLUGIN_APP_PKG_NAME);
        }
        return null;
    }

    public boolean isAppPluginNeedInstall() {
        String plugin_type = getPluginType();
        if (PLUGIN_TYPE_APP.equals(plugin_type) || PLUGIN_TYPE_DEX.equals(plugin_type)) {
            return "true".equals(Params.get(KEY_PLUGIN_APP_NEED_INSTALL));
        }
        return false;
    }

    public boolean isAppPluginNeedUnzip() {
        String plugin_type = getPluginType();
        if (PLUGIN_TYPE_APP.equals(plugin_type) || PLUGIN_TYPE_DEX.equals(plugin_type)) {
            return "true".equals(Params.get(KEY_PLUGIN_APP_NEED_UNZIP));
        }
        return false;
    }

    public boolean isAppPluginSilenceInstall() {
        String plugin_type = getPluginType();
        if (PLUGIN_TYPE_APP.equals(plugin_type) || PLUGIN_TYPE_DEX.equals(plugin_type)) {
            return "true".equals(Params.get(KEY_PLUGIN_APP_SILENCE_INSTALL));
        }
        return false;
    }

    public String getPluginJsSetup() {
        if (hasH5Param()) {
            return Params.get(KEY_PLUGIN_H5_JS_SETUP);
        }
        return null;
    }

    public String getPluginJsTeardown() {
        if (hasH5Param()) {
            return Params.get(KEY_PLUGIN_H5_JS_TEARDOWN);
        }
        return null;
    }

    public String getPluginJsAutoPlay() {
        if (hasH5Param()) {
            return Params.get(KEY_PLUGIN_H5_JS_AUTO_PLAY);
        }
        return null;
    }

    public String getPluginJsUrlRetrieve() {
        // all cp need this js
        return Params.get(KEY_PLUGIN_JS_URL_RETRIEVE);
    }

    public String getPluginJsLibToRetrieveUrl() {
        return Params.get(KEY_PLUGIN_URL_RETRIEVE_JS_LIB);
    }

    public String getPluginUAToRetrieveUrl() {
        return Params.get(KEY_PLUGIN_URL_RETRIEVE_UA);
    }

    public boolean getPluginH5FullScreenPlay() {
        if (hasH5Param()) {
            return "true".equalsIgnoreCase(Params.get(KEY_PLUGIN_H5_FULL_SCREEN_AUTO_PLAY));
        }
        return false;
    }

    public boolean getPluginH5UseLocalPlayer() {
        if (hasH5Param()) {
            return !"false".equalsIgnoreCase(Params.get(KEY_PLUGIN_H5_USE_LOCAL_PLAYER)); // default is true
        }
        return true; // default is true;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(super.toString())
                .append(KEY_PLUGIN_TYPE).append(": ").append(getPluginType()).append("\n");

        if (PLUGIN_TYPE_DEX.equals(getPluginType())) {
            sb.append(KEY_PLUGIN_APK_NAME).append(": ").append(getPluginApkName()).append("\n")
                .append(KEY_PLUGIN_DEX_CLASS_NAME).append(": ").append(getPluginClassName()).append("\n")
                .append("md5").append(": ").append(getMd5()).append("\n")
                .append("url").append(": ").append(getPluginDownloadUrl()).append("\n");
        }

        if (hasH5Param()) {
            sb.append(KEY_PLUGIN_H5_FULL_SCREEN_AUTO_PLAY).append(": ").append(getPluginH5FullScreenPlay()).append("\n");
            sb.append(KEY_PLUGIN_H5_USE_LOCAL_PLAYER).append(": ").append(getPluginH5UseLocalPlayer()).append("\n");

            int length;
            length = TextUtils.isEmpty(getPluginJsSetup()) ? 0 : getPluginJsSetup().length();
            sb.append(KEY_PLUGIN_H5_JS_SETUP).append(": length: ").append(length).append("\n");

            length = TextUtils.isEmpty(getPluginJsTeardown()) ? 0 : getPluginJsTeardown().length();
            sb.append(KEY_PLUGIN_H5_JS_TEARDOWN).append(": length: ").append(length).append("\n");

            length = TextUtils.isEmpty(getPluginJsAutoPlay()) ? 0 : getPluginJsAutoPlay().length();
            sb.append(KEY_PLUGIN_H5_JS_AUTO_PLAY).append(": length: ").append(length).append("\n");
        }

        if (PLUGIN_TYPE_APP.equals(getPluginType()) || PLUGIN_TYPE_DEX.equals(getPluginType())) {
            sb.append(KEY_PLUGIN_APK_NAME).append(": ").append(getPluginApkName()).append("\n")
                    .append(KEY_PLUGIN_APP_PKG_NAME).append(": ").append(getPluginPkgName()).append("\n")
                    .append(KEY_PLUGIN_APP_NEED_INSTALL).append(": ").append(isAppPluginNeedInstall()).append("\n")
                    .append(KEY_PLUGIN_APP_NEED_UNZIP).append(": ").append(isAppPluginNeedUnzip()).append("\n")
                    .append(KEY_PLUGIN_APP_SILENCE_INSTALL).append(": ").append(isAppPluginSilenceInstall()).append("\n");
        }


        int len = TextUtils.isEmpty(getPluginJsUrlRetrieve()) ? 0 : getPluginJsUrlRetrieve().length();
        sb.append(KEY_PLUGIN_JS_URL_RETRIEVE).append(": length: ").append(len).append("\n");

        return sb.toString();
    }
}
