package com.mothership.tvhome;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.squareup.okhttp.OkHttpClient;

/**
 * Created by liuhuadonbg on 3/14/16.
 */
public class TvHomeApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient okHttpClient = new OkHttpClient();
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory.newBuilder(getApplicationContext(), okHttpClient).build();

        Fresco.initialize(getApplicationContext(), config);

    }
}
