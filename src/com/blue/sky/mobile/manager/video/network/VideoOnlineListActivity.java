package com.blue.sky.mobile.manager.video.network;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.adapter.VideoOnlineListAdapter;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.common.VideoUtil;
import com.blue.sky.mobile.manager.video.http.VideoCallbackResult;
import com.blue.sky.mobile.manager.video.http.VideoHttpAsyncTask;
import com.wole56.sdk.Video;
import org.json.JSONObject;

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
public class VideoOnlineListActivity extends BaseActivity implements OnScrollListener
{

	private List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();

	private ListView listView;

	private VideoOnlineListAdapter videoListAdapter;

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

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.video_common_list);
        init();
	}


	private void init()
	{
        String title = getIntent().getStringExtra("categoryName");
        UIHelp.setHeaderMenuView(this, title);

        categoryId = getIntent().getStringExtra("categoryId");
		moreView = getLayoutInflater().inflate(R.layout.sky_common_loading, null);
		progressBar = (ProgressBar) moreView.findViewById(R.id.pg);
		progressBar.setContentDescription("正在加载,请稍后...");
		loadingTip = (TextView) moreView.findViewById(R.id.loadtips);

        videoListAdapter = new VideoOnlineListAdapter(this, videoInfoList, 2);
		listView = (ListView) this.findViewById(R.id.list_view);
		listView.addFooterView(moreView);
		listView.setAdapter(videoListAdapter);
		listView.setOnItemClickListener(null);
		listView.setOnScrollListener(this);

        loadVideo();
	}

    private void loadVideo(){

        if(isLoading)return;
        isLoading = true;
        // 最热视频
        new VideoHttpAsyncTask(new VideoCallbackResult() {

            @Override
            public List<VideoInfo> request(String... params) {
                Log.i(">>>result pageIndex:" , pageIndex+"");
                JSONObject result = Video.getHotVideo(VideoOnlineListActivity.this, categoryId + "", pageSize + "", pageIndex + "");
                return VideoUtil.parseHotVideo(result);
            }

            @Override
            public void response(List<VideoInfo> list) {
                if(list!=null){
                    videoInfoList.addAll(list);
                    videoListAdapter.notifyDataSetChanged();
                    lastVisibleIndex = videoInfoList.size();
                    tempPaceCount = list.size();
                    pageIndex = pageIndex + 1;
                    isLoading = false;
                }
            }

        }).execute();
    }

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		// 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex >= videoListAdapter.getCount())
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
