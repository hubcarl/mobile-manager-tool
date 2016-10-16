package com.blue.sky.common.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import com.blue.sky.mobile.manager.assistant.cache.MetroDBHelper;
import com.blue.sky.mobile.manager.file.common.FileDBHelper;
import com.blue.sky.mobile.manager.music.cache.MusicDBHelper;
import com.blue.sky.mobile.manager.video.cache.VideoDBHelper;

/**
 * 
 * 类名 SQLiteHelper.java
 * 说明  
 * 作者  BlueSky
 * 版权  蓝天科技工作室
 * 创建时间  2014-10-16
 */
public class SQLiteHelper extends SQLiteOpenHelper {

	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(MusicDBHelper.createMusicFavoriteTable());
        db.execSQL(MusicDBHelper.createMusicTable());
        db.execSQL(FileDBHelper.createFileTable());
        db.execSQL(VideoDBHelper.createVideoTable());
        db.execSQL(MetroDBHelper.createCityMetroTable());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
