package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mothership.tvhome.R;
import com.mothership.tvhome.Utils;
import com.tv.ui.metro.model.DisplayItem;

import static android.support.v17.leanback.widget.FocusHighlight.ZOOM_FACTOR_SMALL;

/**
 * Created by Shawn on 16/3/8.
 */
public class BasePresenter extends Presenter
{
    private static final String TAG = "BasePresenter";
    protected int mBaseWidth = 0;
    protected int mBaseHeight = 0;
    private FocusHelper.FocusHighlightHandler mFocusHighlight = new FocusHelper.BrowseItemFocusHighlight(ZOOM_FACTOR_SMALL,false);

    public class VH extends ViewHolder
    {
        public ImageView mImg;
        public TextView mTitle;
        public TextView mSubTitle;
        public ItemTextView mText;
        public String imageUrl;
        OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener();
        public VH(View aView)
        {
            super(aView);
            mImg = (ImageView) aView.findViewById(R.id.di_img);
            mTitle = (TextView) aView.findViewById(R.id.di_title);
            mSubTitle = (TextView) aView.findViewById(R.id.di_subtitle);
            mText = (ItemTextView)aView.findViewById(R.id.di_text);

        }

        public View getBaseSizeView(){
            return mImg;
        }
    }

    @Override
    final public ViewHolder onCreateViewHolder(ViewGroup parent)
    {
        VH vh = (VH)createViewHolder(parent);
//        ViewGroup.LayoutParams lpImg = vh.mImg.getLayoutParams();
//        lpImg.width = mBaseWidth;
//        lpImg.height = mBaseHeight;
        View presenterView = vh.view;
        if (presenterView != null) {
            vh.mFocusChangeListener.mChainedListener = presenterView.getOnFocusChangeListener();
            presenterView.setOnFocusChangeListener(vh.mFocusChangeListener);
        }
        if (mFocusHighlight != null) {
            mFocusHighlight.onInitializeView(presenterView);
        }
        return vh;
    }

    public ViewHolder createViewHolder(ViewGroup parent){
        LayoutInflater inf = LayoutInflater.from(parent.getContext());
        View presenterView = inf.inflate(getLayoutResId(), parent, false);
        presenterView.setFocusable(true);
        VH vh = new VH(presenterView);
        return vh;
    }

    public int getLayoutResId(){
        return R.layout.di_base_view;
    }

    @Override
    public void onBindViewHolder(ViewHolder aViewHolder, Object aItem)
    {
        final VH vh = (VH) aViewHolder;
        DisplayItem di = (DisplayItem) aItem;
        if(di.ui_type!=null) {
            vh.view.setId(di.ui_type.rows() + 100000);
        }else{
            vh.view.setId(View.NO_ID);
        }

        if(vh.mSubTitle != null)
        {
            vh.mSubTitle.setText(di.sub_title);
        }
        if(vh.mTitle != null)
        {
            vh.mTitle.setText(di.title);
        }

        if(vh.mText!=null){
            vh.mText.setText(di.title);
        }

        ViewGroup.LayoutParams lpImg = vh.mImg.getLayoutParams();
        if(mBaseHeight != 0 && mBaseWidth != 0)
        {
            lpImg.width = mBaseWidth;
            lpImg.height = mBaseHeight;
            if(di.ui_type!=null) {
                int w = di.ui_type.w();
                int h = di.ui_type.h();
                if (w > 0 && h > 0) {
                    lpImg.width *= w;
                    lpImg.height *= h;
                }
            }
        }
        if(di.images != null && di.images.poster() != null)
        {
            Log.d(TAG, di.images.poster().url);
            final String posterUrl = di.images.poster().url;
            if (posterUrl != null)
            {
                vh.imageUrl = posterUrl;
                if(Utils.isScrolling()){
                    vh.mImg.setImageResource(R.drawable.item_bg_default);
                    postLoadImage(vh);
                }else{
                    Glide.with(vh.mImg.getContext())
                            .load(posterUrl)
                            .fitCenter()
                            .dontTransform()
                            .thumbnail(0.1f)
                            .error(R.mipmap.ic_launcher)
                            .into(vh.mImg);
                }


            }
        }
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder)
    {
        VH vh = (VH)viewHolder;
        Glide.clear(vh.mImg);

    }

    public int getBaseWidth(){
        return mBaseWidth;
    };
    public int getBaseHeight(){
        return mBaseHeight;
    };
    public void setBaseSize(int w,int h){
        mBaseWidth = w;
        mBaseHeight = h;
    };
    public int getRealWidth(Context contect){
        return mBaseWidth;
    };

    public int getRealHeight(Context contect){
        return mBaseHeight+(int)contect.getResources().getDimension(R.dimen.item_text_bar_height);
    };

    final class OnFocusChangeListener implements View.OnFocusChangeListener {
        View.OnFocusChangeListener mChainedListener;

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if(hasFocus)
            Log.d("focus","View "+view, new Exception("e"));
            if (mFocusHighlight != null) {
                mFocusHighlight.onItemFocused(view, hasFocus);
            }
            if (mChainedListener != null) {
                mChainedListener.onFocusChange(view, hasFocus);
            }

        }
    }

    private void postLoadImage(final VH holder){
        if(Utils.isScrolling()){
            holder.mImg.postDelayed(new Runnable() {
                @Override
                public void run() {
                    postLoadImage(holder);
                }
            }, 100);
        }else{
            Glide.with(holder.mImg.getContext())
                    .load(holder.imageUrl)
                    .fitCenter()
                    .dontTransform()
                    .thumbnail(0.1f)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mImg);
        }
    }
}
