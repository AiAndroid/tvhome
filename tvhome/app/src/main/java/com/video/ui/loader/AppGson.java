package com.video.ui.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tv.ui.metro.model.DisplayItem;

public class AppGson {
    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                //.registerTypeAdapter(DisplayItem.Target.class, new DisplayItem.Target.Deserializer())
                .create();
    }


    public static Gson get() {
        return gson;
    }
}
