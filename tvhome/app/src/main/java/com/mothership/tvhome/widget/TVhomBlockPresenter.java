package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mothership.tvhome.R;
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
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        ViewHolder vh = (ViewHolder) holder;
        if (item instanceof Block) {
            Block<DisplayItem> displayItemBlock = (Block<DisplayItem>) item;
            super.onBindRowViewHolder(holder, new Row(new HeaderItem(0, "")));
        }
    }
}
