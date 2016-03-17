package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.Row;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.ViewGroup;

import com.mothership.tvhome.R;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;

import static android.support.v7.widget.GridLayout.spec;

/**
 * Created by wangwei on 3/14/16.
 */
public class BlockGridPresenter extends RowPresenter {
    ViewGroup mParent;
    final DisplayItemSelector mDisplayItemSelector = new DisplayItemSelector();
    public static class ViewHolder extends RowPresenter.ViewHolder {
        Context mContext;
        GridLayout mGridLayout;
        public ViewHolder(View rootView, GridLayout gridView) {
            super(rootView);
            mContext = rootView.getContext();
            mGridLayout = gridView;
            int paddingh = (int)rootView.getResources().getDimension(R.dimen.grid_block_horizontal_padding);
            int itemmargin = (int) rootView.getResources().getDimension(R.dimen.grid_item_margin);
            mGridLayout.setPadding(-itemmargin/2,0,0,0);

        }
    }


    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent) {
        mParent = parent;
        GridLayout gridLayout = new FocusGridLayout(parent.getContext());
        gridLayout.setUseDefaultMargins(true);
        gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridLayout.setClipToPadding(false);
        return new ViewHolder(gridLayout,gridLayout);
    }

    @Override
    protected void onBindRowViewHolder(final RowPresenter.ViewHolder holder, Object item) {
        final ViewHolder vh = (ViewHolder) holder;
        if (item instanceof Block) {
            final Block<DisplayItem> displayItemBlock = (Block<DisplayItem>) item;
            super.onBindRowViewHolder(holder, new Row(new HeaderItem(0, displayItemBlock.title)));
            vh.mGridLayout.removeAllViews();
            int columns = displayItemBlock.ui_type.columns();
            int rows = displayItemBlock.items.size() / columns;
            vh.mGridLayout.setColumnCount(columns);
            int itemmargin = (int) mParent.getResources().getDimension(R.dimen.grid_item_margin);
            int gridpaddingHor = (int) mParent.getResources().getDimension(R.dimen.grid_block_horizontal_padding);
            int itemwidth = (int) ((mParent.getWidth() - gridpaddingHor*2
                    - itemmargin * (columns - 1)) / columns);
            //vh.mGridLayout.setItemMargin(itemmargin);
            int itemheight = (int) (itemwidth / displayItemBlock.ui_type.ratio());
            BasePresenter basePresenter = (BasePresenter)mDisplayItemSelector.getPresenter(displayItemBlock);
            basePresenter.setBaseSize(itemwidth, itemheight);
            for (int i = 0; i < displayItemBlock.items.size(); ++i) {
                if (displayItemBlock.items.get(i).ui_type != null) {
                    DisplayItem di = displayItemBlock.items.get(i);

                    final BasePresenter.VH itemholder = (BasePresenter.VH)basePresenter.onCreateViewHolder(vh.mGridLayout);
                    basePresenter.onBindViewHolder(itemholder,di);
                    View view = itemholder.view;
                    int columnstart = displayItemBlock.items.get(i).ui_type.x();
                    int columnspan = displayItemBlock.items.get(i).ui_type.w();
                    int rowstart = displayItemBlock.items.get(i).ui_type.y();
                    int rowspan = displayItemBlock.items.get(i).ui_type.h();
                    GridLayout.Spec itemColumnSpec              = spec(columnstart, columnspan);
                    GridLayout.Spec itemRowSpec              = spec(rowstart,rowspan);
                    GridLayout.LayoutParams gridlayout = (GridLayout.LayoutParams)view.getLayoutParams();
                            new GridLayout.LayoutParams(itemRowSpec, itemColumnSpec);
                    gridlayout.width = basePresenter.getRealWidth(mParent.getContext())*columnspan+(columnspan - 1) * itemmargin;
                    gridlayout.height = basePresenter.getRealHeight(mParent.getContext())*rowspan+(rowspan - 1) * itemmargin;
                    gridlayout.columnSpec = itemColumnSpec;
                    gridlayout.rowSpec = itemRowSpec;
                    gridlayout.setMargins(itemmargin / 2, itemmargin / 2, itemmargin / 2, itemmargin / 2);
                    vh.mGridLayout.addView(view, gridlayout);
                    View imageView = itemholder.getBaseSizeView();
                    ViewGroup.LayoutParams lpImg = imageView.getLayoutParams();
                    lpImg.width = itemwidth*columnspan+(columnspan-1)*itemmargin;
                    lpImg.height = itemheight*rowspan+(rowspan - 1) * itemmargin;

                    final int itemIdx = i;
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vh.getOnItemViewClickedListener().onItemClicked(itemholder,
                                    displayItemBlock.items.get(itemIdx), vh, null);

                        }
                    });

                }
            }

        }
    }
}
