package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Created by wangwei on 3/17/16.
 */
public class PagerGroup extends ViewPager {
    public PagerGroup(Context context) {
        super(context);
    }

    public PagerGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        return false;
    }
}
