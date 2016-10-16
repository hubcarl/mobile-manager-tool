package com.blue.sky.common.http;

import android.os.Handler;
import com.blue.sky.mobile.manager.video.common.VideoInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/1/3.
 */
public interface IHttpCache<T> {

    List<T> loadCache(Object... params);

    List<T> loadHttp(Object... params);

    void cache(Object obj);

    void response(Object obj, int type);


}
