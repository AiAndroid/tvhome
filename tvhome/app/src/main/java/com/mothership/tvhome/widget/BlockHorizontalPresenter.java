package com.mothership.tvhome.widget;

import android.view.ViewGroup;

import com.mothership.tvhome.R;

/**
 * Created by wangwei on 2/29/16.
 */
public class BlockHorizontalPresenter extends BlockBasePresenter{

    @Override
    protected BlockBasePresenter.ViewHolder createBlockViewHolder(ViewGroup parent) {
        BlockView rowView = new BlockView(parent.getContext());
        rowView.initLayout(R.layout.block_horizontal);
        return new BlockBasePresenter.ViewHolder(rowView, rowView.getGridView(), this);
    }

}
