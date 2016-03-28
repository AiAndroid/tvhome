package com.mothership.tvhome.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.transition.TransitionListener;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.BrowseFrameLayout;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mothership.tvhome.R;
import com.mothership.tvhome.widget.BlockAdapter;
import com.mothership.tvhome.widget.BlockPresenterSelector;
import com.mothership.tvhome.widget.BlockVerticalPresenter;
import com.mothership.tvhome.widget.CardPresenter;
import com.mothership.tvhome.widget.CardPresenterSelector;
import com.mothership.tvhome.widget.RowPresenter;
import com.mothership.tvhome.widget.TvViewGroupPresenter;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final String TAG = "MainFragment";

    private static final String LB_HEADERS_BACKSTACK = "lbHeadersBackStack_";

    private static boolean DEBUG = true;

    /** The headers fragment is enabled and shown by default. */
    public static final int HEADERS_ENABLED = 1;

    /** The headers fragment is enabled and hidden by default. */
    public static final int HEADERS_HIDDEN = 2;

    /** The headers fragment is disabled and will never be shown. */
    public static final int HEADERS_DISABLED = 3;


    private HeadersFragment mHeadersFragment;
    private PagesFragment mPagesFragment;
    private ObjectAdapter mAdapter;

    private int mHeadersState = HEADERS_ENABLED;
    private int mBrandColor = Color.TRANSPARENT;
    private boolean mBrandColorSet;

    private BrowseFrameLayout mBrowseFrame;
    private boolean mHeadersBackStackEnabled = true;
    private String mWithHeadersBackStackName;
    private boolean mShowingHeaders = true;
    private boolean mCanShowHeaders = true;
    private int mContainerListMarginStart;
    private int mContainerListMarginStartExpand;
    private int mContainerListMarginHeader;
    private int mContainerListAlignTop;
    private boolean mRowScaleEnabled = true;
    private RowPresenter.OnItemViewSelectedListener mExternalOnItemViewSelectedListener;
    private RowPresenter.OnItemViewClickedListener mOnItemViewClickedListener;
    private int mSelectedPosition = -1;

    private PresenterSelector mHeaderPresenterSelector;
    //private final SetSelectionRunnable mSetSelectionRunnable = new SetSelectionRunnable();

    // transition related:
    private Object mSceneWithHeaders;
    private Object mSceneWithoutHeaders;
    private Object mSceneAfterEntranceTransition;
    private Object mHeadersTransition;
    private BackStackListener mBackStackChangedListener;
    private BrowseTransitionListener mBrowseTransitionListener;

    //private static final String ARG_TITLE = BrowseFragment.class.getCanonicalName() + ".title";
    //private static final String ARG_BADGE_URI = BrowseFragment.class.getCanonicalName() + ".badge";
    //private static final String ARG_HEADERS_STATE =
    //        BrowseFragment.class.getCanonicalName() + ".headersState";



    // BUNDLE attribute for saving header show/hide status when backstack is used:
    static final String HEADER_STACK_INDEX = "headerStackIndex";
    // BUNDLE attribute for saving header show/hide status when backstack is not used:
    static final String HEADER_SHOW = "headerShow";

    final class BackStackListener implements FragmentManager.OnBackStackChangedListener {
        int mLastEntryCount;
        int mIndexOfHeadersBackStack;

        BackStackListener() {
            mLastEntryCount = getFragmentManager().getBackStackEntryCount();
            mIndexOfHeadersBackStack = -1;
        }

        void load(Bundle savedInstanceState) {
            if (savedInstanceState != null) {
                mIndexOfHeadersBackStack = savedInstanceState.getInt(HEADER_STACK_INDEX, -1);
                mShowingHeaders = mIndexOfHeadersBackStack == -1;
            } else {
                if (!mShowingHeaders) {
                    getFragmentManager().beginTransaction()
                            .addToBackStack(mWithHeadersBackStackName).commit();
                }
            }
        }

        void save(Bundle outState) {
            outState.putInt(HEADER_STACK_INDEX, mIndexOfHeadersBackStack);
        }


        @Override
        public void onBackStackChanged() {
            if (getFragmentManager() == null) {
                Log.w(TAG, "getFragmentManager() is null, stack:", new Exception());
                return;
            }
            int count = getFragmentManager().getBackStackEntryCount();
            // if backstack is growing and last pushed entry is "headers" backstack,
            // remember the index of the entry.
            if (count > mLastEntryCount) {
                FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(count - 1);
                if (mWithHeadersBackStackName.equals(entry.getName())) {
                    mIndexOfHeadersBackStack = count - 1;
                }
            } else if (count < mLastEntryCount) {
                // if popped "headers" backstack, initiate the show header transition if needed
                if (mIndexOfHeadersBackStack >= count) {
                    mIndexOfHeadersBackStack = -1;
                    if (!mShowingHeaders) {
                        startHeadersTransitionInternal(true);
                    }
                }
            }
            mLastEntryCount = count;
        }
    }

    private HeadersFragment.OnHeaderViewSelectedListener mHeaderViewSelectedListener =
            new HeadersFragment.OnHeaderViewSelectedListener() {
                @Override
                public void onHeaderSelected(Presenter.ViewHolder viewHolder, int position) {
                    if (DEBUG) Log.v(TAG, "header selected position " + position);
                    pageSelect(position);
                }
            };

    public MainFragment() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        /*
        TypedArray ta = getActivity().obtainStyledAttributes(android.support.v17.leanback.R.styleable.LeanbackTheme);
        mContainerListMarginStart = (int) ta.getDimension(
                android.support.v17.leanback.R.styleable.LeanbackTheme_browseRowsMarginStart, getActivity().getResources()
                        .getDimensionPixelSize(android.support.v17.leanback.R.dimen.lb_browse_rows_margin_start));
        mContainerListAlignTop = (int) ta.getDimension(
                android.support.v17.leanback.R.styleable.LeanbackTheme_browseRowsMarginTop, getActivity().getResources()
                        .getDimensionPixelSize(android.support.v17.leanback.R.dimen.lb_browse_rows_margin_top));
        ta.recycle();*/

        mContainerListMarginStart = (int)getActivity().getResources().getDimension(R.dimen.browse_margin_top);
        mContainerListMarginStartExpand = (int)getActivity().getResources().getDimension(R.dimen.browse_margin_top_expand);
        mContainerListMarginHeader = (int)getActivity().getResources().getDimension(R.dimen.browse_headers_anim_top);
        //readArguments(getArguments());

        if (mCanShowHeaders) {
            if (mHeadersBackStackEnabled) {
                mWithHeadersBackStackName = LB_HEADERS_BACKSTACK + this;
                mBackStackChangedListener = new BackStackListener();
                getFragmentManager().addOnBackStackChangedListener(mBackStackChangedListener);
                mBackStackChangedListener.load(savedInstanceState);
            } else {
                if (savedInstanceState != null) {
                    mShowingHeaders = savedInstanceState.getBoolean(HEADER_SHOW);
                }
            }
        }
        if (savedInstanceState == null) {
            prepareEntranceTransition();
        }
        startEntranceTransition();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getChildFragmentManager().findFragmentById(R.id.browse_container_dock) == null) {
            mHeadersFragment = new HeadersFragment();
            mPagesFragment = new PagesFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.browse_headers_dock, mHeadersFragment)
                    .replace(R.id.browse_container_dock, mPagesFragment).commit();
        } else {
            mHeadersFragment = (HeadersFragment) getChildFragmentManager()
                    .findFragmentById(R.id.browse_headers_dock);
            mPagesFragment = (PagesFragment) getChildFragmentManager()
                    .findFragmentById(R.id.browse_container_dock);
        }

        //mHeadersFragment.setHeadersGone(!mCanShowHeaders);


        mHeadersFragment.setAdapter(mAdapter);
        mPagesFragment.setAdapter(mAdapter);

        //mRowsFragment.enableRowScaling(false);
        //mRowsFragment.setOnItemViewSelectedListener(mRowViewSelectedListener);
        mHeadersFragment.setOnHeaderViewSelectedListener(mHeaderViewSelectedListener);
        //mHeadersFragment.setOnHeaderClickedListener(mHeaderClickedListener);
        mPagesFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);

        View root = inflater.inflate(R.layout.browse_fragment, container, false);


        //setTitleView((TitleView) root.findViewById(android.support.v17.leanback.R.id.browse_title_group));

        mBrowseFrame = (BrowseFrameLayout) root.findViewById(android.support.v17.leanback.R.id.browse_frame);
        mBrowseFrame.setOnChildFocusListener(mOnChildFocusListener);
        mBrowseFrame.setOnFocusSearchListener(mOnFocusSearchListener);

        //if (mBrandColorSet) {
            mHeadersFragment.setBackgroundColor(getResources().getColor(R.color.lb_default_brand_color));
        //}

        mSceneWithHeaders = sTransitionHelper.createScene(mBrowseFrame, new Runnable() {
            @Override
            public void run() {
                showHeaders(true);
            }
        });
        mSceneWithoutHeaders =  sTransitionHelper.createScene(mBrowseFrame, new Runnable() {
            @Override
            public void run() {
                showHeaders(false);
            }
        });
        mSceneAfterEntranceTransition = sTransitionHelper.createScene(mBrowseFrame, new Runnable() {
            @Override
            public void run() {
                setEntranceTransitionEndState();
            }
        });
        return root;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onResume() {
        super.onResume();
        startHeadersTransitionInternal(mShowingHeaders);
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    /*
    private HeadersFragment.OnHeaderClickedListener mHeaderClickedListener =
            new HeadersFragment.OnHeaderClickedListener() {
                @Override
                public void onHeaderClicked() {
                    if (!mCanShowHeaders || !mShowingHeaders || isInHeadersTransition()) {
                        return;
                    }
                    startHeadersTransitionInternal(false);
                    mRowsFragment.getVerticalGridView().requestFocus();
                }
            };
*/
    /*
    private HeadersFragment.OnHeaderViewSelectedListener mHeaderViewSelectedListener =
            new HeadersFragment.OnHeaderViewSelectedListener() {
                @Override
                public void onHeaderSelected(RowHeaderPresenter.ViewHolder viewHolder, Row row) {
                    int position = mHeadersFragment.getVerticalGridView().getSelectedPosition();
                    if (DEBUG) Log.v(TAG, "header selected position " + position);
                    onRowSelected(position);
                }
            };*/

    private RowPresenter.OnItemViewSelectedListener mRowViewSelectedListener = new RowPresenter.OnItemViewSelectedListener() {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            //int position = mRowsFragment.getVerticalGridView().getSelectedPosition();
            //if (DEBUG) Log.v(TAG, "row selected position " + position);
            //onRowSelected(position);
            //if (mExternalOnItemViewSelectedListener != null) {
            //    mExternalOnItemViewSelectedListener.onItemSelected(itemViewHolder, item,
            //            rowViewHolder, row);
            //}
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onActivityCreated(savedInstanceState);

        setupEventListeners();
        //loadRows();
    }

    private void loadRows() {
        //List<DisplayItem> list =

        ArrayObjectAdapter pageAdapter = new ArrayObjectAdapter();
        List<DisplayItem> list =  new ArrayList<DisplayItem>();
        PresenterSelector selector = new CardPresenterSelector();

        for(int k = 0; k < 5; k++) {
            ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new BlockVerticalPresenter());
            //ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new BlockHorizontalPresenter());
            for (int i = 0; i < 9; i++) {
                //if (i != 0) {
                //    Collections.shuffle(list);
                //}
                // For good performance, it's important to use a single instance of
                // a card presenter for all rows using that presenter.
                final CardPresenter cardPresenter = new CardPresenter();
                ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(cardPresenter);

                for (int j = 0; j < 15; j++) {
                    //listRowAdapter.add(list.get(j));
                    DisplayItem item = new DisplayItem();
                    if (i % 2 == 0) {
                        item.id = "land";
                    }
                    listRowAdapter.add(item);

                    listRowAdapter.setPresenterSelector(selector);
                }

                HeaderItem header = new HeaderItem(i, "HEADER");
                rowsAdapter.add(new ListRow(header, listRowAdapter));
            }
            pageAdapter.add(rowsAdapter);

        }


        setAdapter(pageAdapter);

    }

    public void setAdapter(ObjectAdapter adapter) {
        mAdapter = adapter;
        mAdapter.registerObserver(new ObjectAdapter.DataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(mPagesFragment != null) {
                    mPagesFragment.mPageAdapter.notifyDataSetChanged();
                }
            }
        });
        if (mHeadersFragment != null) {
            mHeadersFragment.setAdapter(mAdapter);
            mPagesFragment.setAdapter(mAdapter);
        }
    }

    /**
     * Starts a headers transition.
     *
     * <p>This method will begin a transition to either show or hide the
     * headers, depending on the value of withHeaders. If headers are disabled
     * for this browse fragment, this method will throw an exception.
     *
     * @param withHeaders True if the headers should transition to being shown,
     *        false if the transition should result in headers being hidden.
     */
    public void startHeadersTransition(boolean withHeaders) {
        if (!mCanShowHeaders) {
            throw new IllegalStateException("Cannot start headers transition");
        }
        if (isInHeadersTransition() || mShowingHeaders == withHeaders) {
            return;
        }
        startHeadersTransitionInternal(withHeaders);
    }

    /**
     * Returns true if the headers transition is currently running.
     */
    public boolean isInHeadersTransition() {
        return mHeadersTransition != null;
    }

    /**
     * Returns true if headers are shown.
     */
    public boolean isShowingHeaders() {
        return mShowingHeaders;
    }

    /**
     * Listener for transitions between browse headers and rows.
     */
    public static class BrowseTransitionListener {
        /**
         * Callback when headers transition starts.
         *
         * @param withHeaders True if the transition will result in headers
         *        being shown, false otherwise.
         */
        public void onHeadersTransitionStart(boolean withHeaders) {
        }
        /**
         * Callback when headers transition stops.
         *
         * @param withHeaders True if the transition will result in headers
         *        being shown, false otherwise.
         */
        public void onHeadersTransitionStop(boolean withHeaders) {
        }
    }
    /**
     * Sets a listener for browse fragment transitions.
     *
     * @param listener The listener to call when a browse headers transition
     *        begins or ends.
     */
    public void setBrowseTransitionListener(BrowseTransitionListener listener) {
        mBrowseTransitionListener = listener;
    }


    private void startHeadersTransitionInternal(final boolean withHeaders) {
        //if(true) return;
        if (getFragmentManager().isDestroyed()) {
            return;
        }
        mShowingHeaders = withHeaders;

        mHeadersFragment.onTransitionPrepare();
        mHeadersFragment.onTransitionStart();
        createHeadersTransition();
        if (mBrowseTransitionListener != null) {
            mBrowseTransitionListener.onHeadersTransitionStart(withHeaders);
        }
        sTransitionHelper.runTransition(withHeaders ? mSceneWithHeaders : mSceneWithoutHeaders,
                mHeadersTransition);
        if (mHeadersBackStackEnabled) {
            if (!withHeaders) {
                getFragmentManager().beginTransaction()
                        .addToBackStack(mWithHeadersBackStackName).commit();
            } else {
                int index = mBackStackChangedListener.mIndexOfHeadersBackStack;
                if (index >= 0) {
                    FragmentManager.BackStackEntry entry = getFragmentManager().getBackStackEntryAt(index);
                    getFragmentManager().popBackStackImmediate(entry.getId(),
                            android.app.FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        }

    }

    private void createHeadersTransition() {
        mHeadersTransition = sTransitionHelper.loadTransition(getActivity(),
                mShowingHeaders ?
                        R.transition.lb_browse_headers_in : R.transition.lb_browse_headers_out);

        sTransitionHelper.setTransitionListener(mHeadersTransition, new TransitionListener() {
            @Override
            public void onTransitionStart(Object transition) {
            }

            @Override
            public void onTransitionEnd(Object transition) {
                mHeadersTransition = null;
                mPagesFragment.onTransitionEnd();
                mHeadersFragment.onTransitionEnd();
                if (mShowingHeaders) {
                    mHeadersFragment.requestFocus();
                   /* VerticalGridView headerGridView = mHeadersFragment.getVerticalGridView();
                    if (headerGridView != null && !headerGridView.hasFocus()) {
                        headerGridView.requestFocus();
                    }*/
                } else {
                    mPagesFragment.requestFocus();
                    /*VerticalGridView rowsGridView = mRowsFragment.getVerticalGridView();
                    if (rowsGridView != null && !rowsGridView.hasFocus()) {
                        rowsGridView.requestFocus();
                    }*/
                }
                if (mBrowseTransitionListener != null) {
                    mBrowseTransitionListener.onHeadersTransitionStop(mShowingHeaders);
                }
            }
        });
    }

    private final BrowseFrameLayout.OnFocusSearchListener mOnFocusSearchListener =
            new BrowseFrameLayout.OnFocusSearchListener() {
                @Override
                public View onFocusSearch(View focused, int direction) {
                    // if headers is running transition,  focus stays
                    if (mCanShowHeaders && isInHeadersTransition()) {
                        return focused;
                    }
                    if (DEBUG) Log.v(TAG, "onFocusSearch focused " + focused + " + direction " + direction);
                    if(mCanShowHeaders && direction == View.FOCUS_UP){
                        return mHeadersFragment.getView();
                    }else if(direction == View.FOCUS_DOWN && mShowingHeaders){
                        return mPagesFragment.getView();
                    }else{
                        return null;
                    }


                }
            };

    private final BrowseFrameLayout.OnChildFocusListener mOnChildFocusListener =
            new BrowseFrameLayout.OnChildFocusListener() {

                @Override
                public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
                    if (getChildFragmentManager().isDestroyed()) {
                        return true;
                    }
                    // Make sure not changing focus when requestFocus() is called.
                    if (mCanShowHeaders && mShowingHeaders) {
                        if (mHeadersFragment != null && mHeadersFragment.getView() != null &&
                                mHeadersFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                            return true;
                        }
                    }
                    if (mPagesFragment != null && mPagesFragment.getView() != null &&
                            mPagesFragment.getView().requestFocus(direction, previouslyFocusedRect)) {
                        return true;
                    }
                    return false;
                };

                @Override
                public void onRequestChildFocus(View child, View focused) {
                    if (getChildFragmentManager().isDestroyed()) {
                        return;
                    }
                    if (!mCanShowHeaders || isInHeadersTransition()) return;
                    int childId = child.getId();
                    if (childId == R.id.browse_container_dock && mShowingHeaders) {
                        startHeadersTransitionInternal(false);
                    } else if (childId == R.id.browse_headers_dock && !mShowingHeaders) {
                        startHeadersTransitionInternal(true);
                    }
                }
            };

    private void setHeadersOnScreen(boolean onScreen) {
        ViewGroup.MarginLayoutParams lp;
        View containerList;
        containerList = mHeadersFragment.getView();
        lp = (ViewGroup.MarginLayoutParams) containerList.getLayoutParams();
        lp.setMargins(0,onScreen ? 0 : -mContainerListMarginHeader,0,0);
        containerList.setLayoutParams(lp);
    }

    private void setRowsAlignedTop(boolean alignTop) {
        ViewGroup.MarginLayoutParams lp;
        View containerList;
        containerList = mPagesFragment.getView();
        lp = (ViewGroup.MarginLayoutParams) containerList.getLayoutParams();
        lp.setMargins(0, alignTop ? mContainerListMarginStartExpand: mContainerListMarginStart, 0, 0);
        containerList.setLayoutParams(lp);
    }

    private void showHeaders(boolean show) {
        if (DEBUG) Log.v(TAG, "showHeaders " + show);
        mHeadersFragment.setHeadersEnabled(show);
        setHeadersOnScreen(show);
        setRowsAlignedTop(!show);
        //mRowsFragment.setExpand(!show);
    }


    /**
     * Sets the state for the headers column in the browse fragment. Must be one
     * of {@link #HEADERS_ENABLED}, {@link #HEADERS_HIDDEN}, or
     * {@link #HEADERS_DISABLED}.
     *
     * @param headersState The state of the headers for the browse fragment.
     */
    public void setHeadersState(int headersState) {
        if (headersState < HEADERS_ENABLED || headersState > HEADERS_DISABLED) {
            throw new IllegalArgumentException("Invalid headers state: " + headersState);
        }
        if (DEBUG) Log.v(TAG, "setHeadersState " + headersState);

        if (headersState != mHeadersState) {
            mHeadersState = headersState;
            switch (headersState) {
                case HEADERS_ENABLED:
                    mCanShowHeaders = true;
                    mShowingHeaders = true;
                    break;
                case HEADERS_HIDDEN:
                    mCanShowHeaders = true;
                    mShowingHeaders = false;
                    break;
                case HEADERS_DISABLED:
                    mCanShowHeaders = false;
                    mShowingHeaders = false;
                    break;
                default:
                    Log.w(TAG, "Unknown headers state: " + headersState);
                    break;
            }
            if (mHeadersFragment != null) {
                mHeadersFragment.setHeadersGone(!mCanShowHeaders);
            }
        }
    }

    /**
     * Returns the state of the headers column in the browse fragment.
     */
    public int getHeadersState() {
        return mHeadersState;
    }

    @Override
    protected Object createEntranceTransition() {
        return sTransitionHelper.loadTransition(getActivity(),
                android.support.v17.leanback.R.transition.lb_browse_entrance_transition);
    }

    @Override
    protected void runEntranceTransition(Object entranceTransition) {
        sTransitionHelper.runTransition(mSceneAfterEntranceTransition,
                entranceTransition);
    }

    @Override
    protected void onEntranceTransitionPrepare() {
        mHeadersFragment.onTransitionPrepare();
        mPagesFragment.onTransitionPrepare();
    }

    @Override
    protected void onEntranceTransitionStart() {
        mHeadersFragment.onTransitionStart();
        mPagesFragment.onTransitionStart();
    }

    @Override
    protected void onEntranceTransitionEnd() {
        mPagesFragment.onTransitionEnd();
        mHeadersFragment.onTransitionEnd();
    }

    /*void setSearchOrbViewOnScreen(boolean onScreen) {
        View searchOrbView = getTitleView().getSearchAffordanceView();
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) searchOrbView.getLayoutParams();
        lp.setMarginStart(onScreen ? 0 : -mContainerListMarginStart);
        searchOrbView.setLayoutParams(lp);
    }*/

    void setEntranceTransitionStartState() {
        setHeadersOnScreen(false);
        //setSearchOrbViewOnScreen(false);
        //mPagesFragment.setEntranceTransitionState(false);
    }

    void setEntranceTransitionEndState() {
        //setHeadersOnScreen(mShowingHeaders);
        setHeadersOnScreen(true);
        setRowsAlignedTop(false);
        //setSearchOrbViewOnScreen(true);
        //mPagesFragment.setEntranceTransitionState(true);
    }

    void pageSelect(int position){
        mPagesFragment.setPage(position);
    }



    private void setupEventListeners() {
        setOnItemViewClickedListener(new ItemViewClickedListener());
        setOnItemViewSelectedListener(new ItemViewSelectedListener());
    }


    /**
     * Sets an item selection listener.
     */
    public void setOnItemViewSelectedListener(RowPresenter.OnItemViewSelectedListener listener) {
        mExternalOnItemViewSelectedListener = listener;
    }

    /**
     * Returns an item selection listener.
     */
    public RowPresenter.OnItemViewSelectedListener getOnItemViewSelectedListener() {
        return mExternalOnItemViewSelectedListener;
    }

    /**
     * Sets an item clicked listener on the fragment.
     * OnItemViewClickedListener will override {@link View.OnClickListener} that
     * item presenter sets during {@link Presenter#onCreateViewHolder(ViewGroup)}.
     * So in general,  developer should choose one of the listeners but not both.
     */
    public void setOnItemViewClickedListener(RowPresenter.OnItemViewClickedListener listener) {
        mOnItemViewClickedListener = listener;
        if (mPagesFragment != null) {
            mPagesFragment.setOnItemViewClickedListener(listener);
        }
    }

    /**
     * Returns the item Clicked listener.
     */
    public RowPresenter.OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }

    private final class ItemViewSelectedListener implements RowPresenter.OnItemViewSelectedListener {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            /*if (item instanceof Movie) {
                mBackgroundURI = ((Movie) item).getBackgroundImageURI();
                startBackgroundTimer();
            }*/

        }
    }
    private final class ItemViewClickedListener implements RowPresenter.OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {

            DisplayItem di = (DisplayItem) item;

            try{
                if(di != null && di.target != null){
                    //jump to album
                    if("album".equals(di.target.entity)){
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("mitv://video/album"+"?rid=" + di.id));
                            intent.putExtra("item", di);
                            getContext().startActivity(intent);
                            return;
                        } catch (Exception ne) {
                            ne.printStackTrace();
                        }
                    }

                    //intent
                    if(di.target.params != null && !TextUtils.isEmpty(di.target.params.android_intent())) {
                        try {
                            Intent intent = Intent.parseUri(di.target.params.android_intent(), 0);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                            getContext().startActivity(intent);
                            return;
                        }catch (Exception ne){}
                    }
                }


                //for launch 3-rd application
                if("intent".equals(di.target.entity)){
                    if(!TextUtils.isEmpty(di.target.url)) {
                        String data = di.target.url;

                        Intent intent = new Intent();
                        intent.setData(Uri.parse(data));
                        try {
                            getContext().startActivity(intent);
                            return;
                        }catch (Exception ne){}
                    }
                }

                if(!TextUtils.isEmpty(di.target.action)) {
                    try {
                        Intent intent = Intent.parseUri(di.target.action, 0);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                        getContext().startActivity(intent);
                        return;
                    }catch (Exception ne){}
                }

                return;
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
//            Toast.makeText(getActivity(), "click item", Toast.LENGTH_SHORT)
//                    .show();


        }
    }

    public void LoadData(GenericBlock<DisplayItem> data){
        if(data==null) return;
        final boolean useTestP = false;
        //loadRows();
        TvViewGroupPresenter testP = null;
        if(useTestP)
        {
            testP = new TvViewGroupPresenter();
        }
        final BlockPresenterSelector blockPresenterSelector = new BlockPresenterSelector();
        ArrayObjectAdapter pageAdapter = new ArrayObjectAdapter();
        if(data.blocks!=null) {
            for (int i = 0; i < data.blocks.size(); i++) {
                Block<DisplayItem> block = (Block<DisplayItem>)data.blocks.get(i);
                //if(block.ui_type.id() == 100){
                BlockAdapter blockAdapter = null;
                if(useTestP)
                {
                    blockAdapter = new BlockAdapter(data.blocks.get(i), testP);
                }
                else
                {
                    blockAdapter = new BlockAdapter(data.blocks.get(i),blockPresenterSelector);
                }


                pageAdapter.add(blockAdapter);
                //}/*else if(block.ui_type.id() == 100){

                //}*/
            }
            setAdapter(pageAdapter);
            mHeadersFragment.setSelectedPosition(1);
        }
    }

}



