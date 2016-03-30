package com.mothership.tvhome.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.JsonObjectRequest;
import com.mothership.tvhome.R;
import com.mothership.tvhome.view.TagViewGroup;
import com.video.search.access.CommonUrl;
import com.video.search.access.Params;
import com.video.search.access.VolleyHttpUtils;
import com.video.search.access.api.ApiGetEntity;
import com.video.search.access.msg.RequestMessage;
import com.video.search.Action;
import com.video.search.Config;
//import com.video.fragment.detail.MediaListFragment;
import com.video.search.model.CineasteSR;
import com.video.search.model.HotSR;
import com.video.search.model.MediaSR;
import com.video.utils.Tools;

/**
 * Search Result Fragment
 * Created by zhuzhenhua on 15-12-22.
 */
public class SearchResultFragment extends BaseVolleyFragment {

    private TextView mTitleTv;
    private TagViewGroup mTagViewGroup;

    private String mKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_result, container, false);
    }

    @Override
    protected void initViews(View root) {
        mTitleTv = (TextView) root.findViewById(R.id.result_title);
        mTitleTv.setText(R.string.search_rank);

        mTagViewGroup = (TagViewGroup) root.findViewById(R.id.tag_container);
    }

    @Override
    protected void initData() {
        if (TextUtils.isEmpty(mKey)) {
            mTagViewGroup.setVisibility(View.GONE);
            getHotSearchList(true);
        } else {
            searchByKey();
        }
    }

    private void getHotSearchList(final boolean noKey) {
        CommonUrl ub = new CommonUrl(Config.URL_GET_HOT_SEARCH);
        RequestMessage msg = new RequestMessage(ub.build());
        JsonObjectRequest request = VolleyHttpUtils.newGetRequest(msg, new ApiGetEntity<HotSR>() {

            @Override
            public void onSuccess(HotSR res) {
                try {
                    if (getActivity() != null && isAdded()) {
                        if (res.data == null || res.data.length == 0) {
                            getRecommendList();
                            return;
                        }

                        String title = "";
                        if (noKey) {
                            mTitleTv.setText(R.string.search_rank);
                        } else {
                            mTitleTv.setText(String.format(getString(R.string.search_empty_result), mKey));
                            title = getString(R.string.search_recommend);
                        }

                        FragmentManager manager = getChildFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.container_search_res_media, MediaListFragment.newInstance(title, Tools.toArray(res.data)));
                        transaction.commitAllowingStateLoss();
                        manager.executePendingTransactions();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        request.setTag(getReqTag());
        mRequestQueue.add(request);
    }

    private void getRecommendList() {
        CommonUrl ub = new CommonUrl(Config.URL_SEARCH_RECOMMENDATIONS);
        RequestMessage msg = new RequestMessage(ub.build());
        JsonObjectRequest request = VolleyHttpUtils.newGetRequest(msg, new ApiGetEntity<HotSR>() {

            @Override
            public void onSuccess(HotSR res) {
                if (getActivity() == null || isDetached()) {
                    return;
                }

                if (res.data == null || res.data.length == 0)
                    return; // no data

                mTitleTv.setText(String.format(getString(R.string.search_empty_result), mKey));
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container_search_res_media, MediaListFragment.newInstance(Tools.toArray(res.data)));
                transaction.commitAllowingStateLoss();
                manager.executePendingTransactions();
            }
        });
        request.setTag(getReqTag());
        mRequestQueue.add(request);
    }

    private void searchByKey() {
        searchMediaByKey();
        searchCineasteByKey();
    }

    private void searchCineasteByKey() {
        CommonUrl ub = new CommonUrl(Config.URL_SEARCH_CINEASTE);
        ub.putParam(Params.CINEASTE_NAME, mKey);
        ub.putParam(Params.SEARCH_TYPE, "10");
        RequestMessage msg = new RequestMessage(ub.build());
        JsonObjectRequest request = VolleyHttpUtils.newGetRequest(msg, new ApiGetEntity<CineasteSR>() {

            @Override
            public void onSuccess(final CineasteSR res) {
                if (res.data == null || res.data.cineastes == null) return;// no data
                mTagViewGroup.setVisibility(View.VISIBLE);
                mTagViewGroup.setOnTagClickListener(new TagViewGroup.OnTagClickListener() {
                    @Override
                    public void onTagClick(int pos) {
                        if (pos == TagViewGroup.TYPE_TAG_MORE) {
                            Intent intent = new Intent(Action.ACTION_SEARCH_CINEASTE);
                            intent.putExtra(Params.SEARCH_KEY, mKey);
                            getActivity().startActivity(intent);
                        } else {
                            Intent intent = new Intent(Action.ACTION_WORKS);
                            intent.putExtra(Params.ID, res.data.cineastes.get(pos).cineasteid);
//                            intent.putExtra(Params.INVOKER_INT, StatisticsUtil.EInvokeBy.EInvokeBySearch.ordinal());
                            getActivity().startActivity(intent);
                        }
                    }
                });
                mTagViewGroup.setTags(res.data.getTags());
            }
        });
        request.setTag(getReqTag());
        mRequestQueue.add(request);
    }

    private void searchMediaByKey() {
        CommonUrl ub = new CommonUrl(Config.URL_SEARCH_MEDIA);
        ub.putParam(Params.MEDIA_NAME, mKey);
        ub.putParam(Params.SEARCH_TYPE, "10");
        RequestMessage msg = new RequestMessage(ub.build());
        JsonObjectRequest request = VolleyHttpUtils.newGetRequest(msg, new ApiGetEntity<MediaSR>() {

            @Override
            public void onSuccess(MediaSR res) {
                if (getActivity() == null || isDetached()) {
                    return;
                }

                if (res.data == null || res.data.size() == 0) {
                    getHotSearchList(false);
                    return;// no data
                }

                mTitleTv.setText(R.string.search_result);
                FragmentManager manager = getChildFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container_search_res_media, MediaListFragment.newInstance(res.data));
                transaction.commitAllowingStateLoss();
                manager.executePendingTransactions();
            }
        });
        request.setTag(getReqTag());
        mRequestQueue.add(request);
    }

    public void search(String text) {
        mKey = text;
        initData();
    }
}
