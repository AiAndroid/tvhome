package com.mothership.tvhome.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mothership.tvhome.R;
import com.mothership.tvhome.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;
import com.tv.ui.metro.model.DisplayItem;

import java.net.URI;

/**
 * Created by wangwei on 2/26/16.
 */
public class CardPresenter extends Presenter {
    private static final String TAG = "CardPresenter";

    protected static Context mContext;
    private static int CARD_WIDTH = 200;
    private static int CARD_HEIGHT = 300;

    static class ViewHolder extends Presenter.ViewHolder {
        private DisplayItem mItems;
        private ImageView mCardView;
        public ImageView mImageView;
        public ViewHolder(View view)
        {
            super(view);
            mCardView = (ImageView) view;
            mImageView = (ImageView) view;
        }

        public void setMovie(DisplayItem m)
        {
            mItems = m;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");
        mContext = parent.getContext();

        //ImageCardView cardView = new ImageCardView(mContext);
        //cardView.setFocusable(true);
        //cardView.setFocusableInTouchMode(true);
        //cardView.setBackgroundColor(mContext.getResources().getColor(R.color.fastlane_background));
        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(20, 20, 20, 20);
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(getWidth(), getHeight()));
        return new ViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Log.d(TAG, "onBindViewHolder");
        DisplayItem mItem = (DisplayItem) item;
        ViewHolder holder = ((ViewHolder) viewHolder);
        holder.setMovie(mItem);

        if(mItem.images != null && mItem.images.poster() != null)
        {
            Log.d(TAG, mItem.images.poster().url);
            String posterUrl = mItem.images.poster().url;
            if (posterUrl != null)
            {
                Glide.with(holder.mImageView.getContext())
                        .load(posterUrl)
                        .thumbnail(0.1f)
                        .error(R.mipmap.ic_launcher)
                        .into(holder.mImageView);
            }
        }
    }


    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        Log.d(TAG, "onUnbindViewHolder");
        ViewHolder holder = (ViewHolder) viewHolder;
        Glide.clear(holder.mImageView);
    }

    @Override
    public void onViewAttachedToWindow(Presenter.ViewHolder viewHolder) {
        // TO DO
    }


    public static int getWidth(){
        return CARD_WIDTH;
    };

    public static int getHeight(){
        return CARD_HEIGHT;
    };

    public Context getContext(){return mContext;}


}
