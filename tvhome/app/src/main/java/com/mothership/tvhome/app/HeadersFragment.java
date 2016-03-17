package com.mothership.tvhome.app;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnChildViewHolderSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.SinglePresenterSelector;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mothership.tvhome.R;
import com.mothership.tvhome.widget.MainHeaderPresenter;

/**
 * Created by wangwei on 3/1/16.
 */
public class HeadersFragment extends Fragment {

    interface OnHeaderClickedListener {
        void onHeaderClicked();
    }

    interface OnHeaderViewSelectedListener {
        void onHeaderSelected(Presenter.ViewHolder viewHolder,int position);
    }


    private ObjectAdapter mAdapter;
    private HorizontalGridView mHorizontalGridView;
    private PresenterSelector mPresenterSelector;
    private ItemBridgeAdapter mBridgeAdapter;
    private int mSelectedPosition = -1;
    private boolean mPendingTransitionPrepare;

    private OnHeaderViewSelectedListener mOnHeaderViewSelectedListener;
    private OnHeaderClickedListener mOnHeaderClickedListener;
    private boolean mHeadersEnabled = true;
    private boolean mHeadersGone = false;
    private int mBackgroundColor;
    private boolean mBackgroundColorSet;
    private static final PresenterSelector sHeaderPresenter = new SinglePresenterSelector(
            new MainHeaderPresenter());

    private final OnChildViewHolderSelectedListener mSelectedListener =
            new OnChildViewHolderSelectedListener() {
                @Override
                public void onChildViewHolderSelected(RecyclerView parent,
                                                      RecyclerView.ViewHolder view, int position, int subposition) {
                    onSelected(parent, view, position, subposition);
                }
            };

    public HeadersFragment() {
        mPresenterSelector = sHeaderPresenter;
    }


    public void setOnHeaderClickedListener(OnHeaderClickedListener listener) {
        mOnHeaderClickedListener = listener;
    }

    public void setOnHeaderViewSelectedListener(OnHeaderViewSelectedListener listener) {
        mOnHeaderViewSelectedListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.headers_fragment, container, false);
        mHorizontalGridView = (HorizontalGridView) view.findViewById(R.id.headers);
        updateAdapter();
        if (mPendingTransitionPrepare) {
            mPendingTransitionPrepare = false;
            onTransitionPrepare();
        }
        return view;
    }

    void onSelected(RecyclerView parent, RecyclerView.ViewHolder viewHolder,
                       int position, int subposition) {
        if (mOnHeaderViewSelectedListener != null) {
            if (viewHolder != null && position >= 0) {
                ItemBridgeAdapter.ViewHolder vh = (ItemBridgeAdapter.ViewHolder) viewHolder;
                mOnHeaderViewSelectedListener.onHeaderSelected(
                        (Presenter.ViewHolder) vh.getViewHolder(),position);
            } else {
                mOnHeaderViewSelectedListener.onHeaderSelected(null, 0);
            }
        }
    }

    private final ItemBridgeAdapter.AdapterListener mAdapterListener =
            new ItemBridgeAdapter.AdapterListener() {
                @Override
                public void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
                    View headerView = viewHolder.getViewHolder().view;
                    headerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mOnHeaderClickedListener != null) {
                                mOnHeaderClickedListener.onHeaderClicked();
                            }
                        }
                    });
                    headerView.setFocusable(true);
                    headerView.setFocusableInTouchMode(true);
                    if (mWrapper != null) {
                        viewHolder.itemView.addOnLayoutChangeListener(sLayoutChangeListener);
                    } else {
                        headerView.addOnLayoutChangeListener(sLayoutChangeListener);
                    }
                }

            };

    private static View.OnLayoutChangeListener sLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                   int oldLeft, int oldTop, int oldRight, int oldBottom) {
            v.setPivotX(v.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL ? v.getWidth() : 0);
            v.setPivotY(v.getMeasuredHeight() / 2);
        }
    };


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mHorizontalGridView == null) {
            return;
        }
        mHorizontalGridView.setOnChildViewHolderSelectedListener(mSelectedListener);

        //view.setBackgroundColor(getBackgroundColor());
        //updateFadingEdgeToBrandColor(getBackgroundColor());
        updateListViewVisibility();
    }

    private void updateListViewVisibility() {
        if (mHorizontalGridView != null) {
            getView().setVisibility(mHeadersGone ? View.GONE : View.VISIBLE);
            if (!mHeadersGone) {
                if (mHeadersEnabled) {
                    mHorizontalGridView.setChildrenVisibility(View.VISIBLE);
                } else {
                    mHorizontalGridView.setChildrenVisibility(View.INVISIBLE);
                }
            }
        }
    }

    void setHeadersEnabled(boolean enabled) {
        mHeadersEnabled = enabled;
        updateListViewVisibility();
    }

    void setHeadersGone(boolean gone) {
        mHeadersGone = gone;
        updateListViewVisibility();
    }

    static class NoOverlappingFrameLayout extends FrameLayout {

        public NoOverlappingFrameLayout(Context context) {
            super(context);
        }

        /**
         * Avoid creating hardware layer for header dock.
         */
        @Override
        public boolean hasOverlappingRendering() {
            return false;
        }
    }

    // Wrapper needed because of conflict between RecyclerView's use of alpha
    // for ADD animations, and RowHeaderPresnter's use of alpha for selected level.
    private final ItemBridgeAdapter.Wrapper mWrapper = null;/*new ItemBridgeAdapter.Wrapper() {
        @Override
        public void wrap(View wrapper, View wrapped) {
            ((FrameLayout) wrapper).addView(wrapped);
        }

        @Override
        public View createWrapper(View root) {
            return new NoOverlappingFrameLayout(root.getContext());
        }
    };*/

    /**
     * Sets the adapter for the fragment.
     */
    public final void setAdapter(ObjectAdapter Adapter) {
        mAdapter = Adapter;
        updateAdapter();
    }

    void updateAdapter() {
        if (mBridgeAdapter != null) {
            // detach observer from ObjectAdapter
            mBridgeAdapter.clear();
            mBridgeAdapter = null;
        }

        if (mAdapter != null) {
            // If presenter selector is null, adapter ps will be used
            mBridgeAdapter = new ItemBridgeAdapter(mAdapter, mPresenterSelector);
        }
        if (mHorizontalGridView != null) {
            mHorizontalGridView.setAdapter(mBridgeAdapter);
            if (mBridgeAdapter != null && mSelectedPosition != -1) {
                mHorizontalGridView.setSelectedPosition(mSelectedPosition);
            }
        }
        ItemBridgeAdapter adapter = mBridgeAdapter;
        if (adapter != null) {
            adapter.setAdapterListener(mAdapterListener);
            adapter.setWrapper(mWrapper);
        }
        if (adapter != null && mHorizontalGridView!= null) {
         //   FocusHighlightHelper.setupHeaderItemFocusHighlight(getVerticalGridView());
        }
    }

    void setBackgroundColor(int color) {
        mBackgroundColor = color;
        mBackgroundColorSet = true;

        if (getView() != null) {
            getView().setBackgroundColor(mBackgroundColor);
            updateFadingEdgeToBrandColor(mBackgroundColor);
        }
    }

    private void updateFadingEdgeToBrandColor(int backgroundColor) {
        View fadingView = getView().findViewById(R.id.fade_out_edge);
        Drawable background = fadingView.getBackground();
        if (background instanceof GradientDrawable) {
            background.mutate();
            ((GradientDrawable) background).setColors(
                    new int[]{Color.TRANSPARENT, backgroundColor});
        }
    }

    int getBackgroundColor() {
        if (getActivity() == null) {
            throw new IllegalStateException("Activity must be attached");
        }

        if (mBackgroundColorSet) {
            return mBackgroundColor;
        }

        TypedValue outValue = new TypedValue();
        if (getActivity().getTheme().resolveAttribute(R.attr.defaultBrandColor, outValue, true)) {
            return getResources().getColor(outValue.resourceId);
        }
        return getResources().getColor(R.color.lb_default_brand_color);
    }


    boolean onTransitionPrepare() {
        if (mHorizontalGridView != null) {
            mHorizontalGridView.setAnimateChildLayout(false);
            mHorizontalGridView.setScrollEnabled(false);
            return true;
        }
        mPendingTransitionPrepare = true;
        return false;
    }

    void onTransitionStart() {
        if (mHorizontalGridView != null) {
            mHorizontalGridView.setPruneChild(false);
            mHorizontalGridView.setLayoutFrozen(true);
            mHorizontalGridView.setFocusSearchDisabled(true);
        }
        if (!mHeadersEnabled) {
            // When enabling headers fragment,  the RowHeaderView gets a focus but
            // isShown() is still false because its parent is INVSIBILE, accessibility
            // event is not sent.
            // Workaround is: prevent focus to a child view during transition and put
            // focus on it after transition is done.
            if (mHorizontalGridView != null) {
                mHorizontalGridView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                if (mHorizontalGridView.hasFocus()) {
                    mHorizontalGridView.requestFocus();
                }
            }
        }
    }

    void onTransitionEnd() {
        if (mHeadersEnabled) {
            if (mHorizontalGridView != null) {
                mHorizontalGridView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                if (mHorizontalGridView.hasFocus()) {
                    mHorizontalGridView.requestFocus();
                }
            }
        }
        // be careful that fragment might be destroyed before header transition ends.
        if (mHorizontalGridView != null) {
            mHorizontalGridView.setLayoutFrozen(false);
            mHorizontalGridView.setAnimateChildLayout(true);
            mHorizontalGridView.setPruneChild(true);
            mHorizontalGridView.setFocusSearchDisabled(false);
            mHorizontalGridView.setScrollEnabled(true);
        }
    }

    void setItemAlignment() {
        if (mHorizontalGridView != null) {
            // align the top edge of item
            mHorizontalGridView.setItemAlignmentOffset(0);
            mHorizontalGridView.setItemAlignmentOffsetPercent(
                    VerticalGridView.ITEM_ALIGN_OFFSET_PERCENT_DISABLED);
        }
    }

    void setWindowAlignmentFromTop(int alignedTop) {
        if (mHorizontalGridView != null) {
            // align to a fixed position from top
            mHorizontalGridView.setWindowAlignmentOffset(alignedTop);
            mHorizontalGridView.setWindowAlignmentOffsetPercent(
                    VerticalGridView.WINDOW_ALIGN_OFFSET_PERCENT_DISABLED);
            mHorizontalGridView.setWindowAlignment(VerticalGridView.WINDOW_ALIGN_NO_EDGE);
        }
    }

    public void requestFocus(){
        //if(mHorizontalGridView!=null) {
            mHorizontalGridView.requestFocus();
        //}
    }

    public void setSelectedPosition(int position){
        mHorizontalGridView.setSelectedPosition(position);
    }
}
