package com.video.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;

public class SignatureUtil {
	public static final String TAG = "SignatureUtil";

	public static String getMD5(String message) {
		if (message == null) {
			return "";
		}
		return getMD5(message.getBytes());
	}

	public static String getMD5(byte[] bytes) {
		if (bytes == null) {
			return "";
		}
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.update(bytes);
			return IOUtil.byteArray2HexString(algorithm.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getMD5(File file) {
		if (file == null) {
			return "";
		}
		try {
			return getMD5(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getMD5(InputStream is) {
		if (is == null) {
			return "";
		}
		try {
			byte[] buffer = new byte[1024];
			MessageDigest digest = MessageDigest.getInstance("MD5");
			int numRead = 0;
			while (numRead != -1) {
				numRead = is.read(buffer);
				if (numRead > 0) {
					digest.update(buffer, 0, numRead);
				}
			}
			return IOUtil.byteArray2HexString(digest.digest());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "";
	}

}
