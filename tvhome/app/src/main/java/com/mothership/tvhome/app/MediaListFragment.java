package com.mothership.tvhome.app;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.mothership.tvhome.R;
import com.video.search.access.access.Params;
import com.video.search.access.access.VolleyHelper;
import com.video.model.MediaBase;
import com.video.search.access.recyclerview.adapter.AlphaAnimatorAdapter;
import com.video.utils.ViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * media list
 *
 * Created by zhuzhenhua on 15-12-15.
 */
public class MediaListFragment extends BaseListFragment {

    protected VolleyHelper mVolleyHelper;
    protected RequestQueue mRequestQueue;

    private String mActivityName;

    protected List<MediaBase> mData;

    public static Fragment newInstance(List<MediaBase> medias) {
        return newInstance("", medias);
    }

    public static Fragment newInstance(String title, List<MediaBase> medias) {
        MediaListFragment f = new MediaListFragment();
        Bundle args = new Bundle();
        args.putString(Params.TITLE, title);
        args.putParcelableArrayList(Params.DATA, (ArrayList<? extends Parcelable>) medias);
        f.setArguments(args);
        return f;
    }

    @Override
    protected void initViews(View root) {
        mVolleyHelper = VolleyHelper.getInstance(getContext());
        mActivityName = getActivity().getClass().getSimpleName();
        super.initViews(root);
    }

    @Override
    protected void initData() {
        mData = getArguments().getParcelableArrayList(Params.DATA);
        mListView.setAdapter(new AlphaAnimatorAdapter(new ListAdapter(mData), mListView));
        mTitleTv.getLayoutParams();
        ViewHelper.setText(mTitleTv, getArguments().getString(Params.TITLE));
    }

    public class ListAdapter extends RecyclerView.Adapter<VH> {

        private List<MediaBase> data;

        private View.OnClickListener clickListener;

        public ListAdapter(List<MediaBase> data) {
            this.data = data;
            clickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VH holder = (VH) v.getTag();
//                    DetailsActivity.launch(getContext(), getItem(holder.position), mActivityName, StatisticsUtil.EInvokeBy.EInvokeBySearch.ordinal(), null);
                }
            };
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_media, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            holder.position = position;
            holder.itemView.setTag(holder);
            holder.itemView.setOnClickListener(clickListener);

            MediaBase m = getItem(position);
            if (m.isNormalItem()) {
                holder.title.setText(m.medianame);

                StringBuffer sb = new StringBuffer(m.category);
                if (m.year > 0) {
                    sb.append("（");
                    sb.append(m.year);
                    sb.append("）");
                } else {
                    sb.append("（");
                    sb.append(2015);
                    sb.append("）");
                }
                holder.subText.setText(sb.toString());
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public MediaBase getItem(int pos) {
            return data.get(pos);
        }
    }

    public class VH extends RecyclerView.ViewHolder {
        public int position;
        public TextView title;
        public TextView subText;
        public View arrow;

        public VH(View v) {
            super(v);
            title = (TextView) v.findViewById(R.id.media_item_text);
            subText = (TextView) v.findViewById(R.id.media_item_sub_text);
            arrow = v.findViewById(R.id.media_item_text);
        }
    }

}
