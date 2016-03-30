package com.video.search.access;

/**
 * Response Status
 *
 * Created by zhuzhenhua on 15-12-16.
 */
public enum ResponseStatus {

    SUCCESS(0, "OK"),
    EMPTY_RESULT(13, "空返回结果"),
    INVALID_PARAM(14, "非法参数"),
    USER_AUTH_FAILED(19, "签名验证错误"),
    SESSION_TIMEOUT(22, "超时"),
    INVALID_IP(28, "非法IP"),
    SERVER_ERROR(32, "服务器端错误"),
    NOT_MODIFIED(64, "数据未改变"),
    LIVE_STREAM_NOT_AVAILABLE(1001, "直播当前时间不可用"),
    LIVE_STREAM_TOKEN_ERROR(1002, "获取直播令牌出错");

    public final int status;
    public final String msg;
    ResponseStatus(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static ResponseStatus valueOf(int status) {
        ResponseStatus rs = SUCCESS;
        if (EMPTY_RESULT.status == status)
            rs = EMPTY_RESULT;
        else if(INVALID_PARAM.status == status)
            rs = INVALID_PARAM;
        else if(USER_AUTH_FAILED.status == status)
            rs = USER_AUTH_FAILED;
        else if(SESSION_TIMEOUT.status == status)
            rs = SESSION_TIMEOUT;
        else if(INVALID_IP.status == status)
            rs = INVALID_IP;
        else if(SERVER_ERROR.status == status)
            rs = SERVER_ERROR;
        else if(NOT_MODIFIED.status == status)
            rs = NOT_MODIFIED;
        else if(LIVE_STREAM_NOT_AVAILABLE.status == status)
            rs = LIVE_STREAM_NOT_AVAILABLE;
        else if(LIVE_STREAM_TOKEN_ERROR.status == status)
            rs = LIVE_STREAM_TOKEN_ERROR;
        return rs;
    }
}
