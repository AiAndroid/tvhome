package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.RowPresenter;
import android.view.ViewGroup;

/**
 * Created by wangwei on 2/29/16.
 */
public class BlockHorizontalPresenter extends ListRowPresenter {
    @Override
    protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        ListRowPresenter.ViewHolder holder = (ListRowPresenter.ViewHolder)super.createRowViewHolder(parent);
        HorizontalGridView horizontalGridView = holder.getGridView();
        horizontalGridView.setNumRows(3);
        return holder;
    }
    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        super.onBindRowViewHolder(holder, item);
        ViewHolder vh = (ViewHolder) holder;
        ListRow rowItem = (ListRow) item;
        vh.mItemBridgeAdapter.setAdapter(rowItem.getAdapter());
        vh.mGridView.setAdapter(vh.mItemBridgeAdapter);
    }

}
