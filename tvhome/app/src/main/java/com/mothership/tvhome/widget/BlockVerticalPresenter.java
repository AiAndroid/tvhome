package com.mothership.tvhome.widget;

import android.view.ViewGroup;

import com.mothership.tvhome.R;

/**
 * Created by wangwei on 3/4/16.
 */
public class BlockVerticalPresenter extends BlockBasePresenter{

    @Override
    protected BlockBasePresenter.ViewHolder createBlockViewHolder(ViewGroup parent) {
        //initStatics(parent.getContext());
        BlockView rowView = new BlockView(parent.getContext());
        //setupFadingEffect(rowView);
        rowView.initLayout(R.layout.block_horizontal);
        //HorizontalGridView gridView = (HorizontalGridView)rowView.getGridView();
        return new ViewHolder(rowView, rowView.getGridView(), this);
    }


}
