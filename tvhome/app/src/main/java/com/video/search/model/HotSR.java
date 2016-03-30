package com.video.search.model;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Hot Search Result
 * Created by zhuzhenhua on 16-1-7.
 */
public class HotSR extends BaseEntity {

    public HotSRItem[] data;

    public static class HotSRItem {
        @JSONField(name="default")
        public boolean def;
        public String name;
        public MediaBase[] medias;
    }
}
