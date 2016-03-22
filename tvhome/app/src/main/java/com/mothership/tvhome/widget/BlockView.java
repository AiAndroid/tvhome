package com.mothership.tvhome.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mothership.tvhome.R;

/**
 * Created by wangwei on 3/7/16.
 */
public class BlockView extends FrameLayout {

    private RecyclerView mGridView;
    private boolean mIsVertical = false;

    public BlockView(Context context) {
        this(context, null);
    }

    public BlockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    protected void initLayout(int resId){
        LayoutInflater inflater = LayoutInflater.from(getContext());

        inflater.inflate(resId, this);
        mGridView = (RecyclerView) findViewById(R.id.block_content);

        // Uncomment this to experiment with page-based scrolling.
        // mGridView.setFocusScrollStrategy(HorizontalGridView.FOCUS_SCROLL_PAGE);

        setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        /*if(mGridView instanceof HorizontalGridView){
            HorizontalGridView view = (HorizontalGridView)mGridView;
            view.setFocusScrollStrategy(HorizontalGridView.FOCUS_SCROLL_ALIGNED);
        }else if(mGridView instanceof VerticalGridView){
            VerticalGridView view = (VerticalGridView)mGridView;
            view.setFocusScrollStrategy(VerticalGridView.FOCUS_SCROLL_ALIGNED);
        }*/
        //mGridView.setFocusable(false);

    }
    /**
     * Returns the HorizontalGridView.
     */
    public RecyclerView getGridView() {
        return mGridView;
    }

}
