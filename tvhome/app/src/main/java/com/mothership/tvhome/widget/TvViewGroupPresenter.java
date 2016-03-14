package com.mothership.tvhome.widget;

import android.view.ViewGroup;

import com.mothership.tvhome.view.TvViewGroup;
import com.tv.ui.metro.model.Block;

/**
 * Created by Shawn on 16/3/10.
 */
public class TvViewGroupPresenter extends RowPresenter
{


    @Override
    protected ViewHolder createRowViewHolder(ViewGroup parent)
    {
        RowPresenter.ViewHolder vh = new RowPresenter.ViewHolder(new TvViewGroup(parent.getContext(), null));
        return vh;
    }

    @Override
    protected void onBindRowViewHolder(ViewHolder vh, Object item)
    {
//        super.onBindRowViewHolder(vh, item);
        ((TvViewGroup)vh.view).setData((Block) item);
    }

    @Override
    protected void onUnbindRowViewHolder(ViewHolder vh)
    {
        ((TvViewGroup)vh.view).setData(null);
//        super.onUnbindRowViewHolder(vh);
    }

    //
//    @Override
//    public void onBindViewHolder(ViewHolder viewHolder, Object item)
//    {
//        ((TvViewGroup)viewHolder.view).setAdapter((RecyclerView.Adapter) item);
//    }
//
//    @Override
//    public void onUnbindViewHolder(ViewHolder viewHolder)
//    {
//        ((TvViewGroup)viewHolder.view).setAdapter(null);
//    }
}
