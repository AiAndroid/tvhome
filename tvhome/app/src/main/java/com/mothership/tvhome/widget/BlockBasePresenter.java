package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v17.leanback.system.Settings;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.FocusHighlight;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v17.leanback.widget.ItemAlignmentFacet;
import android.support.v17.leanback.widget.ItemBridgeAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowView;
import android.support.v17.leanback.widget.OnChildSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.ShadowOverlayHelper;
import android.support.v17.leanback.widget.VerticalGridView;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.mothership.tvhome.R;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;

import java.util.HashMap;

/**
 * Created by wangwei on 3/4/16.
 */
public class BlockBasePresenter extends RowPresenter {
    private static final String TAG = "BlockBasePresenter";
    private static final boolean DEBUG = true;
    private static final int DEFAULT_RECYCLED_POOL_SIZE = 30;
    final DisplayItemSelector mDisplayItemSelector = new DisplayItemSelector();
    /**
     * ViewHolder for the BlockBasePresenter.
     */
    public static class ViewHolder extends RowPresenter.ViewHolder {
        final BlockBasePresenter mBlockBasePresenter;
        final RecyclerView mGridView;
        ItemBridgeAdapter mItemBridgeAdapter;
        //final HorizontalHoverCardSwitcher mHoverCardViewSwitcher = new HorizontalHoverCardSwitcher();
        final int mPaddingTop;
        final int mPaddingBottom;
        final int mPaddingLeft;
        final int mPaddingRight;

        public ViewHolder(View rootView, RecyclerView gridView, BlockBasePresenter p) {
            super(rootView);
            mGridView = gridView;
            mBlockBasePresenter = p;
            mPaddingTop = mGridView.getPaddingTop();
            mPaddingBottom = mGridView.getPaddingBottom();
            mPaddingLeft = mGridView.getPaddingLeft();
            mPaddingRight = mGridView.getPaddingRight();

            if (mGridView instanceof HorizontalGridView) {
                HorizontalGridView gridview = (HorizontalGridView) mGridView;
                gridview.setScrollEnabled(false);
            } else if (mGridView instanceof VerticalGridView) {
                VerticalGridView gridview = (VerticalGridView) mGridView;
                gridview.setScrollEnabled(false);
            }

        }

        public final BlockBasePresenter getBlockPresenter() {
            return mBlockBasePresenter;
        }

        public final RecyclerView getGridView() {
            return mGridView;
        }

        public final ItemBridgeAdapter getBridgeAdapter() {
            return mItemBridgeAdapter;
        }
    }


    class BlockPresenterItemBridgeAdapter extends ItemBridgeAdapter {
        BlockBasePresenter.ViewHolder mBlockViewHolder;

        BlockPresenterItemBridgeAdapter(BlockBasePresenter.ViewHolder rowViewHolder) {
            mBlockViewHolder = rowViewHolder;
        }

        @Override
        protected void onCreate(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (viewHolder.itemView instanceof ViewGroup) {
                //TransitionHelper.setTransitionGroup((ViewGroup) viewHolder.itemView, true);
            }
            //if (mShadowOverlayHelper != null) {
            //    mShadowOverlayHelper.onViewCreated(viewHolder.itemView);
            //}
        }

        @Override
        public void onBind(final ItemBridgeAdapter.ViewHolder viewHolder) {
            // Only when having an OnItemClickListner, we will attach the OnClickListener.
            if (mBlockViewHolder.getOnItemViewClickedListener() != null) {
                viewHolder.getViewHolder().view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ItemBridgeAdapter.ViewHolder ibh = (ItemBridgeAdapter.ViewHolder)
                                mBlockViewHolder.mGridView.getChildViewHolder(viewHolder.itemView);
                        if (mBlockViewHolder.getOnItemViewClickedListener() != null) {
                            mBlockViewHolder.getOnItemViewClickedListener().onItemClicked(viewHolder.getViewHolder(),
                                    ibh.getItem(), mBlockViewHolder, (Row) mBlockViewHolder.getRow());
                        }
                    }
                });
            }
        }

        @Override
        public void onUnbind(ItemBridgeAdapter.ViewHolder viewHolder) {
            if (mBlockViewHolder.getOnItemViewClickedListener() != null) {
                viewHolder.getViewHolder().view.setOnClickListener(null);
            }
        }

        @Override
        public void onAttachedToWindow(ItemBridgeAdapter.ViewHolder viewHolder) {
            //if (mShadowOverlayHelper != null && mShadowOverlayHelper.needsOverlay()) {
                //int dimmedColor = mRowViewHolder.mColorDimmer.getPaint().getColor();
                //mShadowOverlayHelper.setOverlayColor(viewHolder.itemView, dimmedColor);
            //}
            mBlockViewHolder.syncActivatedStatus(viewHolder.itemView);
        }

        @Override
        public void onAddPresenter(Presenter presenter, int type) {
            mBlockViewHolder.getGridView().getRecycledViewPool().setMaxRecycledViews(
                    type, getRecycledPoolSize(presenter));
        }
    }

    private int mRowHeight;
    private int mExpandedRowHeight;
    private PresenterSelector mHoverCardPresenterSelector;
    private int mFocusZoomFactor;
    private boolean mUseFocusDimmer;
    private boolean mShadowEnabled = true;
    private int mBrowseRowsFadingEdgeLength = -1;
    private boolean mRoundedCornersEnabled = true;
    private boolean mKeepChildForeground = true;
    private HashMap<Presenter, Integer> mRecycledPoolSize = new HashMap<Presenter, Integer>();
    //private ShadowOverlayHelper mShadowOverlayHelper;
    //private ItemBridgeAdapter.Wrapper mShadowOverlayWrapper;

    //private static int sSelectedRowTopPadding;
    //private static int sExpandedSelectedRowTopPadding;
    //private static int sExpandedRowNoHovercardBottomPadding;

    private View mParent;

    /**
     * Constructs a ListRowPresenter with defaults.
     * Uses {@link FocusHighlight#ZOOM_FACTOR_MEDIUM} for focus zooming and
     * disabled dimming on focus.
     */
    public BlockBasePresenter() {
        this(FocusHighlight.ZOOM_FACTOR_MEDIUM);
    }

    /**
     * Constructs a ListRowPresenter with the given parameters.
     *
     * @param focusZoomFactor Controls the zoom factor used when an item view is focused. One of
     *         {@link FocusHighlight#ZOOM_FACTOR_NONE},
     *         {@link FocusHighlight#ZOOM_FACTOR_SMALL},
     *         {@link FocusHighlight#ZOOM_FACTOR_XSMALL},
     *         {@link FocusHighlight#ZOOM_FACTOR_MEDIUM},
     *         {@link FocusHighlight#ZOOM_FACTOR_LARGE}
     * Dimming on focus defaults to disabled.
     */
    public BlockBasePresenter(int focusZoomFactor) {
        this(focusZoomFactor, false);
    }

    /**
     * Constructs a ListRowPresenter with the given parameters.
     *
     * @param focusZoomFactor Controls the zoom factor used when an item view is focused. One of
     *         {@link FocusHighlight#ZOOM_FACTOR_NONE},
     *         {@link FocusHighlight#ZOOM_FACTOR_SMALL},
     *         {@link FocusHighlight#ZOOM_FACTOR_XSMALL},
     *         {@link FocusHighlight#ZOOM_FACTOR_MEDIUM},
     *         {@link FocusHighlight#ZOOM_FACTOR_LARGE}
     * @param useFocusDimmer determines if the FocusHighlighter will use the dimmer
     */
    public BlockBasePresenter(int focusZoomFactor, boolean useFocusDimmer) {
        //if (!FocusHighlightHelper.isValidZoomIndex(focusZoomFactor)) {
        //    throw new IllegalArgumentException("Unhandled zoom factor");
        //}
        mFocusZoomFactor = focusZoomFactor;
        mUseFocusDimmer = useFocusDimmer;
    }

    /**
     * Sets the row height for rows created by this Presenter. Rows
     * created before calling this method will not be updated.
     *
     * @param rowHeight Row height in pixels, or WRAP_CONTENT, or 0
     * to use the default height.
     */
    //public void setRowHeight(int rowHeight) {
    //    mRowHeight = rowHeight;
    //}

    /**
     * Returns the row height for list rows created by this Presenter.
     */
    //public int getRowHeight() {
    //    return mRowHeight;
    //}


    /**
     * Sets the expanded row height for rows created by this Presenter.
     * If not set, expanded rows have the same height as unexpanded
     * rows.
     *
     * @param rowHeight The row height in to use when the row is expanded,
     *        in pixels, or WRAP_CONTENT, or 0 to use the default.
     */
    public void setExpandedRowHeight(int rowHeight) {
        mExpandedRowHeight = rowHeight;
    }

    /**
     * Returns the expanded row height for rows created by this Presenter.
     */
    public int getExpandedRowHeight() {
        return mExpandedRowHeight != 0 ? mExpandedRowHeight : mRowHeight;
    }

    /**
     * Returns the zoom factor used for focus highlighting.
     */
    public final int getFocusZoomFactor() {
        return mFocusZoomFactor;
    }

    /**
     * Returns the zoom factor used for focus highlighting.
     * @deprecated use {@link #getFocusZoomFactor} instead.
     */
    @Deprecated
    public final int getZoomFactor() {
        return mFocusZoomFactor;
    }

    /**
     * Returns true if the focus dimmer is used for focus highlighting; false otherwise.
     */
    public final boolean isFocusDimmerUsed() {
        return mUseFocusDimmer;
    }


    @Override
    protected void initializeRowViewHolder(RowPresenter.ViewHolder holder) {
        super.initializeRowViewHolder(holder);
        final ViewHolder blockViewHolder = (ViewHolder) holder;
        Context context = holder.view.getContext();
/*        if (mShadowOverlayHelper == null) {
            mShadowOverlayHelper = new ShadowOverlayHelper.Builder()
                    .needsOverlay(needsDefaultListSelectEffect())
                    .needsShadow(needsDefaultShadow())
                    .needsRoundedCorner(areChildRoundedCornersEnabled())
                    .preferZOrder(isUsingZOrder(context))
                    .keepForegroundDrawable(mKeepChildForeground)
                    .options(createShadowOverlayOptions())
                    .build(context);
            if (mShadowOverlayHelper.needsWrapper()) {
                mShadowOverlayWrapper = new ItemBridgeAdapterShadowOverlayWrapper(
                        mShadowOverlayHelper);
            }
        }*/
        blockViewHolder.mItemBridgeAdapter = new BlockPresenterItemBridgeAdapter(blockViewHolder);
        // set wrapper if needed
        //rowViewHolder.mItemBridgeAdapter.setWrapper(mShadowOverlayWrapper);
        //mShadowOverlayHelper.prepareParentForShadow(rowViewHolder.mGridView);

       // FocusHighlightHelper.setupBrowseItemFocusHighlight(blockViewHolder.mItemBridgeAdapter,
       //         mFocusZoomFactor, mUseFocusDimmer);
        //blockViewHolder.mGridView.setFocusDrawingOrderEnabled(mShadowOverlayHelper.getShadowType()
        //        == ShadowOverlayHelper.SHADOW_STATIC);
        setEventListener(blockViewHolder.mGridView,blockViewHolder);
    }

    private void setEventListener(RecyclerView view,ViewHolder viewHolder){
        final ViewHolder blockViewHolder = viewHolder;
        if(view instanceof HorizontalGridView){
            HorizontalGridView horizontalGridView = (HorizontalGridView)view;
            horizontalGridView.setOnChildSelectedListener(
                    new OnChildSelectedListener() {
                        @Override
                        public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                            selectChildView(blockViewHolder, view, true);
                        }
                    });
            horizontalGridView.setOnUnhandledKeyListener(
                    new HorizontalGridView.OnUnhandledKeyListener() {
                        @Override
                        public boolean onUnhandledKey(KeyEvent event) {
                            if (blockViewHolder.getOnKeyListener() != null &&
                                    blockViewHolder.getOnKeyListener().onKey(
                                            blockViewHolder.view, event.getKeyCode(), event)) {
                                return true;
                            }
                            return false;
                        }
                    });
        }else if(view instanceof VerticalGridView){
            VerticalGridView verticalGridView = (VerticalGridView)view;
            verticalGridView.setOnChildSelectedListener(
                    new OnChildSelectedListener() {
                        @Override
                        public void onChildSelected(ViewGroup parent, View view, int position, long id) {
                            selectChildView(blockViewHolder, view, true);
                        }
                    });
            verticalGridView.setOnUnhandledKeyListener(
                    new HorizontalGridView.OnUnhandledKeyListener() {
                        @Override
                        public boolean onUnhandledKey(KeyEvent event) {
                            if (blockViewHolder.getOnKeyListener() != null &&
                                    blockViewHolder.getOnKeyListener().onKey(
                                            blockViewHolder.view, event.getKeyCode(), event)) {
                                return true;
                            }
                            return false;
                        }
                    });
        }
    }

    /**
     * Sets the recycled pool size for the given presenter.
     */
    public void setRecycledPoolSize(Presenter presenter, int size) {
        mRecycledPoolSize.put(presenter, size);
    }

    /**
     * Returns the recycled pool size for the given presenter.
     */
    public int getRecycledPoolSize(Presenter presenter) {
        return mRecycledPoolSize.containsKey(presenter) ? mRecycledPoolSize.get(presenter) :
                DEFAULT_RECYCLED_POOL_SIZE;
    }

    /**
     * Sets the {@link PresenterSelector} used for showing a select object in a hover card.
     */
    public final void setHoverCardPresenterSelector(PresenterSelector selector) {
        mHoverCardPresenterSelector = selector;
    }

    /**
     * Returns the {@link PresenterSelector} used for showing a select object in a hover card.
     */
    public final PresenterSelector getHoverCardPresenterSelector() {
        return mHoverCardPresenterSelector;
    }

    /*
     * Perform operations when a child of horizontal grid view is selected.
     */
    private void selectChildView(ViewHolder rowViewHolder, View view, boolean fireEvent) {
        if (view != null) {
            if (rowViewHolder.isExpanded() && rowViewHolder.isSelected()) {
                ItemBridgeAdapter.ViewHolder ibh = (ItemBridgeAdapter.ViewHolder)
                        rowViewHolder.mGridView.getChildViewHolder(view);

                /*if (mHoverCardPresenterSelector != null) {
                    rowViewHolder.mHoverCardViewSwitcher.select(
                            rowViewHolder.mGridView, view, ibh.getItem());
                }*/
                if (fireEvent && rowViewHolder.getOnItemViewSelectedListener() != null) {
                    rowViewHolder.getOnItemViewSelectedListener().onItemSelected(
                            ibh.getViewHolder(), ibh.getItem(), rowViewHolder, rowViewHolder.getRow());
                }
            }
        } else {
            /*if (mHoverCardPresenterSelector != null) {
                rowViewHolder.mHoverCardViewSwitcher.unselect();
            }*/
            if (fireEvent && rowViewHolder.getOnItemViewSelectedListener() != null) {
                rowViewHolder.getOnItemViewSelectedListener().onItemSelected(
                        null, null, rowViewHolder, rowViewHolder.getRow());
            }
        }
    }
/*
    private void setVerticalPadding(ListRowPresenter.ViewHolder vh) {
        int paddingTop, paddingBottom;
        // Note: sufficient bottom padding needed for card shadows.
        if (vh.isExpanded()) {
            int headerSpaceUnderBaseline = getSpaceUnderBaseline(vh);
            if (DEBUG) Log.v(TAG, "headerSpaceUnderBaseline " + headerSpaceUnderBaseline);
            paddingTop = (vh.isSelected() ? sExpandedSelectedRowTopPadding : vh.mPaddingTop) -
                    headerSpaceUnderBaseline;
            paddingBottom = mHoverCardPresenterSelector == null ?
                    sExpandedRowNoHovercardBottomPadding : vh.mPaddingBottom;
        } else if (vh.isSelected()) {
            paddingTop = sSelectedRowTopPadding - vh.mPaddingBottom;
            paddingBottom = sSelectedRowTopPadding;
        } else {
            paddingTop = 0;
            paddingBottom = vh.mPaddingBottom;
        }
        vh.getGridView().setPadding(vh.mPaddingLeft, paddingTop, vh.mPaddingRight,
                paddingBottom);
    }*/

    protected BlockBasePresenter.ViewHolder createBlockViewHolder(ViewGroup parent){
        return null;
    }
    @Override
    protected BlockBasePresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
        mParent = parent;
        return createBlockViewHolder(parent);
    }

    /**
     * Dispatch item selected event using current selected item in the {@link HorizontalGridView}.
     * The method should only be called from onRowViewSelected().
     */
    @Override
    protected void dispatchItemSelectedListener(RowPresenter.ViewHolder holder, boolean selected) {
        ViewHolder vh = (ViewHolder)holder;
        ItemBridgeAdapter.ViewHolder itemViewHolder = null;
        if(vh.mGridView instanceof HorizontalGridView){
            HorizontalGridView view = (HorizontalGridView)vh.mGridView;
            itemViewHolder = (ItemBridgeAdapter.ViewHolder)
                    vh.mGridView.findViewHolderForPosition(view.getSelectedPosition());
        }else if(vh.mGridView instanceof VerticalGridView){
            VerticalGridView view = (VerticalGridView)vh.mGridView;
            itemViewHolder = (ItemBridgeAdapter.ViewHolder)
                    vh.mGridView.findViewHolderForPosition(view.getSelectedPosition());
        }

        if (itemViewHolder == null) {
            super.dispatchItemSelectedListener(holder, selected);
            return;
        }

        if (selected) {
            if (holder.getOnItemViewSelectedListener() != null) {
                holder.getOnItemViewSelectedListener().onItemSelected(
                        itemViewHolder.getViewHolder(), itemViewHolder.getItem(), vh, vh.getRow());
            }
        }
    }

    @Override
    protected void onRowViewSelected(RowPresenter.ViewHolder holder, boolean selected) {
        super.onRowViewSelected(holder, selected);
        ViewHolder vh = (ViewHolder) holder;
        //setVerticalPadding(vh);
        updateFooterViewSwitcher(vh);
    }

    /*
 * Show or hide hover card when row selection or expanded state is changed.
 */
    private void updateFooterViewSwitcher(ViewHolder vh) {
        if (vh.isExpanded() && vh.isExpanded()) {
            if (mHoverCardPresenterSelector != null) {
                //vh.mHoverCardViewSwitcher.init((ViewGroup) vh.view,
                //        mHoverCardPresenterSelector);
            }
            ItemBridgeAdapter.ViewHolder ibh = null;
            if(vh.mGridView instanceof HorizontalGridView){
                HorizontalGridView view = (HorizontalGridView)vh.mGridView;
                ibh = (ItemBridgeAdapter.ViewHolder)
                        vh.mGridView.findViewHolderForPosition(view.getSelectedPosition());
            }else if(vh.mGridView instanceof VerticalGridView){
                VerticalGridView view = (VerticalGridView)vh.mGridView;
                ibh = (ItemBridgeAdapter.ViewHolder)
                        vh.mGridView.findViewHolderForPosition(view.getSelectedPosition());
            }
            selectChildView(vh, ibh == null ? null : ibh.itemView, false);
        } else {
            if (mHoverCardPresenterSelector != null) {
                //vh.mHoverCardViewSwitcher.unselect();
            }
        }
    }
    private void setupFadingEffect(ListRowView rowView) {
        // content is completely faded at 1/2 padding of left, fading length is 1/2 of padding.
        HorizontalGridView gridView = rowView.getGridView();
        /*if (mBrowseRowsFadingEdgeLength < 0) {
            TypedArray ta = gridView.getContext()
                    .obtainStyledAttributes(R.styleable.LeanbackTheme);
            mBrowseRowsFadingEdgeLength = (int) ta.getDimension(
                    R.styleable.LeanbackTheme_browseRowsFadingEdgeLength, 0);
            ta.recycle();
        }*/
        gridView.setFadingLeftEdgeLength(mBrowseRowsFadingEdgeLength);
    }

    @Override
    protected void onRowViewExpanded(RowPresenter.ViewHolder holder, boolean expanded) {
        super.onRowViewExpanded(holder, expanded);
        ViewHolder vh = (ViewHolder) holder;
        /*if (getRowHeight() != getExpandedRowHeight()) {
            int newHeight = expanded ? getExpandedRowHeight() : getRowHeight();
            vh.getGridView().setRowHeight(newHeight);
        }*/
        //setVerticalPadding(vh);
        updateFooterViewSwitcher(vh);
    }

    @Override
    protected void onBindRowViewHolder(RowPresenter.ViewHolder holder, Object item) {
        ViewHolder vh = (ViewHolder) holder;
        if(item instanceof Block){
            Block<DisplayItem> displayItemBlock = (Block<DisplayItem>)item;
            int type = displayItemBlock.ui_type.id();
            {
                BasePresenter basePresenter = (BasePresenter)mDisplayItemSelector.getPresenter(displayItemBlock);
                super.onBindRowViewHolder(holder, new Row(new HeaderItem(0,displayItemBlock.title)));
                //super.onBindRowViewHolder(holder, new Row(new HeaderItem(0,"")));
                if (displayItemBlock.items != null) {
                    int columns = displayItemBlock.ui_type.columns();
                    int rows = displayItemBlock.items.size() / columns;
                    if(displayItemBlock.items.size()%columns>0){
                        rows+=1;
                    }
                    ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(basePresenter);

                    int itemmargin = (int) mParent.getResources().getDimension(R.dimen.grid_item_margin);
                    int gridpaddingHor = (int) mParent.getResources().getDimension(R.dimen.grid_block_horizontal_padding);
                    int itemwidth = (int) ((mParent.getWidth() - gridpaddingHor*2
                            - itemmargin * (columns - 1)) / columns);
                    int itemheight = (int) (itemwidth / displayItemBlock.ui_type.ratio());
                    if (vh.mGridView instanceof HorizontalGridView) {
                        //for(int k=0;k<columns;++k) {
                            for (int i = 0; i < displayItemBlock.items.size(); ++i) {
                                //if(i%columns==k) {
                                    listRowAdapter.add(displayItemBlock.items.get(i));
                                //}
                            }
                        //}
                        vh.mItemBridgeAdapter.setAdapter(listRowAdapter);
                        vh.mGridView.setAdapter(vh.mItemBridgeAdapter);
                        HorizontalGridView gridView = (HorizontalGridView) vh.mGridView;
                        gridView.setNumRows(rows);
                        gridView.setItemMargin(itemmargin);
                        basePresenter.setBaseSize(itemwidth, (int) (itemwidth / displayItemBlock.ui_type.ratio()));
                    } else if (vh.mGridView instanceof VerticalGridView) {
                        for (int i = 0; i < displayItemBlock.items.size(); ++i) {
                            listRowAdapter.add(displayItemBlock.items.get(i));
                        }
                        vh.mItemBridgeAdapter.setAdapter(listRowAdapter);
                        vh.mGridView.setAdapter(vh.mItemBridgeAdapter);
                        VerticalGridView gridView = (VerticalGridView) vh.mGridView;
                        gridView.setNumColumns(columns);
                        gridView.setItemMargin(itemmargin);
                        basePresenter.setBaseSize(itemwidth, itemheight);
                        //TODO get from recycler
//                    Log.d(TAG, "create measure item");
//                    RecyclerView.ViewHolder bVh = vh.mItemBridgeAdapter.onCreateViewHolder(vh.mGridView, vh.mItemBridgeAdapter.getItemViewType(0));
//                    bVh.itemView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                    int height = bVh.itemView.getMeasuredHeight();
                        //TODO recycle bVh
                        ViewGroup.LayoutParams lp = gridView.getLayoutParams();
                        lp.height = (basePresenter.getRealHeight(mParent.getContext())) * rows + gridView.getHorizontalMargin() * (rows-1)
                                + gridView.getPaddingTop() + gridView.getPaddingBottom();
                        gridView.setLayoutParams(lp);
                    }
                    if(rows>1){
                        ItemAlignmentFacet.ItemAlignmentDef[] defs = new ItemAlignmentFacet.ItemAlignmentDef[rows + 1];
                        ItemAlignmentFacet facet = new ItemAlignmentFacet();
                        // by default align details_frame to half window height
                        ItemAlignmentFacet.ItemAlignmentDef alignDef0 = new ItemAlignmentFacet.ItemAlignmentDef();
                        alignDef0.setItemAlignmentOffset(0);
                        alignDef0.setItemAlignmentOffsetPercent(50);
                        defs[0] = alignDef0;

                        for (int i = 0; i < rows; ++i) {
                            // when description is selected, align details_frame to top edge
                            ItemAlignmentFacet.ItemAlignmentDef alignDef2 = new ItemAlignmentFacet.ItemAlignmentDef();
                            alignDef2.setItemAlignmentFocusViewId(100000 + i);
                            alignDef2.setItemAlignmentOffsetPercent(100f / rows * i + 100f / rows * 0.7f);
                            defs[i + 1] = alignDef2;
                        }

                        facet.setAlignmentDefs(defs);
                        vh.mContainerViewHolder.setFacet(ItemAlignmentFacet.class, facet);

                        for (int i = 0; i < displayItemBlock.items.size(); ++i) {
                            if (displayItemBlock.items.get(i).ui_type == null) {
                                displayItemBlock.items.get(i).ui_type = new DisplayItem.UI();
                            }
                            displayItemBlock.items.get(i).ui_type.put("rows", "" + i / columns);
                        }
                    }else{
                        for (int i = 0; i < displayItemBlock.items.size(); ++i) {
                            displayItemBlock.items.get(i).ui_type = null;
                        }
                    }
                }

            }
        }else{
            super.onBindRowViewHolder(holder, item);
            ListRow rowItem = (ListRow) item;
            vh.mItemBridgeAdapter.setAdapter(rowItem.getAdapter());
            vh.mGridView.setAdapter(vh.mItemBridgeAdapter);
        }

    }

    @Override
    protected void onUnbindRowViewHolder(RowPresenter.ViewHolder holder) {
        ViewHolder vh = (ViewHolder) holder;
        vh.mGridView.setAdapter(null);
        vh.mItemBridgeAdapter.clear();
        super.onUnbindRowViewHolder(holder);
    }

    /**
     * ListRowPresenter overrides the default select effect of {@link RowPresenter}
     * and return false.
     */
    @Override
    public final boolean isUsingDefaultSelectEffect() {
        return false;
    }

    /**
     * Returns true so that default select effect is applied to each individual
     * child of {@link HorizontalGridView}.  Subclass may return false to disable
     * the default implementation.
     * @see #onSelectLevelChanged(RowPresenter.ViewHolder)
     */
    public boolean isUsingDefaultListSelectEffect() {
        return true;
    }

    /**
     * Returns true if SDK >= 18, where default shadow
     * is applied to each individual child of {@link HorizontalGridView}.
     * Subclass may return false to disable.
     */
    public boolean isUsingDefaultShadow() {
        return ShadowOverlayHelper.supportsShadow();
    }

    /**
     * Returns true if SDK >= L, where Z shadow is enabled so that Z order is enabled
     * on each child of horizontal list.   If subclass returns false in isUsingDefaultShadow()
     * and does not use Z-shadow on SDK >= L, it should override isUsingZOrder() return false.
     */
    public boolean isUsingZOrder(Context context) {
        return !Settings.getInstance(context).preferStaticShadows();
    }

    /**
     * Enables or disables child shadow.
     * This is not only for enable/disable default shadow implementation but also subclass must
     * respect this flag.
     */
    public final void setShadowEnabled(boolean enabled) {
        mShadowEnabled = enabled;
    }

    /**
     * Returns true if child shadow is enabled.
     * This is not only for enable/disable default shadow implementation but also subclass must
     * respect this flag.
     */
    public final boolean getShadowEnabled() {
        return mShadowEnabled;
    }

    /**
     * Enables or disabled rounded corners on children of this row.
     * Supported on Android SDK >= L.
     */
    public final void enableChildRoundedCorners(boolean enable) {
        mRoundedCornersEnabled = enable;
    }

    /**
     * Returns true if rounded corners are enabled for children of this row.
     */
    public final boolean areChildRoundedCornersEnabled() {
        return mRoundedCornersEnabled;
    }

    final boolean needsDefaultShadow() {
        return isUsingDefaultShadow() && getShadowEnabled();
    }

    /**
     * When ListRowPresenter applies overlay color on the child,  it may change child's foreground
     * Drawable.  If application uses child's foreground for other purposes such as ripple effect,
     * it needs tell ListRowPresenter to keep the child's foreground.  The default value is true.
     *
     * @param keep true if keep foreground of child of this row, false ListRowPresenter might change
     *             the foreground of the child.
     */
    public final void setKeepChildForeground(boolean keep) {
        mKeepChildForeground = keep;
    }

    /**
     * Returns true if keeps foreground of child of this row, false otherwise.  When
     * ListRowPresenter applies overlay color on the child,  it may change child's foreground
     * Drawable.  If application uses child's foreground for other purposes such as ripple effect,
     * it needs tell ListRowPresenter to keep the child's foreground.  The default value is true.
     *
     * @return true if keeps foreground of child of this row, false otherwise.
     */
    public final boolean isKeepChildForeground() {
        return mKeepChildForeground;
    }

    /**
     * Create ShadowOverlayHelper Options.  Subclass may override.
     * e.g.
     * <code>
     * return new ShadowOverlayHelper.Options().roundedCornerRadius(10);
     * </code>
     *
     * @return The options to be used for shadow, overlay and rouded corner.
     */
    protected ShadowOverlayHelper.Options createShadowOverlayOptions() {
        return ShadowOverlayHelper.Options.DEFAULT;
    }

    /**
     * Applies select level to header and draw a default color dim over each child
     * of {@link HorizontalGridView}.
     * <p>
     * Subclass may override this method.  A subclass
     * needs to call super.onSelectLevelChanged() for applying header select level
     * and optionally applying a default select level to each child view of
     * {@link HorizontalGridView} if {@link #isUsingDefaultListSelectEffect()}
     * is true.  Subclass may override {@link #isUsingDefaultListSelectEffect()} to return
     * false and deal with the individual item select level by itself.
     * </p>
     */
    @Override
    protected void onSelectLevelChanged(RowPresenter.ViewHolder holder) {
        super.onSelectLevelChanged(holder);
        /*if (mShadowOverlayHelper != null && mShadowOverlayHelper.needsOverlay()) {
            ViewHolder vh = (ViewHolder) holder;
            int dimmedColor = vh.mColorDimmer.getPaint().getColor();
            for (int i = 0, count = vh.mGridView.getChildCount(); i < count; i++) {
                mShadowOverlayHelper.setOverlayColor(vh.mGridView.getChildAt(i), dimmedColor);
            }
            if (vh.mGridView.getFadingLeftEdge()) {
                vh.mGridView.invalidate();
            }
        }*/
    }

    @Override
    public void freeze(RowPresenter.ViewHolder holder, boolean freeze) {
        ViewHolder vh = (ViewHolder) holder;
        if(vh.mGridView instanceof HorizontalGridView){
            HorizontalGridView view = (HorizontalGridView) vh.mGridView;
            view.setScrollEnabled(!freeze);
        }else if(vh.mGridView instanceof VerticalGridView){
            VerticalGridView view = (VerticalGridView) vh.mGridView;
            view.setScrollEnabled(!freeze);
        }
    }

    @Override
    public void setEntranceTransitionState(RowPresenter.ViewHolder holder,
                                           boolean afterEntrance) {
        super.setEntranceTransitionState(holder, afterEntrance);

        if(((ViewHolder) holder).mGridView instanceof HorizontalGridView){
            HorizontalGridView view = (HorizontalGridView)((ViewHolder)holder).mGridView;
            view.setChildrenVisibility(
                    afterEntrance ? View.VISIBLE : View.INVISIBLE);
        }else if(((ViewHolder) holder).mGridView instanceof VerticalGridView){
            VerticalGridView view = (VerticalGridView)((ViewHolder)holder).mGridView;
            view.setChildrenVisibility(
                    afterEntrance ? View.VISIBLE : View.INVISIBLE);
        }
    }


}
