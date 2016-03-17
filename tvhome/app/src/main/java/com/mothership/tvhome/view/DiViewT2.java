package com.mothership.tvhome.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * Created by Shawn on 16/3/15.
 */
public class DiViewT2 extends FrameLayout
{
    public DiViewT2(Context context, AttributeSet attrs)
    {
        super(context, attrs);
//        setAlpha(DiBaseView.UnFocusAlpha);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
    {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        DiBaseView.onFocusChange(this, gainFocus);
    }
}
