package com.mothership.tvhome.widget;

import android.content.Context;

import com.mothership.tvhome.R;

/**
 * Created by Shawn on 16/3/11.
 */
public class PresenterT3 extends BasePresenter
{
    @Override
    public int getLayoutResId(){
        return R.layout.di_view_t3;
    }

    @Override
    public void onBindViewHolder(ViewHolder aViewHolder, Object aItem)
    {
        super.onBindViewHolder(aViewHolder, aItem);
        VH vh = (VH) aViewHolder;
        vh.mImg.getLayoutParams().width = mBaseWidth - vh.view.getPaddingLeft() - vh.view.getPaddingRight();
        vh.mImg.getLayoutParams().height = mBaseHeight - vh.view.getPaddingBottom() - vh.view.getPaddingTop();
    }

    public int getRealWidth(Context contect){
        return mBaseWidth;
    };

    public int getRealHeight(Context contect){
        return mBaseHeight+(int)contect.getResources().getDimension(R.dimen.item_text_bar_height);
    };
}
