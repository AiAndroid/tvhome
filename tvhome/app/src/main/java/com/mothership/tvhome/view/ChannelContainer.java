package com.mothership.tvhome.view;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mothership.tvhome.R;

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
        final int StartFlag = Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;
        if(adpt != null && null == mAdpt)
        {
            mAdpt = adpt;
            RecyclerView.ViewHolder vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(0));
            mAdpt.onBindViewHolder(vh, 0);
            this.addView(vh.itemView, 0);
            vh.itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent startIntent = new Intent("com.xiaomi.mitv.tvplayer.ATV_PLAY");
                    startIntent.setComponent(new ComponentName("com.xiaomi.mitv.tvplayer", "com.xiaomi.mitv.tvplayer.AtvActivity"));
                    startIntent.setFlags(StartFlag);
                    getContext().startActivity(startIntent);
                }
            });

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(1));
            mAdpt.onBindViewHolder(vh, 1);
            mC2.addView(vh.itemView);
            vh.itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {

                        ComponentName component = new ComponentName(
                                "com.mitv.video",
                                "com.mitv.video.activity.MainActivity");
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        intent.setComponent(component);
                        getContext().startActivity(intent);
                    }
                    catch (Exception e)
                    {

                    }

                }
            });

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(2));
            mAdpt.onBindViewHolder(vh, 2);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 50;
            vh.itemView.setLayoutParams(lp);
            mC2.addView(vh.itemView);

            vh.itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    try
                    {
                        ComponentName component = new ComponentName(
                                "com.mitv.video",
                                "com.mitv.video.activity.MainActivity");
                        Intent intent = new Intent("android.intent.action.MAIN");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        intent.setComponent(component);
                        getContext().startActivity(intent);
                    }
                    catch (Exception e)
                    {

                    }
                }
            });





            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(3));
            mAdpt.onBindViewHolder(vh, 3);
            mC3.addView(vh.itemView);
            vh.itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        Intent intent = new Intent("android.intent.action.MITV_VIDEO_PLAY_RECORD");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        getContext().startActivity(intent);
                    }
                    catch (Exception e)
                    {

                    }

                }
            });

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(4));
            mAdpt.onBindViewHolder(vh, 4);
            lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 47;
            vh.itemView.setLayoutParams(lp);
            mC3.addView(vh.itemView);

            vh.itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.xiaomi.mitv.mediaexplorer",
                            "com.xiaomi.mitv.mediaexplorer.NewScraperMainEntryActivity"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    getContext().startActivity(intent);
                }
            });

            vh = mAdpt.onCreateViewHolder(this, mAdpt.getItemViewType(5));
            mAdpt.onBindViewHolder(vh, 5);
            lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.topMargin = 47;
            vh.itemView.setLayoutParams(lp);
            mC3.addView(vh.itemView);

            vh.itemView.setOnClickListener(new OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.setComponent(new ComponentName("com.xiaomi.mitv.settings",
                            "com.xiaomi.mitv.settings.entry.MainActivity"));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    getContext().startActivity(intent);
                }
            });


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
