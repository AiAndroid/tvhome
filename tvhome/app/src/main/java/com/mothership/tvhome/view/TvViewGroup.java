package com.mothership.tvhome.view;


import android.content.Context;
import android.graphics.Rect;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.FocusHighlightHelper;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.mothership.tvhome.widget.BasePresenter;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by user on 16-3-2.
 */
public class TvViewGroup extends ViewGroup
{

    private static final String TAG = "TvViewGroup";
    ArrayList<Rect> mLayoutPos = new ArrayList<>();
    ArrayList<Presenter.ViewHolder> mHolders = new ArrayList<>();
    static final int Factor = 1;
    static final int RowCnt = 9;// / Factor;
    static final int ColCnt = 16;// / Factor;
    static final int BaseSizeW = 150;// * Factor;
    static final int BaseSizeH = 150;// * Factor;
    static final int Space = 10;
    static final int MaxTestCnt = 8;
    static final int RandColDiv = 2;
    static final Random Rand = new Random();

    Block<DisplayItem> mBlk;
    public TvViewGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);
//        setBackgroundColor(Color.RED);

//        FocusHighlightHelper.setupBrowseItemFocusHighlight();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int childCnt = getChildCount();
        for (int i = 0; i < childCnt; ++i)
        {
            View child = getChildAt(i);

            Rect pos = mLayoutPos.get(i);
//            child.setMainImageDimensions(pos.width(), pos.height());
//            Log.d(TAG, "measure " + i + "(" + pos.width() + ", " + pos.height());
            child.measure(MeasureSpec.makeMeasureSpec(pos.width(), MeasureSpec.EXACTLY),
                    MeasureSpec.makeMeasureSpec(pos.height(), MeasureSpec.EXACTLY));
        }
//        ViewGroup.LayoutParams param = (ViewGroup.LayoutParams) getLayoutParams();
        setMeasuredDimension(RowCnt * BaseSizeH, ColCnt * BaseSizeW);
//        Log.d(TAG, " " + getMeasuredWidth() + ", " + getMeasuredHeight());
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    public void requestChildFocus(View child, View focused)
    {
        super.requestChildFocus(child, focused);
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        int childCnt = getChildCount();
        for (int i = 0; i < childCnt; ++i)
        {
            View child = getChildAt(i);
            Rect pos = mLayoutPos.get(i);
            child.layout(pos.left, pos.top, pos.right, pos.bottom);
        }

    }

    public void setData(Block<DisplayItem> aBlk)
    {
        if(aBlk != null)
        {
            mLayoutPos.clear();
            mBlk = aBlk;
            int dataCnt = aBlk.items.size();
            mLayoutPos.ensureCapacity(dataCnt);
            mHolders.clear();

            ItemBridgeAdapter adapter = new ItemBridgeAdapter();
            ArrayObjectAdapter itmAdpt = new ArrayObjectAdapter(new BasePresenter());
            for(DisplayItem itm : aBlk.items)
            {
                itmAdpt.add(itm);
            }
            adapter.setAdapter(itmAdpt);
            FocusHighlightHelper.setupBrowseItemFocusHighlight(adapter, FocusHighlight.ZOOM_FACTOR_XSMALL, true);
            for (int i = 0; i < dataCnt; ++i)
            {
                RecyclerView.ViewHolder vh = (RecyclerView.ViewHolder) adapter.onCreateViewHolder(this, adapter.getItemViewType(i));
                adapter.onBindViewHolder(vh, i);
                addView(vh.itemView);
            }
            randLayout(dataCnt);
        }
        else
        {
            removeAllViews();
        }

    }


    void randLayout(int aDataCnt)
    {
        int range = aDataCnt;
        int randW, randH;
        int startX = 0, startY = 0, finishX = 0, finishY = 0;
        int accH = 0;
        while (range > 0)
        {
            randW = Rand.nextInt(ColCnt / RandColDiv) + 1;
            while (accH < RowCnt && range > 0)
            {
                randH = Rand.nextInt(RowCnt - accH) + 1;

                finishY = startY + randH * BaseSizeH + (randH - 1) * Space;
                if (accH > 0)
                {
                    int accW = 0;
                    int rW = 0;
                    int preStartX = startX;
                    while (accW < randW && range > 0)
                    {
                        rW = Rand.nextInt(randW - accW) + 1;
                        accW += rW;
                        finishX = startX + rW * BaseSizeW + (rW - 1) * Space;
                        mLayoutPos.add(new Rect(startX, startY, finishX, finishY));
                        startX = finishX + Space;
                        --range;
                    }
                    startX = preStartX;
                }
                else
                {
                    finishX = startX + randW * BaseSizeW + (randW - 1) * Space;
                    mLayoutPos.add(new Rect(startX, startY, finishX, finishY));
                    --range;
                }
                accH += randH;
//                Log.d(TAG, "accH~" + accH);
                startY = finishY + Space;
            }
//            Log.d(TAG, "accH " + accH);
            startX = finishX + Space;
            startY = 0;
            accH = 0;

        }
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(1920, RowCnt * BaseSizeH + (RowCnt - 1) * Space);
//        lp.gravity = Gravity.LEFT | Gravity.TOP;
//        lp.leftMargin = 0;
//        setLayoutParams(lp);
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
