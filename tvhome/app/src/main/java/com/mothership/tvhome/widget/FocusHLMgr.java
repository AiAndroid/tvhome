package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.mothership.tvhome.R;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
 * Created by Shawn on 16/3/16.
 */
public class FocusHLMgr implements ViewTreeObserver.OnScrollChangedListener
{
    static WeakHashMap<Context, WeakReference<FocusHLMgr>> SMgrMap = new WeakHashMap<Context, WeakReference<FocusHLMgr>>();
    ImageView mFocusHL;
    View mFocusHLT;
    View mLastFocus;
    Rect mTmpR = new Rect();
    Rect mTmpP = new Rect();
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
    public FocusHLMgr(ImageView aFocusHL, View aFocusHLT)
    {
        mFocusHL = aFocusHL;
        mFocusHLT = aFocusHLT;
        SMgrMap.put(aFocusHL.getContext(), new WeakReference<FocusHLMgr>(this));

        aFocusHL.getViewTreeObserver().addOnScrollChangedListener(this);

//        mHandler = new Handler(Looper.myLooper(), mCb);
    }

    public void viewGotFocus(View aFV)
    {
        mLastFocus = aFV;
        move2LastFocus();
    }

    public void viewLostFocus(View aFV)
    {
        if(mLastFocus == aFV)
        {
            mFocusHLT.setVisibility(View.INVISIBLE);
            mFocusHL.setVisibility(View.INVISIBLE);
        }
    }

    void move2LastFocus()
    {
        mFocusHL.setVisibility(View.VISIBLE);
        mFocusHLT.setVisibility(View.VISIBLE);
        mLastFocus.getDrawingRect(mTmpR);
        ViewGroup pv = (ViewGroup) mFocusHL.getParent();

        pv.offsetDescendantRectToMyCoords(mLastFocus, mTmpR);

        mFocusHL.setX(mTmpR.left + (mTmpR.width() - mFocusHL.getWidth()) / 2);
        mFocusHL.setY(mTmpR.top + (mTmpR.height() - mFocusHL.getHeight()) / 2);

        NinePatchDrawable np = (NinePatchDrawable) mFocusHLT.getBackground();
        np.getPadding(mTmpP);

        int deltaXBase = mTmpR.width(), deltaYBase = mTmpR.height();
        View v = mLastFocus.findViewById(R.id.di_img);
        if(v != null)
        {
            v.getDrawingRect(mTmpR);
            pv.offsetDescendantRectToMyCoords(mLastFocus, mTmpR);
        }
        float scaleF = 1.1f, delta = 0.1f;
        ViewGroup.LayoutParams lp = mFocusHLT.getLayoutParams();
        lp.width = (int) (scaleF * mTmpR.width()) + mTmpP.left + mTmpP.right;
        lp.height = (int) (scaleF * (mTmpR.height())) + mTmpP.top + mTmpP.bottom;
        mFocusHLT.requestLayout();
//        np.setBounds(0, 0, (int) (scaleF * mTmpR.width()) + mTmpP.left + mTmpP.right,
//                (int) (scaleF * (mTmpR.height())) + mTmpP.top + mTmpP.bottom);
//        mFocusHLT.setScaleX(1.1f);
//        mFocusHLT.setScaleY(1.1f);
        mFocusHLT.setX(mTmpR.left - deltaXBase * delta / 2 - mTmpP.left);
        mFocusHLT.setY(mTmpR.top - deltaYBase * delta / 2 - mTmpP.top);
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
