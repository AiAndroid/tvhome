package com.mothership.tvhome.app;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mothership.tvhome.R;
import com.mothership.tvhome.widget.BlockRowPresenter;
import com.mothership.tvhome.widget.CardPresenter;
import com.mothership.tvhome.widget.CardPresenterSelector;
import com.tv.ui.metro.model.DisplayItem;

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
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final String TAG = "BrowseFragment";

    //private static final String LB_HEADERS_BACKSTACK = "lbHeadersBackStack_";

    private static boolean DEBUG = true;

    /** The headers fragment is enabled and shown by default. */
    public static final int HEADERS_ENABLED = 1;

    /** The headers fragment is enabled and hidden by default. */
    public static final int HEADERS_HIDDEN = 2;

    /** The headers fragment is disabled and will never be shown. */
    public static final int HEADERS_DISABLED = 3;

    private OnItemViewSelectedListener mExternalOnItemViewSelectedListener;
    private OnItemViewClickedListener mOnItemViewClickedListener;

    private PageRowsFragment mRowsFragment;
    private HeadersFragment mHeadersFragment;

    private ArrayObjectAdapter mRowsAdapter;

    private ObjectAdapter mAdapter;


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getChildFragmentManager().findFragmentById(R.id.browse_container_dock) == null) {
            mRowsFragment = new PageRowsFragment();
            //mHeadersFragment = new HeadersFragment();
            getChildFragmentManager().beginTransaction()
                   // .replace(R.id.browse_headers_dock, mHeadersFragment)
                    .replace(R.id.browse_container_dock, mRowsFragment).commit();
        } else {
            //mHeadersFragment = (HeadersFragment) getChildFragmentManager()
            //        .findFragmentById(R.id.browse_headers_dock);
            mRowsFragment = (PageRowsFragment) getChildFragmentManager()
                    .findFragmentById(R.id.browse_container_dock);
        }

        //mHeadersFragment.setHeadersGone(!mCanShowHeaders);

        mRowsFragment.setAdapter(mAdapter);

       // mHeadersFragment.setAdapter(mAdapter);

        mRowsFragment.enableRowScaling(false);
        mRowsFragment.setOnItemViewSelectedListener(mRowViewSelectedListener);
        //mHeadersFragment.setOnHeaderViewSelectedListener(mHeaderViewSelectedListener);
        //mHeadersFragment.setOnHeaderClickedListener(mHeaderClickedListener);
        //mRowsFragment.setOnItemViewClickedListener(mOnItemViewClickedListener);

        View root = inflater.inflate(R.layout.lb_browse_fragment, container, false);

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

    private OnItemViewSelectedListener mRowViewSelectedListener = new OnItemViewSelectedListener() {
        @Override
        public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item,
                                   RowPresenter.ViewHolder rowViewHolder, Row row) {
            int position = mRowsFragment.getVerticalGridView().getSelectedPosition();
            if (DEBUG) Log.v(TAG, "row selected position " + position);
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

        loadRows();

        //setupEventListeners();
    }

    private void loadRows() {
        //List<DisplayItem> list =

        mRowsAdapter = new ArrayObjectAdapter(new BlockRowPresenter());

        List<DisplayItem> list =  new ArrayList<DisplayItem>();
        PresenterSelector selector = new CardPresenterSelector();
        int i;
        for (i = 0; i < 9; i++) {
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
                if(i%2==0) {
                    item.id = "land";
                }
                listRowAdapter.add(item);

                listRowAdapter.setPresenterSelector(selector);
            }

            HeaderItem header = new HeaderItem(i, "HEADER");
            mRowsAdapter.add(new ListRow(header, listRowAdapter));
        }


        setAdapter(mRowsAdapter);

    }

    public void setAdapter(ObjectAdapter adapter) {
        mAdapter = adapter;
        if (mRowsFragment != null) {
            mRowsFragment.setAdapter(adapter);
            //mHeadersFragment.setAdapter(adapter);
        }
    }
}
