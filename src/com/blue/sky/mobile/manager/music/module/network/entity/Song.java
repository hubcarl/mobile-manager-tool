package com.blue.sky.mobile.manager.music.module.network.entity;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/11/5.
 */
public class Song extends SongBase implements Serializable {

    private List<SongExtend> urlList = new ArrayList<SongExtend>();

    public List<SongExtend> getUrlList() {
        return urlList;
    }

    public void setUrlList(List<SongExtend> urlList) {
        this.urlList = urlList;
    }

    public void setUrlList(SongExtend songExtend) {
        this.urlList.add(songExtend);
    }

    public void setSingleList(String url, String duration){
        SongExtend extend = new SongExtend();
        extend.setUrl(url);
        extend.setDuration(duration);
        this.urlList.add(extend);
    }


    public void addExtend(JSONObject json){
        SongExtend extend = new SongExtend();
        extend.transJSONToExtend(json);
        if(extend!=null){
            this.urlList.add(extend);
        }
    }

    public String getPlayUrl(){
        if(this.urlList.size()>0){
            return this.urlList.get(this.urlList.size()-1).getUrl();
        }else{
            return null;
        }
    }

    public SongExtend getPlayInfo(){
        if(this.urlList.size()>0){
            return this.urlList.get(this.urlList.size()-1);
        }else{
            return null;
        }
    }

    public String getSongFormat(){
        if(this.urlList.size()>0){
            return this.urlList.get(this.urlList.size()-1).getFormat();
        }else{
            return null;
        }
    }

    public String getDuration(){
        return this.urlList.isEmpty() ? "未知" : this.urlList.get(0).getDuration();
    }
}
