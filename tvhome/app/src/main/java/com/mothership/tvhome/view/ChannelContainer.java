package com.mothership.tvhome.view;

import android.content.Context;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mothership.tvhome.R;
import com.mothership.tvhome.widget.BasePresenter;

/**
 * Created by Shawn on 16/3/15.
 */
public class ChannelContainer extends LinearLayout
{
    LinearLayout mC2, mC3;
    ItemBridgeAdapter mAdpt;
    public ChannelContainer(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();
        mC2 = (LinearLayout) findViewById(R.id.c2);
        mC3 = (LinearLayout) findViewById(R.id.c3);
    }

    public void setAdapter(ItemBridgeAdapter adpt)
    {
        if(adpt != null && null == mAdpt)
        {
            mAdpt = adpt;
            RecyclerView.ViewHolder vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(0));
            mAdpt.onBindViewHolder(vh, 0);
            this.addView(vh.itemView, 0);

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(1));
            mAdpt.onBindViewHolder(vh, 1);
            mC2.addView(vh.itemView);

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(2));
            mAdpt.onBindViewHolder(vh, 2);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 50;
            vh.itemView.setLayoutParams(lp);
            mC2.addView(vh.itemView);





            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(3));
            mAdpt.onBindViewHolder(vh, 3);
            mC3.addView(vh.itemView);

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(4));
            mAdpt.onBindViewHolder(vh, 4);
            lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 50;
            vh.itemView.setLayoutParams(lp);
            mC3.addView(vh.itemView);

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(5));
            mAdpt.onBindViewHolder(vh, 5);
            lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 50;
            vh.itemView.setLayoutParams(lp);
            mC3.addView(vh.itemView);


        }
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
