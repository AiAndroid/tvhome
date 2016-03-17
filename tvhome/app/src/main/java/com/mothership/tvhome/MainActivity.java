package com.mothership.tvhome;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.duokan.VolleyHelper;
import com.google.gson.reflect.TypeToken;
import com.mothership.tvhome.app.MainFragment;
import com.mothership.tvhome.view.AdView;
import com.mothership.tvhome.view.AlertDialogHelper;
import com.mothership.tvhome.view.EmptyLoadingView;
import com.mothership.tvhome.widget.BlockHorizontalPresenter;
import com.mothership.tvhome.widget.CardPresenter;
import com.mothership.tvhome.widget.DisplayItemSelector;
import com.tv.ui.metro.model.AppVersion;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.GenericBlock;
import com.video.ui.idata.BackgroundService;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.BaseGsonLoader;
import com.video.ui.loader.CommonBaseUrl;
import com.video.ui.loader.CommonUrl;
import com.video.ui.loader.video.TabsGsonLoader;

import java.io.BufferedReader;
import java.io.StringReader;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<GenericBlock<DisplayItem>>,  AdView.AdListener{

    private static final String TAG = "MainActivity";
    protected DisplayItem item;
    protected EmptyLoadingView mLoadingView;
    protected BaseGsonLoader mLoader;
    DisplayItemSelector mDiSel = new DisplayItemSelector();
    ArrayObjectAdapter mAdapter = new ArrayObjectAdapter(new CardPresenter());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        convert2Adapter(null);
        setContentView(R.layout.activity_main);
        MainFragment mf = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.main_browse_fragment);
        mf.setAdapter(mAdapter);

        getLoaderManager().initLoader(TabsGsonLoader.LOADER_ID, null, MainActivity.this);


        //check version upgrade
        checkForAppUpgrade(getApplicationContext());
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
        //convert2Adapter(data);
        //mAdapter.notifyArrayItemRangeChanged(0, mAdapter.size());
        MainFragment mainFragment = (MainFragment)
                getSupportFragmentManager().findFragmentById(R.id.main_browse_fragment);
        if(mainFragment!=null){
            mainFragment.LoadData(data);
        }
    }


    void convert2Adapter(GenericBlock<DisplayItem> aSrc)
    {
        mAdapter.removeItems(0, mAdapter.size());
//        mAdapter = new ArrayObjectAdapter();

        for(Block<DisplayItem> blk : aSrc.blocks)
        {
            ArrayObjectAdapter pageAdt = new ArrayObjectAdapter(new BlockHorizontalPresenter());
            ArrayObjectAdapter listAdt = new ArrayObjectAdapter(mDiSel);
            flattenBlock(blk, listAdt, pageAdt);
            Log.d(TAG, "add block " + blk.title);
            pageAdt.add(new ListRow(new HeaderItem(blk.title), listAdt));
            mAdapter.add(pageAdt);
        }
    }

    void flattenBlock(Block<DisplayItem> aBlk, ArrayObjectAdapter aLstAdt, ArrayObjectAdapter aPageAdt)
    {
        if(aBlk.items != null)
        {
            for(DisplayItem itm : aBlk.items)
            {
                aLstAdt.add(itm);
            }
        }
        if(aBlk.blocks != null)
        {
            for(Block<DisplayItem> blk : aBlk.blocks)
            {
                ArrayObjectAdapter listAdt = new ArrayObjectAdapter(mDiSel);
                flattenBlock(blk, listAdt, aPageAdt);
                Log.d(TAG, "add row for page " + blk.title + " cnt " + listAdt.size());
                aPageAdt.add(new ListRow(new HeaderItem(blk.title), listAdt));
            }
        }

    }

    @Override
    public void onLoaderReset(Loader<GenericBlock<DisplayItem>> loader) {

    }

    private void checkForAppUpgrade(final Context context){
        Response.Listener<AppVersion> listener = new Response.Listener<AppVersion>() {
            @Override
            public void onResponse(final AppVersion response) {
                PackageManager pm = context.getPackageManager();
                try {
                    int versionCode = CommonBaseUrl.versionCode;
                    if (versionCode < 0) {
                        versionCode = pm.getPackageInfo(context.getPackageName(), 0).versionCode;
                    }

                    if (response.version_code() > versionCode) {
                        String msg = response.recent_change() + "\nVersion Code:" +
                                response.version_code() + "\n" +
                                response.release_date() + "\n" +
                                response.released_by() + "\n";

                        AlertDialog dialog = new AlertDialog.Builder(context).create();
                        dialog.setTitle("软件更新");
                        dialog.setMessage(msg);
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "立即更新", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                                //begin to download apk

                            }
                        });
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setCancelable(true);
                        try {
                            dialog.show();
                        } catch (Exception e) {
                            Log.e(TAG, e.getLocalizedMessage());
                        }
                    }
                } catch (Exception e) {}
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };

        final String appCheck = "https://raw.githubusercontent.com/AiAndroid/tvhome/master/appupgrade.json";
        RequestQueue requestQueue = VolleyHelper.getInstance(context).getAPIRequestQueue();
        BaseGsonLoader.GsonRequest<AppVersion> gsonRequest = new BaseGsonLoader.GsonRequest<AppVersion>(appCheck, new TypeToken<AppVersion>() {
        }.getType(), null, listener, errorListener);
        requestQueue.add(gsonRequest);
    }
}
