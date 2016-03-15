package com.mothership.tvhome.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Shawn on 16/3/15.
 */
public class DiBaseView extends LinearLayout
{
    static final float UnFocusAlpha = 0.6f;
    static final float FocusAlpha = 1.0f;
    public DiBaseView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setAlpha(UnFocusAlpha);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
    {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        onFocusChange(this, gainFocus);
    }

    static final void onFocusChange(View aView, boolean aGainFocus)
    {
        if(aGainFocus)
        {
            aView.animate().withLayer().alpha(FocusAlpha);
        }
        else
        {
            aView.animate().withLayer().alpha(UnFocusAlpha);
        }
    }
}
