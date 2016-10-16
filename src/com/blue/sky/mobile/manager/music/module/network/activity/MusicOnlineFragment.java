package com.blue.sky.mobile.manager.music.module.network.activity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.blue.sky.common.http.*;
import com.blue.sky.common.utils.NetWorkHelper;
import com.blue.sky.control.extend.MusicPanel;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.MusicDBHelper;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.utils.MusicJsonUtil;
import com.blue.sky.mobile.manager.video.adapter.CategoryAdapter;
import com.blue.sky.mobile.manager.video.cache.VideoDBHelper;
import com.blue.sky.mobile.manager.video.common.Category;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.common.VideoUtil;
import com.wole56.sdk.Video;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/14.
 */
public class MusicOnlineFragment extends Fragment {

    private View rootView;
    private GridView gridView;
    private MusicPanel dailyPanel;
    private MusicPanel happyPanel;
    private MusicPanel hotPanel;

    private List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.music_online_main_fragment, container, false);
        initCategory();
        initGridView();
        initPanel();
        initData();
        return rootView;
    }

    private void initGridView(){
        gridView = (GridView)rootView.findViewById(R.id.gridView);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new CategoryAdapter(getActivity(),categoryList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MusicOnlineListActivity.class);
                intent.putExtra("isNav", true);
                intent.putExtra("categoryId", categoryList.get(position).getCategoryId());
                intent.putExtra("categoryName", categoryList.get(position).getCategoryName());
                getActivity().startActivity(intent);
            }
        });
    }
    private void initPanel(){
        dailyPanel = (MusicPanel) rootView.findViewById(R.id.dailyPanel);
        happyPanel = (MusicPanel) rootView.findViewById(R.id.happyPanel);
        hotPanel = (MusicPanel) rootView.findViewById(R.id.hotPanel);

        dailyPanel.init("热歌榜", "111");
        happyPanel.init("新歌榜", "112");
        hotPanel.init("网络歌曲榜", "934");
    }

    private void initData()
    {
        loadOnlineMusic(dailyPanel, "http://v1.ard.tj.itlily.com/ttpod?a=getnewttpod&utdid=VFOxtTHYewEDADCYIQ0ZJ/wI&id=111&page=1&size=50");
        loadOnlineMusic(happyPanel, "http://v1.ard.tj.itlily.com/ttpod?a=getnewttpod&utdid=VFOxtTHYewEDADCYIQ0ZJ/wI&id=112&page=1&size=50");
        loadOnlineMusic(hotPanel, "http://v1.ard.tj.itlily.com/ttpod?a=getnewttpod&utdid=VFOxtTHYewEDADCYIQ0ZJ/wI&id=934&page=1&size=50");
    }

    private  void loadOnlineMusic(final MusicPanel panel,final String url){

        new HttpCacheClient(getActivity(), new IHttpCache<Song>() {

            @Override
            public List<Song> loadCache(Object... params) {
                return MusicDBHelper.getMusicList(getActivity(),Integer.valueOf(panel.getCategoryId()), 1, 8);
            }

            @Override
            public List<Song> loadHttp(Object... params) {
                String response = HttpSyncClient.httpGet(url, null);
                List<Song> list = MusicJsonUtil.parse(response).subList(0,8);
                return list;
            }

            @Override
            public void cache(Object obj) {
                MusicDBHelper.addMusic(getActivity(),(List<Song>)obj, panel.getCategoryId());
            }

            @Override
            public void response(Object obj, int type) {
                panel.refresh((List<Song>)obj);
            }

        }).execute();
    }

    private void initCategory(){
        categoryList = new ArrayList<Category>();
        categoryList.add(new Category("3","经典"));
        categoryList.add(new Category("4","网络"));
        categoryList.add(new Category("79","电台"));

        categoryList.add(new Category("14","DJ舞曲"));
        categoryList.add(new Category("106","好声音"));
        categoryList.add(new Category("46","铃声"));
    }

}