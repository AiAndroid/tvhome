package com.mothership.tvhome.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.mothership.tvhome.R;
import com.mothership.tvhome.widget.BlockAdapter;

/**
 * Created by wangwei on 3/1/16.
 */
public class PagesFragment extends BaseFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private static final String TAG = "PagesFragment";
    private static boolean DEBUG = true;

    private OnItemViewSelectedListener mExternalOnItemViewSelectedListener;
    private OnItemViewClickedListener mOnItemViewClickedListener;

    private ObjectAdapter mAdapter;

    PagesAdapter mPageAdapter ;
    ViewPager mPager;

    private boolean mExpand = true;

    public class PagesAdapter extends FragmentPagerAdapter {
        public PagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            if(mAdapter==null) return 0;
            return mAdapter.size();
        }

        @Override
        public Fragment getItem(int position) {
            PageRowsFragment rowsFragment = new PageRowsFragment();
            if((BlockAdapter)mAdapter.get(position) instanceof BlockAdapter){
                BlockAdapter blockAdapter = (BlockAdapter)mAdapter.get(position);
                rowsFragment.setAdapter(blockAdapter);
                /*if(blockAdapter.mHasBlocks){
                    if(blockAdapter.get(position) instanceof Block){
                        BlockAdapter rowblock = new BlockAdapter((Block)blockAdapter.get(position),new BlockVerticalPresenter());
                        rowsFragment.setAdapter(rowblock);
                    }

                }*/
            }else{
                rowsFragment.setAdapter((ObjectAdapter)mAdapter.get(position));
            }

            rowsFragment.enableRowScaling(false);
            rowsFragment.setOnItemViewSelectedListener(mRowViewSelectedListener);
            rowsFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);
            return rowsFragment;
        }
    }


    public PagesFragment() {
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
    public static PagesFragment newInstance(String param1, String param2) {
        PagesFragment fragment = new PagesFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.pagers_fragment, container, false);

        mPager = (ViewPager)root.findViewById(R.id.pages_fragment);
        //mPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        //        ViewGroup.LayoutParams.WRAP_CONTENT));
        mPageAdapter = new PagesAdapter(getChildFragmentManager());
        mPager.setAdapter(mPageAdapter);
        return root;
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
    }


    private OnItemViewSelectedListener mRowViewSelectedListener = new OnItemViewSelectedListener() {
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


    public void setAdapter(ObjectAdapter adapter) {
        mAdapter = adapter;
        if(mPager!=null) {
            mPager.getAdapter().notifyDataSetChanged();
        }
    }
    public void requestFocus(){
        if(mPager!=null) {
            mPager.requestFocus();
        }
    }


    class ExpandPreLayout implements ViewTreeObserver.OnPreDrawListener {

        final View mVerticalView;
        final Runnable mCallback;
        int mState;

        final static int STATE_INIT = 0;
        final static int STATE_FIRST_DRAW = 1;
        final static int STATE_SECOND_DRAW = 2;

        ExpandPreLayout(Runnable callback) {
            mVerticalView = mPager;
            mCallback = callback;
        }

        void execute() {
            mVerticalView.getViewTreeObserver().addOnPreDrawListener(this);
            setExpand(false);
            mState = STATE_INIT;
        }

        @Override
        public boolean onPreDraw() {
            if (getView() == null || getActivity() == null) {
                mVerticalView.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
            if (mState == STATE_INIT) {
                setExpand(true);
                mState = STATE_FIRST_DRAW;
            } else if (mState == STATE_FIRST_DRAW) {
                mCallback.run();
                mVerticalView.getViewTreeObserver().removeOnPreDrawListener(this);
                mState = STATE_SECOND_DRAW;
            }
            return false;
        }
    }

    void onExpandTransitionStart(boolean expand, final Runnable callback) {
        onTransitionPrepare();
        onTransitionStart();
        if (expand) {
            callback.run();
            return;
        }
        // Run a "pre" layout when we go non-expand, in order to get the initial
        // positions of added rows.
        new ExpandPreLayout(callback).execute();
    }

    boolean onTransitionPrepare() {
        boolean prepared = true;
        if (prepared) {
            //freezeRows(true);
        }
        return prepared;
    }

    void onTransitionStart() {

    }

    void onTransitionEnd() {
        //freezeRows(false);
    }

    /**
     * Set the visibility of titles/hovercard of browse rows.
     */
    public void setExpand(boolean expand) {
        mExpand = expand;
        /*VerticalGridView listView = getVerticalGridView();
        if (listView != null) {
            updateRowScaling();
            final int count = listView.getChildCount();
            if (DEBUG) Log.v(TAG, "setExpand " + expand + " count " + count);
            for (int i = 0; i < count; i++) {
                View view = listView.getChildAt(i);
                ItemBridgeAdapter.ViewHolder vh = (ItemBridgeAdapter.ViewHolder) listView.getChildViewHolder(view);
                setRowViewExpanded(vh, mExpand);
            }
        }*/
    }

    public void setPage(int index){
        if(mPager!=null&&index>=0&&index<mPager.getAdapter().getCount()) {
            mPager.setCurrentItem(index,true);
        }
    }

    /**
     * Sets an item clicked listener on the fragment.
     * OnItemViewClickedListener will override {@link View.OnClickListener} that
     * item presenter sets during {@link Presenter#onCreateViewHolder(ViewGroup)}.
     * So in general,  developer should choose one of the listeners but not both.
     */
    public void setOnItemViewClickedListener(OnItemViewClickedListener listener) {
        mOnItemViewClickedListener = listener;
    }

    /**
     * Returns the item clicked listener.
     */
    public OnItemViewClickedListener getOnItemViewClickedListener() {
        return mOnItemViewClickedListener;
    }
}

