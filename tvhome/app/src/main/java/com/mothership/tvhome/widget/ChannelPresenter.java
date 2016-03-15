package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v17.leanback.widget.ItemBridgeAdapterShadowOverlayWrapper;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.ShadowOverlayHelper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mothership.tvhome.view.ChannelContainer;
import com.mothership.tvhome.view.DiViewT2;
import com.tv.ui.metro.model.Image;

import java.sql.Wrapper;

/**
 * Created by Shawn on 16/3/15.
 */
public class ChannelPresenter extends Presenter
{
    ItemBridgeAdapterShadowOverlayWrapper mWrapper;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        DiViewT2 t2 = new DiViewT2(parent.getContext(), null);

        ImageView img = new ImageView(parent.getContext());
        t2.addView(img);
        t2.setFocusable(true);
        t2.setFocusableInTouchMode(true);
        return new Presenter.ViewHolder(t2);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item)
    {
        ((ImageView)((ViewGroup)viewHolder.view).getChildAt(0)).setImageResource(((Integer) item).intValue());
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder)
    {

    }
}
