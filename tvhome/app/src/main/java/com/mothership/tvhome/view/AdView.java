package com.mothership.tvhome.view;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.google.gson.reflect.TypeToken;
import com.mothership.tvhome.util.ViewUtils;
import com.squareup.picasso.*;
import com.tv.ui.metro.model.Block;
import com.tv.ui.metro.model.DisplayItem;
import com.tv.ui.metro.model.XiaomiStatistics;
import com.video.ui.idata.SyncServiceHelper;
import com.video.ui.idata.iDataORM;
import com.video.ui.loader.AppGson;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import com.mothership.tvhome.*;

/**
 * Created by liuhuadonbg on 3/20/15.
 */
public class AdView extends RelativeLayout {

    public static String APP_ID     = "2882303761517336595";
    public static String APP_Key    = "5861733616595";
    public static String APP_Secret = "2v2fKW0c7RV9rsoR132VpQ==";
    final static String TAG = AdView.class.getSimpleName();
    private static boolean DEBUG = false;

    public AdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if(ads_count == 0){
            ads_count = iDataORM.getIntValue(context, "ads_count_tick", 0);
        }

        initUI();

        int count = 3000;
        int s_count = 3;

        if(block != null && block.settings != null ){
            s_count = DisplayItem.getInt(block.settings.get("time_count"), 3);
            boolean loopMode = DisplayItem.getBoolean(block.settings.get("loop"), false);
            if(loopMode ){
                iDataORM.addSetting(context, iDataORM.startup_ads_loop, loopMode?"1":"0");
            }
            count = s_count*1000;
        }
        countDownTimer = new CountDownTimer(count, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                fillCountDown((int) (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {
                if(mListener != null){
                    mListener.onFinish();
                }
            }
        };
        countDownTimer.start();
        fillCountDown(s_count);
    }

    public interface AdListener{
        void onFinish();
    }

    @Override
    protected void onDetachedFromWindow(){
        super.onDetachedFromWindow();

        ViewUtils.unbindDrawables(this);
    }

    static  int ads_count=0;
    private AdListener mListener;
    public void setAdListener(AdListener listener){
        mListener = listener;
    }

    public AdView(Context context) {
        this(context, null, 0);
    }

    private static WeakReference<Activity> homeActivity;
    public static void setActivity(Activity activity){
        homeActivity = new WeakReference<Activity>(activity);
    }

    private static Block getAdsBlock(Context context){
        try {
            if (SyncServiceHelper.ads_object == null) {
                String ads = iDataORM.getStringValue(context, iDataORM.startup_ads, null);
                if (!TextUtils.isEmpty(ads)) {
                    SyncServiceHelper.ads_object = AppGson.get().fromJson(ads, new TypeToken<Block<DisplayItem>>() {
                    }.getType());

                    if(SyncServiceHelper.ads_object.times == null){
                        SyncServiceHelper.ads_object.times = new DisplayItem.Times();
                    }
                }
            }
        }catch (Exception ne){}

        return SyncServiceHelper.ads_object;
    }

    public static DisplayItem getAdsItem(Context context){
        DisplayItem item = null;

        Block container = getAdsBlock(context);
        if (container != null) {
            try {
                block = (Block<DisplayItem>) container.blocks.get(0);
                if (block != null) {

                    if (block.blocks != null && block.blocks.size() > 0) {
                        item = block.blocks.get(ads_count % block.blocks.size());
                        ads_count++;
                    } else if (block.items != null && block.items.size() > 0) {
                        item = block.items.get(ads_count % block.items.size());
                        ads_count++;
                    } else {
                        item = block;
                    }
                }

                iDataORM.addSetting(context, "ads_count_tick", String.valueOf(ads_count));
            }catch (Exception ne){
                Log.d(TAG, "" + ne.getMessage());
                //clear settings
                iDataORM.addSetting(context, iDataORM.startup_ads, "");
            }
        }
        return item;
    }
    private CountDownTimer countDownTimer;
    private TextView       mCountDownView;
    private ImageView      mBottomView;
    private Button         mJump;
    private RelativeLayout image_container;
    public  static         Block<DisplayItem> block=null;
    private DisplayItem    currentItem;
    private void initUI(){
        final View view = View.inflate(getContext(), R.layout.ad_main_layout, null);
        mCountDownView = (TextView)view.findViewById(R.id.countdown);
        image_container = (RelativeLayout) view.findViewById(R.id.image_container);
        mBottomView    = (ImageView) view.findViewById(R.id.ads_bottom_image);

        mJump          = (Button)view.findViewById(R.id.ads_jump);
        mJump.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    mListener.onFinish();
                }

                //add statics for ignore click
                addStats(currentItem, "ignore");
            }
        });

        addView(view);

        boolean show_bottom = true;
        try {
            final DisplayItem item = getAdsItem(getContext());
            currentItem  = item;
            if (block != null && currentItem != null) {

                if(block.settings != null){
                    if("0".equals(block.settings.get("show_bottom"))){
                        mBottomView.setVisibility(GONE);
                        show_bottom = false;
                    }

                    if("0".equals(block.settings.get("show_ignore"))){
                        mJump.setVisibility(GONE);
                    }

                    if("0".equals(block.settings.get("show_count"))){
                        mCountDownView.setVisibility(GONE);
                    }
                }

                showAdView(view, item);

                ImageView logo_container = (ImageView) findViewById(R.id.ads_bottom_image);
                logo_container.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(homeActivity != null && homeActivity.get() != null){
                            BaseCardView.launcherAction(homeActivity.get(), block);
                        }else {
                            BaseCardView.launcherAction(getContext(), block);
                        }
                    }
                });

                if (block.images != null && block.images.bottom() != null && show_bottom) {
                    String url = block.images.bottom().url;
                    if (!TextUtils.isEmpty(url)) {
                        BaseCardView.getSinglePicasso(getContext()).load(url).centerCrop().into(logo_container);
                        //VolleyHelper.getInstance(getContext()).getImageLoader().get(url, getCommonViewImageListener(logo_container, R.drawable.default_ads, 0));
                    }
                }

            }else {

                if(DEBUG)
                Toast.makeText(getContext(), "I am null why", Toast.LENGTH_LONG);

                mBottomView.setVisibility(GONE);
                mCountDownView.setVisibility(GONE);

                //if no block
                if(mListener != null){
                    mListener.onFinish();
                }
            }
        }catch (Exception ne){ne.printStackTrace();

            if(DEBUG)
                Toast.makeText(getContext(), "exception :"+ne.getMessage(), Toast.LENGTH_LONG).show();
            mBottomView.setVisibility(GONE);
            mCountDownView.setVisibility(GONE);

            //if crash, just finish the ads
            if(mListener != null){
                mListener.onFinish();
            }
        }
    }

    public void showAdView(View view, final DisplayItem item)
    {
        try {
            final ImageView ads_container = (ImageView) view.findViewById(R.id.ads_image);
            if (item.images != null && item.images.poster() != null) {
                if (XiaomiStatistics.initialed)
                    MiStatInterface.recordCountEvent(XiaomiStatistics.Advertise, item.images.poster().url);

                final long pre = System.currentTimeMillis();
                //add statics for present
                addStats(item, "present");

                if(DEBUG)
                Toast.makeText(getContext(), "present", Toast.LENGTH_LONG).show();

                try {
                    if(DEBUG)
                        Log.d(TAG, item.images.poster().url);

                    BaseCardView.getSinglePicasso(homeActivity!=null?homeActivity.get():getContext()).load(item.images.poster().url).priority(Picasso.Priority.HIGH).into(ads_container);

                } catch (Exception ne) {
                    if(DEBUG)
                        Toast.makeText(getContext(), "try exception ne:"+ne.getMessage(), Toast.LENGTH_LONG).show();

                    if (mListener != null) {
                        mListener.onFinish();
                    }
                }

            }

            ads_container.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (homeActivity != null && homeActivity.get() != null) {
                        BaseCardView.launcherAction(homeActivity.get(), item);
                    } else {
                        BaseCardView.launcherAction(getContext(), item);
                    }

                    //add statics for ignore click
                    addStats(item, "click");
                }
            });

            BaseCardView.uploadPresentAction(getContext(), item);
        }catch (Exception ne){
            if (mListener != null) {
                mListener.onFinish();
            }
        }
    }

    private void addStats(DisplayItem item, String event){
        HashMap<String, String> map = new HashMap<String, String>();
        if (item != null) {
            if (item.images != null && item.images.poster() != null) {
                map.put("url", item.images.poster().url);
            }
            map.put("id", item.id);
            if (item.settings != null && "1".equals(item.settings.get("ads_splash"))) {
                map.put("i_am_ads", "true");
            } else {
                map.put("i_am_ads", "false");
            }
            BaseCardView.formartShowInfo(item, map);
        }
        BaseCardView.formartDeviceMap(map);
        if (XiaomiStatistics.initialed)
            MiStatInterface.recordCalculateEvent(XiaomiStatistics.Advertise, event, 1, map);
    }

    private void fillCountDown(int leftSeconds){
        String text = "<font color='#ff307ab3'>&nbsp;" + leftSeconds + " </font>";
        //if(block != null && block.settings != null && "1".equals(block.settings.get("show_count"))
        {
            mCountDownView.setText(Html.fromHtml(text));
        }
    }
}
