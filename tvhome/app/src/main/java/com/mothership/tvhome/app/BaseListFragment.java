package com.mothership.tvhome.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mothership.tvhome.R;

/**
 * Base List Fragment
 *
 * Created by zhuzhenhua on 15-12-15.
 */
public abstract class BaseListFragment extends BaseFragment {

    protected TextView mTitleTv;
    protected RecyclerView mListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_list, container, false);
        initViews(res);
        initData();
        return  res;
    }

    protected void initData()
    {

    }

    protected void initViews(View root) {
        mTitleTv = (TextView) root.findViewById(R.id.title);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mTitleTv.getLayoutParams();
        params.bottomMargin = (int) getResources().getDimension(R.dimen.margin_small);
        mListView = (RecyclerView) root.findViewById(R.id.list);
        mListView.setFocusable(false);// 布局中设置不生效
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(getContext()));
        // mListView.addItemDecoration(new DividerItemDecoration(getContext()));
        mListView.post(new Runnable() {
            @Override
            public void run() {
                onRvLayoutFinish(mListView.getWidth(), mListView.getHeight());
            }
        });
    }

    public void requestFocus() {
        mListView.requestFocus();
    }

    protected void onRvLayoutFinish(int width, int height) {
        // TODO 子类根据需要重载该方法.可用得到Rv的宽高
    }

}
