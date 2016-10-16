package com.blue.sky.mobile.manager.file.common;

import com.blue.sky.common.utils.TimeUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2010/12/20.
 */
public class FileInfo {

    private String path;
    private String name;
    private int type;
    private String createDate;
    private boolean isDir;

    public FileInfo(){

    }

    public FileInfo(File file){
        this.name = file.getName();
        this.path = file.getAbsolutePath();
        this.type = FileType.getFileType(file).getIndex();
        this.createDate = TimeUtil.dateToStrLong(new Date(file.lastModified()));
        this.isDir = file.isDirectory();
    }

    public FileInfo(String name ,String path, String createDate, boolean isDir){
        this.name = name;
        this.path = path;
        this.createDate = createDate;
        this.isDir = isDir;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean isDir) {
        this.isDir = isDir;
    }

    public static List<FileInfo> convert(File[] files){
        List<FileInfo> list = new ArrayList<FileInfo>();
        for(File file :files){
            if(!file.getName().startsWith(".")){
                list.add(new FileInfo(file));
            }
        }
        return list;
    }

}
