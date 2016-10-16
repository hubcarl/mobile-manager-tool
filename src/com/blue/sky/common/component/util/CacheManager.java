package com.blue.sky.common.component.util;

import android.app.Activity;
import android.content.Context;
import com.blue.sky.common.component.service.HttpCache;
import com.blue.sky.common.component.service.impl.ImageCache;
import com.blue.sky.common.component.service.impl.ImageSDCardCache;

/**
 * CacheManager
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-11
 */
public class CacheManager {

    private static HttpCache httpCache = null;

    private CacheManager() {

    }

    /**
     * get the singleton instance of HttpCache
     * 
     * @param context {@link android.app.Activity#getApplicationContext()}
     * @return
     */
    public static HttpCache getHttpCache(Context context) {
        if (httpCache == null) {
            synchronized (CacheManager.class) {
                if (httpCache == null) {
                    httpCache = new HttpCache(context);
                }
            }
        }
        return httpCache;
    }

    /**
     * get the singleton instance of ImageCache
     * 
     * @return
     * @see ImageCacheManager#getImageCache()
     */
    public static ImageCache getImageCache() {
        return ImageCacheManager.getImageCache();
    }

    /**
     * get the singleton instance of ImageSDCardCache
     * 
     * @return
     * @see ImageCacheManager#getImageSDCardCache()
     */
    public static ImageSDCardCache getImageSDCardCache() {
        return ImageCacheManager.getImageSDCardCache();
    }
}
