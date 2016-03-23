package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by wangwei on 3/22/16.
 */
public class DebugVerticalGridView extends VerticalGridView {
    final String TAG = "fps";

    public DebugVerticalGridView(Context context) {
        this(context, null);
    }

    public DebugVerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DebugVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean fling(int velocityX, int velocityY) {
        Log.d(TAG, "fling velocityY" + velocityY);
        boolean fling = super.fling(velocityX,velocityY);

        return fling;
    }

    @Override
    public void smoothScrollBy(int dx, int dy) {
        Log.d(TAG,"smoothScrollBy 0 dy"+dy);
        super.smoothScrollBy(dx, dy);
        //Log.d(TAG, "smoothScrollBy 1 dy" + dy);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.d(TAG,"onLayout 0");
        super.onLayout(changed, l, t, r, b);
        //Log.d(TAG, "onLayout 1",new Exception(""));
    }

    @Override
    public void scrollBy(int x, int y) {
        Log.d(TAG,"scrollBy 0");
        super.scrollBy(x, y);
        //Log.d(TAG, "scrollBy 1", new Exception(""));
    }
}
