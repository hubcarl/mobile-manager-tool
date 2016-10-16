package com.blue.sky.mobile.manager.music.module.network.entity;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/11/5.
 */
public class SongExtend implements Serializable {

    private String duration;
    private String format;
    private String rate;
    private String type;
    private String typeDesc;
    private String size;
    private String url;

    public SongExtend(){

    }

    public SongExtend(JSONObject jsonObject){
        transJSONToExtend(jsonObject);
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void transJSONToExtend(JSONObject obj){
        try{
            setUrl(obj.getString("url"));
            setDuration(obj.getString("duration"));
            setFormat(obj.getString("format"));
            setSize(obj.getString("size"));
            setType(obj.optString("type"));
            setTypeDesc(obj.getString("type_description"));
        }catch (JSONException e){
            Log.e(">>>transJSONToExtend:",e.toString());
        }
    }
}
