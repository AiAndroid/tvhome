package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.FocusHighlightHelper;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mothership.tvhome.R;
import com.mothership.tvhome.view.ChannelContainer;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by wangwei on 3/10/16.
 */
public class TVhomBlockPresenter extends RowPresenter {
    protected static Context mContext;

    static class BlockViewHolder extends Presenter.ViewHolder {
        public BlockViewHolder(View view)
        {
            super(view);
        }
    }


    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        mContext = parent.getContext();
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View view = inf.inflate(R.layout.home_block, parent, false);
//        view.setFocusable(true);
//        view.setFocusableInTouchMode(true);
        setSelectEffectEnabled(false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        ViewHolder vh = (ViewHolder) holder;
        if (item instanceof Block) {
//            Block<DisplayItem> displayItemBlock = (Block<DisplayItem>) item;
            super.onBindRowViewHolder(holder, new Row(new HeaderItem(0, "")));

            ArrayObjectAdapter adapter = new ArrayObjectAdapter(new ChannelPresenter());
            adapter.add(Integer.valueOf(R.drawable.a1));
            adapter.add(Integer.valueOf(R.drawable.a2));
            adapter.add(Integer.valueOf(R.drawable.a3));
            adapter.add(Integer.valueOf(R.drawable.a4));
            adapter.add(Integer.valueOf(R.drawable.a5));
            adapter.add(Integer.valueOf(R.drawable.a6));

            ItemBridgeAdapter adpt = new ItemBridgeAdapter(adapter);
            FocusHighlightHelper.setupBrowseItemFocusHighlight(adpt, FocusHighlight.ZOOM_FACTOR_MEDIUM, false);

            ((ChannelContainer) ((ViewHolder) holder).view).setAdapter(adpt);

        }
    }
}
