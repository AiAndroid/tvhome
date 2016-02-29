package com.mothership.tvhome.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.tv.ui.metro.model.Constants;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public final class ViewUtils {
    //release all image resource from view
	public static void unbindDrawables(View view) {
		if (null == view) {
			return;
		}
		if (view.getBackground() != null) {
			view.getBackground().setCallback(null);
            if(Build.VERSION.SDK_INT >= 16)
			    view.setBackground(null);
            else
                view.setBackgroundDrawable(null);
		}
		if(view instanceof ImageView){
			ImageView imageView = (ImageView)view;
			
			if(imageView.getDrawable() != null){
				imageView.getDrawable().setCallback(null);
				imageView.setImageDrawable(null);
			}
		}
		if (view instanceof ViewGroup) {
			for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
				unbindDrawables(((ViewGroup) view).getChildAt(i));
			}
			if (!(view instanceof AdapterView<?>)) {
				((ViewGroup) view).removeAllViews();
			}
		}
	}

    public static void unbindImageDrawables(View view) {
        if (null == view) {
            return;
        }

        if(view instanceof ImageView){
            ImageView imageView = (ImageView)view;

            if(imageView.getDrawable() != null){
                imageView.getDrawable().setCallback(null);
                imageView.setImageDrawable(null);
            }
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindImageDrawables(((ViewGroup) view).getChildAt(i));
            }
        }
    }

    static int screenHeight = -1;
    static int screenWidth  = -1;
    static int[] location = new int[2];
    public static boolean isInVisisble(View view){
        if(screenHeight == -1){
            screenHeight = view.getContext().getResources().getDisplayMetrics().heightPixels;
            screenWidth  = view.getContext().getResources().getDisplayMetrics().widthPixels;
        }

        view.getLocationOnScreen(location);
        int left  = location[0];
        int top  = location[1];
        int height = view.getHeight();

        //in top
        if((top + height) < -0.5*screenHeight){
            return true;
        }

        //in bottom
        if(top > screenHeight + 0.5*screenHeight){
            return true;
        }

        return false;
    }

    public static boolean isRealInVisisble(View view){
        if(screenHeight == -1){
            screenHeight = view.getContext().getResources().getDisplayMetrics().heightPixels;
            screenWidth  = view.getContext().getResources().getDisplayMetrics().widthPixels;
        }

        view.getLocationOnScreen(location);
        int left  = location[0];
        int top  = location[1];
        int height = view.getHeight();

        //in top 20% visibility
        if(((top > -(4.0*height)/5.0f) && (top + height/5.0f) < screenHeight) && (left  < (screenWidth + 30) && left > -screenWidth/2)){
            if(Constants.DEBUG){
                Log.d("ViewUtils", "isRealInVisisble :"+top + " left:"+left + " view: "+view);
            }
            return true;
        }

        return false;
    }

    public static boolean isMeetBottom(View view){
        if(screenHeight == -1){
            screenHeight = view.getContext().getResources().getDisplayMetrics().heightPixels;
            screenWidth  = view.getContext().getResources().getDisplayMetrics().widthPixels;
        }

        view.getLocationOnScreen(location);
        int left  = location[0];
        int top  = location[1];
        int height = view.getHeight();

        //in top
        if((top < 0 && (Math.abs(top) + screenHeight + 20 > height)) || (top > 0 && top + height + 20 < screenHeight)){
            if(Constants.DEBUG){
                Log.d("ViewUtils", "isMeetBottom :"+top + " view: "+view);
            }
            return true;
        }

        return false;
    }

    public static void unbindInvisibleImageDrawables(View view) {
        if (null == view) {
            return;
        }

        if(view instanceof ImageView){
            ImageView imageView = (ImageView)view;

            if(imageView.getDrawable() != null && isInVisisble(imageView)){
                imageView.getDrawable().setCallback(null);
                imageView.setImageDrawable(null);
            }
        }
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindInvisibleImageDrawables(((ViewGroup) view).getChildAt(i));
            }
        }
    }

	private ViewUtils() {
	}

    //large than 3G
    static float memsize = -1.0f;
    public static boolean LargerMemoryMode(Context context) {
        if(true) {
            if (memsize == -1.0f) {
                memsize = getMemeorySize(context);
                Log.d("memory", "size = " + memsize);
            }
            return memsize >= 2.75f;//1.75, just for x7
        }

        return true;
    }

    //large than 3G
    public static boolean smallMemoryMode(Context context) {
        if(true) {
            if (memsize == -1.0f) {
                memsize = getMemeorySize(context);
                Log.d("memory", "size = " + memsize);
            }
            return memsize < 0.9f;//1.75, just for x7
        }

        return true;
    }

    private static  float getMemeorySize(Context context){
        if(Build.VERSION.SDK_INT >=16) {
            ActivityManager actManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
            actManager.getMemoryInfo(memInfo);
            float totalMemory = (memInfo.totalMem / 1073741824.0f);
            return totalMemory;
        }else {
            return getTotalRAM();
        }
    }

    public static float getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        float lastValue= 0;
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            float mb = (float) (totRam / 1024.0f);
            float gb = (float) (totRam / 1048576.0f);
            float tb = (float) (totRam / 1073741824.0f);

            lastValue = gb;
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            // Streams.close(reader);
        }

        return lastValue;
    }
}
