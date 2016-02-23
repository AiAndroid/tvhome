package com.video.ui.loader;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import com.tv.ui.metro.model.PlaySource;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Utils {
	
	public static boolean DEBUG = true;
	public static final int LARGE_NUMBER_BASE = 100000;
    public static boolean isConnected(Context context) {
        ConnectivityManager connManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

	public static String getStringMD5(String key){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md5.update(key.getBytes());
		//important: use Base64.URL_SAFE flag to avoid "+" and "/"
		return new String(Base64.encode(md5.digest(), Base64.URL_SAFE));
	}
	
	public static String getCacheFolder(Context context) {
		File cacheFolder = new File(context.getCacheDir().getAbsolutePath() + File.separator + "app_icons");
		if (!cacheFolder.exists()) {
			cacheFolder.mkdir();
		}
		return cacheFolder.getAbsolutePath();
	}
    
	public static String getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
		SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signingKey);
		byte[] rawHmac = mac.doFinal(data);
		return byte2HexStr(rawHmac);
	}
	
    public static String byte2HexStr(byte[] b)  
    {  
        String stmp="";  
        StringBuilder sb = new StringBuilder("");  
        for (int n=0;n<b.length;n++)  
        {  
            stmp = Integer.toHexString(b[n] & 0xFF);  
            sb.append((stmp.length()==1)? "0"+stmp : stmp);
        }  
        return sb.toString().toLowerCase().trim();  
    }
    
	public static Integer getKeyByValue(Map<Integer, String> map, Object value) {
		Integer key = -1;
		Iterator<Entry<Integer, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, String> entry = (Entry<Integer, String>) it.next();
			String obj = entry.getValue();
			if (obj != null && obj.equals(value)) {
				// break as find the first key, assuming key and value are one-to-one
				key = (Integer) entry.getKey();
				break;
			}
		}
		return key;
	}

	public static int px2sp(Context context, float pxValue) {  
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);  
	}

    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        return new Point(width, height);
    }

    public static int dpToPx(int dp, Context ctx) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public static boolean isHtml5Source(PlaySource ps) {
        if (ps.h5_preferred) {
            return true;
        } else if (ps.app_info == null) {
            return true;
        }
        return false;
    }
}
