package com.mothership.tvhome.widget;

import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by wangwei on 2/29/16.
 */
public class LandCardPresenter extends CardPresenter{

    public int getWidth(){
        return 300;
    };

    public  int getHeight(){
        return 200;
    };

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        mContext = parent.getContext();

        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(20, 20, 20, 20);
        imageView.setFocusable(true);
        imageView.setFocusableInTouchMode(true);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(getWidth(), getHeight()));
        return new ViewHolder(imageView);
    }

}
