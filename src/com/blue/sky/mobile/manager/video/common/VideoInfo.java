package com.blue.sky.mobile.manager.video.common;

import android.graphics.Bitmap;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.mobile.manager.apk.utils.FileUtil;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2014/10/14.
 */
public class VideoInfo implements Serializable{

    private String id;
    private String title;
    private String path;
    private String displayName;
    private Bitmap bitmap;
    private String mineType;
    private String content;
    private String imageUrl;
    private String mImageUrl;
    private String bImageUrl;
    private String comments;
    private String createTime;
    private String size;
    private String time;
    private String tag;
    private String playTimes;
    private int categoryId;

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPath(String path) {
        if(Strings.isNotEmpty(path)){
            File file = new File(path);
            this.path = path;
            this.size = FileUtil.format(new File(path).length());
            this.time = TimeUtil.dateToStr(new Date(file.lastModified()));
        }
    }

    public String getPath() {
        return path;
    }


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getMineType() {
        return mineType;
    }

    public void setMineType(String mineType) {
        this.mineType = mineType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getbImageUrl() {
        return bImageUrl;
    }

    public void setbImageUrl(String bImageUrl) {
        this.bImageUrl = bImageUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPlayTimes() {
        return playTimes;
    }

    public void setPlayTimes(String playTimes) {
        this.playTimes = playTimes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
