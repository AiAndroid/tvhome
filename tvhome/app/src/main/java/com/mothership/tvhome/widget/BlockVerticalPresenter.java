package com.mothership.tvhome.widget;

import android.view.ViewGroup;

import com.mothership.tvhome.R;

/**
 * Created by wangwei on 3/4/16.
 */
public class BlockVerticalPresenter extends BlockBasePresenter{

    @Override
    protected BlockBasePresenter.ViewHolder createBlockViewHolder(ViewGroup parent) {
        BlockView rowView = new BlockView(parent.getContext());
        rowView.initLayout(R.layout.block_vertical);
        return new ViewHolder(rowView, rowView.getGridView(), this);
    }

}
