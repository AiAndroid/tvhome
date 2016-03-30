package com.video.search.access.api;

import com.video.search.access.Params;
import com.video.search.access.ResponseStatus;

import org.json.JSONObject;

public abstract class ApiCallback<T> {

    /* 接口服务器处理正常 */
    public static final int RES_OK = ResponseStatus.SUCCESS.status;

    public int code;

    public String msg;

    public boolean isSuccess(JSONObject json) {
        code = json.optInt(Params.STATUS);
        return RES_OK == code;
    }

    public abstract Object parse(JSONObject json) throws ParseException;

    public void onSuccess(T result) {
    }

    public void onFail(String err)
    {

    }

}
