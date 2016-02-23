/*
 * Copyright (C) 2013, Xiaomi Inc. All rights reserved.
 */

package miui.os;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import com.video.utils.WLReflect;

/**
 * Information about the current build, extracted from system properties.
 */
public class BuildV5 extends android.os.Build {

    /**
     * Only for extends, please don't instantiate this class.
     * @throws InstantiationException always when this class is instantiated.
     */
    protected BuildV5() throws InstantiationException {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    /** Regular expression for MIUI development version. */
    private static final String REGULAR_EXPRESSION_FOR_DEVELOPMENT = "\\d+.\\d+.\\d+(-internal)?";

    /** Regular expression for MIUI stable version. */
    private static final String REGULAR_EXPRESSION_FOR_STABLE = "([A-Z]{3}|[A-Z]{7})\\d+.\\d+";

    /**
     * Xiaomi serials
     */

    /** Indicates the current device is MI1 WCDMA/CDMA, include MI1 Young Lovers' edition WCDMA/CDMA. */
    public static final boolean IS_MIONE = "mione".equals(DEVICE) || "mione_plus".equals(DEVICE);

    /** Indicates the current device is MI1S WCDMA/CDMA. */
    public static final boolean IS_MI1S = "MI 1S".equals(MODEL) || "MI 1SC".equals(MODEL);

    /** Indicates the current device is MI2 WCDMA/CDMA, include MI2A WCDMA/TDSCDMA. */
    public static final boolean IS_MITWO = "aries".equals(DEVICE) || "taurus".equals(DEVICE) || "taurus_td".equals(DEVICE);

    /** Indicates the current device is MI2A WCDMA/TDSCDMA. */
    public static final boolean IS_MI2A = "MI 2A".equals(MODEL) || "MI 2A TD".equals(MODEL);

    /** Indicates the current device is MI3 WCDMA/CDMA/TDSCDMA. */
    public static final boolean IS_MITHREE = "pisces".equals(DEVICE) || ("cancro".equals(DEVICE) && MODEL.startsWith("MI 3"));

    /** Indicates the current device is MI4. */
    public static final boolean IS_MIFOUR = "cancro".equals(DEVICE) && MODEL.startsWith("MI 4");

    /** Indicates the current device is MI5. */
    public static final boolean IS_MIFIVE = "virgo".equals(DEVICE);

    /** Indicates the current device is Xiaomi serials. */
    public static final boolean IS_XIAOMI = IS_MIONE || IS_MITWO || IS_MITHREE || IS_MIFOUR || IS_MIFIVE;

    /**
     * Pad serials
     */
    /** Indicates the current device is Mi Pad. */
    public static final boolean IS_MIPAD = "mocha".equals(DEVICE);
    /** Indicates the current device is N7. */
    public static final boolean IS_N7 = "flo".equals(DEVICE);

    /**
     * Hongmi serials
     */

    /** Indicates the current device is Hongmi2A WCDMA/CDMA. */
    public static final boolean IS_HONGMI_TWO_A = "armani".equals(DEVICE);

    /** Indicates the current device is Hongmi2S. */
    public static final boolean IS_HONGMI_TWO_S = "HM2014011".equals(DEVICE) || "HM2014012".equals(DEVICE);

    /** Indicates the current device is MTK Hongmi2S LTE. */
    public static final boolean IS_HONGMI_TWOS_LTE_MTK = "HM2014501".equals(DEVICE);

    /** Indicates the current device is Hongmi2 WCDMA/TDSCDMA, include Hongmi2A WCDMA/CDMA, Hongmi2S */
    public static final boolean IS_HONGMI_TWO = "HM2013022".equals(DEVICE) || "HM2013023".equals(DEVICE)
            || IS_HONGMI_TWO_A || IS_HONGMI_TWO_S;

    /** Indicates the current device is Hongmi3. */
    public static final boolean IS_HONGMI_THREE = "lcsh92_wet_jb9".equals(DEVICE) || "lcsh92_wet_tdd".equals(DEVICE);

    /** Indicates the current device is Hongmi3 LTE. */
    public static final boolean IS_HONGMI_THREE_LTE = "dior".equals(DEVICE);

    /** Indicates the current device is Hongmi3 LTE CM. */
    public static final boolean IS_HONGMI_THREE_LTE_CM = IS_HONGMI_THREE_LTE && "LTETD".equals(WLReflect.getSystemProperties("ro.boot.modem"));

    /** Indicates the current device is Hongmi3 LTE CU. */
    public static final boolean IS_HONGMI_THREE_LTE_CU = IS_HONGMI_THREE_LTE && "LTEW".equals(WLReflect.getSystemProperties("ro.boot.modem"));

    /** Indicates the current device is Hongmi2x cu. */
    public static final boolean IS_HONGMI_TWOX_CU = "HM2014811".equals(DEVICE);

    /** Indicates the current device is Hongmi2x ct. */
    public static final boolean IS_HONGMI_TWOX_CT = "HM2014812".equals(DEVICE) || "HM2014821".equals(DEVICE);

    /** Indicates the current device is Hongmi2x cm. */
    public static final boolean IS_HONGMI_TWOX_CM = "HM2014813".equals(DEVICE) || "HM2014112".equals(DEVICE);

    /** Indicates the current device is Hongmi2x in. */
    public static final boolean IS_HONGMI_TWOX_IN = "HM2014818".equals(DEVICE);

    /** Indicates the current device is Hongmi2x sa. */
    public static final boolean IS_HONGMI_TWOX_SA = "HM2014817".equals(DEVICE);

    /** Indicates the current device is Hongmi2x br. */
    public static final boolean IS_HONGMI_TWOX_BR = "HM2014819".equals(DEVICE);

    /** Indicates the current device is Hongmi2x. */
    public static final boolean IS_HONGMI_TWOX = IS_HONGMI_TWOX_CU || IS_HONGMI_TWOX_CT || IS_HONGMI_TWOX_CM || IS_HONGMI_TWOX_IN || IS_HONGMI_TWOX_SA || IS_HONGMI_TWOX_BR;

    /** Indicates the current device is leadcore Hongmi2x. */
    public static final boolean IS_HONGMI_TWOX_LC = "lte26007".equals(DEVICE);

    /** Indicates the current device is Hongmi3x. */
    public static final boolean IS_HONGMI_THREEX = "gucci".equals(DEVICE);

    /** Indicates the current device is Hongmi3x cm. */
    public static final boolean IS_HONGMI_THREEX_CM = IS_HONGMI_THREEX && "cm".equals(WLReflect.getSystemProperties("persist.sys.modem"));

    /** Indicates the current device is Hongmi3x cu. */
    public static final boolean IS_HONGMI_THREEX_CU = IS_HONGMI_THREEX && "cu".equals(WLReflect.getSystemProperties("persist.sys.modem"));

    /** Indicates the current device is Hongmi3x ct. */
    public static final boolean IS_HONGMI_THREEX_CT = IS_HONGMI_THREEX && "ct".equals(WLReflect.getSystemProperties("persist.sys.modem"));


    /** Indicates the current device is Hongmi serials. */
    public static final boolean IS_HONGMI = IS_HONGMI_TWO || IS_HONGMI_THREE || IS_HONGMI_TWOX || IS_HONGMI_THREE_LTE || IS_HONGMI_TWOX_LC || IS_HONGMI_TWOS_LTE_MTK || IS_HONGMI_THREEX;

    /**
     * Modem differentiation
     */

    /** Indicates the current device is MI1 CDMA. */
    public static final boolean IS_MIONE_CDMA = IS_MIONE && hasMsm8660Property();

    /** Indicates the current device is MI2 CDMA. */
    public static final boolean IS_MITWO_CDMA = IS_MITWO && "CDMA".equals(
            WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI3 CDMA. */
    public static final boolean IS_MITHREE_CDMA = IS_MITHREE && "MI 3C".equals(MODEL);

    /** Indicates the current device is MI4 CDMA. */
    public static final boolean IS_MIFOUR_CDMA = IS_MIFOUR && "CDMA".equals(
            WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI2 TDSCDMA. */
    public static final boolean IS_MITWO_TDSCDMA = IS_MITWO && "TD".equals(
            WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI3 TDSCDMA. */
    public static final boolean IS_MITHREE_TDSCDMA = IS_MITHREE && "TD".equals(
            WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI4 LTE CM. */
    public static final boolean IS_MIFOUR_LTE_CM = IS_MIFOUR
            && "LTE-CMCC".equals(WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI4 LTE CU. */
    public static final boolean IS_MIFOUR_LTE_CU = IS_MIFOUR
            && "LTE-CU".equals(WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI4 LTE CT. */
    public static final boolean IS_MIFOUR_LTE_CT = IS_MIFOUR
            && "LTE-CT".equals(WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI4 LTE INDIA. */
    public static final boolean IS_MIFOUR_LTE_INDIA = IS_MIFOUR
            && "LTE-India".equals(WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is MI4 LTE SEASA. */
    public static final boolean IS_MIFOUR_LTE_SEASA = IS_MIFOUR
            && "LTE-SEAsa".equals(WLReflect.getSystemProperties("persist.radio.modem"));

    /** Indicates the current device is Hongmi2 TDSCDMA. */
    public static final boolean IS_HONGMI2_TDSCDMA = "HM2013022".equals(DEVICE);

    /** Indicates the current device is CDMA. */
    public static final boolean IS_CDMA = IS_MIONE_CDMA || IS_MITWO_CDMA || IS_MITHREE_CDMA || IS_MIFOUR_CDMA || IS_MIFOUR_LTE_CT;

    /** Indicates the current device is TDSCDMA. */
    public static final boolean IS_TDS_CDMA = IS_MITHREE_TDSCDMA || IS_HONGMI2_TDSCDMA || IS_MITWO_TDSCDMA;

    /** Indicates the current device is ChinaUnicom customization edition. @hide */
    public static final boolean IS_CU_CUSTOMIZATION = "cu".equals(
            WLReflect.getSystemProperties("ro.carrier.name"));

    /** Indicates the current device is ChinaMobile customization edition. @hide */
    public static final boolean IS_CM_CUSTOMIZATION = "cm".equals(
            WLReflect.getSystemProperties("ro.carrier.name"));

    /** Indicates the current device is ChinaTelcom customization edition. @hide */
    public static final boolean IS_CT_CUSTOMIZATION = "ct".equals(
            WLReflect.getSystemProperties("ro.carrier.name"));

    /** Indicates the current MIUI is development edition. @hide */
    public static final boolean IS_DEVELOPMENT_VERSION =
            !TextUtils.isEmpty(VERSION.INCREMENTAL)
            && VERSION.INCREMENTAL.matches(REGULAR_EXPRESSION_FOR_DEVELOPMENT);

    /** Indicates the current MIUI is stable edition. @hide */
    public static final boolean IS_STABLE_VERSION = "user".equals(BuildV5.TYPE)
            && !IS_DEVELOPMENT_VERSION;

    /** Indicates the current MIUI is a official edition. @hide */
    public static final boolean IS_OFFICIAL_VERSION = IS_DEVELOPMENT_VERSION || IS_STABLE_VERSION;

    /** Indicates the current MIUI is alpha edition. @hide */
    public static final boolean IS_ALPHA_BUILD = WLReflect.getSystemProperties("ro.product.mod_device", "").endsWith("_alpha");

    /** Indicates the current MIUI is only for CTS. @hide */
    public static final boolean IS_CTS_BUILD = "1".equals(
            WLReflect.getSystemProperties("ro.miui.cts"));

    /** Indicates the current MIUI is only for CTA. @hide */
    public static final boolean IS_CTA_BUILD = "1".equals(
            WLReflect.getSystemProperties("ro.miui.cta"));

    /** Indicates the current MIUI is only for CMCC test. @hide */
    public static final boolean IS_CM_CUSTOMIZATION_TEST ="cm".equals(
            WLReflect.getSystemProperties("ro.cust.test"));

    /** Indicates the current MIUI is only for CUCC test. @hide */
    public static final boolean IS_CU_CUSTOMIZATION_TEST ="cu".equals(
            WLReflect.getSystemProperties("ro.cust.test"));

    /** Indicates the current MIUI is in lite mode or not. @hide **/
    public static final boolean FEATURE_WHOLE_ANIM =
            WLReflect.getSystemPropertiesBoolean("ro.sys.ft_whole_anim", true);

    public static final boolean IS_QUALCOMM = HARDWARE.startsWith("qcom") || IS_MIONE || IS_MI1S || IS_MITWO || "cancro".equals(DEVICE) || IS_MI2A || IS_MIFOUR || IS_MIFIVE || IS_HONGMI_TWO_A || IS_HONGMI_THREE_LTE;
    public static final boolean IS_NVIDIA   = "pisces".equals(DEVICE) || IS_MIPAD;
    public static final boolean IS_MEDIATEK = HARDWARE.startsWith("mt") || "HM2013022".equals(DEVICE) || "HM2013023".equals(DEVICE) || IS_HONGMI_TWO_S || IS_HONGMI_TWOS_LTE_MTK || IS_HONGMI_THREE;
    public static final boolean IS_LEADCORE = HARDWARE.startsWith("leadcore") || IS_HONGMI_TWOX_LC;

    public static final String CHIP_ABBR = getChipAbbr();

    public static String getChipAbbr() {
        String chip;
        if (IS_QUALCOMM) {
            if ("taurus".equals(DEVICE) || "taurus_td".equals(DEVICE))
                return null;
            else
                return "qc";
        } else if (IS_NVIDIA) {
            return "nv";
        } else if (IS_MEDIATEK) {
            return "mt";
        } else if (IS_LEADCORE) {
            return "lc";
        } else {
            return null;
        }
    }

    /**
     * Get region.
     * Please use this method to replace {@link #REGION}.
     * Because for international build the region will be correct after
     * use choice the region via Provision,
     * but before that the zygote has initialized {@link #REGION} & #IS_XX_BUILD,
     * and fork to app's process, this cause app get the error region at first boot.
     *
     * To resolve this problem, use this method and receive the broadcast,
     * {@ link miui.os.MiuiInit#ACTION_MIUI_REGION_CHANGE}, which is sent when
     * region changed.
     * @hide
     */
    public static String getRegion() {
        String def =  "CN";
        def =  WLReflect.getSystemProperties("ro.miui.region");
        return def;
    }

    /**
     * Check region.
     * Replace #IS_XX_BUILD
     * Region follows the rule of "ISO 3166-1 alpha-2 codes".
     * "ISO 3166-1 alpha-2 codes" is two-letter country codes
     * defined in ISO 3166-1, part of the ISO 3166 standard,
     * For more detail to visit wiki:
     *     http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2
     *
     * @hide
     */
    public static boolean checkRegion(String region) {
        return getRegion().equalsIgnoreCase(region);
    }

    /**
     * Get region.
     * @hide
     * @deprecated
     */
    public static String  REGION = WLReflect.getSystemProperties("ro.miui.region", "CN");

    /**
     * Indicates the current MIUI is built for Taiwan.
     * @hide
     * @deprecated
     */
    public static boolean IS_TW_BUILD = checkRegion("TW");

    /**
     * Indicates the current MIUI is built for HongKong.
     * @hide
     * @deprecated
     */
    public static boolean IS_HK_BUILD = checkRegion("HK");

    /**
     * Indicates the current MIUI is built for Singapore.
     * @hide
     * @deprecated
     */
    public static boolean IS_SG_BUILD = checkRegion("SG");

    /**
     * Indicates the current MIUI is built for Malaysia.
     * @hide
     * @deprecated
     */
    public static boolean IS_MY_BUILD = checkRegion("MY");

    /**
     * Indicates the current MIUI is built for Philippines.
     * @hide
     * @deprecated
     */
    public static boolean IS_PH_BUILD = checkRegion("PH");

    /**
     * Indicates the current MIUI is built for Indonesia.
     * @hide
     * @deprecated
     */
    public static boolean IS_ID_BUILD = checkRegion("ID");

    /**
     * Indicates the current MIUI is built for India.
     * @hide
     * @deprecated
     */
    public static boolean IS_IN_BUILD = checkRegion("IN");

    /**
     * Indicates the current MIUI is built for Thailand.
     * @hide
     * @deprecated
     */
    public static boolean IS_TH_BUILD = checkRegion("TH");

    /** Indicates the current MIUI is built for international (not for China mainland). @hide */
    public static final boolean IS_INTERNATIONAL_BUILD = WLReflect.getSystemProperties("ro.product.mod_device", "").endsWith("_global");

    /**
     * Indicates the current devices is tablet or not.
     * <p/>
     * Please see http://developer.android.com/guide/practices/screens-distribution.html#FilteringTabletApps.
     * Google consider all devices with large than 600dp width screen are tablet. We follows this spec.
     */
    public static final boolean IS_TABLET = isTablet();

    /** Returns true indicate the device has msm8660 properties. */
    private static boolean hasMsm8660Property() {
        String soc = WLReflect.getSystemProperties("ro.soc.name");
        return "msm8660".equals(soc) || "unkown".equals(soc);
    }

    /** Returns true indicates the current devices is tablet. */
    private static boolean isTablet() {
        // If devices is known as tablet, just return true.
        if (IS_N7 || IS_MIPAD) {
            return true;
        }
        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        return ((int) (Math.min(dm.widthPixels, dm.heightPixels) / dm.density + 0.5f)) >= 600;
    }

    /** Check whether is using miui */

    public static boolean hasCameraFlash(Context context) {
        return !IS_N7 && !IS_MIPAD
                && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    /** Properity for check user mode **/
    public static final String USER_MODE = "persist.sys.user_mode";

    /** Indicates the current user mod is normal mode **/
    public static final int USER_MODE_NORMAL = 0;

    /** Indicates the current user mod is elder mode **/
    public static final int USER_MODE_ELDER = 1;

    //TODO
    /** Return user mode, default is normal mode **/
    public static int getUserMode() {
        return  WLReflect.getSystemPropertiesInt(USER_MODE, USER_MODE_NORMAL);
    }

    //TODO
    /** Set user mode @hide **/
    public static void setUserMode(Context context, int mode) {
        WLReflect.setSystemProperties(USER_MODE, Integer.toString(mode));
        ((PowerManager)context.getSystemService(Context.POWER_SERVICE)).reboot(null);
    }

    /** Get cust variant. @hide **/
    public static String getCustVariant() {
        if (!IS_INTERNATIONAL_BUILD) {
            return WLReflect.getSystemProperties("ro.miui.cust_variant", "cn");
        } else {
            return WLReflect.getSystemProperties("ro.miui.cust_variant", "hk");
        }
    }

    /** Get userdate image version code. @hide **/
    public static final String USERDATA_IMAGE_VERSION_CODE = getUserdataImageVersionCode();

    /**
     * Get userdate image version code.
     * The version code has two parts
     * 1. the version code read from property ro.miui.userdata_version
     * 2. the region: cn or global. If there is a carrier, add it to the end of region.
     * @return String userdate image version code
     */
    private static String getUserdataImageVersionCode(){
        String versionCodeProperty = WLReflect.getSystemProperties("ro.miui.userdata_version", "");
        if ("".equals(versionCodeProperty)) {
            return "Unavailable";
        }

        String region;
        if (IS_INTERNATIONAL_BUILD) {
            region = "global";
        } else {
            region = "cn";
        }

        String carrier = WLReflect.getSystemProperties("ro.carrier.name", "");
        if (!"".equals(carrier)) {
            carrier = "_" + carrier;
        }

        return String.format("%s(%s%s)", versionCodeProperty, region, carrier);
    }
}
