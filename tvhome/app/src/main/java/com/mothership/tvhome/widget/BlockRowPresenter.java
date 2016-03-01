package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.ViewGroup;

/**
 * Created by wangwei on 2/29/16.
 */
public class BlockRowPresenter extends ListRowPresenter {
    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        ListRowPresenter.ViewHolder holder = (ListRowPresenter.ViewHolder)super.createRowViewHolder(parent);
        HorizontalGridView horizontalGridView = holder.getGridView();
        horizontalGridView.setNumRows(3);
        return holder;
    }
}
