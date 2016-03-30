package com.mothership.tvhome.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.volley.RequestQueue;
import com.video.search.access.VolleyHelper;

/**
 * Created by zhuzhenhua on 16-1-7.
 */
public class BaseVolleyFragment extends BaseFragment {

    protected VolleyHelper mVolleyHelper;
    protected RequestQueue mRequestQueue;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mVolleyHelper = VolleyHelper.getInstance(getContext());
        mRequestQueue = mVolleyHelper.getRequestQueue();
        super.onActivityCreated(savedInstanceState);
        initViews(getView());
        initData();
    }

    protected void initViews(View root) {

    }



    protected void initData() {

    }

    @Override
    public void onStop() {
        super.onStop();

        mRequestQueue.cancelAll(getReqTag());
        System.gc();
    }

    protected String getReqTag() {
        return getClass().getSimpleName();
    }

}
