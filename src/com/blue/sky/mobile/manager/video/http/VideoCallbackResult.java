package com.blue.sky.mobile.manager.video.http;

import com.blue.sky.mobile.manager.video.common.VideoInfo;

import java.util.List;

/**
 * Created by Administrator on 2014/11/2.
 */
public interface VideoCallbackResult {
    List<VideoInfo> request(String... params);

    void response(List<VideoInfo> list);
}
