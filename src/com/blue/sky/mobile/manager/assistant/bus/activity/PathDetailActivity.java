package com.blue.sky.mobile.manager.assistant.bus.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.entity.Item;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * 类名 PathDetailActivity.java
 * 说明  
 * 作者  BlueSky
 * 版权  蓝天科技工作室
 * 创建时间  2014-11-02
 */
public class PathDetailActivity extends BaseActivity
{
	private ListView listView;
	private SimpleAdapter listAdapter;
    private List<Map<String, String>> list = new ArrayList<Map<String,String>>();
    private String[] from = { "index", "name" };
    private int[] to = { R.id.item_index, R.id.item_name };

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.assistant_bus_path_detail);
        init();
	}


	private void init()
	{
        final Item item = (Item)getIntent().getSerializableExtra("item");
        String[] sites = item.getItemFive().split(";");
        for(int i=0;i<sites.length;i++){
            Map<String, String> m = new HashMap<String, String>();
            m.put("index", (i+1)+"");
            m.put("name", sites[i]);
            list.add(m);
        }

        listAdapter = new SimpleAdapter(this, list, R.layout.assistant_bus_path_list_item, from, to);
		listView = (ListView) this.findViewById(R.id.list_view);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(SiteDetailActivity.class,"item", item);
            }
        });

        UIHelp.setHeaderMenuView(this, parsePathName(item.getItemOne()));
	}

    private String parsePathName(String fullName){
        int index = fullName.indexOf("(");
        if(index>-1){
            return fullName.substring(0,index).replace("路","");
        }else{
            return fullName;
        }
    }

    private void setStartEndSite(String fullName){

    }
}
