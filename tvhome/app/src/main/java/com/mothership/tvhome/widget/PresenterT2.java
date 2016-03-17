package com.mothership.tvhome.widget;

import android.content.Context;

import com.mothership.tvhome.R;

/**
 * Created by Shawn on 16/3/11.
 */
public class PresenterT2 extends BasePresenter
{
    @Override
    public int getLayoutResId(){
        return R.layout.di_view_t2;
    }

    @Override
    public void onBindViewHolder(ViewHolder aViewHolder, Object aItem)
    {
        super.onBindViewHolder(aViewHolder, aItem);
    }

    public int getRealWidth(Context contect){
        return mBaseWidth;
    };

    public int getRealHeight(Context contect){
        return mBaseHeight;
    };
}
