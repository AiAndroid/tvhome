/**
 * IOUtil.java
 * 
 * @author zzc(zhangchao@xiaomi.com)
 * 
 */

package com.video.utils;

import android.util.Log;

import com.video.ui.Util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class IOUtil {
	public static final String TAG = "IOUtil";

	// all java platforms support these charsets
	public static final String CHARSET_NAME_US_ASCII = "US-ASCII";
	public static final String CHARSET_NAME_UTF_8 = "UTF-8";
	public static final String CHARSET_NAME_UTF_16 = "UTF-16";
	public static final String CHARSET_NAME_UTF_16LE = "UTF-16LE";
	public static final String CHARSET_NAME_UTF_16BE = "UTF-16BE";
	public static final String CHARSET_NAME_ISO_8859_1 = "ISO-8859-1";

	public static byte[] inputStream2ByteArray(InputStream is)
			throws IOException {
		return inputStream2ByteArray(is, 1024);
	}

	public static byte[] inputStream2ByteArray(InputStream is,
			int bufferSize) throws IOException {
		if (null == is) {
			return null;
		}
		if (bufferSize < 1) {
			bufferSize = 1;
		}
		ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

		// this is storage overwritten on each iteration with bytes
		byte[] buffer = new byte[bufferSize];

		// we need to know how may bytes were read to write them to the
		// byteBuffer
		int len = 0;
		while ((len = is.read(buffer)) != -1) {
			byteBuffer.write(buffer, 0, len);
		}

		byteBuffer.close();
		is.close();

		// and then we can return your byte array.
		return byteBuffer.toByteArray();
	}

	public static String byteArray2HexString(byte[] bytes) {
		if (null == bytes) {
			return "";
		}
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

//	public static boolean unzip(String inputPath, String outputDir) {
//		Log.d(TAG, "inputPath: " + inputPath + ", outputDir: " + outputDir);
//		if (null == inputPath || null == outputDir) {
//			return false;
//		}
//        File file = new File(outputDir);
//        if (!file.exists()) {
//            file.mkdirs();  
//        }
//        ZipInputStream zis = null;
//        BufferedOutputStream bos = null;
//        try {
//            zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(inputPath)));
//            byte[] buffer = new byte[1024];
//            int count = 0;
//            ZipEntry entry = null;
//            while ((entry = zis.getNextEntry()) != null) {
//                if (!entry.isDirectory()) {
//                    file = new File(outputDir + File.separator + entry.getName());
//                    Log.d(TAG, "file path: " + file.getAbsolutePath());
//                    if (file.exists()) {
//						file.delete();
//					}
//                    file.createNewFile();
//                    bos = new BufferedOutputStream(new FileOutputStream(file));
//                    while ((count = zis.read(buffer)) > 0) {
//                        bos.write(buffer, 0, count);
//                    }
//                    bos.flush();
//                    bos.close();
//                }
//            }
//            return true;
//		} catch (FileNotFoundException e) {
//			Log.e(TAG, "unzip file: " + inputPath + " failed: FileNotFoundException");
//			e.printStackTrace();
//		} catch (IOException e) {
//			Log.e(TAG, "unzip file: " + inputPath + " failed: IOException");
//			e.printStackTrace();
//		} catch (Exception e) {
//			Log.e(TAG, "unzip file: " + inputPath + " failed: Exception");
//			e.printStackTrace();
//		} finally {
//			try {
//				if (zis != null) {
//					zis.close();
//				}
//				if (bos != null) {
//					bos.close();
//				}
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//        return false;
//    }
	
	private static void dirChecker(String rootDir, String dir) { 
	    File f = new File(rootDir + "/" + dir); 
	    if(!f.isDirectory()) { 
	        f.mkdirs(); 
	    } 
	}
	  
	   /**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
  * @throws IOException 
  * @throws ZipException 
     * @throws Exception
 */
     public static boolean unZip(InputStream is, String folderPath){
         ZipInputStream zip = null;
         ZipEntry ze = null;
         try{
             byte data[] = new byte[1024];
             zip = new ZipInputStream(is);
             while ((ze = zip.getNextEntry()) != null) {
                 if(ze.isDirectory()) { 
                     dirChecker(folderPath, ze.getName());
                 } else { 
                     FileOutputStream fout = new FileOutputStream(folderPath + ze.getName());
                     int count = 0;
                     while ((count = zip.read(data, 0,  1024)) != -1) {
                         fout.write(data, 0, count);
                     }
                     zip.closeEntry(); 
                     fout.close(); 
                 } 
             } 
             zip.close(); 
             return true;
         }catch(Exception e){
             return false;
         }finally{
             if(zip != null){
                 try {
                    zip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
             }
             Util.closeSafely(is);
         }
     }
     
     /**
      * 解压缩功能.
      * 将zipFile文件解压到folderPath目录下.
      */
      public static boolean unZip(File zipFile, String folderPath){
          ZipFile zfile = null;
          FileOutputStream fos = null;
          ZipEntry ze = null;
          try{
              zfile = new ZipFile(zipFile);
              Enumeration<? extends ZipEntry> zList = zfile.entries();
              byte[] buf = new byte[1024];
              while(zList.hasMoreElements()){
                  ze = (ZipEntry)zList.nextElement();    
                  if(ze.isDirectory()){
                      dirChecker(folderPath, ze.getName());
                  }else{
                      Log.d(TAG, "ze.getName() = " + ze.getName());
                      fos = new FileOutputStream(folderPath + ze.getName());
                      InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                      int readLen = 0;
                      while ((readLen = is.read(buf, 0, 1024)) != -1) {
                          fos.write(buf, 0, readLen);
                      }
                      Util.closeSafely(is);
                      Util.closeSafely(fos);
                  }
              }
              return true;
          }catch(Exception e){
              e.printStackTrace();
          }finally{
              if(zfile != null){
                  try {
                    zfile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
              }
          }
          return false;
      }

	/**
	    * 解压缩功能.
	    * 将zipFile文件解压到folderPath目录下.
	 * @throws IOException 
	 * @throws ZipException 
	    * @throws Exception
	*/
	    public static boolean upZipFile(File zipFile, String folderPath) throws ZipException, IOException{
	        ZipFile zfile = new ZipFile(zipFile);
	        Enumeration<? extends ZipEntry> zList = zfile.entries();
	        ZipEntry ze = null;
	        byte[] buf = new byte[1024];
	        while(zList.hasMoreElements()){
	            ze = (ZipEntry)zList.nextElement();    
	            if(ze.isDirectory()){
	                continue;
	            }
	            Log.d(TAG, "ze.getName() = " + ze.getName());
                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
	            InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
	            int readLen = 0;
	            while ((readLen = is.read(buf, 0, 1024)) != -1) {
	                os.write(buf, 0, readLen);
	            }
	            is.close();
	            os.close();    
	        }
	        zfile.close();
	        return true;
	    }

	    /**
	    * 给定根目录，返回一个相对路径所对应的实际文件名.
	    * @param baseDir 指定根目录
	    * @param absFileName 相对路径名，来自于ZipEntry中的name
	    * @return java.io.File 实际的文件
	*/
	    public static File getRealFileName(String baseDir, String absFileName){
	        String[] dirs = absFileName.split("/");
	        File ret = new File(baseDir);
	        String substr = null;
	        if(dirs.length > 1){
	            for (int i = 0; i < dirs.length - 1; i++) {
	                substr = dirs[i];
	                try {
	                    substr = new String(substr.getBytes("8859_1"), "GB2312");
	                } catch (UnsupportedEncodingException e) {
	                    e.printStackTrace();
	                }
	                ret = new File(ret, substr);
	                
	            }
	            Log.d(TAG, "1ret = "+ret);
	            if(!ret.exists())
	                ret.mkdirs();
	            substr = dirs[dirs.length-1];
	            try {
	                substr = new String(substr.getBytes("8859_1"), "GB2312");
	                Log.d(TAG, "substr = "+substr);
	            } catch (UnsupportedEncodingException e) {
	                e.printStackTrace();
	            }
	            
	            ret = new File(ret, substr);
	            Log.d(TAG, "2ret = "+ret);
	            return ret;
	        }
            // a single file name without relatvie path,
            try {
                substr = new String(absFileName.getBytes("8859_1"), "GB2312");
            } catch  (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ret = new File(ret, substr);
	        return ret;
	    }

	public static boolean copy(String fromFile, String toFile){
		InputStream in = null;
		OutputStream out = null;

		try{
			in = new FileInputStream(new File(fromFile));
			out = new FileOutputStream(new File(toFile));
			copy(in,out);
		}catch (Exception e){
			return false;
		}finally {
			if(in != null) try {in.close();} catch (Exception e){}
			if(out != null) try {out.close();} catch (Exception e){};
		}
		return true;
	}

	public static long copy(InputStream from, OutputStream to) throws IOException {
		byte[] buf = new byte[4096];
		long total = 0L;

		while(true) {
			int r = from.read(buf);
			if(r == -1) {
				return total;
			}

			to.write(buf, 0, r);
			total += (long)r;
		}
	}

	public static void delete(File file){
		try{
			if(file.exists()) file.delete();
		}catch (Exception e){}
	}
}
