package com.blue.sky.mobile.manager.video.network;

import android.content.Intent;
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
import com.blue.sky.control.extend.MyCommonPanel;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.adapter.CategoryAdapter;
import com.blue.sky.mobile.manager.video.cache.VideoDBHelper;
import com.blue.sky.mobile.manager.video.common.Category;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.common.VideoUtil;
import com.blue.sky.common.http.HttpCacheClient;
import com.blue.sky.common.http.IHttpCache;
import com.wole56.sdk.Video;
import io.vov.vitamio.Vitamio;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/14.
 */
public class VideoOnlineFragment extends Fragment {

    private View rootView;
    private GridView gridView;
    private MyCommonPanel dailyPanel;
    private MyCommonPanel happyPanel;
    private MyCommonPanel hotPanel;

    private List<Category> categoryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.video_online_main_fragment, container, false);
        initCategory();
        initGridView();
        initPanel();
        initData();
        return rootView;
    }

    private void registerVideoComponent(){
        new Runnable() {
            @Override
            public void run() {
                if (!Vitamio.isInitialized(getActivity().getApplicationContext())){
                    Vitamio.initialize(getActivity().getApplicationContext());
                }
            }

        };
    }

    private void initGridView(){
        gridView = (GridView)rootView.findViewById(R.id.gridView);
        gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        gridView.setAdapter(new CategoryAdapter(getActivity(),categoryList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), VideoOnlineListActivity.class);
                intent.putExtra("categoryId", categoryList.get(position).getCategoryId());
                intent.putExtra("categoryName", categoryList.get(position).getCategoryName());
                getActivity().startActivity(intent);
            }
        });
    }

    private void initPanel(){

        dailyPanel = (MyCommonPanel) rootView.findViewById(R.id.dailyPanel);
        happyPanel = (MyCommonPanel) rootView.findViewById(R.id.happyPanel);

        dailyPanel.init("娱乐视频", "1");
        happyPanel.init("热点视频", "2");
    }

    private void initData()
    {
      loadOnlineVideo(dailyPanel, 1, 1, 8);
      loadOnlineVideo(happyPanel, 2, 1, 8);
    }

    private  void loadOnlineVideo(final MyCommonPanel panel,final int categoryId, final int pageIndex, final int size){
        // 最热视频
        new HttpCacheClient(getActivity(), new IHttpCache<VideoInfo>() {

            @Override
            public List<VideoInfo> loadCache(Object... params) {
                return VideoDBHelper.getVideoList(getActivity(),categoryId, 1, 8);
            }

            @Override
            public List<VideoInfo> loadHttp(Object... params) {
                JSONObject result = Video.getHotVideo(getActivity(), categoryId+"", size+"", pageIndex+"");
                Log.d(">>>result:", result.toString());
                List<VideoInfo> list = VideoUtil.parseHotVideo(result);

                return list;
            }

            @Override
            public void cache(Object obj) {
                for(VideoInfo videoInfo : (List<VideoInfo> )obj){
                    videoInfo.setCategoryId(categoryId);
                    VideoDBHelper.addVideo(getActivity(),videoInfo);
                }
            }

            @Override
            public void response(Object obj, int type) {
                panel.refresh((List<VideoInfo>)obj);
            }

        }).execute();
    }

    private void initCategory(){
        categoryList = new ArrayList<Category>();
        categoryList.add(new Category("1","娱乐"));
        categoryList.add(new Category("2","热点"));
        //categoryList.add(new Category("3","原创"));
        //categoryList.add(new Category("4","搞笑"));

        categoryList.add(new Category("41","音乐"));
        categoryList.add(new Category("14","体育"));
        //categoryBBList.add(new Category("26","游戏"));
        categoryList.add(new Category("10","科技"));

        categoryList.add(new Category("11","女性"));
        //categoryList.add(new Category("34","母婴"));
        categoryList.add(new Category("27","旅游"));
        categoryList.add(new Category("28","汽车"));
    }
}