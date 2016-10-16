package com.blue.sky.mobile.manager.file.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.blue.sky.common.db.DBHelper;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.common.utils.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2014/10/11.
 */
public class FileDBHelper {

    private final static Object LOCK = new Object();
    private final static String FILE_TAB_NAME = "sky_file";
    public final static String[] FILE_TAB_COLS = new String[]{
        "path","name","type","isDir","createDate"
    };

    public static String createFileTable()
    {

        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE IF NOT EXISTS sky_file(");
        sb.append("path nvarchar(512) primary key,");
        sb.append("name nvarchar(128),");
        sb.append("type int(2) default 0,");
        sb.append("isDir int(1) default 0,");
        sb.append("createDate datetime");
        sb.append(")");

        return sb.toString();
    }

    public static void addFile(Context context, File file){

        ContentValues value = new ContentValues();

        value.put("path", file.getAbsolutePath());
        value.put("name", file.getName());
        value.put("type", FileType.getFileType(file).getIndex());
        value.put("isDir", file.isDirectory()? 1 : 0);
        value.put("createDate", TimeUtil.dateToStrLong(new Date(file.lastModified())));
        DBHelper.safeInsert(context,FILE_TAB_NAME, value, "path=?", new String[]{file.getAbsolutePath()});
    }

    public static void addFile(Context context, FileInfo fileInfo){

        ContentValues value = new ContentValues();

        value.put("path", fileInfo.getPath());
        value.put("name", fileInfo.getName());
        value.put("type", fileInfo.getType());
        value.put("isDir", fileInfo.isDir());
        value.put("createDate", fileInfo.getCreateDate());

        DBHelper.insert(context, FILE_TAB_NAME, value);
    }

    public static void addFile(Context context,ContentValues value){
        DBHelper.insert(context, FILE_TAB_NAME, value);
    }

    public static List<FileInfo> getFileList(Context context, String keywords)
    {
        List<FileInfo> list = new ArrayList<FileInfo>();
        synchronized (LOCK){
            SQLiteDatabase db = DBHelper.getDB(context);
            String filterSearchText = Strings.filterSpecialChar(keywords);
            String queryWhere = null;
            if (Strings.isNotEmpty(filterSearchText))
            {
                queryWhere = " name like '%" + filterSearchText + "%'";
            }
            Cursor cursor = db.query(FILE_TAB_NAME, FILE_TAB_COLS, queryWhere, null, null, null, "type asc");
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                FileInfo file = new FileInfo();
                file.setPath(cursor.getString(0));
                file.setName(cursor.getString(1));
                file.setType(cursor.getInt(2));
                file.setDir(cursor.getInt(3) == 1 ? true : false);
                file.setCreateDate(cursor.getString(4));
                list.add(file);
                cursor.moveToNext();
            }
            cursor.close();
        }
        return list;
    }
}
