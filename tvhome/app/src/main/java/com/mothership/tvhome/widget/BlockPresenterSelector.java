package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.util.SparseArray;

import com.mothership.tvhome.Utils;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by wangwei on 3/10/16.
 */
public class BlockPresenterSelector extends PresenterSelector {
    SparseArray<Presenter> mPresenters = new SparseArray<Presenter>();
    BlockVerticalPresenter mVerticalPresenter = new BlockVerticalPresenter();
    BlockHorizontalPresenter mHorizontalPresenter = new BlockHorizontalPresenter();
    TVhomBlockPresenter mTVhomBlockPresenter = new TVhomBlockPresenter();
    public BlockPresenterSelector()
    {
        mPresenters.put(0, mTVhomBlockPresenter);
        mPresenters.put(1, mVerticalPresenter);
        mPresenters.put(11, new BlockGridPresenter());
    }
    @Override
    public Presenter getPresenter(Object item) {
        DisplayItem di = (DisplayItem) item;
        DisplayItem.UI type = di.ui_type;
        int id = Utils.UiNameToId(di);
        if(type != null)
        {
            return mPresenters.get(id/100%100, mVerticalPresenter);
        }
        else
        {
            //Log.d(TAG, di.title + " doesn't has ui_type");
            //return mDefaultPresenter;
        }
        return null;
    }
}
