package com.blue.sky.mobile.manager.music.module.network.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.blue.sky.common.activity.BaseFragmentActivity;
import com.blue.sky.common.http.HttpAsyncClient;
import com.blue.sky.common.http.HttpResponseCallback;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.module.network.adapter.MusicOnlineListAdapter;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.utils.MusicJsonUtil;
import com.blue.sky.mobile.manager.music.module.network.utils.PopMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 类名 VideoListActivity.java
 * 说明  
 * 作者  BlueSky
 * 版权  蓝天科技工作室
 * 创建时间  2014-11-02
 */
public class MusicOnlineListActivity extends BaseFragmentActivity implements OnScrollListener
{

	private List<Song> musicArrayList = new ArrayList<Song>();

	private ListView listView;

	private MusicOnlineListAdapter musicOnlineListAdapter;

	private ProgressBar progressBar;
	
	private TextView loadingTip;

	private View moreView;

	// 最后可见条目的索引
	private int lastVisibleIndex;
	
	private int tempPaceCount = 0;
	
	private int toastCount = 0;

    private String categoryId;
    private int pageSize = 50;
    private int pageIndex = 1;
    private boolean isLoading = false;
    private boolean isNav = false;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_common_list);
        init();
	}


	private void init()
	{

        String title = getIntent().getStringExtra("categoryName");
        UIHelp.setHeaderMenuView(this, title);

        isNav = getIntent().getBooleanExtra("isNav", false);
        categoryId = getIntent().getStringExtra("categoryId");
		moreView = getLayoutInflater().inflate(R.layout.sky_common_loading, null);
		progressBar = (ProgressBar) moreView.findViewById(R.id.pg);
		progressBar.setContentDescription("正在加载,请稍后...");
		loadingTip = (TextView) moreView.findViewById(R.id.loadtips);

        musicOnlineListAdapter = new MusicOnlineListAdapter(this, musicArrayList, 2);
		listView = (ListView) this.findViewById(R.id.list_view);
		listView.addFooterView(moreView);
		listView.setAdapter(musicOnlineListAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopMenu.showMusicPopWindow(MusicOnlineListActivity.this, view, musicArrayList.get(position));
            }
        });

		listView.setOnScrollListener(this);

        loadVideo();
	}

    private void loadVideo(){

        if(isLoading)return;
        isLoading = true;
        // 最热视频
        String url="http://v1.ard.tj.itlily.com/ttpod?a=getnewttpod&utdid=VFOxtTHYewEDADCYIQ0ZJ/wI&id="+categoryId+"&page="+pageIndex+"&size="+pageSize;
        if(isNav){
            url="http://fm.api.ttpod.com/channelsong?utdid=VFOxtTHYewEDADCYIQ0ZJ/wI&page="+pageIndex+"&size="+pageSize+"&tagid="+categoryId;
        }
        new HttpAsyncClient(url, null, new HttpResponseCallback() {
            @Override
            public void onSuccess(String response){
                List<Song> list = MusicJsonUtil.parse(response);
                musicArrayList.addAll(list);
                musicOnlineListAdapter.notifyDataSetChanged();
                lastVisibleIndex = musicArrayList.size();
                tempPaceCount = list.size();
                pageIndex = pageIndex + 1;
                isLoading = false;
            }

            @Override
            public void onError(String response){
                Log.e(">>>loadOnlineMusic onError:", response);
            }
        }).execute();
    }

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex >= musicOnlineListAdapter.getCount())
		{
            progressBar.setVisibility(View.VISIBLE);
            loadingTip.setVisibility(View.VISIBLE);
            loadVideo();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// 计算最后可见条目的索引
		lastVisibleIndex = firstVisibleItem + visibleItemCount -1;
		
		// 所有的条目已经和最大条数相等，则移除底部的View
		if (tempPaceCount == 0 && pageIndex > 1)
		{
			listView.removeFooterView(moreView);
			if(toastCount==0)
			{
				Toast.makeText(this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_SHORT).show();
			}
			toastCount ++ ;
		}

	}

}
