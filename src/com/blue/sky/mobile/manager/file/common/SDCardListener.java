package com.blue.sky.mobile.manager.file.common;

import android.os.FileObserver;
import android.util.Log;

/**
 * SD卡中的目录创建监听器。
 *
 */
public class SDCardListener extends FileObserver {

    public SDCardListener(String path) {
        super(path);
    }

    @Override
    public void onEvent(int event, String path) {
        switch(event) {
            case FileObserver.ALL_EVENTS:
                Log.d("all", "path:"+ path);
                break;
            case FileObserver.CREATE:
                Log.d("Create", "path:"+ path);
                break;
        }
    }

//    SDCardListener listener = new SDCardListener("目录");
//
////开始监听
//    listener.startWatching();
//
///*
// * 在这里做一些操作，比如创建目录什么的
// */
//
////停止监听
//    listener.stopWatching();
}