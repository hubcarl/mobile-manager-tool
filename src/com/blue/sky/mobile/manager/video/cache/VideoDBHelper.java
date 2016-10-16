package com.blue.sky.mobile.manager.video.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.blue.sky.common.db.DBHelper;
import com.blue.sky.mobile.manager.video.common.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/11.
 */
public class VideoDBHelper {

    private final static Object LOCK = new Object();
    private final static String VIDEO_TAB_NAME = "sky_video";
    public final static String[] VIDEO_TAB_COLS = new String[]{
        "id","title","path","mineType","imageUrl","bImageUrl",
        "createTime","playTimes","size","content","totalTime","categoryId"
    };

    public static String createVideoTable()
    {

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS sky_video(");
        sb.append("id int primary key,");
        sb.append("title nvarchar(128),");
        sb.append("path nvarchar(1024),");
        sb.append("imageUrl nvarchar(1024),");
        sb.append("bImageUrl nvarchar(1024),");
        sb.append("mineType nvarchar(32),");
        sb.append("content nvarchar(1024),");
        sb.append("size int default 0,");
        sb.append("totalTime int default 0,");
        sb.append("playTimes int default 1,");
        sb.append("categoryId int default 0,");
        sb.append("createTime datetime");
        sb.append(")");

        return sb.toString();
    }

    public static void addVideo(Context context, VideoInfo videoInfo){

        ContentValues value = new ContentValues();

        value.put("id", videoInfo.getId());
        value.put("title", videoInfo.getTitle());
        value.put("path", videoInfo.getPath());
        value.put("mineType", videoInfo.getMineType());
        value.put("imageUrl", videoInfo.getImageUrl());
        value.put("bImageUrl", videoInfo.getbImageUrl());
        value.put("content", videoInfo.getContent());
        value.put("size", videoInfo.getSize());
        value.put("totalTime", videoInfo.getTime());
        value.put("playTimes", videoInfo.getPlayTimes());
        value.put("createTime", videoInfo.getCreateTime());
        value.put("categoryId", videoInfo.getCategoryId());

        DBHelper.safeInsert(context, VIDEO_TAB_NAME, value, "id=? AND categoryId=?", new String[]{videoInfo.getId(),videoInfo.getCategoryId()+""});
    }

    public static List<VideoInfo> getVideoList(Context context, int categoryId, int pageIndex, int pageSize)
    {
        List<VideoInfo> list = new ArrayList<VideoInfo>();
        synchronized (LOCK){
            SQLiteDatabase db = DBHelper.getDB(context);
            Cursor cursor = db.query(VIDEO_TAB_NAME, VIDEO_TAB_COLS, "categoryId=?", new String[]{categoryId+""}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                VideoInfo videoInfo = new VideoInfo();

                videoInfo.setId(cursor.getString(0));
                videoInfo.setTitle(cursor.getString(1));
                videoInfo.setPath(cursor.getString(2));
                videoInfo.setMineType(cursor.getString(3));
                videoInfo.setImageUrl(cursor.getString(4));
                videoInfo.setbImageUrl(cursor.getString(5));
                videoInfo.setCreateTime(cursor.getString(6));
                videoInfo.setPlayTimes(cursor.getString(7));
                videoInfo.setSize(cursor.getString(8));
                videoInfo.setContent(cursor.getString(9));
                videoInfo.setTime(cursor.getString(10));

                list.add(videoInfo);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(">>>Mobile getVideoList", list.size()+"");
        return list;
    }
}
