package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by wangwei on 2/29/16.
 */
public class CardPresenterSelector extends PresenterSelector{
    private final Presenter mPresenterPort = new CardPresenter();
    private final Presenter mPresenterLand = new LandCardPresenter();
    private final Presenter[] mPresenters = new Presenter[] {
            mPresenterPort, mPresenterLand};
    @Override
    public Presenter getPresenter(Object item) {
        DisplayItem displayItem = (DisplayItem)item;
        if(displayItem.id!=null&&displayItem.id.equals("land")){
            return mPresenterLand;
        }else{
            return mPresenterPort;
        }

    }
}
