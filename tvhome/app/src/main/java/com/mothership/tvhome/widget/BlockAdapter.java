package com.mothership.tvhome.widget;

import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;

import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;

/**
 * Created by wangwei on 3/4/16.
 */
public class BlockAdapter extends ArrayObjectAdapter {
    public Block<DisplayItem> mBlock;
    public boolean mHasBlocks = false;
    /**
     * Constructs an adapter with the given {@link PresenterSelector}.
     */
    public BlockAdapter(Block<DisplayItem> block,PresenterSelector presenterSelector) {
        super(presenterSelector);
        mBlock = block;
        if(mBlock.blocks==null){
            mHasBlocks = false;
        }else{
            mHasBlocks = true;
        }
    }

    /**
     * Constructs an adapter that uses the given {@link Presenter} for all items.
     */
    public BlockAdapter(Block<DisplayItem> block,Presenter presenter) {
        super(presenter);
        mBlock = block;
        if(mBlock.blocks==null){
            mHasBlocks = false;
        }else{
            mHasBlocks = true;
        }
    }

    /**
     * Constructs an adapter.
     */
    public BlockAdapter(Block<DisplayItem> block) {
        super();
        mBlock = block;
        if(mBlock.blocks==null){
            mHasBlocks = false;
        }else{
            mHasBlocks = true;
        }
    }

    @Override
    public int size() {
        if(mHasBlocks){
            return mBlock.blocks.size();
        }else if(mBlock.items!=null){
            return mBlock.items.size();
        }
        return 0;
    }

    @Override
    public Object get(int index) {
        if(mHasBlocks){
            return mBlock.blocks.get(index);
        }else if(mBlock.items!=null){
            return mBlock.items.get(index);
        }
        return null;
    }



}
