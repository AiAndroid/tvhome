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

import com.mothership.tvhome.R;

/**
 * Created by wangwei on 3/1/16.
 */
public class PagesFragment extends Fragment {

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

    //private PageRowsFragment mRowsFragment;

    //private ArrayObjectAdapter mRowsAdapter;

    private ObjectAdapter mAdapter;

    PagesAdapter mPageAdapter ;

    ViewPager mPager;

    public class PagesAdapter extends FragmentPagerAdapter {
        public PagesAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            PageRowsFragment rowsFragment = new PageRowsFragment();
            rowsFragment.setAdapter(mAdapter);
            rowsFragment.enableRowScaling(false);
            rowsFragment.setOnItemViewSelectedListener(mRowViewSelectedListener);
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
        // Inflate the layout for this fragment
/*        if (getChildFragmentManager().findFragmentById(R.id.browse_container_dock) == null) {
            mRowsFragment = new PageRowsFragment();
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.browse_container_dock, mRowsFragment).commit();
        } else {
            mRowsFragment = (PageRowsFragment) getChildFragmentManager()
                    .findFragmentById(R.id.browse_container_dock);
        }*/

        //mRowsFragment.setAdapter(mAdapter);

        //mRowsFragment.enableRowScaling(false);
        //mRowsFragment.setOnItemViewSelectedListener(mRowViewSelectedListener);

        //mRowsFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);

        //View root = inflater.inflate(R.layout.lb_browse_fragment, container, false);

        //setTitleView((TitleView) root.findViewById(android.support.v17.leanback.R.id.browse_title_group));

        //mBrowseFrame = (BrowseFrameLayout) root.findViewById(android.support.v17.leanback.R.id.browse_frame);
        // mBrowseFrame.setOnChildFocusListener(mOnChildFocusListener);
        //mBrowseFrame.setOnFocusSearchListener(mOnFocusSearchListener);

        //if (mBrandColorSet) {
        //    mHeadersFragment.setBackgroundColor(mBrandColor);
        //}
/*
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
        });*/
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
    }
}

