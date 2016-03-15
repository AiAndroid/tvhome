package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangwei on 3/15/16.
 */
public class FocusGridLayout extends GridLayout {
    public FocusGridLayout(Context context) {
        this(context, null, 0);
    }

    public FocusGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setChildrenDrawingOrderEnabled(true);
    }
    @Override
    public void requestChildFocus(View child, View focused)
    {
        super.requestChildFocus(child, focused);
        invalidate();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i)
    {
        View focus = getFocusedChild();
        if(null == focus)
        {
            return i;
        }
        else
        {
            int focusIdx = indexOfChild(focus);
            if(i < focusIdx)
            {
                return  i;
            }
            else if(i < childCount - 1)
            {
                return focusIdx + childCount - 1 - i;
            }
            else
            {
                return focusIdx;
            }
        }
    }
}
