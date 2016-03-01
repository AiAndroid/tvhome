package com.mothership.tvhome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wangwei on 3/1/16.
 */
public class MainHeaderView  extends TextView {

    public MainHeaderView(Context context) {
        this(context, null);
    }

    public MainHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MainHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}

