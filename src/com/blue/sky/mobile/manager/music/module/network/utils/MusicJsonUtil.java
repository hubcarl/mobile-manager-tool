package com.blue.sky.mobile.manager.music.module.network.utils;

import android.util.Log;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.entity.SongExtend;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/11/5.
 */
public class MusicJsonUtil {

    public static List<Song> parse(String jsonText){
        List<Song> list = new ArrayList<Song>();
        try {
            JSONObject root = new JSONObject(jsonText);
            JSONArray jArray = root.getJSONArray("data");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject object = jArray.getJSONObject(i);
                Song s = new Song();
                s.setId(object.getString("song_id"));
                s.setName(object.getString("song_name"));
                s.setSingerId(object.optString("singer_id"));
                s.setSingerName(object.optString("singer_name"));
                s.setAlbumName(object.optString("album_name"));
                s.setPickCount(object.optString("pick_count"));
                JSONArray urlListArray = object.getJSONArray("url_list");
                if(urlListArray!=null){
                    for (int j = 0; j < urlListArray.length(); j++) {
                        s.addExtend(urlListArray.getJSONObject(j));
                    }
                }
                JSONArray mvListArray = object.optJSONArray("mv_list");
                if(mvListArray!=null && mvListArray.length()>0){
                    SongExtend extend = new SongExtend();
                    extend.transJSONToExtend(mvListArray.getJSONObject(0));
                    s.setMv(extend);
                }
                list.add(s);
            }
        } catch (JSONException e) {
            Log.e(">>>music json parse", e.getMessage());
        }
        return list;
    }
}
