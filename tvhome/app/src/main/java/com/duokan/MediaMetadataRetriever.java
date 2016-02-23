/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.duokan;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.net.Uri;
import android.database.Cursor;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.Log;

import java.util.Map;

/**
 * MediaMetadataRetriever class provides a unified interface for retrieving
 * frame and meta data from an input media file. Codec depends on this class,
 * DO NOT move it
 */
public class  MediaMetadataRetriever
{
	static {
		try {
			System.loadLibrary("ffmpeg-miplayer");
			System.loadLibrary("xiaomimediaplayer");
			System.loadLibrary("xiaomiplayerwrapper");
			native_init();
		} catch (Throwable t) {
			Log.w("MediaPlayer", "Unable to load the media library: " + t);
		}
	}
	
	private final static String TAG = "DkMediaMetadataRetriever";
	
	// The field below is accessed by native methods
	@SuppressWarnings("unused")
	private long mNativeContext;
 
    private static final int EMBEDDED_PICTURE_TYPE_ANY = 0xFFFF;

    public MediaMetadataRetriever() {
    	Log.i(TAG, "DkMediaMetadataRetriever");
        native_setup();
    }
    
    /**
     * Transform source Bitmap to targeted width and height
     */
    private static Bitmap transform(Matrix scaler,Bitmap source,int targetWidth,
    		int targetHeight,int options) {
    	
    	Log.i(TAG, "transform --1--");
    	boolean scaleUp = (options & 0x1) != 0;			//OPTIONS_SCALE_UP = 0x1
    	boolean recycle  = (options & 0x2) != 0;		//OPTIONS_RECYCLE_IPPUT = 0x2
    	
    	int deltaX = source.getWidth() - targetWidth;
    	int deltaY = source.getHeight() - targetHeight;
    	if (!scaleUp && (deltaX < 0 || deltaY < 0)) {
    		Log.i(TAG, "transform --2--");
    		/*
    		 * In this case the bitmap is smaller, at least in one dimension,
    		 * than the target. Transform it by placing as much of the image
    		 * as possible into the target and leaving the top/bottom or 
    		 * left/right (or both) black.
    		 */
    		Bitmap b2 = Bitmap.createBitmap(targetWidth,targetHeight,Bitmap.Config.ARGB_8888);
    		Canvas c = new Canvas(b2);
    		
    		int deltaXHalf = Math.max(0, deltaX/2);
    		int deltaYHalf = Math.max(0, deltaY/2);
    		Rect src = new Rect(
    				deltaXHalf,
    				deltaYHalf,
    				deltaXHalf + Math.min(targetWidth, source.getWidth()),
    				deltaYHalf + Math.min(targetHeight,source.getHeight()));
    		int dstX = (targetWidth - src.width()) / 2;
    		int dstY = (targetHeight - src.height()) / 2;
    		Rect dst = new Rect(
    				dstX,
    				dstY,
    				targetWidth - dstX,
    				targetHeight - dstY);
    		c.drawBitmap(source, src,dst,null);
    		if (recycle) {
    			source.recycle();
    		}
    		c.setBitmap(null);
    		Log.i(TAG, "transform --3--");
    		return b2;
    	}
    	
    	float bitmapWidthF = source.getWidth();
    	float bitmapHeightF = source.getHeight();
    	Log.i(TAG, "bitmapWidthF:" + bitmapWidthF + ",bitmapHeightF:" + bitmapHeightF);
    	float bitmapAspect = bitmapWidthF / bitmapHeightF;
    	float viewAspect = (float) targetWidth / targetHeight;
    	Log.i(TAG, "targetWidth:" + targetWidth + ",targetHeight:" + targetHeight);
    	if (bitmapAspect > viewAspect) {
    		float scale = targetHeight / bitmapHeightF;
    		if (scale < .9F || scale > 1F) {
    			scaler.setScale(scale,scale);
    		} else {
    			scaler = null;
    		}
    	} else {
    		float scale = targetWidth / bitmapWidthF;
    		if (scale < .9F || scale > 1F) {
    			scaler.setScale(scale, scale);
    		} else {
    			scaler = null;
    		}
    	}
    	
    	Bitmap b1;
    	if (scaler != null) {
    		Log.i(TAG, "transform --3.1--");
    		// this is used for minithumb and crop, so we want to filter here.
    		b1 = Bitmap.createBitmap(source,0,0,
    				source.getWidth(),source.getHeight(),scaler,true);
    	} else {
    		Log.i(TAG, "transform --3.2--");
    		b1 = source;
    	}
    	
    	if (recycle && b1 != source) {
    		Log.i(TAG, "transform --4--");
    		source.recycle();
    	}
    	
    	int dx1 = Math.max(0, b1.getWidth() - targetWidth);
    	int dy1 = Math.max(0,b1.getHeight() - targetHeight);
    	
    	Bitmap b2 = Bitmap.createBitmap(
    			b1,
    			dx1 / 2,
    			dy1 / 2,
    			targetWidth,
    			targetHeight);
    	
    	if (b2 != b1) {
    		if (recycle || b1 != source) {
    			Log.i(TAG, "transform --5--");
    			b1.recycle();
    		}
    	}
    	Log.i(TAG, "transform --6--");
    	
    	if ( b2==null ) {
    		Log.i(TAG,"transform --7--");
    	}
    	return b2;
    }

    /**
     * Creates a centered bitmap of the desired size.
     * 
     * @param sourcde original bitmap source
     * @param width targeted width
     * @param height targeted height
     * @param options options used during thumbnail extraction
     */
    public static Bitmap extractThumbnail (Bitmap source, int width, int height, int options) {
    	Log.i(TAG, "extractThumbnail --1--");
    	if (source == null) {
    		Log.i(TAG, "extractThumbnail --2--");
    		return null;
    	}
    	
    	float scale;
    	if (source.getWidth() < source.getHeight()) {
    		scale = width / (float) source.getWidth();
    	} else {
    		scale = height / (float) source.getHeight();
    	}
    	Matrix matrix = new Matrix();
    	matrix.setScale(scale, scale);
    	Bitmap  thumbnail = transform(matrix,source,width,height,0x1|options);		//OPTIONS_SCALE_UP = 0x1
    	Log.i(TAG, "extractThumbnail --3--");
    	
    	if ( thumbnail==null) {
    		Log.i(TAG,"extractThumbnail --4--");
    	}
    	return thumbnail;
    }
    
    /** 
     * Create a video thumbnail for a video. May return null if the video is
     * corrupt or the format is not supported.
     * 
     *  @param filePath the path of video file
     *  @param kind could be MINI_KIND or MICRO_KIND
     */
    
    public static Bitmap createVideoThumbnail(String filePath, int kind) {
    	return createVideoThumbnail(filePath, kind, -1);
    }
    
    /** 
     * Create a video thumbnail for a video. May return null if the video is
     * corrupt or the format is not supported.
     * 
     *  @param filePath the path of video file
     *  @param kind could be MINI_KIND or MICRO_KIND
     *  @param media position in us
     */    
    public static Bitmap createVideoThumbnail(String filePath, int kind, long timeUs) {    	
    	Log.i(TAG, "createVideoThumbnail");
    	Bitmap bitmap = null;
    	MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    	try {
    		Log.i(TAG, "createVideoThumbnail --1--");
    		retriever.setDataSource(filePath);
    		bitmap = retriever.getFrameAtTime(timeUs);
    	} catch (IllegalArgumentException ex) {
    		//Assume this is a corrupt video file
    	} catch (RuntimeException ex) {
    		//Assume this a corrupt video file
    	} finally {
    		Log.i(TAG, "createVideoThumbnail --2--");
    		try {
    			Log.i(TAG, "createVideoThumbnail --3--");
    			retriever.release();
    		} catch (RuntimeException ex) {
    			//Ignore failures while cleaning up
    		}
    	}
    	
    	if (bitmap == null ) {
    		Log.i(TAG, "bitmap is null");
    		return null;
    	}
    	
    	if (kind == Images.Thumbnails.MINI_KIND) {
    		//Scale down the bitmap if it's too large
    		int width = bitmap.getWidth();
    		int height = bitmap.getHeight();
    		int max = Math.max(width, height);
    		if (max > 512) {
    			float scale = 512f/max;
    			int w = Math.round(scale * width);
    			int h = Math.round(scale * height);
    			bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
    		}
    	} else if (kind == Images.Thumbnails.MICRO_KIND) {
    		Log.i(TAG, "createVideoThumbnail --4--");
    		bitmap = extractThumbnail(bitmap,
    				TARGET_SIZE_MICRO_THUMBNAIL,
    				TARGET_SIZE_MICRO_THUMBNAIL,
    				0x2);		//OPTIONS_RECYCLE_IPPUT = 0x2
    	}
    	
    	return bitmap;
    	
    }

    private String getRealFilePathFromContentUri(Context context, Uri contentUri) {
        String[] columns = new String[]{MediaStore.Video.Media.DATA};
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(contentUri, columns, null, null, null);
            if (cursor == null) {
                return null;
            }
            int index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(index);
            cursor.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            return null;
        }
    }
    
    /**
     * Sets the data source (file pathname) to use. Call this
     * method before the rest of the methods in this class. This method may be
     * time-consuming.
     * 
     * @param path The path of the input media file.
     * @throws IllegalArgumentException If the path is invalid.
     */
    public native void setDataSource(String path) throws IllegalArgumentException;
    
    /**
     * Sets the data source as a content Uri. Call this method before 
     * the rest of the methods in this class. This method may be time-consuming.
     * 
     * @param context the Context to use when resolving the Uri
     * @param uri the Content URI of the data you want to play
     * @throws IllegalArgumentException if the Uri is invalid
     * @throws SecurityException if the Uri cannot be used due to lack of
     * permission.
     */
    public void setDataSource(Context context, Uri uri)
        throws IllegalArgumentException, SecurityException {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        
        String scheme = uri.getScheme();
        if(scheme == null || scheme.equals("file")) {
            setDataSource(uri.getPath());
            return;
        } else if (scheme.equals("content")) {
            String realPath = getRealFilePathFromContentUri(context, uri);
            if (realPath == null) {
                throw new IllegalArgumentException("Can not get real path from content uri!!!");
            }
            setDataSource(realPath);
            return;
        }

        setDataSource(uri.toString());
    }    

    private native void _setDataSource(String uri, Map<String, String> headers) throws IllegalArgumentException;
    /**
     * Sets the data source (URI) to use. Call this
     * method before the rest of the methods in this class. This method may be
     * time-consuming.
     *
     * @param uri The URI of the input media.
     * @param headers the headers to be sent together with the request for the data
     * @throws IllegalArgumentException If the URI is invalid.
     */
    public void setDataSource(String uri,  Map<String, String> headers) throws IllegalArgumentException {
        _setDataSource(uri, headers);
    }

    /**
     * Call this method after setDataSource(). This method retrieves the 
     * meta data value associated with the keyCode.
     * 
     * The keyCode currently supported is listed below as METADATA_XXX
     * constants. With any other value, it returns a null pointer.
     * 
     * @param keyCode One of the constants listed below at the end of the class.
     * @return The meta data value associate with the given keyCode on success; 
     * null on failure.
     */
    public native String extractMetadata(int keyCode);

    private native Bitmap _getFrameAtTime(long timeUs, int option);
    
    /**
     * Call this method after setDataSource(). This method finds a
     * representative frame close to the given time position by considering
     * the given option if possible, and returns it as a bitmap. This is
     * useful for generating a thumbnail for an input data source or just
     * obtain and display a frame at the given time position.
     *
     * @param timeUs The time position where the frame will be retrieved.
     * When retrieving the frame at the given time position, there is no
     * guarantee that the data source has a frame located at the position.
     * When this happens, a frame nearby will be returned. If timeUs is
     * negative, time position and option will ignored, and any frame
     * that the implementation considers as representative may be returned.
     *
     * @param option a hint on how the frame is found. Use
     * {@link #OPTION_PREVIOUS_SYNC} if one wants to retrieve a sync frame
     * that has a timestamp earlier than or the same as timeUs. Use
     * {@link #OPTION_NEXT_SYNC} if one wants to retrieve a sync frame
     * that has a timestamp later than or the same as timeUs. Use
     * {@link #OPTION_CLOSEST_SYNC} if one wants to retrieve a sync frame
     * that has a timestamp closest to or the same as timeUs. Use
     * {@link #OPTION_CLOSEST} if one wants to retrieve a frame that may
     * or may not be a sync frame but is closest to or the same as timeUs.
     * {@link #OPTION_CLOSEST} often has larger performance overhead compared
     * to the other options if there is no sync frame located at timeUs.
     *
     * @return A Bitmap containing a representative video frame, which 
     *         can be null, if such a frame cannot be retrieved.
     */
    public Bitmap getFrameAtTime(long timeUs, int option) {
        if (option < OPTION_PREVIOUS_SYNC ||
            option > OPTION_CLOSEST) {
            throw new IllegalArgumentException("Unsupported option: " + option);
        }

        return _getFrameAtTime(timeUs, option);
    }

    /**
     * Call this method after setDataSource(). This method finds a
     * representative frame close to the given time position if possible,
     * and returns it as a bitmap. This is useful for generating a thumbnail
     * for an input data source. Call this method if one does not care
     * how the frame is found as long as it is close to the given time;
     * otherwise, please call {@link #getFrameAtTime(long, int)}.
     *
     * @param timeUs The time position where the frame will be retrieved.
     * When retrieving the frame at the given time position, there is no
     * guarentee that the data source has a frame located at the position.
     * When this happens, a frame nearby will be returned. If timeUs is
     * negative, time position and option will ignored, and any frame
     * that the implementation considers as representative may be returned.
     *
     * @return A Bitmap containing a representative video frame, which
     *         can be null, if such a frame cannot be retrieved.
     *
     * @see #getFrameAtTime(long, int)
     */
    public Bitmap getFrameAtTime(long timeUs) {
        return getFrameAtTime(timeUs, OPTION_CLOSEST_SYNC);
    }

    /**
     * Call this method after setDataSource(). This method finds a
     * representative frame at any time position if possible,
     * and returns it as a bitmap. This is useful for generating a thumbnail
     * for an input data source. Call this method if one does not
     * care about where the frame is located; otherwise, please call
     * {@link #getFrameAtTime(long)} or {@link #getFrameAtTime(long, int)}
     *
     * @return A Bitmap containing a representative video frame, which
     *         can be null, if such a frame cannot be retrieved.
     *
     * @see #getFrameAtTime(long)
     * @see #getFrameAtTime(long, int)
     */
    public Bitmap getFrameAtTime() {
        return getFrameAtTime(-1, OPTION_CLOSEST_SYNC);
    }

    /**
     * Call it when one is done with the object. This method releases the memory
     * allocated internally.
     */
    public native void release();
    private native void native_setup();
    private static native void native_init();

    private native final void native_finalize();

//    @Override
//    protected void finalize() throws Throwable {
//        try {
//            native_finalize();
//        } finally {
//            super.finalize();
//        }
//    }

    /**
     * Option used in method {@link #getFrameAtTime(long, int)} to get a
     * frame at a specified location.
     *
     * @see #getFrameAtTime(long, int)
     */
    /* Do not change these option values without updating their counterparts
     * in include/media/stagefright/MediaSource.h!
     */
    /**
     * This option is used with {@link #getFrameAtTime(long, int)} to retrieve
     * a sync (or key) frame associated with a data source that is located
     * right before or at the given time.
     *
     * @see #getFrameAtTime(long, int)
     */
    public static final int OPTION_PREVIOUS_SYNC    = 0x00;
    /**
     * This option is used with {@link #getFrameAtTime(long, int)} to retrieve
     * a sync (or key) frame associated with a data source that is located
     * right after or at the given time.
     *
     * @see #getFrameAtTime(long, int)
     */
    public static final int OPTION_NEXT_SYNC        = 0x01;
    /**
     * This option is used with {@link #getFrameAtTime(long, int)} to retrieve
     * a sync (or key) frame associated with a data source that is located
     * closest to (in time) or at the given time.
     *
     * @see #getFrameAtTime(long, int)
     */
    public static final int OPTION_CLOSEST_SYNC     = 0x02;
    /**
     * This option is used with {@link #getFrameAtTime(long, int)} to retrieve
     * a frame (not necessarily a key frame) associated with a data source that
     * is located closest to or at the given time.
     *
     * @see #getFrameAtTime(long, int)
     */
    public static final int OPTION_CLOSEST          = 0x03;

    /*
     * Do not change these metadata key values without updating their
     * counterparts in include/media/mediametadataretriever.h!
     */
    /**
     * The metadata key to retrieve the numberic string describing the
     * order of the audio data source on its original recording.
     */
    public static final int METADATA_KEY_CD_TRACK_NUMBER = 0;
    /**
     * The metadata key to retrieve the information about the album title
     * of the data source.
     */
    public static final int METADATA_KEY_ALBUM           = 1;
    /**
     * The metadata key to retrieve the information about the artist of
     * the data source.
     */
    public static final int METADATA_KEY_ARTIST          = 2;
    /**
     * The metadata key to retrieve the information about the author of
     * the data source.
     */
    public static final int METADATA_KEY_AUTHOR          = 3;
    /**
     * The metadata key to retrieve the information about the composer of
     * the data source.
     */
    public static final int METADATA_KEY_COMPOSER        = 4;
    /**
     * The metadata key to retrieve the date when the data source was created
     * or modified.
     */
    public static final int METADATA_KEY_DATE            = 5;
    /**
     * The metadata key to retrieve the content type or genre of the data
     * source.
     */
    public static final int METADATA_KEY_GENRE           = 6;
    /**
     * The metadata key to retrieve the data source title.
     */
    public static final int METADATA_KEY_TITLE           = 7;
    /**
     * The metadata key to retrieve the year when the data source was created
     * or modified.
     */
    public static final int METADATA_KEY_YEAR            = 8;
    /**
     * The metadata key to retrieve the playback duration of the data source.
     */
    public static final int METADATA_KEY_DURATION        = 9;
    /**
     * The metadata key to retrieve the number of tracks, such as audio, video,
     * text, in the data source, such as a mp4 or 3gpp file.
     */
    public static final int METADATA_KEY_NUM_TRACKS      = 10;
    /**
     * The metadata key to retrieve the information of the writer (such as
     * lyricist) of the data source.
     */
    public static final int METADATA_KEY_WRITER          = 11;
    /**
     * The metadata key to retrieve the mime type of the data source. Some
     * example mime types include: "video/mp4", "audio/mp4", "audio/amr-wb",
     * etc.
     */
    public static final int METADATA_KEY_MIMETYPE        = 12;
    /**
     * The metadata key to retrieve the information about the performers or
     * artist associated with the data source.
     */
    public static final int METADATA_KEY_ALBUMARTIST     = 13;
    /**
     * The metadata key to retrieve the numberic string that describes which
     * part of a set the audio data source comes from.
     */
    public static final int METADATA_KEY_DISC_NUMBER     = 14;
    /**
     * The metadata key to retrieve the music album compilation status.
     */
    public static final int METADATA_KEY_COMPILATION     = 15;
    /**
     * If this key exists the media contains audio content.
     */
    public static final int METADATA_KEY_HAS_AUDIO       = 16;
    /**
     * If this key exists the media contains video content.
     */
    public static final int METADATA_KEY_HAS_VIDEO       = 17;
    /**
     * If the media contains video, this key retrieves its width.
     */
    public static final int METADATA_KEY_VIDEO_WIDTH     = 18;
    /**
     * If the media contains video, this key retrieves its height.
     */
    public static final int METADATA_KEY_VIDEO_HEIGHT    = 19;
    /**
     * This key retrieves the average bitrate (in bits/sec), if available.
     */
    public static final int METADATA_KEY_BITRATE         = 20;
    /**
     * This key retrieves the language code of text tracks, if available.
     * If multiple text tracks present, the return value will look like:
     * "eng:chi"
     * @hide
     */
    public static final int METADATA_KEY_TIMED_TEXT_LANGUAGES      = 21;
    /**
     * If this key exists the media is drm-protected.
     * @hide
     */
    public static final int METADATA_KEY_IS_DRM          = 22;
    /**
     * This key retrieves the location information, if available.
     * The location should be specified according to ISO-6709 standard, under
     * a mp4/3gp box "@xyz". Location with longitude of -90 degrees and latitude
     * of 180 degrees will be retrieved as "-90.0000+180.0000", for instance.
     */
    public static final int METADATA_KEY_LOCATION        = 23;
    
    /**
     * Constant used to indicate the dimension of micro thumbnail.
     */
    public static final int TARGET_SIZE_MICRO_THUMBNAIL = 96;

    // Add more here...
}
