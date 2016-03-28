package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by wangwei on 3/22/16.
 */
public class FocusVerticalGridView extends VerticalGridView {
    final String TAG = "FocusVerticalGridView";

    public FocusVerticalGridView(Context context) {
        this(context, null);
    }

    public FocusVerticalGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FocusVerticalGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {

        int index;
        int increment;
        int end;
        int count = getChildCount();
        if((direction & FOCUS_UP) != 0) {
            Rect better = new Rect();
            getChildAt(0).getFocusedRect(better);
            this.offsetDescendantRectToMyCoords(getChildAt(0), better);
            int pos = 0;
            for (int i = 1; i != count; i += 1) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    Rect rect = new Rect();
                    getChildAt(i).getFocusedRect(rect);
                    this.offsetDescendantRectToMyCoords(getChildAt(i), rect);
                    if(rect.bottom>better.bottom){
                        better = rect;
                        pos = i;
                    }
                }
            }
            if(pos<count){
                if (getChildAt(pos).requestFocus(direction, previouslyFocusedRect)) {
                    return true;
                }
            }

        }else{
            if ((direction & FOCUS_FORWARD) != 0) {
                index = 0;
                increment = 1;
                end = count;

            } else {
                index = count - 1;
                increment = -1;
                end = -1;
            }

            for (int i = index; i != end; i += increment) {
                View child = getChildAt(i);
                if (child.getVisibility() == VISIBLE) {
                    if (child.requestFocus(direction, previouslyFocusedRect)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void requestChildFocus(View child, View focused) {
        //if(child==focused)return;
        super.requestChildFocus(child,focused);
    }
}
