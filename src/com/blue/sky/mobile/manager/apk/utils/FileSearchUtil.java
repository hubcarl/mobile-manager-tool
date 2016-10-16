package com.blue.sky.mobile.manager.apk.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.blue.sky.mobile.manager.apk.model.AppInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取手机上apk文件信息类，主要是判断是否安装再手机上了，安装的版本比较现有apk版本信息
 * <a href="http://my.oschina.net/arthor" target="_blank" rel="nofollow">@author</a>  Dylan
 */
public class FileSearchUtil {
    private static int UNINSTALLED = 0; // 表示未安装
    private static int INSTALLED = 1; // 表示已经安装，且跟现在这个apk文件是一个版本
    private static int INSTALLED_UPDATE =2; // 表示已经安装，版本比现在这个版本要低，可以点击按钮更新

    private Context context;
    private List<AppInfo> myFiles = new ArrayList<AppInfo>();

    public List<AppInfo> getMyFiles() {
        return myFiles;
    }

    public void setMyFiles(List<AppInfo> myFiles) {
        this.myFiles = myFiles;
    }

    public FileSearchUtil(Context context) {
        super();
        this.context = context;
    }

    /**
     * 运用递归的思想，递归去找每个目录下面的apk文件
     */
    public void FindAllAPKFile(File file) {

        // 手机上的文件,目前只判断SD卡上的APK文件
        // file = Environment.getDataDirectory();
        // SD卡上的文件目录
        if (file.isFile()) {
            String name_s = file.getName();
            AppInfo fileInfo = new AppInfo();
            String apk_path = null;
            // MimeTypeMap.getSingleton()
            if (name_s.toLowerCase().endsWith(".apk")) {
                apk_path = file.getAbsolutePath();// apk文件的绝对路劲
                // System.out.println("----" + file.getAbsolutePath() + "" +
                // name_s);
                PackageManager pm = context.getPackageManager();
                PackageInfo packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);
                ApplicationInfo appInfo = packageInfo.applicationInfo;


                /**获取apk的图标 */
                appInfo.sourceDir = apk_path;
                appInfo.publicSourceDir = apk_path;
                Drawable apk_icon = appInfo.loadIcon(pm);
                fileInfo.setIcon(apk_icon);
                /** 得到包名 */
                String packageName = packageInfo.packageName;
                fileInfo.setPackageName(packageName);
                /** apk的绝对路劲 */
                fileInfo.setFilePath(file.getAbsolutePath());
                /** apk的版本名称 String */
                String versionName = packageInfo.versionName;
                fileInfo.setVersionName(versionName);
                /** apk的版本号码 int */
                int versionCode = packageInfo.versionCode;
                fileInfo.setVersionCode(versionCode);
                /**安装处理类型*/
                int type = checkApkStatus(pm, packageName, versionCode);
                fileInfo.setStatus(type);

                Log.i("ok", "处理类型:" + String.valueOf(type) + "\n" + "------------------我是纯洁的分割线-------------------");
                myFiles.add(fileInfo);
            }
            // String apk_app = name_s.substring(name_s.lastIndexOf("."));
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    FindAllAPKFile(file_str);
                }
            }
        }
    }

    /*
     * 判断该应用是否在手机上已经安装过，有以下集中情况出现
     * 1.未安装，这个时候按钮应该是“安装”点击按钮进行安装
     * 2.已安装，按钮显示“已安装” 可以卸载该应用
     * 3.已安装，但是版本有更新，按钮显示“更新” 点击按钮就安装应用
     */

    /**
     * 判断该应用在手机中的安装情况
     * @param pm                   PackageManager
     * @param packageName  要判断应用的包名
     * @param versionCode     要判断应用的版本号
     */
    private int checkApkStatus(PackageManager pm, String packageName, int versionCode) {
        List<PackageInfo> listPackages = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);
        for (PackageInfo pi : listPackages) {
            String pi_packageName = pi.packageName;
            int pi_versionCode = pi.versionCode;
            //如果这个包名在系统已经安装过的应用中存在
            if(packageName.endsWith(pi_packageName)){
                //Log.i("test","此应用安装过了");
                if(versionCode==pi_versionCode){
                    Log.i("test","已经安装，不用更新，可以卸载该应用");
                    return INSTALLED;
                }else if(versionCode>pi_versionCode){
                    Log.i("test","已经安装，有更新");
                    return INSTALLED_UPDATE;
                }
            }
        }
        Log.i("test","未安装该应用，可以安装");
        return UNINSTALLED;
    }

}