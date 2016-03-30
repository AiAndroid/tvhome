package com.video.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.StatFs;
import android.util.Log;
import android.view.SoundEffectConstants;
import android.view.View;
import android.webkit.MimeTypeMap;

//import com.mitv.video.activity.MainActivity;
//import com.video.common.Action;
import com.video.search.model.HotSR;
import com.video.search.model.Item;
import com.video.search.model.MediaBase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Tools {

    public static final int SOUND_KEYSTONE_KEY = 1;
    public static final int SOUND_ERROR_KEY = 0;
    public static final int LARGE_NUMBER_BASE = 100000;

    public static <E> ArrayList<E> newArrayList() {
        return new ArrayList<E>();
    }

    public static <K, V> HashMap<K, V> newHashMap() {
        return new HashMap<K, V>();
    }

    public static <E> ArrayList<E> newArrayList(E... elements) {
        int capacity = (elements.length * 110) / 100 + 5;
        ArrayList<E> list = new ArrayList<E>(capacity);
        Collections.addAll(list, elements);
        return list;
    }

    public static long readSystemAvailableSize() {
        String path = "/data";
        StatFs sf = new StatFs(path);
        long blockSize = sf.getBlockSize();
        Log.d("block size", "block size: " + blockSize);
        //long blockCount = sf.getBlockCount();
        //Log.d("available count", "available count: " + sf.getAvailableBlocks());
        long availCount = sf.getAvailableBlocks();
        Log.d("available count", "available count: " + availCount);
        return blockSize * availCount / 1024;
    }

    public static void playKeySound(View view, int soundKey) {
        if (null != view) {
            if (soundKey == SOUND_KEYSTONE_KEY) {
                view.playSoundEffect(SoundEffectConstants.NAVIGATION_DOWN);
            } else if (soundKey == SOUND_ERROR_KEY) {
                view.playSoundEffect(5);
            }
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            type = mime.getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String longToDate(long timeMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        return sdf.format(timeMillis);
    }

    public static long dateToLong(String time) {
        return dateToLong(time, "yyyy-MM-dd HH:mm:ss");
    }

    public static long dateToLong(String time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static byte[] readStreamToByteArray(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[512 * 1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static int getMemoryClass(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return am.getMemoryClass();
    }

    public static String getSignature(byte[] data, byte[] key) throws InvalidKeyException, NoSuchAlgorithmException {
        SecretKeySpec signingKey = new SecretKeySpec(key, "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data);
        return byte2HexString(rawHmac);
    }

    public static String byte2HexString(byte[] b) {
        String s;
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            s = Integer.toHexString(b[n] & 0xFF);
            sb.append((s.length() == 1) ? "0" + s : s);
        }
        return sb.toString().toLowerCase().trim();
    }

    public static String largeNumberPattern(int largeNumber) {
        String patternedString = "0";
        if (largeNumber > 0) {
            String unit = null;
            if (largeNumber >= LARGE_NUMBER_BASE) {
                largeNumber /= 10000;
                unit = "万";
            }
            DecimalFormat df = new DecimalFormat("#,###");
            patternedString = df.format(largeNumber);
            if (null != unit) {
                patternedString += unit;
            }
        }
        return patternedString;
    }

    public static boolean containNonEnglishCharacter(String str) {
        char[] charArray = str.toCharArray();
        for (int i = 0; i < charArray.length; i++) {
            if (charArray[i] > 255 || charArray[i] < 0) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Integer> mergeArrays(int[] a, int[] b) {
        int aLength = a.length;
        int bLength = b.length;
        ArrayList<Integer> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < aLength || j < bLength) {
            if (i == aLength && j < bLength) {
                result.add(b[j]);
                j++;
            } else if (i < aLength && j == bLength) {
                result.add(a[i]);
                i++;
            } else {
                if (a[i] <= b[j]) {
                    result.add(a[i]);
                    i++;
                } else if (a[i] > b[j]) {
                    result.add(b[j]);
                    j++;
                }
            }
        }

        return result;
    }

    public static <T> List mergeList(List<T> a, List<T> b, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        if (a == null && b != null) {
            return b;
        } else if(b == null && a != null) {
            return a;
        } else if(a == null && b == null) {
            return result;
        }

        int aLength = a.size();
        int bLength = b.size();
        int i = 0, j = 0;

        while (i < aLength || j < bLength) {
            if (i == aLength && j < bLength) {
                result.add(b.get(j));
                j++;
            } else if (i < aLength && j == bLength) {
                result.add(a.get(i));
                i++;
            } else {
                if (comparator.compare(a.get(i), b.get(j)) <= 0) {
                    result.add(a.get(i));
                    i++;
                } else if (comparator.compare(a.get(i), b.get(j)) > 0) {
                    result.add(b.get(j));
                    j++;
                }
            }
        }

        return result;
    }

    ////////////////////////////////////////////////////////////////////

    public static String toString(List<Item> list, String separator) {
        if (list == null || list.size() == 0)
            return "未知";

        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = list.size(); i < size; i++) {
            sb.append(list.get(i).name);
            if (i == size - 1)
                return sb.toString();
            sb.append(separator);
        }
        return sb.toString();
    }

    public static ArrayList<MediaBase> toArray(HotSR.HotSRItem[] data) {
        ArrayList<MediaBase> list = new ArrayList<>();
        for (int i = 0, size = data.length; i < size; i++) {
            if (!data[i].def) {
                MediaBase[] medias = data[i].medias;
                for (int j = 0, sizeJ = medias.length; j < sizeJ; j++)
                    list.add(medias[j]);
            }
        }
        return list;
    }




    public static String getInnerPackageVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            return "内测版本：" + pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static int getPackageVersion(Context context) {
        return getPackageVersion(context, context.getPackageName());
    }

    public static int getPackageVersion(Context context, String name) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(name, 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static PackageInfo getPackageInfo(Context context, String name) {
        try {
            return context.getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInstalled(Context context, String name) {
        try {
            context.getPackageManager().getPackageInfo(name, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String genSignature(String str, String token, String key) {
        str +=  "&token=" + token;
        String opaque = null;
        try {
            opaque = Tools.getSignature(str.getBytes(), key.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return opaque;
    }


    public static boolean isListEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static int getListLength(List list) {
        return list == null ? 0 : list.size();
    }
}
