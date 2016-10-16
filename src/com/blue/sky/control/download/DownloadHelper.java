package com.blue.sky.control.download;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import com.blue.sky.control.download.providers.DownloadManager;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;

/**
 * Project: IntelliJ IDEA.
 * User: Sky
 * Date: 2014/12/13
 * Time: 14:34
 * Desc:
 */
public class DownloadHelper {

    private static DownloadManager mDownloadManager;

    public static long startDownload(Context context,Song song) {

        if(mDownloadManager==null){
            mDownloadManager = new DownloadManager(context.getContentResolver(),context.getPackageName());
        }
        try {

            Uri srcUri = Uri.parse(song.getPlayUrl());
            DownloadManager.Request request = new DownloadManager.Request(srcUri);

            // 设置文件保存的路径
            request.setTitle(song.getName());
            request.setDescription(song.getSingerName());
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/");
            request.setDestinationInExternalFilesDir(context,Environment.DIRECTORY_DOWNLOADS, song.getName() + "." + song.getSongFormat());
            request.setVisibleInDownloadsUi(true);
            request.setAddMediaScanner();
            return mDownloadManager.enqueue(request);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
