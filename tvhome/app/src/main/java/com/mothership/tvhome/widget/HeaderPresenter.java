package com.mothership.tvhome.widget;

import android.graphics.Paint;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowHeaderView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by wangwei on 3/1/16.
 */
public class HeaderPresenter extends Presenter {
    private final int mLayoutResourceId;
    private final Paint mFontMeasurePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mNullItemVisibilityGone;

    public HeaderPresenter() {
        this(android.support.v17.leanback.R.layout.lb_row_header);
    }

    /**
     * @hide
     */
    public HeaderPresenter(int layoutResourceId) {
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
        RowHeaderView headerView = (RowHeaderView) LayoutInflater.from(parent.getContext())
                .inflate(mLayoutResourceId, parent, false);

        ViewHolder viewHolder = new ViewHolder(headerView);
        viewHolder.mOriginalTextColor = headerView.getCurrentTextColor();
        viewHolder.mUnselectAlpha = parent.getResources().getFraction(
                android.support.v17.leanback.R.fraction.lb_browse_header_unselect_alpha, 1, 1);
        setSelectLevel(viewHolder, 0);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        HeaderItem headerItem = item == null ? null : ((Row) item).getHeaderItem();
        if (headerItem == null) {
            ((RowHeaderView) viewHolder.view).setText(null);
            if (mNullItemVisibilityGone) {
                viewHolder.view.setVisibility(View.GONE);
            }
        } else {
            viewHolder.view.setVisibility(View.VISIBLE);
            ((RowHeaderView) viewHolder.view).setText(headerItem.getName());
        }
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        ((RowHeaderView) viewHolder.view).setText(null);
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
        holder.view.setAlpha(holder.mUnselectAlpha + holder.mSelectLevel *
                (1f - holder.mUnselectAlpha));
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

}
