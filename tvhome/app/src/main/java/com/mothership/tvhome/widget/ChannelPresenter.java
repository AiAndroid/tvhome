package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.Presenter;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Shawn on 16/3/15.
 */
public class ChannelPresenter extends Presenter
{
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        ImageView img = new ImageView(parent.getContext());
        img.setFocusable(true);
        img.setFocusableInTouchMode(true);
        return new Presenter.ViewHolder(img);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item)
    {
        ((ImageView)viewHolder.view).setImageResource(((Integer)item).intValue());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder)
    {

    }
}
