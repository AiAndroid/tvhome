package com.mothership.tvhome.tv;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import mitv.common.ConfigurationManager;
import mitv.display.ThreeDimensionManager;
import mitv.sound.SoundManager;
import mitv.tv.AtvManager;
import mitv.tv.DtvManager;
import mitv.tv.HdmiManager;
import mitv.tv.Player;
import mitv.tv.PlayerManager;
import mitv.tv.SourceManager;
import mitv.tv.TvContext;
import mitv.tv.TvPlayer;
import mitv.util.ConstTranslate;

/**
 * Created by wangwei on 3/25/16.
 */
public class TVSurfaceView extends SurfaceView implements SurfaceHolder.Callback,MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener{
    private static final String TAG = "TVSurfaceView";
    private TvContext mTvContext;
    private AtvManager mAtvManager;
    private DtvManager mDtvManager;
    private SourceManager mSourceManager;
    private PlayerManager mPlayerManager;
    private ConfigurationManager mConfigManager;
    private ThreeDimensionManager m3DManager;
    private SoundManager mSoundManager;
    private HdmiManager mHdmiManager;
    private int[] mProductSourceList;
    private int[] mAvailableSourceList;
    private int mCurrentSource;
    SurfaceHolder mHolder = null;
    private TvPlayer mPlayer;
    public TVSurfaceView(Context context) {
        this(context, null);
    }


    public TVSurfaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TVSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        if (!initTvContext()) {
            return;
        }
        if (!initInputSource()) {
            return;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setDisplay(holder);
        mPlayer.setOnErrorListener(this);
        mPlayer.setOnPreparedListener(this);
        int source = mSourceManager.getCurrentSource();
        if (source != SourceManager.TYPE_INPUT_SOURCE_ATV) {
            Log.w(TAG,"why current input source not ATV?");
        }
        String sourceStr = mConfigManager.getPlayerDataSourceConfig(source,"");
        try {
            mPlayer.setDataSource(sourceStr);
            mPlayer.prepare();
        } catch (java.io.IOException ex) {
        }
        ConfigurationManager.PlayerParameterPair paras[] = mConfigManager.getPlayerParameters(source);
        if (paras != null) {
            for (int i=0; i<paras.length; i++) {
                mPlayer.setParameter(paras[i].key,paras[i].value);
            }
        }
        mPlayer.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG,"surfaceChanged come ("+width+","+height+")");
        int[] position = new int[2];
        getLocationOnScreen(position);

        mPlayer.setCommonCommand(Player.COMMAND_ATV_SHAPE_CHANGE,
                //String.valueOf(position[0]),String.valueOf(position[1]),
                String.valueOf(200),String.valueOf(200),
                String.valueOf(width),String.valueOf(height));

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mPlayer.release();
        mSourceManager.removeSourceStatusListener(mSourceListener);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    SourceManager.OnSourceChangeListener mSourceListener = new SourceManager.OnSourceChangeListener() {
        public boolean onSourceConnected(int source) {
            Log.i(TAG, "onSourceConnected " + source);
            //mSourceManager.setCurrentSource(source);
            updateAvailableSource();
            String cecInfo = "";
            if ((mCurrentSource==SourceManager.TYPE_INPUT_SOURCE_HDMI)
                    || mCurrentSource==SourceManager.TYPE_INPUT_SOURCE_HDMI2
                    || mCurrentSource==SourceManager.TYPE_INPUT_SOURCE_HDMI3
                    || mCurrentSource==SourceManager.TYPE_INPUT_SOURCE_HDMI4) {
                Log.i(TAG,"hdmi source connect, cec name="+mHdmiManager.getCecDeviceName(mCurrentSource));
                cecInfo = " cec name "+mHdmiManager.getCecDeviceName(mCurrentSource);
            }
            //if (mEventOutput != null) {
            //    mEventOutput.setText("connect "+ConstTranslate.getInputSourceName(source)+cecInfo);
            //}
            return true;
        }
        public boolean onSourceRemoved(int source) {
            Log.i(TAG,"onSourceRemoved"+source);
            //if (mEventOutput != null) {
            //    mEventOutput.setText("remove "+ConstTranslate.getInputSourceName(source));
            //}
            //mSourceManager.setCurrentSource(SourceManager.TYPE_INPUT_SOURCE_ATV);
            updateAvailableSource();
            return true;
        }
        public boolean onCurrentSourceChanged(int source) {
            Log.d(TAG,"onSourceChanged "+source);
            //if (mEventOutput != null) {
            //    mEventOutput.setText("source change to "+ConstTranslate.getInputSourceName(source));
            //}
            updateAvailableSource();
            return true;
        }
        public boolean onSignalFirstStable(int source, boolean stable) {
            //if (mEventOutput != null) {
            //    mEventOutput.setText("onSignalFirstStable "+ConstTranslate.getInputSourceName(source)+" "+stable);
            //}
            return true;
        }
        public boolean onSourceSignalStable(int source, boolean stable) {
            //if (mEventOutput != null) {
            //    mEventOutput.setText("onSourceSignalStable "+ConstTranslate.getInputSourceName(source)+" "+stable);
            //}
            return true;
        }
        public boolean onFirstFrameStable(int source, boolean stable) {
            Log.i(TAG, "=================> onFirstFrameStable source/stable = " + source + "/" + stable);
            return true;
        }
    };

    private boolean initTvContext() {
        boolean ret = true;
        mTvContext = TvContext.getInstance();
        try {
            mAtvManager = (AtvManager)mTvContext.getAtvManager();
            Log.e(TAG,"=========================> initTvContext mAtvManager = " + mAtvManager);

            mDtvManager = (DtvManager)mTvContext.getDtvManager();
            Log.e(TAG,"=========================> initTvContext mDtvManager = " + mDtvManager);

            mSourceManager = (SourceManager)mTvContext.getSourceManager();
            Log.e(TAG,"=========================> initTvContext mSourceManager = " + mSourceManager);

            mPlayerManager = (PlayerManager)mTvContext.getPlayerManager();
            Log.e(TAG,"=========================> initTvContext mPlayerManager = " + mPlayerManager);

            mHdmiManager = (HdmiManager)mTvContext.getHdmiManager();
            Log.e(TAG,"=========================> initTvContext mHdmiManager = " + mHdmiManager);


            //
            mConfigManager = ConfigurationManager.getInstance();
            Log.e(TAG,"==========================> initTvContext " +mConfigManager );

            mPlayer = mPlayerManager.createTvPlayer();
            Log.e(TAG,"==========================> initTvContext " + mPlayer);

            mSourceManager.addSourceStatusListener(mSourceListener);
            mAtvManager.addAtvListener(mChannelListener);

            mHdmiManager.addCecStatusListener(mCecNameReadyListener);

            m3DManager = ThreeDimensionManager.getInstance();
            Log.e(TAG,"==========================> initTvContext " + m3DManager);

            mSoundManager = SoundManager.getInstance();
            Log.e(TAG,"==========================> initTvContext " + mSoundManager);

        } catch (Exception e) {
            Log.e(TAG,"initTvContext find error, e = "+e);
            ret = false;
        }
        return ret;
    }
    AtvManager.OnChannelChangeListener mChannelListener = new AtvManager.OnChannelChangeListener() {
        public boolean onChannelScanned(long freq, int index) {
            Log.i(TAG,"onChannelScanned come freq:"+freq+" index:"+index);
            //Toast.makeText(FullScreen.this, "onChannelScanned come freq:"+freq+" index:"+index+" percent:"+percent,Toast.LENGTH_SHORT).show();
            return true;
        }
        public boolean onScanProgressChanged(int percent) {
            Log.i(TAG,"onScanProgressChanged percent "+percent);
            return true;
        }
        public boolean onChannelTunedCompleted(int index) {
            Log.i(TAG,"onChannelTunedCompleted index="+index);
            return true;
        }
    };

    class CecNameReadyListener extends HdmiManager.CommonCecStatusListener {
        public boolean onCecNameReady() {
            Log.i(TAG,"hdmi1 name="+mHdmiManager.getCecDeviceName(SourceManager.TYPE_INPUT_SOURCE_HDMI));
            Log.i(TAG,"hdmi2 name="+mHdmiManager.getCecDeviceName(SourceManager.TYPE_INPUT_SOURCE_HDMI2));
            Log.i(TAG,"hdmi3 name="+mHdmiManager.getCecDeviceName(SourceManager.TYPE_INPUT_SOURCE_HDMI3));
            return true;
        }
    }
    CecNameReadyListener mCecNameReadyListener  = new CecNameReadyListener();

    private boolean initInputSource() {
        updateConfigSource();
        if (mSourceManager.isSourceConncted(SourceManager.TYPE_INPUT_SOURCE_ATV)) {
            mSourceManager.setCurrentSource(SourceManager.TYPE_INPUT_SOURCE_ATV);
        }
        updateAvailableSource();
        return true;
    }
    private void updateConfigSource() {
        mProductSourceList = mConfigManager.queryAllSources();
        String sourceList = "";
        if (mProductSourceList != null) {
            for (int i=0; i<mProductSourceList.length; i++) {
                sourceList += ConstTranslate.getInputSourceName(mProductSourceList[i]);
                sourceList += " ";
            }
        }
        Log.i(TAG,"product preconfig source list are "+sourceList);
        //if (mProductSourceOutput != null) {
        //    mProductSourceOutput.setText(sourceList);
        //}
    }
    private void updateAvailableSource() {
        mAvailableSourceList = mSourceManager.getConnectedSourceList();
        String sourceList = "";
        if (mAvailableSourceList != null) {
            for (int i=0; i<mAvailableSourceList.length; i++) {
                sourceList += ConstTranslate.getInputSourceName(mAvailableSourceList[i]);
                sourceList += " ";
            }
        }
        Log.i(TAG,"current source list are "+sourceList);
        mCurrentSource = mSourceManager.getCurrentSource();
        Log.i(TAG,"current input source is "+ConstTranslate.getInputSourceName(mCurrentSource));
        sourceList += "\n";
        sourceList += "current source: ";
        sourceList += ConstTranslate.getInputSourceName(mCurrentSource);
        //if (mAvailableSourceOutput != null) {
        //    Log.i(TAG,"set mAvailableSourceOutput text");
        //    mAvailableSourceOutput.setText(sourceList);
        //}
        //mSet3D = false;
    }
}
