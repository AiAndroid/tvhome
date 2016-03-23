package com.mothership.tvhome.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mothership.tvhome.R;
import com.mothership.tvhome.widget.FocusHLMgr;

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
        //setAlpha(UnFocusAlpha);
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect)
    {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        onFocusChange(this, gainFocus);
    }

    static final void onFocusChange(View aView, boolean aGainFocus)
    {
        FocusHLMgr mgr = FocusHLMgr.getMgr(aView.getContext());
        if(aGainFocus)
        {
//            aView.animate().withLayer().alpha(FocusAlpha);

            if(mgr != null)
            {
                mgr.viewGotFocus(aView);
            }
        }
        else
        {
            if(mgr != null)
            {
                mgr.viewLostFocus(aView);
            }
//            aView.animate().withLayer().alpha(UnFocusAlpha);
        }

        View tv = aView.findViewById(R.id.di_title);
        View text = aView.findViewById(R.id.di_text);
        if(tv != null)
        {
            tv.setSelected(aGainFocus);
        }
        if(tv!=null&&text!=null){
            if(aGainFocus){
                tv.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
            }else{
                tv.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
            }
        }

    }
}
