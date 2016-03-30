package com.video.search.access.api;

import com.alibaba.fastjson.JSON;
import com.video.utils.ReflectUtil;

import org.json.JSONObject;

/**
 * Created by zzh on 2015/10/7.
 */
public class ApiGetEntity<T> extends ApiCallback<T> {

    @Override
    public T parse(JSONObject json) throws ParseException {
        Class<T> cls = (Class<T>) ReflectUtil.getTClass(getClass());
        return JSON.parseObject(json.toString(), cls);
    }
}
