package com.blue.sky.mobile.manager.music.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.blue.sky.common.db.DBHelper;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.entity.SongExtend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2010/10/11.
 */
public class MusicDBHelper {

    private final static Object LOCK = new Object();
    private final static String SKY_MUSIC = "sky_music";
    private final static String SKY_MUSIC_FAVORITE = "sky_music_favorite";
    public final static String[] SKY_MUSIC_FAVORITE_TAB_COLS = new String[]{
            "id", "name", "thumbPath", "filePath", "sortOrder"
    };

    public static String createMusicTable() {

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS sky_music(");
        sb.append("id integer,");
        sb.append("name nvarchar(128),");
        sb.append("singerName nvarchar(64),");
        sb.append("singerId nvarchar(16),");
        sb.append("albumName nvarchar(64),");
        sb.append("pickCount nvarchar(8),");
        sb.append("duration nvarchar(8),");
        sb.append("format nvarchar(8),");
        sb.append("rate nvarchar(8),");
        sb.append("size nvarchar(8),");
        sb.append("type nvarchar(8),");
        sb.append("url nvarchar(1024),");
        sb.append("mv nvarchar(1024),");
        sb.append("categoryId int default 0,");
        sb.append("status integer default 1");
        sb.append(")");

        return sb.toString();
    }


    public static String createMusicFavoriteTable() {

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS sky_music_favorite(");
        sb.append("id integer primary key,");
        sb.append("name nvarchar(64),");
        sb.append("thumbPath nvarchar(256),");
        sb.append("filePath nvarchar(256),");
        sb.append("sortOrder integer default 1,");
        sb.append("createDate datetime,");
        sb.append("status integer default 1");
        sb.append(")");

        return sb.toString();
    }

    public static void addFavoriteMusic(Context context, ContentValues value) {
        DBHelper.insert(context, SKY_MUSIC_FAVORITE, value);
    }

    public static void deleteFavorite(Context context, long id) {
        DBHelper.delete(context, SKY_MUSIC_FAVORITE, "id=?", new String[]{id + ""});
    }

    public static List<Song> getFavoriteMusicList(Context context) {
        List<Song> list = new ArrayList<Song>();
        synchronized (LOCK) {
            SQLiteDatabase db = DBHelper.getDB(context);
            Cursor cursor = db.query(SKY_MUSIC_FAVORITE, SKY_MUSIC_FAVORITE_TAB_COLS, null, null, null, null, "createDate DESC");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Song music = new Song();
                music.setId(cursor.getInt(0)+"");
                music.setName(cursor.getString(1));
                music.setArtist(cursor.getString(2));
                music.setSingleList(cursor.getString(3), "");
                list.add(music);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    public static void addMusic(Context context, List<Song> listSong, String categoryId){
        for(Song s : listSong){
            s.setCategoryId(Integer.valueOf(categoryId));
            addMusic(context, s);
        }
    }

    public static void addMusic(Context context, Song song){

        ContentValues value = new ContentValues();

        value.put("id", song.getId());
        value.put("name", song.getName());
        value.put("singerName", song.getSingerName());
        value.put("singerId", song.getSingerId());
        value.put("albumName", song.getAlbumName());
        value.put("pickCount", song.getPickCount());
        value.put("duration", song.getPlayInfo().getDuration());
        value.put("format", song.getPlayInfo().getFormat());
        value.put("rate", song.getPlayInfo().getRate());
        value.put("size", song.getPlayInfo().getSize());
        value.put("type", song.getPlayInfo().getType());
        value.put("url", song.getPlayInfo().getUrl());
        value.put("mv", song.getMv() == null ? "" : song.getMv().getUrl());
        value.put("categoryId", song.getCategoryId());

        DBHelper.safeInsert(context, SKY_MUSIC, value, "id=? AND categoryId=?", new String[]{song.getId(),song.getCategoryId()+""});
    }

    public static List<Song> getMusicList(Context context,int categoryId, int pageIndex, int pageSize) {
        List<Song> list = new ArrayList<Song>();
        synchronized (LOCK) {
            SQLiteDatabase db = DBHelper.getDB(context);
            Cursor cursor = db.query(SKY_MUSIC, null, "categoryId=? limit ?", new String[]{categoryId+"", pageSize+""}, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Song song = new Song();

                song.setId(cursor.getString(cursor.getColumnIndex("id")));
                song.setName(cursor.getString(cursor.getColumnIndex("name")));
                song.setSingerName(cursor.getString(cursor.getColumnIndex("singerName")));
                song.setSingerId(cursor.getString(cursor.getColumnIndex("singerId")));
                song.setAlbumName(cursor.getString(cursor.getColumnIndex("albumName")));
                song.setPickCount(cursor.getString(cursor.getColumnIndex("pickCount")));
                song.setCategoryId(cursor.getInt(cursor.getColumnIndex("categoryId")));

                song.setUrlList(getSongExtendInfo(cursor, null));

                String mvUrl  = cursor.getString(cursor.getColumnIndex("mv"));
                if(Strings.isNotEmpty(mvUrl)){
                  song.setMv(getSongExtendInfo(cursor,mvUrl));
                }

                list.add(song);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }

    private static SongExtend getSongExtendInfo(Cursor cursor, String mvUrl){
        SongExtend songExtend = new SongExtend();
        songExtend.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        songExtend.setDuration(cursor.getString(cursor.getColumnIndex("duration")));
        songExtend.setFormat(cursor.getString(cursor.getColumnIndex("format")));
        songExtend.setRate(cursor.getString(cursor.getColumnIndex("rate")));
        songExtend.setSize(cursor.getString(cursor.getColumnIndex("size")));
        songExtend.setType(cursor.getString(cursor.getColumnIndex("type")));
        if(Strings.isNotEmpty(mvUrl)){
            songExtend.setUrl(mvUrl);
        }else{
            songExtend.setUrl(cursor.getString(cursor.getColumnIndex("url")));
        }
        return songExtend;
    }
}
