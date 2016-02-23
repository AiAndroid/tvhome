package com.video.utils;

import android.app.DownloadManager;
import android.content.ContentResolver;

import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.ResultReceiver;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.ListView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 反射方法调用系统未公开的API
 * @author ROBIN.LIU
 *
 */
public class WLReflect {
	
	public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;//PackageManager隐藏的常量
	public static void installPackage(PackageManager pckManager,
            Uri packageURI, 
            IPackageInstallObserver observer, 
            int flags,
            String installerPackageName) 
            		throws IllegalArgumentException, 
            		IllegalAccessException, 
            		InvocationTargetException, 
            		NoSuchMethodException{
		Class<?>[] arrayOfClass = new Class[4];
	    arrayOfClass[0] = Uri.class;
	    arrayOfClass[1] = IPackageInstallObserver.class;
	    arrayOfClass[2] = int.class;
	    arrayOfClass[3] = String.class;
	    
	    Object[] arrayOfObject = new Object[4];
	    arrayOfObject[0] = packageURI;
	    arrayOfObject[1] = observer;
	    arrayOfObject[2] = flags;
	    arrayOfObject[3] = installerPackageName;
	    
		Method localMethod;
		localMethod = pckManager.getClass().getMethod(
				"installPackage", arrayOfClass);
		localMethod.setAccessible(true);
		localMethod.invoke(pckManager, arrayOfObject);
	}

	public static void unInstallPackage(PackageManager pckManager,
									  IPackageDeleteObserver observer,
									  int flags,
									  String installerPackageName)
			throws IllegalArgumentException,
			IllegalAccessException,
			InvocationTargetException,
			NoSuchMethodException{
		Class<?>[] arrayOfClass = new Class[3];
		arrayOfClass[0] = String.class;
		arrayOfClass[1] = IPackageDeleteObserver.class;
		arrayOfClass[2] = int.class;

		Object[] arrayOfObject = new Object[3];
		arrayOfObject[0] = installerPackageName;
		arrayOfObject[1] = observer;
		arrayOfObject[2] = flags;

		Method localMethod;
		localMethod = pckManager.getClass().getMethod(
				"deletePackage", arrayOfClass);
		localMethod.setAccessible(true);
		localMethod.invoke(pckManager, arrayOfObject);
	}

	public static String getSystemProperties(String key){
		try {
			Class osSystem = Class.forName("android.os.SystemProperties");
			Method getInvoke = osSystem.getMethod("get", new Class[]{String.class});
			return  (String) getInvoke.invoke(osSystem,  new Object[]{key});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return  "";
	}

	public static String getSystemProperties(String key, String def){
		String defaultStr = def;
		try {
			Class osSystem = Class.forName("android.os.SystemProperties");
			Method getInvoke = osSystem.getMethod("get", new Class[]{String.class});
			defaultStr =  (String) getInvoke.invoke(osSystem,  new Object[]{key});
			if(defaultStr == null)
				defaultStr = def;

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return  defaultStr;
	}

	public static boolean getSystemPropertiesBoolean(String key, boolean def){
		Boolean defaultres = def;
		try {
			Class osSystem = Class.forName("android.os.SystemProperties");
            Method getInvoke = osSystem.getMethod("getBoolean", String.class, Boolean.class);
            defaultres =  (Boolean) getInvoke.invoke(osSystem,  key, def);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return defaultres;
	}

    public static long getSystemPropertiesLong(String key, long def){
        long defaultres = def;
        try {
            Class osSystem = Class.forName("android.os.SystemProperties");
            Method getInvoke = osSystem.getMethod("getLong", String.class, Long.class);
            defaultres =  (Long) getInvoke.invoke(osSystem,  key, def);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return defaultres;
    }

    public static int getSystemPropertiesInt(String key, int def){
        int defaultres = def;
        try {
            Class osSystem = Class.forName("android.os.SystemProperties");
            Method getInvoke = osSystem.getMethod("getInt", String.class, Integer.class);
            defaultres =  (Integer) getInvoke.invoke(osSystem,  key, def);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return defaultres;
    }

    public static void setSystemProperties(String key, String def){
        try {
            Class osSystem = Class.forName("android.os.SystemProperties");
            Method getInvoke = osSystem.getMethod("set", String.class, String.class);
            getInvoke.invoke(osSystem,  key, def);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }


    public static void setDefaultExecutor() {
		Method localMethod;
		Class<?> sp;
		try {
			sp = Class.forName("android.os.AsyncTask");
			localMethod = sp.getMethod("setDefaultExecutor", Executor.class);
			localMethod.setAccessible(true);
			Field f = sp.getField("THREAD_POOL_EXECUTOR");
			f.setAccessible(true);
			Executor exec = (Executor)f.get(null);//静态变量?
			localMethod.invoke(null, exec);//静态方法为null
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将输入法界面调出来
	 * @param inputManager
	 * @param flags
	 * @param resultReceiver
	 */
	public static void showSoftInputUnchecked(InputMethodManager inputManager,
			int flags, ResultReceiver resultReceiver){
		Class<?>[] arrayOfClass = new Class[2];
	    arrayOfClass[0] = int.class;
	    arrayOfClass[1] = ResultReceiver.class;
	    
	    Object[] arrayOfObject = new Object[2];
	    arrayOfObject[0] = 0;
	    arrayOfObject[1] = resultReceiver;
	    
		Method localMethod;
		try {
			localMethod = inputManager.getClass().getMethod(
					"showSoftInputUnchecked", arrayOfClass);
			localMethod.setAccessible(true);
			localMethod.invoke(inputManager, arrayOfObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param view
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @return
	 */
	public static boolean setFrame(View view, int left, int top, int right, int bottom) {
		Class<?>[] arrayOfClass = new Class[4];
	    arrayOfClass[0] = int.class;
	    arrayOfClass[1] = int.class;
	    arrayOfClass[2] = int.class;
	    arrayOfClass[3] = int.class;
	    
	    Object[] arrayOfObject = new Object[4];
	    arrayOfObject[0] = left;
	    arrayOfObject[1] = top;
	    arrayOfObject[2] = right;
	    arrayOfObject[3] = bottom;
	    
	    Method localMethod;
	    boolean ret = false;
		try {
			Class<?> cls = Class.forName("android.view.View");
			localMethod = cls.getDeclaredMethod(
					"setFrame", arrayOfClass);
			localMethod.setAccessible(true);
			ret = (Boolean)localMethod.invoke(view, arrayOfObject);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void setViewPrivateVar(View view, 
			String fieldName, int value){
		try {
			Class<?> cls = Class.forName("android.view.View");
			Field f = cls.getDeclaredField(fieldName);
			f.setAccessible(true);
			f.set(view, value);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	
	public static int getViewPrivateVar(View view, 
			String fieldName){
		try {
			Class<?> cls = Class.forName("android.view.View");
			Field f = cls.getDeclaredField(fieldName);
			f.setAccessible(true);
			return (Integer)f.get(view);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 
	 * @param listView
	 * @param position
	 * @param offset
	 */
	public static void smoothScrollToPositionFromTop(ListView listView,
			int position, int offset){
		Class<?>[] arrayOfClass = new Class[2];
	    arrayOfClass[0] = int.class;
	    arrayOfClass[1] = int.class;
	    
	    Object[] arrayOfObject = new Object[2];
	    arrayOfObject[0] = position;
	    arrayOfObject[1] = offset;
		try {
			Method localMethod = listView.getClass().getMethod(
					"smoothScrollToPositionFromTop", 
					arrayOfClass);
			localMethod.invoke(listView, arrayOfObject);
		} catch (Exception e) {e.printStackTrace();
		}
	}
	
	/**
	 * 清除selector区域
	 * @param listView
	 */
	public static void clearSelector(AbsListView listView){
		Class<?> cls;
		try {
			cls = Class.forName("android.widget.AbsListView");
			Field f = cls.getDeclaredField("mSelectorRect");
			f.setAccessible(true);
			Rect rect = new Rect(0, 0, 0, 0);
			f.set(listView, rect);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void setCalendarViewShown(DatePicker d, boolean shown){
		Method localMethod;
		Class<?> sp;
		try {
			sp = Class.forName("android.widget.DatePicker");
			localMethod = sp.getMethod("setCalendarViewShown", boolean.class);
			localMethod.setAccessible(true);
			localMethod.invoke(d, shown);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	public static void setSpinnersShown(DatePicker d, boolean shown){
		Method localMethod;
		Class<?> sp;
		try {
			sp = Class.forName("android.widget.DatePicker");
			localMethod = sp.getMethod("setSpinnersShown", boolean.class);
			localMethod.setAccessible(true);
			localMethod.invoke(d, shown);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 其构造函数是HIDE 没办法只能用反射了
	 * @param resolver
	 * @param packageName
	 * @return
	 */
	public static DownloadManager createDownloadManager(ContentResolver resolver, 
			String packageName){
		DownloadManager downloadManager = null;
		try {
			Constructor<DownloadManager> construct = DownloadManager.class
					.getConstructor(ContentResolver.class, String.class);
			if (construct != null) {
				downloadManager = construct.newInstance(resolver, packageName);
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		return downloadManager;
	}
	
	public static int getRawWidth(Display display){
		int width = 0;
		try {
			Method m = display.getClass().getMethod("getRawWidth");
			width = (Integer)m.invoke(display);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return width;
	}
	
	public static int getRawHeight(Display display){
		int height = 0;
		try {
			Method m = display.getClass().getMethod("getRawHeight");
			height = (Integer)m.invoke(display);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return height;
	}

    public static Bitmap screenshot(int width, int height)
    {
    	Bitmap bitmap = null;
    	try {
    		Class<?>[] arrayOfClass = new Class[2];
    	    arrayOfClass[0] = int.class;
    	    arrayOfClass[1] = int.class;
    	    
    	    Object[] arrayOfObject = new Object[2];
    	    arrayOfObject[0] = width;
    	    arrayOfObject[1] = height;
    	    
			Class<?> surface = Class.forName("android.view.Surface");
			Method m = surface.getMethod("screenshot", arrayOfClass);
			bitmap = (Bitmap)m.invoke(null, arrayOfObject);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
    	
    	return bitmap;
    }
    
	/**
	 * 在miui v5系统中，(DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE); 返回的就是
	 * MiuiDownloadManager，但考虑到单发版的移植，故使用了反射的方法实现暂停下载。
	 * @param dm
	 * @param ids
	 */
	public static void pauseDownload(DownloadManager dm ,long[] ids){
		try {
			if(!dm.getClass().getName().equalsIgnoreCase("android.app.MiuiDownloadManager")){
				return;
			}
			Class<?> miuidownlaod = Class.forName("android.app.MiuiDownloadManager");
			Method pauseDownload = miuidownlaod.getMethod("pauseDownload", ids.getClass());
			pauseDownload.setAccessible(true);
			pauseDownload.invoke(dm, ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 在miui v5系统中，(DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE); 返回的就是
	 * MiuiDownloadManager，但考虑到单反版的移植，故使用了反射的方法实现恢复下载。
	 * @param dm
	 * @param ids
	 */
	public static void resumeDownload(DownloadManager dm ,long[] ids){
		try {
			if(!dm.getClass().getName().equalsIgnoreCase("android.app.MiuiDownloadManager")){
				return;
			}
			Class<?> miuidownlaod = Class.forName("android.app.MiuiDownloadManager");
			Method resumeDownload = miuidownlaod.getMethod("resumeDownload", ids.getClass());
			resumeDownload.setAccessible(true);
			resumeDownload.invoke(dm, ids);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
