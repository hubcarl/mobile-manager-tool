package com.blue.sky.common.component.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import com.blue.sky.common.AppMain;
import com.blue.sky.common.utils.Strings;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * SystemUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-5-15
 */
public class SystemUtils {

    /** recommend default thread pool size according to system available processors, {@link #getDefaultThreadPoolSize()} **/
    public static final int DEFAULT_THREAD_POOL_SIZE = getDefaultThreadPoolSize();

    /**
     * get recommend default thread pool size
     * 
     * @return if 2 * availableProcessors + 1 less than 8, return it, else return 8;
     * @see {@link #getDefaultThreadPoolSize(int)} max is 8
     */
    public static int getDefaultThreadPoolSize() {
        return getDefaultThreadPoolSize(8);
    }

    /**
     * get recommend default thread pool size
     * 
     * @param max
     * @return if 2 * availableProcessors + 1 less than max, return it, else return max;
     */
    public static int getDefaultThreadPoolSize(int max) {
        int availableProcessors = 2 * Runtime.getRuntime().availableProcessors() + 1;
        return availableProcessors > max ? max : availableProcessors;
    }

    public static int getSDKVersion()
    {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static boolean isHighVersion()
    {
        return getSDKVersion()>10;
    }

    public static String getDeviceId()
    {
        TelephonyManager tm = (TelephonyManager) AppMain.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getMobile()
    {
        TelephonyManager tm = (TelephonyManager) AppMain.getInstance().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }


    public static String getApkVersionCode(){
        // 获取packagemanager的实例
        PackageManager packageManager = AppMain.getInstance().getApplicationContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(AppMain.getInstance().getApplicationContext().getPackageName(),0);
            return packInfo==null ? Strings.EMPTY_STRING : packInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return Strings.EMPTY_STRING;
        }
    }

    private String getMacAddress(){
        String result = "";
        WifiManager wifiManager = (WifiManager) AppMain.getInstance().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        result = wifiInfo.getMacAddress();
        return result;
    }

    private String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        Log.i(">>>SystemUtils", "cpuinfo:" + cpuInfo[0] + " " + cpuInfo[1]);
        return cpuInfo;
    }

    private String[] getTotalMemory() {

        String[] result = {"",""};  //1-total 2-avail
        final ActivityManager activityManager = (ActivityManager) AppMain.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(mi);

        long mTotalMem = 0;
        long mAvailMem = mi.availMem;
        String str1 = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            mTotalMem = Integer.valueOf(arrayOfString[1]).intValue() * 1024;
            localBufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result[0] = Formatter.formatFileSize(AppMain.getInstance(), mTotalMem);
        result[1] = Formatter.formatFileSize(AppMain.getInstance(), mAvailMem);
        Log.i(">>>SystemUtils", "meminfo total:" + result[0] + " used:" + result[1]);
        return result;
    }

    public static String getDeviceInfo(){
        return "--------------------------"
                +"   imei:" + getDeviceId()
                + "  model:" + android.os.Build.MODEL
                + "  sdk:" + android.os.Build.VERSION.RELEASE
                + "  version:" + getApkVersionCode()
                +"--------------------------";
    }

    public static String getDeviceDetailInfo(){
        String phoneInfo = "[IMEI: " + getDeviceId();
        phoneInfo += ", MODEL: " + android.os.Build.MODEL;
        phoneInfo += ", BRAND: " + android.os.Build.BRAND;
        phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
        phoneInfo += ", SDK.RELEASE: " + android.os.Build.VERSION.RELEASE;
        phoneInfo += ", PRODUCT:" + android.os.Build.PRODUCT;
        phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
        phoneInfo += ", TAGS: " + android.os.Build.TAGS;
        phoneInfo += ", VERSION_CODES.BASE: " + android.os.Build.VERSION_CODES.BASE;
        phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
        phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
        phoneInfo += ", BOARD: " + android.os.Build.BOARD;
        phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
        phoneInfo += ", ID: " + android.os.Build.ID;
        phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
        phoneInfo += ", USER: " + android.os.Build.USER;
        phoneInfo +="]";
        return phoneInfo;
    }
}
