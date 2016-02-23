package com.video.ui;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public final class Util {

    public static String formatFloat(float t) {
        try {
            return String.format(Locale.US, "%.1f", t);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0";
    }

    public static int parseInt(String value){
        int ret = 0;
        try{
            ret = Integer.parseInt(value);
        }catch(Exception e){
        }
        return ret;
    }

    public static List<String> string2List(String str, String splitter) {
        List<String> list = new ArrayList<String>();
        string2List(str, splitter, list);
        return list;
    }

    public static void string2List(String str, String splitter,
            List<String> list) {
        if (!str.equals("")) {
            String[] arr = str.split(splitter);
            for (int i = 0; i < arr.length; i++) {
                list.add(arr[i]);
            }
        }
    }

    public static String list2String(List<String> list, String splitter) {
        StringBuilder sb = new StringBuilder();
        int len = list.size();
        for (int i = 0; i < len; i++) {
            sb.append(list.get(i));
            if (i != len - 1) {
                sb.append(splitter);
            }
        }
        return sb.toString();
    }

    public static int[] integerSetToInt(Set<Integer>  integerSet) {
        if( integerSet == null) 
            return new int[0];

        int i = 0;
        int[] intArrays = new int[integerSet.size()];
        for(Iterator<Integer> Itr = integerSet.iterator(); Itr.hasNext();) {
            intArrays[i] = Itr.next().intValue();
            i++;
        }		
        return intArrays;
    }

    public static String msTime2Date(long msTime) {
        Date date = new Date(msTime);
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        String str = dateFormate.format(date);
        return str;
    }

    public static boolean isToday(String dateStr) {
        long curTime = System.currentTimeMillis();
        Date date = new Date(curTime);
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        String str = dateFormate.format(date);
        if(!isEmpty(dateStr) && dateStr.equals(str)) {
            return true;
        }
        return false;
    }

    public static boolean isYesterday(String dateStr) {
        long curTime = System.currentTimeMillis();
        long yesTime = curTime - 24 * 60 * 60 * 1000;
        Date date = new Date(yesTime);
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
        String str = dateFormate.format(date);
        if(!isEmpty(dateStr) && dateStr.equals(str)) {
            return true;
        }
        return false;
    }

    public static Bitmap getBmpByResId(Context context, int id) {
        InputStream isBackground = context.getResources().openRawResource(id);
        return BitmapFactory.decodeStream(isBackground);
    }

    public static boolean fileExists(Context context, String mediaUrl){
        if(!Util.isEmpty(mediaUrl)){
            Uri uri = Uri.parse(mediaUrl);
            String filename = null;
            if(!Util.isEmpty(uri.getScheme()) && 
                    uri.getScheme().equals("content")){
                filename = getRealFilePathFromContentUri(context, uri);
            }else{
                filename = mediaUrl.replace("file://", "");
            }
            if(!Util.isEmpty(filename)){
                File file = new File(filename);
                return file.exists();
            }
        }
        return false;
    }

    private static String getRealFilePathFromContentUri(Context context, Uri contentUri) {
        try{
            String[] columns = new String[]{MediaStore.Video.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, columns, null, null, null);
            if (cursor == null) {
                return null;
            }
            int index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            String result = cursor.getString(index);
            cursor.close();
            return result;
        }catch (Exception e) {
            return null;
        }
    }

    public static void delDir(File dir) {
        try {
            if(dir != null) {
                delAllFiles(dir);
                dir.delete();
            }
        } catch (Exception e) {
        }
    }

    public static void delDir(String dirFullName) {
        delDir(new File(dirFullName));
    }

    public static void delAllFiles(File dir) {
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        String dirFullName = dir.getAbsolutePath();

        String[] fileList = dir.list();
        File tempFile = null;
        for (int i = 0; i < fileList.length; i++) {
            if (dirFullName.endsWith(File.separator)) {
                tempFile = new File(dirFullName + fileList[i]);
            } else {
                tempFile = new File(dirFullName + File.separator + fileList[i]);
            }
            if (tempFile.isFile()) {
                tempFile.delete();
            }
            if (tempFile.isDirectory()) {
                delAllFiles(dirFullName + "/" + fileList[i]);
                delDir(dirFullName + "/" + fileList[i]);
            }
        }
    }

    public static void delAllFiles(String dirFullName) {
        delAllFiles(new File(dirFullName));
    }
    
    public static void showInputMethodWindow(Activity activity, View view){
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        } catch (Throwable e) {
        }
    }

    public static void closeInputMethodWindow(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Throwable e) {
        }
    }

    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static boolean hasExternalXDir() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static boolean isEmpty(CharSequence string) {
        return string == null || string.length() == 0;
    }

    public static byte[] readInputStream(InputStream is) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        final int blockSize = 8192;
        byte[] buffer = new byte[blockSize];
        int count = 0;
        try {
            while ((count = is.read(buffer, 0, blockSize)) > 0) {
                byteStream.write(buffer, 0, count);
            }
            return byteStream.toByteArray();
        } catch (Exception e) {
            return null;
        }finally{
            if(is != null){
                try{
                    is.close();
                }catch (Exception e) {
                }
            }
        }
    }

    public static int dp2px(Context context, float dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static String formatScore(double score) {
        boolean isNegative = false;
        if (score < 0) {
            isNegative = true;
            score = -score;
        }
        float k = Math.round(score * 100);
        k = k / 10;
        k = Math.round(k);
        int n = (int) k;
        String number = n + "";
        if (n == 0) {
            return "0.0";
        }
        StringBuilder result = new StringBuilder();
        if (isNegative) {
            result.append("-");
        }
        if (number.length() == 1) {
            result.append("0.");
            result.append(number);
        } else {
            result.append(number.substring(0, number.length() - 1));
            result.append(".");
            result.append(number.charAt(number.length() - 1));
        }
        return result.toString();
    }

    public static <T> T[] concatArray(T[] array1, T[] array2) {
        if (array1 == null || array1.length == 0) {
            return array2;
        }
        if (array2 == null || array2.length == 0) {
            return array1;
        }
        T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }  

    public static String md5sum(String filename){
        try {
            InputStream is = new FileInputStream(filename);
            return md5sum(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public static String md5sum(InputStream is){
            byte[] buffer = new byte[1024];
            int numRead = 0;
            MessageDigest md5;
            try{
                md5 = MessageDigest.getInstance("MD5");
                md5.reset();
                while((numRead = is.read(buffer, 0, 1024)) > 0) {
                    md5.update(buffer,0,numRead);
                }
                return toHexString(md5.digest());   
            } catch (Exception e) {
                return "";
            }finally{
                Util.closeSafely(is);
            }
    }
    
    public static String getMD5(String message) {
        if( message == null)
            return "";

        return getMD5(message.getBytes());
    }

    public static String getMD5(byte[] bytes)
    {
        if( bytes == null)
            return "";

        String digest = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            digest = toHexString(algorithm.digest());
        } catch (Exception e) {
        }
        return digest;
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String str = Integer.toHexString(0xFF & b);
            while (str.length() < 2) {
                str = "0" + str;
            }
            hexString.append(str);
        }
        return hexString.toString();
    }

//
//    public static long getSDAvailaleSize() {
//        File path = Environment.getExternalStorageDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSize();
//        long availableBlocks = stat.getAvailableBlocks();
//        Log.d("test", "getSDAvailaleSize " + availableBlocks * blockSize);
//        return availableBlocks * blockSize;
//    }
//
//    public static long getSDAllSize() {
//        File path = Environment.getExternalStorageDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSize();
//        long availableBlocks = stat.getBlockCount();
//        Log.d("test", "getSDAllSize " + availableBlocks * blockSize);
//        return availableBlocks * blockSize;
//    }

    public static String convertToFormateSize(long size){
        if(size > 1024){
            long sizeInKB = size / 1024;
            if(sizeInKB > 1024){
                long sizeInMB = sizeInKB / 1024;
                if(sizeInMB > 1024){
                    long sizeInGB = sizeInMB / 1024;
                    sizeInMB = (long) (sizeInMB % 1024 / 102.4);
                    return sizeInGB + (sizeInMB == 0 ? "" : "." + sizeInMB) + "GB";
                }
                sizeInKB = (long) (sizeInKB % 1024 / 102.4);
                return sizeInMB + (sizeInKB == 0 ? "" : "." + sizeInKB) + "MB";
            }
            size = (long) (size % 1024 / 102.4);
            return sizeInKB + (size == 0 ? "" : "." + size) + "KB";
        }
        return size + "B";
    }

    public static String convertToFormateSize100M(long size){
        if(size > 1024){
            long sizeInKB = size / 1024;
            if(sizeInKB > 1024){
                long sizeInMB = sizeInKB / 1024;
                if(sizeInMB > 1024){
                    long sizeInGB = sizeInMB / 1024;
                    sizeInMB = (long) (sizeInMB % 1024 / 102.4);
                    return sizeInGB + (sizeInMB == 0 ? "" : "." + sizeInMB) + "GB";
                }

                return (sizeInMB/50)*50 + "MB";
            }
            return "<1MB";
        }
        return "<1MB";
    }


    public static <T> T[] list2Array(List<T> list, Class<T> clazz){
        if(list != null){
            @SuppressWarnings("unchecked")
            T[] array = (T[]) Array.newInstance(clazz, list.size());
            for(int i = 0; i  < list.size(); i++){
                array[i] = list.get(i);
            }
            return array;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static public <T> T dynamicCast(Object object, Class<T> clazz){
        if(object != null && clazz != null && object.getClass().equals(clazz)){
            return (T)object;
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    static public <T> T dynamicCast(Object object){
        if(object != null){
            try{
                return (T)object;
            }catch(Exception e){
            }
        }
        return null;
    }

    public static void closeSafely(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void disconnectSafely(HttpURLConnection connect){
        if(connect != null){
            try {
                connect.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
