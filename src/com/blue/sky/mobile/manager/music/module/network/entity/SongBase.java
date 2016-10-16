package com.blue.sky.mobile.manager.music.module.network.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/11/5.
 */
public class SongBase implements Serializable {

    private String id;
    private String name;
    private String singerName;
    private String singerId;
    private long mDuration;

    public long getMDuration() {
        return mDuration;
    }

    public void setMDuration(long duration) {
        this.mDuration = duration;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    private String albumId;
    private String albumName;
    private String pickCount;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    private String artist;
    private int categoryId;


    private SongExtend mv;

    public SongExtend getMv() {
        return mv;
    }

    public void setMv(SongExtend mv) {
        this.mv = mv;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getSingerId() {
        return singerId;
    }

    public void setSingerId(String singerId) {
        this.singerId = singerId;
    }

    public String getAlbumName() {
        return albumName==null ? "" : albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getPickCount() {
        return pickCount;
    }

    public void setPickCount(String pickCount) {
        this.pickCount = pickCount;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

}
