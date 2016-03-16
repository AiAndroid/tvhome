package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.util.Log;
import android.util.SparseArray;

import com.mothership.tvhome.Utils;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by Shawn on 16/3/8.
 */
public class DisplayItemSelector extends PresenterSelector
{
    private static final String TAG = "DisplayItemSelector";
    SparseArray<Presenter> mPresenters = new SparseArray<Presenter>();
    BasePresenter mDefaultPresenter = new BasePresenter();

    public DisplayItemSelector()
    {
        mPresenters.put(1, mDefaultPresenter);
        mPresenters.put(2, new PresenterT2());
        mPresenters.put(3, new PresenterT3());
    }
    @Override
    public Presenter getPresenter(Object aItem)
    {
        DisplayItem di = (DisplayItem) aItem;
        DisplayItem.UI type = di.ui_type;
//        return mDefaultPresenter;
        int id = Utils.UiNameToId(di);
        if(type != null)
        {

            Log.d(TAG, "type " + type.id());
            return mPresenters.get(id%100, mDefaultPresenter);
        }
        else
        {
            Log.d(TAG, di.title + " doesn't has ui_type");
            return mDefaultPresenter;
        }
    }

}
