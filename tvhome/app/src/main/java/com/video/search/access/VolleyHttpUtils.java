package com.video.search.access;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.video.search.access.api.ApiCallback;
import com.video.search.access.api.ParseException;
import com.video.search.access.msg.RequestMessage;

import org.json.JSONObject;

/**
 * HttpUtils for volley
 *
 * Created by zhuzhenhua
 */
public class VolleyHttpUtils {

    private static final String Tag = "HttpUtils";

    public static JsonObjectRequest newPostRequest(RequestMessage msg, final ApiCallback callback) {
        return newRequest(Request.Method.POST, msg, callback);
    }

    public static JsonObjectRequest newGetRequest(RequestMessage msg, final ApiCallback callback) {
        return newRequest(Request.Method.GET, msg, callback);
    }

    public static JsonObjectRequest newRequest(int method, RequestMessage msg, final ApiCallback callback) {
        JsonObjectRequest request = new JsonObjectRequest(method, msg.getUrl(), msg.getMessage(),
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        if (callback.isSuccess(response)) {
                            try {
                                Object obj = callback.parse(response);
                                callback.onSuccess(obj);
                            } catch (ParseException e) {
                                e.printStackTrace();
                                callback.onFail(e.getMessage());
                            }
                        } else {
                            String message = "错误码：" + callback.code;
                            callback.onFail(message);
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                String message = VolleyErrorHelper.getMessage(error);
                callback.onFail(message);
                //ToastUtil.showShortToast(message);
            }
        });

        return request;
    }
}
