package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcelable;
import android.support.v17.leanback.widget.VerticalGridView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by wangwei on 3/22/16.
 */
public class DebugVerticalGridView extends VerticalGridView {
    final String TAG = "GridView";

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
        Log.d(TAG, "onLayout 0");
        super.onLayout(changed, l, t, r, b);
        //Log.d(TAG, "onLayout 1",new Exception(""));
    }

    @Override
    public void scrollBy(int x, int y) {
        Log.d(TAG, "scrollBy 0");
        super.scrollBy(x, y);
        //Log.d(TAG, "scrollBy 1", new Exception(""));
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        Log.d(TAG,"onRequestFocusInDescendants",new Exception(""));

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
}
