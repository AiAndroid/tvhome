package com.mothership.tvhome.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.RowHeaderPresenter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mothership.tvhome.R;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by wangwei on 3/1/16.
 */
public class MainHeaderPresenter extends Presenter {

    private final int mLayoutResourceId;
    private final Paint mFontMeasurePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mNullItemVisibilityGone;
    HeaderItemFocusHighlight mHeaderItemFocusHighlight;
    public MainHeaderPresenter() {
        this(R.layout.main_header);
    }

    /**
     * @hide
     */
    public MainHeaderPresenter(int layoutResourceId) {
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Optionally sets the view visibility to {@link View#GONE} when bound to null.
     */
    public void setNullItemVisibilityGone(boolean nullItemVisibilityGone) {
        mNullItemVisibilityGone = nullItemVisibilityGone;
    }

    /**
     * Returns true if the view visibility is set to {@link View#GONE} when bound to null.
     */
    public boolean isNullItemVisibilityGone() {
        return mNullItemVisibilityGone;
    }

    /**
     * A ViewHolder for the RowHeaderPresenter.
     */
    public static class ViewHolder extends Presenter.ViewHolder {
        float mSelectLevel;
        int mOriginalTextColor;
        float mUnselectAlpha;

        public ViewHolder(View view) {
            super(view);
        }
        public final float getSelectLevel() {
            return mSelectLevel;
        }
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        MainHeaderView headerView = (MainHeaderView) LayoutInflater.from(parent.getContext())
                .inflate(mLayoutResourceId, parent, false);

        ViewHolder viewHolder = new ViewHolder(headerView);
        viewHolder.mOriginalTextColor = headerView.getCurrentTextColor();
        viewHolder.mUnselectAlpha = parent.getResources().getFraction(
                R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        setSelectLevel(viewHolder, 0);
        if(mHeaderItemFocusHighlight==null){
            mHeaderItemFocusHighlight = new HeaderItemFocusHighlight(parent.getContext());
        }
        headerView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(mHeaderItemFocusHighlight!=null) {
                    mHeaderItemFocusHighlight.onItemFocused(v,hasFocus);
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        if(item instanceof BlockAdapter) {
            BlockAdapter blockAdapter = (BlockAdapter)item;
            DisplayItem displayItem = (DisplayItem) blockAdapter.mBlock;
            if (displayItem.title != null) {
                viewHolder.view.setVisibility(View.VISIBLE);
                ((MainHeaderView) viewHolder.view).setText(displayItem.title);
            } else {
                ((MainHeaderView) viewHolder.view).setText(null);
                if (mNullItemVisibilityGone) {
                    viewHolder.view.setVisibility(View.GONE);
                }
            }
        }else{
            viewHolder.view.setVisibility(View.VISIBLE);
            ((MainHeaderView) viewHolder.view).setText("page");
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ((MainHeaderView) viewHolder.view).setText(null);
        setSelectLevel((ViewHolder) viewHolder, 0);
    }

    /**
     * Sets the select level.
     */
    public final void setSelectLevel(ViewHolder holder, float selectLevel) {
        holder.mSelectLevel = selectLevel;
        onSelectLevelChanged(holder);
    }

    /**
     * Called when the select level changes.  The default implementation sets the alpha on the view.
     */
    protected void onSelectLevelChanged(ViewHolder holder) {
        /*holder.view.setAlpha(holder.mUnselectAlpha + holder.mSelectLevel *
                (1f - holder.mUnselectAlpha));*/
    }

    /**
     * Returns the space (distance in pixels) below the baseline of the
     * text view, if one exists; otherwise, returns 0.
     */
    public int getSpaceUnderBaseline(ViewHolder holder) {
        int space = holder.view.getPaddingBottom();
        if (holder.view instanceof TextView) {
            space += (int) getFontDescent((TextView) holder.view, mFontMeasurePaint);
        }
        return space;
    }

    protected static float getFontDescent(TextView textView, Paint fontMeasurePaint) {
        if (fontMeasurePaint.getTextSize() != textView.getTextSize()) {
            fontMeasurePaint.setTextSize(textView.getTextSize());
        }
        if (fontMeasurePaint.getTypeface() != textView.getTypeface()) {
            fontMeasurePaint.setTypeface(textView.getTypeface());
        }
        return fontMeasurePaint.descent();
    }

    static class HeaderItemFocusHighlight {
        private static boolean sInitialized;
        private static float sSelectScale;
        private static int sDuration;

        HeaderItemFocusHighlight(Context context) {
            lazyInit(context.getResources());
        }

        private static void lazyInit(Resources res) {
            if (!sInitialized) {
                sSelectScale =
                        Float.parseFloat(res.getString(R.dimen.lb_browse_header_select_scale));
                sDuration =
                        Integer.parseInt(res.getString(R.dimen.lb_browse_header_select_duration));
                sInitialized = true;
            }
        }

        private void viewFocused(View view, boolean hasFocus) {
            view.setSelected(hasFocus);
            FocusHelper.FocusAnimator animator = (FocusHelper.FocusAnimator) view.getTag(android.support.v17.leanback.R.id.lb_focus_animator);
            if (animator == null) {
                animator = new FocusHelper.FocusAnimator(view, sSelectScale, false, sDuration);
                view.setTag(android.support.v17.leanback.R.id.lb_focus_animator, animator);
            }
            animator.animateFocus(hasFocus, false);
        }

        public void onItemFocused(View view, boolean hasFocus) {
            viewFocused(view, hasFocus);
        }

        public void onInitializeView(View view) {
        }

    }
}
