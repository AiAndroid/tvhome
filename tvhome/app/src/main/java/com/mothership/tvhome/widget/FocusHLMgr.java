package com.mothership.tvhome.widget;

import android.animation.ValueAnimator;
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
    ValueAnimator mAnim = ValueAnimator.ofFloat(1.0f, 1.1f);
    int mPl, mPt;
    Handler.Callback mCb = new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
//            Log.d(TAG, "handle message");
            move2LastFocus(false);
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
        mAnim.setDuration(150);
        mAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator anim)
            {
                float scaleF = ((Float) anim.getAnimatedValue()).floatValue();
                ViewGroup.LayoutParams lp = mFocusHLT.getLayoutParams();
                int nw = (int) (scaleF * mTmpR.width()) + mTmpP.left + mTmpP.right;
                int nh = (int) (scaleF * (mTmpR.height())) + mTmpP.top + mTmpP.bottom;
                if(lp.width != nw || lp.height != nh)
                {
                    lp.width = nw;
                    lp.height = nh;
                    mFocusHLT.requestLayout();
                }

                int curX = (int) (mTmpR.left - mTmpR.width() * (scaleF - 1.0f) / 2 + mPl - mTmpP.left);
                int curY = (int) (mTmpR.top - mTmpR.height() * (scaleF - 1.0f) / 2 + mPt - mTmpP.top);
                mFocusHLT.setX(curX);
                mFocusHLT.setY(curY);
            }
        });
    }

    public void viewGotFocus(View aFV)
    {
        mLastFocus = aFV;
        move2LastFocus(true);
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

    void move2LastFocus(boolean aFocusChanged)
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
            View v = mLastFocus.findViewById(R.id.di_img);
            if (v != null)
            {
                v.getDrawingRect(mTmpR);
                ((ViewGroup)mLastFocus).offsetDescendantRectToMyCoords(v, mTmpR);
                mPl = mTmpR.left;
                mPt = mTmpR.top;

                v.getDrawingRect(mTmpR);
                pv.offsetDescendantRectToMyCoords(v, mTmpR);
            }

            if(aFocusChanged)
            {
                mAnim.start();
            }

            float value = ((Float) mAnim.getAnimatedValue()).floatValue();
            int curX = (int) (mTmpR.left - mTmpR.width() * (value - 1.0f) / 2 + mPl - mTmpP.left);
            int curY = (int) (mTmpR.top - mTmpR.height() * (value - 1.0f) / 2 + mPt - mTmpP.top);
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
            move2LastFocus(false);
        }
    }
}
