package com.video.search.model;

import android.text.TextUtils;

/**
 * Cineaste 编剧,编导,导演,电影业人士
 * Created by zhuzhenhua on 15-12-25.
 */
public class Cineaste extends Item {
    public String cineasteid;
    public int gender;
    public int order;
    public Image poster;

    public String getId() {
        if (TextUtils.isEmpty(id)) {
            return cineasteid;
        }
        return id;
    }

    public String getAvatarUrl() {
        String url = poster.posterurl;
        if (TextUtils.isEmpty(url)) {
            url = poster.url;
        }
        return url;
    }
}
