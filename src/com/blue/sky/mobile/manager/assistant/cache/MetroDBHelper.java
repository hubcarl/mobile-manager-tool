package com.blue.sky.mobile.manager.assistant.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.blue.sky.common.db.DBHelper;
import com.blue.sky.mobile.manager.assistant.metro.CityMetro;
import com.blue.sky.mobile.manager.video.common.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/11.
 */
public class MetroDBHelper {

    private final static Object LOCK = new Object();
    private final static String SKY_CITY_METRO_TAB = "sky_city_metro";
    public final static String[] CITY_METRO_TAB_COLS = new String[]{
        "cityId","cityName","imageUrl"
    };

    public static String createCityMetroTable()
    {

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS sky_city_metro(");
        sb.append("cityId primary key,");
        sb.append("cityName nvarchar(32),");
        sb.append("imageUrl nvarchar(512)");
        sb.append(")");

        return sb.toString();
    }

    public static void addCityMetro(Context context, List<CityMetro> metros){

        for(CityMetro  metro : metros){
            ContentValues value = new ContentValues();
            value.put("cityId", metro.getCityId());
            value.put("cityName", metro.getCityName());
            value.put("imageUrl", metro.getImageUrl());
            DBHelper.safeInsert(context, SKY_CITY_METRO_TAB, value, "cityId=?", new String[]{metro.getCityId()+""});
        }
    }

    public static List<CityMetro> getCityMetroList(Context context)
    {
        List<CityMetro> list = new ArrayList<CityMetro>();
        synchronized (LOCK){
            SQLiteDatabase db = DBHelper.getDB(context);
            Cursor cursor = db.query(SKY_CITY_METRO_TAB, CITY_METRO_TAB_COLS, null,null, null, null, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                CityMetro metro = new CityMetro();
                metro.setCityId(cursor.getInt(0));
                metro.setCityName(cursor.getString(1));
                metro.setImageUrl(cursor.getString(2));
                list.add(metro);
                cursor.moveToNext();
            }
            cursor.close();
        }
        Log.d(">>>Mobile getVideoList", list.size()+"");
        return list;
    }
}
