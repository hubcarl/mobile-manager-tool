package com.blue.sky.mobile.manager.file.common;

import com.blue.sky.mobile.manager.R;

import java.io.File;

/**
 * Created by Administrator on 2010/12/20.
 */
public enum FileType {


    DIR(R.drawable.icon_folder, 0),
    APP(R.drawable.icon_app2, 1),
    IMAGE(R.drawable.icon_photos, 2),
    MUSIC(R.drawable.icon_music2, 3),
    VIDEO(R.drawable.icon_video2, 4),
    TXT(R.drawable.icon_documents, 5),
    CHM(R.drawable.icon_documents,6),
    PDF(R.drawable.icon_pdf, 7),
    ZIP(R.drawable.icon_zip, 8),
    WORD(R.drawable.icon_word, 9),
    XLS(R.drawable.icon_excel, 10),
    PPT(R.drawable.icon_ppt, 11),
    HTML(R.drawable.icon_html, 12),
    DEFAULT(R.drawable.icon_default,99);

    // 成员变量
    private int resId;
    private int index;

    // 构造方法
    private FileType(int resId, int index) {
        this.resId = resId;
        this.index = index;
    }

    // 普通方法
    public static int getResId(int index) {
        for (FileType c : FileType.values()) {
            if (c.getIndex() == index) {
                return c.resId;
            }
        }
        return DEFAULT.getResId();
    }

    // get set 方法
    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }


    public static  FileType getFileType(File file){


        String fileName = file.getName().toLowerCase();

        if(file.isDirectory()){
         return FileType.DIR;
        }
        else if(fileName.endsWith(".apk")){
            return FileType.APP;
        }else  if(fileName.endsWith(".png")||fileName.endsWith(".jpg")
                ||fileName.endsWith(".jpeg")||fileName.endsWith(".bmp")
                ||fileName.endsWith(".gif")||fileName.endsWith(".bmp")){
            return FileType.IMAGE;
        }else  if(fileName.endsWith(".mp2")||fileName.endsWith(".mp3")
                ||fileName.endsWith(".wma")){
            return FileType.MUSIC;
        }else if(fileName.endsWith(".mp4")||fileName.endsWith(".rmvb")
                ||fileName.endsWith(".wmv")||fileName.endsWith(".rm")
                ||fileName.endsWith(".avi")||fileName.endsWith(".3gp")
                ||fileName.endsWith(".asf")||fileName.endsWith(".mov")){
            return FileType.VIDEO;
        }else if(fileName.endsWith(".txt")){
            return FileType.TXT;
        }else if(fileName.endsWith(".chm")){
            return FileType.CHM;
        }else if(fileName.endsWith(".doc")||fileName.endsWith(".docx")){
            return FileType.WORD;
        }else if(fileName.endsWith(".xls")||fileName.endsWith(".xlsx")){
            return FileType.XLS;
        }else if(fileName.endsWith(".zip")||fileName.endsWith(".rar")){
            return FileType.ZIP;
        }else if(fileName.endsWith(".pdf")){
            return FileType.PDF;
        }else{
            return FileType.DEFAULT;
        }
    }
}
