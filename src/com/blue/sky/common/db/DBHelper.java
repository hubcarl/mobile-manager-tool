package com.blue.sky.common.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.blue.sky.common.utils.Strings;
import java.util.List;

/**
 * 类名 DBHelper.java
 * 说明 作者 BlueSky 
 * 版权 蓝天科技工作室 
 * 创建时间 2014-10-2
 */
public class DBHelper
{

    private final static String DATABASE_PATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/mobile_manager";

    private final static Object LOCK = new Object();

    private final static String DATABASE_FILENAME = "mobile_manager.db";

    private static SQLiteHelper instance;

    private static synchronized SQLiteHelper getHelper(Context context)
    {
        if (instance == null)
            instance = new SQLiteHelper(context, DATABASE_FILENAME, null, 1);

        return instance;
    }

    public static  synchronized SQLiteDatabase getDB(Context context)
    {
        return getHelper(context).getWritableDatabase();
    }

    public static String getPrimarySQL(String[] primaryKeys, ContentValues values){
        String strPrimarySQL= Strings.EMPTY_STRING;
        for(int i=0;i<primaryKeys.length;i++){
            String key = primaryKeys[i];
            if(i==0){
                strPrimarySQL += key+"="+values.getAsString(key);
            }else{
                strPrimarySQL += " AND " + key+"="+values.getAsString(key);
            }
        }
        return strPrimarySQL;
    }

    public static void safeInsert(Context context, String tabName, ContentValues value,String whereClause, String[] whereArgs)
    {
        synchronized (LOCK){
            SQLiteDatabase db = getDB(context);
            db.delete(tabName, whereClause, whereArgs);
            db.insert(tabName, null, value);
        }
    }

    public static void insert(Context context, String tabName, ContentValues value)
    {
        synchronized (LOCK){
            SQLiteDatabase db = getDB(context);
            db.insert(tabName, null, value);
        }
    }

    public static void insert(Context context, String tabName, String[] primaryKeys, List<ContentValues> listValue)
    {
        synchronized (LOCK){
            SQLiteDatabase db = getDB(context);
            if (null != listValue)
            {
                for (ContentValues values : listValue)
                {
                    if(primaryKeys!=null){
                        db.delete(tabName,getPrimarySQL(primaryKeys,values), null);
                    }
                    db.insert(tabName, null, values);
                }
            }
        }
    }

    public static long getMaxId(Context context, String tabName, String columnId)
    {
        long maxId = 0;
        synchronized (LOCK){
            SQLiteDatabase db = getDB(context);
            Cursor cursor = db.rawQuery("select max(" + columnId + ") from " + tabName, null);
            if (cursor.moveToFirst())
            {
                maxId = cursor.getLong(0);
            }
            cursor.close();
        }
        return maxId;
    }

    public static void delete(Context context, String tabName, String whereClause, String[] whereArgs){
        synchronized (LOCK){
            SQLiteDatabase db = getHelper(context).getWritableDatabase();
            db.delete(tabName,whereClause, whereArgs);
        }
    }

    public static int getCount(Context context, String tabName, String strWhere)
    {
        String strSql = "select count(*) from " + tabName;
        if(Strings.isNotEmpty(strWhere)){
            strSql = strSql + " where " + strWhere;
        }
        int count = 0;
        synchronized (LOCK){
            SQLiteDatabase db = getHelper(context).getWritableDatabase();
            Cursor cursor = db.rawQuery(strSql, null);
            if (cursor.moveToFirst())
            {
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }
}
