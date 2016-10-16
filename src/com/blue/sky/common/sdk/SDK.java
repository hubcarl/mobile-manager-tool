package com.blue.sky.common.sdk;

import android.content.Context;
import com.blue.sky.common.utils.NetWorkHelper;
import com.blue.sky.mobile.manager.R;
import com.wole56.sdk.APP;
import io.vov.vitamio.Vitamio;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Administrator on 2014/11/1.
 */
public class SDK {
    public static final String APP_56_KEY = "3000008539";
    public static final String APP_56_SECRET = "5f9f1b4e6854641b";

    public static String QQ_APP_ID = "1103278420";

//    public static final String WDJ_AD_APP_ID = "100017777";
//    public static final String WDJ_AD_APP_Secret_Key = "0450485d6919337503e625ff74b03179";
//    public static final String WDJ_AD_APP_LIST = "03d644d3b6b069be7afafaf8ae5ead93";
//    public static final String WDJ_AD_APP_LIST_GAME = "368d727a0bf4e4488670a4f95f11bd81";
//    public static final String WDJ_AD_APP_LIST_APP = "638a43324024edf1633ae26c2fde0c45";

    public static void initWDJSDK(Context context) {
        try {
            if (NetWorkHelper.isConnect(context)) {
                //Ads.init(context, SDK.WDJ_AD_APP_ID, SDK.WDJ_AD_APP_Secret_Key);
            }
        } catch (Exception e) {

        }
    }

    public static void init56SDK(Context context){
        APP.init(context, SDK.APP_56_KEY, SDK.APP_56_SECRET);
    }


    public static void registerVideoComponent(Context context) {
        Vitamio.initialize(context);
        if (Vitamio.isInitialized(context))
            return;
        //反射解压
        try {
            Class c = Class.forName("io.vov.vitamio.Vitamio");
            Method extractLibs = c.getDeclaredMethod("extractLibs", new Class[]{android.content.Context.class, int.class});
            extractLibs.setAccessible(true);
            extractLibs.invoke(c, new Object[]{context, R.raw.libarm});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
