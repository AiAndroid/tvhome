package com.mothership.tvhome.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mothership.tvhome.R;

/**
 * Created by Shawn on 16/3/11.
 */
public class PresenterT2 extends BasePresenter
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View res = inf.inflate(R.layout.di_view_t2, parent, false);
        VH vh = new VH(res);
        return vh;
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
