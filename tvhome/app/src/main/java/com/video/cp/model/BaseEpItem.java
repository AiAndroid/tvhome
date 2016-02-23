package com.video.cp.model;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.util.HashMap;

/**
 * Created by lijianbo1 on 15-3-25.
 */
public class BaseEpItem implements Serializable{
    private static final long serialVersionUID = 1L;

    private String Name;
    private String Id;
    private String Type;

    private String IconUrl;
    private String Desc;
    private String ApkClientUrl;

    private String VersionName;
    private int VersionCode;
    private String UpdatedTime;

    protected HashMap<String, String> Params;

    private RawCpItem mRawItem;

    public void copyFrom(RawCpItem item) {
        if (item != null) {
            mRawItem = item;
            Name = item.name;
            Id = item.id;
            IconUrl = item.icon;

            if (item.app != null) {
                Desc = item.app.desc;
                ApkClientUrl = item.app.apk_url;
            }

            if (item.plugin != null) {
                Type = item.plugin.type;
                VersionName = item.plugin.version_name;
                try {
                    VersionCode = Integer.valueOf(item.plugin.version);
                } catch (Exception e) {
                    e.printStackTrace();
                    VersionCode = 0;
                }
                UpdatedTime = item.plugin.updated_at;

                if (item.plugin.params != null) {
                    Params = item.plugin.params;
                }
            }

            if (Params == null) {
                Params = new HashMap<String, String>();
            }
        }
    }

    public String getType() {
        return Type;
    }

    protected void setType(String type) {
        Type = type;
    }

    public <T extends BaseEpItem> T asType(Class<T> cls) {
        try {
            Constructor<?> ctor = cls.getConstructor(RawCpItem.class);
            T obj = (T)ctor.newInstance(mRawItem);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getEpId() {
        return Id;
    }

    public String getIconUrl() {
        return IconUrl;
    }

    public String getName() {
        return Name;
    }

    public String getDesc() {
        return Desc;
    }

    public String getClientApkUrl() {
        return ApkClientUrl;
    }

    public String getVersionName() {
        return VersionName;
    }

    public int getVersionCode() {
        return VersionCode;
    }

    public String getUpdatedTime() {
        return UpdatedTime;
    }

    public String getParams(String key) {
        return Params.get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append("cp: ").append(getEpId()).append("\n")
                .append("name: ").append(getName()).append("\n")
                .append("type: ").append(getType()).append("\n")
                .append("icon: ").append(getIconUrl()).append("\n")
                .append("des: ").append(getDesc()).append("\n")
                .append("apk_url: ").append(getClientApkUrl()).append("\n")
                .append("update_at: ").append(getUpdatedTime()).append("\n")
                .append("version_name: ").append(getVersionName()).append("\n")
                .append("version: ").append(getVersionCode()).append("\n");
        return sb.toString();
    }
}
