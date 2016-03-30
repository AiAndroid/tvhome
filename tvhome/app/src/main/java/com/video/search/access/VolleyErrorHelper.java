package com.video.search.access;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

import java.util.HashMap;

public class VolleyErrorHelper {

    public static String getMessage(Object error) {
        if (error instanceof TimeoutError) {
            return "server time out";
        } else if (isServerProblem(error)) {
            return handleServerError(error);
        } else if (isNetworkProblem(error)) {
            return "network err";
        }
        return "server err";
    }

    private static boolean isNetworkProblem(Object error) {
        return (error instanceof NetworkError) || (error instanceof NoConnectionError);
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError) || (error instanceof AuthFailureError);
    }

    private static String handleServerError(Object err) {
        VolleyError error = (VolleyError) err;

        NetworkResponse response = error.networkResponse;

        if (response != null) {
            switch (response.statusCode) {
                case 404:
                case 422:
                case 401:
                    try {
                        /* HashMap<String, String> result = new Gson().fromJson(new String(response.data),
                                new TypeToken<String>() {
                                }.getType());*/

                        HashMap<String, String> result = JSON.parseObject(new String(response.data),
                                new TypeReference<HashMap<String, String>>() {
                                }.getType());

                        if (result != null && result.containsKey("error")) {
                            return result.get("error");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return e.getMessage();
                    }
                    return error.getMessage();
                default:
                    return "server err " + response.statusCode;
            }
        }
        return "server err";
    }
}
