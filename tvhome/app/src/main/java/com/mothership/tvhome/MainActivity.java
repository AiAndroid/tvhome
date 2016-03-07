package com.mothership.tvhome;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Loader;
import android.media.MediaActionSound;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ObjectAdapter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mothership.tvhome.app.MainFragment;
import com.mothership.tvhome.view.AdView;
import com.mothership.tvhome.view.EmptyLoadingView;
import com.mothership.tvhome.widget.BlockRowPresenter;
import com.mothership.tvhome.widget.CardPresenter;
import com.mothership.tvhome.widget.CardPresenterSelector;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.video.TabsGsonLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<GenericBlock<DisplayItem>>,  AdView.AdListener{

    protected DisplayItem item;
    protected EmptyLoadingView mLoadingView;
    protected BaseGsonLoader mLoader;
    ArrayObjectAdapter mAdapter = new ArrayObjectAdapter(new CardPresenter());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        convert2Adapter(null);
        setContentView(R.layout.activity_main);
        MainFragment mf = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.main_browse_fragment);
        mf.setAdapter(mAdapter);

        getLoaderManager().initLoader(TabsGsonLoader.LOADER_ID, null, MainActivity.this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFinish() {

    }

    @Override
    public Loader<GenericBlock<DisplayItem>> onCreateLoader(int loaderId, Bundle args) {
        if(loaderId == TabsGsonLoader.LOADER_ID){
            mLoader = new TabsGsonLoader(this, item);
            //mLoader.setProgressNotifiable(mLoadingView);
            mLoader.forceLoad();
            return mLoader;
        }else{
            return null;
        }
    }


    @Override
    public void onLoadFinished(Loader<GenericBlock<DisplayItem>> loader, GenericBlock<DisplayItem> data) {
        //data returned
        Log.d("MainActivity", "dataloaded" + data);

        convert2Adapter(data);
//        setContentView(R.layout.activity_main);
        MainFragment mf = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.main_browse_fragment);
//        mf.
//        mf.setAdapter(mAdapter);
//        mAdapter.
        mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
    }


    void convert2Adapter(GenericBlock<DisplayItem> aSrc)
    {
        mAdapter.removeItems(0, mAdapter.size());
//        mAdapter = new ArrayObjectAdapter();

        int blockIdx = 0;
        for(Block<DisplayItem> blk : aSrc.blocks)
        {
            ArrayObjectAdapter blockAdpt = new ArrayObjectAdapter(new BlockRowPresenter());
            ArrayObjectAdapter listAdpt = new ArrayObjectAdapter(new CardPresenter());
            flattenBlock(blk, listAdpt);
            blockAdpt.add(new ListRow(new HeaderItem(blockIdx ++,  blk.title), listAdpt));
            mAdapter.add(blockAdpt);


        }

/*
        ArrayObjectAdapter pageAdapter = mAdapter;
        List<DisplayItem> list =  new ArrayList<DisplayItem>();
        PresenterSelector selector = new CardPresenterSelector();

        for(int k = 0; k < 5; k++) {
            ArrayObjectAdapter rowsAdapter = new ArrayObjectAdapter(new BlockRowPresenter());
            for (int i = 0; i < 9; i++) {
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
                    if (i % 2 == 0) {
                        item.id = "land";
                    }
                    listRowAdapter.add(item);

                    listRowAdapter.setPresenterSelector(selector);
                }

                HeaderItem header = new HeaderItem(i, "HEADER");
                rowsAdapter.add(new ListRow(header, listRowAdapter));
            }
            pageAdapter.add(rowsAdapter);
        }
        */
    }

    void flattenBlock(Block<DisplayItem> aBlk, ArrayObjectAdapter adapter)
    {
        if(aBlk.items != null)
        {
            for(DisplayItem itm : aBlk.items)
            {
                adapter.add(itm);
            }
        }
        if(aBlk.blocks != null)
        {
            for(Block<DisplayItem> blk : aBlk.blocks)
            {
                flattenBlock(blk, adapter);
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<GenericBlock<DisplayItem>> loader) {

    }
}
