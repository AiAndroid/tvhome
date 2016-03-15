package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.util.SparseArray;

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
        mPresenters.put(101, mVerticalPresenter);
        mPresenters.put(100, mTVhomBlockPresenter);
        mPresenters.put(110, new BlockGridPresenter());
    }
    @Override
    public Presenter getPresenter(Object item) {
        DisplayItem di = (DisplayItem) item;
        DisplayItem.UI type = di.ui_type;
        if(type != null)
        {
            return mPresenters.get(di.ui_type.id(), mVerticalPresenter);
        }
        else
        {
            //Log.d(TAG, di.title + " doesn't has ui_type");
            //return mDefaultPresenter;
        }
        return null;
    }
}
