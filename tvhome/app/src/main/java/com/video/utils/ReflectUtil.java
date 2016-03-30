package com.video.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 反射工具类
 *
 * Created by zzh on 2015/10/20.
 */
public class ReflectUtil {

    /**
     * 获取 T.class
     *
     * Type genType = getClass().getGenericSuperclass();
     * Class<T> cls = (Class<T>) ((ParameterizedType) genType).getActualTypeArguments()[0];
     *
     * @param c
     * @param <T>
     * @return
     */
    public static<T> Class<T> getTClass(Class<T> c) {
        Type genType = c.getGenericSuperclass();
        return (Class<T>) ((ParameterizedType) genType).getActualTypeArguments()[0];
    }

}
