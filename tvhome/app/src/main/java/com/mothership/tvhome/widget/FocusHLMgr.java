package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
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
    private static final String TAG = "FocusHLMgr";
    static WeakHashMap<Context, WeakReference<FocusHLMgr>> SMgrMap = new WeakHashMap<Context, WeakReference<FocusHLMgr>>();
    ImageView mFocusHL;
    View mFocusHLT;
    View mLastFocus;
    Rect mTmpR = new Rect();
    Rect mTmpP = new Rect();
    Handler mHandler;
    int mCheckLoop;
    static final int MaxCheckLoop = 5;
    static final int Move2LastFocus = 1;
    static final int MoveDelay = 30;
    int mLastX, mLastY;
    Handler.Callback mCb = new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
//            Log.d(TAG, "handle message");
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

        mHandler = new Handler(Looper.myLooper(), mCb);
    }

    public void viewGotFocus(View aFV)
    {
        mLastFocus = aFV;
        move2LastFocus();
        postCheckMsg();
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
        if(!mLastFocus.hasFocus())
        {
            return;
        }
        try
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
            int pl = 0, pt = 0;
            View v = mLastFocus.findViewById(R.id.di_img);
            if (v != null)
            {
                v.getDrawingRect(mTmpR);
                ((ViewGroup)mLastFocus).offsetDescendantRectToMyCoords(v, mTmpR);
                pl = mTmpR.left;
                pt = mTmpR.top;

                v.getDrawingRect(mTmpR);
                pv.offsetDescendantRectToMyCoords(mLastFocus, mTmpR);
            }
            float scaleF = 1.1f, delta = 0.1f;
            ViewGroup.LayoutParams lp = mFocusHLT.getLayoutParams();
            int nw = (int) (scaleF * mTmpR.width()) + mTmpP.left + mTmpP.right;
            int nh = (int) (scaleF * (mTmpR.height())) + mTmpP.top + mTmpP.bottom;
            if(lp.width != nw || lp.height != nh)
            {
                lp.width = nw;
                lp.height = nh;
                mFocusHLT.requestLayout();
            }


            int curX = (int)(mTmpR.left - deltaXBase * delta / 2 + pl - mTmpP.left);
            int curY = (int)(mTmpR.top - deltaYBase * delta / 2 + pt - mTmpP.top);
            mFocusHLT.setX(curX);
            mFocusHLT.setY(curY);

            if(curX != mLastX || curY != mLastY)
            {
                Log.d(TAG, "check later");
                postCheckMsg();
                mCheckLoop = 0;
            }
            else
            {
                if(++ mCheckLoop < MaxCheckLoop)
                {
                    postCheckMsg();
                }
            }
            mLastX = curX;
            mLastY = curY;
        }
        catch (Exception e)
        {
            Log.d(TAG, "view recycled");
        }
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


    void postCheckMsg()
    {
        mHandler.removeMessages(Move2LastFocus);
        mHandler.sendEmptyMessageDelayed(Move2LastFocus, MoveDelay);
    }

    @Override
    public void onScrollChanged()
    {
        if(mLastFocus != null)
        {
            postCheckMsg();
            move2LastFocus();
        }
    }
}
