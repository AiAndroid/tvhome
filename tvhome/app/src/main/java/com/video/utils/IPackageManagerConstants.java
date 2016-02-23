package com.video.utils;

/**
 * Framework中隐藏的变量
 * @author ROBIN.LIU
 *
 */
public interface IPackageManagerConstants {
	/**
	 * from android.content.pm.PackageManager 的Hide变量
	 */
    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} on success.
     * 
     */
	int INSTALL_SUCCEEDED = 1;
	
	   /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} if the package manager
     * service found that the device didn't have enough storage space to install the app.
     * @hide
     */
    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;
	
    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} if
     * the new package uses a shared library that is not available.
     * @hide
     */
    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;
    
    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} if
     * a secure container mount point couldn't be accessed on external media.
     * @hide
     */
    public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;
	
    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} if
     * the new package couldn't be installed in the specified install
     * location.
     * @hide
     */
    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;
	
    /**
     * Installation return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} if
     * the new package couldn't be installed in the specified install
     * location because the media is not available.
     * @hide
     */
    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;
    
    /** 
     * if the new package couldn't be installed because the verification timed out. 
     * 验证超时 
     */  
    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;  
      
      
    /** 
     * if the new package couldn't be installed because the verification did not succeed. 
     * 验证失败 
     */  
    public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22; 
    
	/**
	 * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
	 * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
	 * if the parser found inconsistent certificates on the files in the .apk.
	 * 
	 */
	int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;
	
	/**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser was given a path that is not a file, or does not end with the expected
     * '.apk' extension.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser was unable to retrieve the AndroidManifest.xml file.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser encountered an unexpected exception.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser did not find any certificates in the .apk.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser encountered a CertificateEncodingException in one of the
     * files in the .apk.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser encountered a bad or missing package name in the manifest.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser encountered a bad shared user id name in the manifest.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser encountered some structural problem in the manifest.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;

    /**
     * Installation parse return code: this is passed to the {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)}
     * if the parser did not find any actionable tags (instrumentation or application)
     * in the manifest.
     * @hide
     */
    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;
    /**
     * Flag parameter for {@link #installPackage} to indicate that you want to replace an already
     * installed package, if one exists.
     * @hide
     */
	int INSTALL_REPLACE_EXISTING = 0x00000002;
}
