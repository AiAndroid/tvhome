package com.video.cp.model;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lijianbo1 on 15-3-25.
 */
public class ExternalPackageData {
    public static final String TYPE_PLAYER_PLUGIN = "player_plugin";
    public static final String TYPE_APP_PLUGIN = "app_plugin";
    public static final String TYPE_APP_APK = "app_apk";

    private ArrayList<RawCpItem> data;

    private transient HashMap<String, PlayerPluginInfo> mPlayerPluginInfo = new HashMap<String, PlayerPluginInfo>();

    private transient HashMap<String, AppPkgInfo> mAppPkgInfo = new HashMap<String, AppPkgInfo>();

    private transient HashMap<String, AppPluginCpInfo> mAppPluginCpInfo = new HashMap<String, AppPluginCpInfo>();

    public synchronized void init() {
        mPlayerPluginInfo.clear();
        mAppPkgInfo.clear();
        mAppPluginCpInfo.clear();

        for (RawCpItem item : data) {
            if (TYPE_PLAYER_PLUGIN.equals(item.getItemType())) {
                PlayerPluginInfo i = new PlayerPluginInfo(item);
                mPlayerPluginInfo.put(i.getCp(), i);
                continue;
            }

            if (TYPE_APP_PLUGIN.equals(item.getItemType())) {
                AppPluginCpInfo i = new AppPluginCpInfo(item);
                mAppPluginCpInfo.put(i.getEpId(), i);
                continue;
            }

            if (TYPE_APP_APK.equals(item.getItemType())) {
                AppPkgInfo i = new AppPkgInfo(item);
                mAppPkgInfo.put(i.getEpId(), i);
                continue;
            }
        }
    }

    public ArrayList<RawCpItem> getPluginItemByType(String type) {
        ArrayList<RawCpItem> item_list = new ArrayList<RawCpItem>();
        if (TextUtils.isEmpty(type)) {
            return null;
        }

        for (RawCpItem item : data) {
            if (item != null && type.equals(item.getItemType())) {
                item_list.add(item);
            }
        }
        return item_list;
    }

    public PlayerPluginInfo getPlayerPluginCpInfo(String cp) {
        return mPlayerPluginInfo.get(cp);
    }

    public AppPluginCpInfo getAppPluginCpInfo(String cp) {
        return mAppPluginCpInfo.get(cp);
    }

    public AppPkgInfo getAppPkgInfo(String cp) {
        return mAppPkgInfo.get(cp);
    }

    public AppPkgInfo getAppPkgInfoByName(String package_name) {
        if (TextUtils.isEmpty(package_name)) {
            return null;
        }

        for (AppPkgInfo pi : mAppPkgInfo.values()) {
            if (package_name.equalsIgnoreCase(pi.getAppPkgName())) {
                return pi;
            }
        }

        return null;
    }

    public List<String> getPlayPluginCpList() {
        return new ArrayList<String> (mPlayerPluginInfo.keySet());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

//        sb.append("data: \n");
//
//        for (RawCpItem item : data) {
//            if (item != null) {
//                sb.append(item.toString()).append("\n ............ \n");
//            }
//        }

        sb.append("SDK Plugin: \n");
        sb.append("===================\n");
        for (String cp : mPlayerPluginInfo.keySet()) {
            sb.append(mPlayerPluginInfo.get(cp))
                    .append("----------\n");
        }

        sb.append("AppPlugin: \n");
        sb.append("===================\n");
        for (String cp: mAppPluginCpInfo.keySet()) {
            sb.append(mAppPluginCpInfo.get(cp))
                    .append("----------\n");
        }

        sb.append("AppAPk: \n");
        sb.append("===================\n");
        for (String cp: mAppPkgInfo.keySet()) {
            sb.append(mAppPkgInfo.get(cp))
                    .append("----------\n");
        }

        sb.append("===================\n");
        return sb.toString();
    }
}
