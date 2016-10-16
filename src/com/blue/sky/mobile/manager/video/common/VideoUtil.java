package com.blue.sky.mobile.manager.video.common;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/11/2.
 */
public class VideoUtil {

    public static List<VideoInfo> parseRecVideo(JSONObject json) {

        List<VideoInfo> list = new ArrayList<VideoInfo>();

        if (json == null) {
            return list;
        }
        try {
            JSONArray jArray = json.getJSONArray("list");
            if (jArray == null) {
                return list;
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                if (object != null) {
                    VideoInfo bean = new VideoInfo();
                    bean.setId(object.getString("vid"));
                    bean.setTitle(object.getString("title"));
                    bean.setContent(object.getString("content"));
                    bean.setbImageUrl(object.getString("pic"));
                    bean.setmImageUrl(object.getString("pic"));
                    bean.setImageUrl(object.getString("pic"));
                    bean.setTime(object.getString("totaltime"));
                    bean.setComments(object.getString("comment"));
                    list.add(bean);
                }
            }

        } catch (JSONException e) {
            Log.e(">>>json parseHotVideo", e.getMessage());
        }
        return list;
    }

    public static List<VideoInfo> parseHotVideo(JSONObject json) {

        List<VideoInfo> list = new ArrayList<VideoInfo>();

        if (json == null) {
            return list;
        }
        try {
            JSONArray jArray = json.getJSONArray("list");
            if (jArray == null) {
                return list;
            }
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                if (object != null) {
                    VideoInfo bean = new VideoInfo();
                    bean.setId(object.getString("vid"));
                    bean.setTitle(object.getString("title"));
                    Log.i(">>>title",object.getString("title"));
                    bean.setbImageUrl(object.getString("bimg"));
                    bean.setmImageUrl(object.getString("mimg"));
                    bean.setImageUrl(object.getString("img"));
                    bean.setCreateTime(object.getString("public_time"));
                    bean.setTime(object.getString("totaltime"));
                    bean.setComments(object.getString("comments"));
                    bean.setPlayTimes(object.getString("play_times"));
                    bean.setTag(object.getString("tag"));
                    list.add(bean);
                }
            }

        } catch (JSONException e) {
           Log.e(">>>json parseHotVideo", e.getMessage());
        }
        return list;
    }

    public static Map<String,String> getVideoUrl(JSONObject json) {
        Map<String,String> urlMapping=new HashMap<String, String>();
        try {
            JSONObject jObject=json.getJSONObject("info");
            if (jObject!=null) {
                JSONArray jArray=jObject.getJSONArray("rfiles");
                for (int i = 0; i < jArray.length(); i++) {
                    String type=jArray.getJSONObject(i).getString("type");
                    String url=jArray.getJSONObject(i).getString("url");
                    urlMapping.put(type, url);
                }

            }
        } catch (Exception e) {

        }
        return urlMapping;
    }
}
