package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by Shawn on 16/3/16.
 */
public class FocusHLMgr implements ViewTreeObserver.OnScrollChangedListener
{
    static WeakHashMap<Context, WeakReference<FocusHLMgr>> SMgrMap = new WeakHashMap<Context, WeakReference<FocusHLMgr>>();
    ImageView mFocusHL;
    View mLastFocus;
    Rect mTmpR = new Rect();
    Handler mHandler;
    static final int Move2LastFocus = 1;
    static final int MoveDelay = 10;
    Handler.Callback mCb = new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            move2LastFocus();
            return true;
        }
    };
    public FocusHLMgr(ImageView aFocusHL)
    {
        mFocusHL = aFocusHL;
        SMgrMap.put(aFocusHL.getContext(), new WeakReference<FocusHLMgr>(this));

        aFocusHL.getViewTreeObserver().addOnScrollChangedListener(this);

//        mHandler = new Handler(Looper.myLooper(), mCb);
    }

    public void viewGotFocus(View aFV)
    {
        mLastFocus = aFV;
        move2LastFocus();
    }

    void move2LastFocus()
    {
        mFocusHL.setVisibility(View.VISIBLE);
        mLastFocus.getDrawingRect(mTmpR);
        ViewGroup pv = (ViewGroup) mFocusHL.getParent();

        pv.offsetDescendantRectToMyCoords(mLastFocus, mTmpR);

        mFocusHL.setX(mTmpR.left + (mTmpR.width() -  mFocusHL.getWidth()) / 2);
        mFocusHL.setY(mTmpR.top + (mTmpR.height() - mFocusHL.getHeight()) / 2);
    }


    static public FocusHLMgr getMgr(Context aCt)
    {
        WeakReference<FocusHLMgr> ref = SMgrMap.get(aCt);
        if(ref != null)
        {
            return ref.get();
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onScrollChanged()
    {
        if(mLastFocus != null)
        {
//            mHandler.removeMessages(Move2LastFocus);
//            mHandler.sendEmptyMessageDelayed(Move2LastFocus, MoveDelay);
            move2LastFocus();
        }
    }
}
