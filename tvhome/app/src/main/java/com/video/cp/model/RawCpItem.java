package com.video.cp.model;

import java.util.HashMap;

/**
 * Created by lijianbo1 on 15-3-31.
 */
public class RawCpItem {
    public String name;
    public String id;
    public String icon;

    public RawAppItem app;
    public RawPluginItem plugin;

    static class RawAppItem {
        public String apk_url;
        public String desc;

        @Override
        public String toString() {
            return "RawAppItem{" +
                    "apk_url='" + apk_url + '\'' +
                    ", desc='" + desc + '\'' +
                    '}';
        }
    }

    static class RawPluginItem {
        public String type;
        public String url;
        public String md5;
        public String version_name;
        public String version;
        public String updated_at;
        public HashMap<String, String> params;

        @Override
        public String toString() {
            return "RawPluginItem{" +
                    ", type='" + type + '\'' +
                    ", url='" + url + '\'' +
                    ", md5='" + md5 + '\'' +
                    ", version_name='" + version_name + '\'' +
                    ", version'" + version + '\'' +
                    ", updated_at='" + updated_at + '\'' +
                    ", params=" + params +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "RawCpItem{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", icon='" + icon + '\'' +
                ", app=" + app +
                ", plugin=" + plugin +
                '}';
    }

    public String getItemType() {
        if (plugin != null) {
            return plugin.type;
        }
        return null;
    }
}
