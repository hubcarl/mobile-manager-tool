package com.blue.sky.mobile.manager.game;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.http.HttpAsyncClient;
import com.blue.sky.common.http.HttpResponseCallback;
import com.blue.sky.common.utils.*;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.utils.MusicJsonUtil;
import com.blue.sky.mobile.message.ActionManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 类名 GameListActivity.java
 * 说明  
 * 作者  BlueSky
 * 版权  蓝天科技工作室
 * 创建时间  2014-11-02
 */
public class GameListActivity extends BaseActivity
{

	private List<GameInfo> gameList = new ArrayList<GameInfo>();

	private ListView listView;

	private GameListAdapter gameListAdapter;

	private ProgressBar progressBar;

	private View moreView;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sky_game_list);
        init();
	}


	private void init()
	{

        UIHelp.setHeaderMenuView(this, "游戏娱乐");

		moreView = getLayoutInflater().inflate(R.layout.sky_common_loading, null);
		progressBar = (ProgressBar) moreView.findViewById(R.id.pg);
		progressBar.setContentDescription("正在加载,请稍后...");

        gameListAdapter = new GameListAdapter(this, gameList, 2);
		listView = (ListView) this.findViewById(R.id.list_view);
		listView.addFooterView(moreView);
		listView.setAdapter(gameListAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GameListActivity.this, GamePlayActivity.class);
                intent.putExtra("item", gameList.get(position));
                startActivity(intent);
            }
        });

        String  gameStr = SharedPreferenceUtil.getInstance(GameListActivity.this).getString("gameList");
        if(Strings.isEmpty(gameStr)){
            if(NetWorkHelper.isConnect(this)){
                loadData(102);
            }else{
                showToast("连不上网络哟～请检查网络设置!");
            }
        }else{
            bindListView(parse(gameStr));
            if(NetWorkHelper.isConnect(this)) {
                loadData(102);
            }
        }
	}

    private void bindListView(List<GameInfo> list){
        gameList.addAll(list);
        gameListAdapter.notifyDataSetChanged();
        listView.removeFooterView(moreView);
        SharedPreferenceUtil.getInstance(GameListActivity.this).putInt(EnumUtil.Navigation.Game.toString(), gameList.size());
        sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
    }


    private void loadData(int freeId){

        String url= Constants.GAME_WEBSITE_DOMAIN + "/api/freelist.php?freeId="+freeId;

        new HttpAsyncClient(url, null, new HttpResponseCallback() {
            @Override
            public void onSuccess(String response){
                SharedPreferenceUtil.getInstance(GameListActivity.this).putString("gameList",response);
                bindListView(parse(response));
            }

            @Override
            public void onError(String response){
                Log.e(">>>loadOnlineMusic onError:", response);
                showToast("连不上网络哟～请检查网络设置!");
            }
        }).execute();
    }

    public static List<GameInfo> parse(String str)
    {
        List<GameInfo> list = new ArrayList<GameInfo>();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(str);
            for(int i=0;i<jsonArray.length();i++){
                GameInfo item = new GameInfo();
                JSONObject json = (JSONObject)jsonArray.opt(i);
                item.setId(json.getLong("id"));
                item.setGameId(json.getString("gameId"));
                item.setShortName(json.getString("shortName"));
                item.setLongName(json.getString("longName"));
                item.setGameIcon(json.getString("gameIcon"));
                item.setGameUrl(json.getString("gameUrl"));
                item.setHitCount(json.getInt("hitCount"));
                item.setPlayCount(json.getInt("playCount"));
                item.setUnPlayCount(json.getInt("unPlayCount"));
                item.setScore(Float.valueOf(json.getString("score")));
                item.setCommentCount(json.getInt("commentCount"));
                item.setSummery(json.getString("summary"));
                item.setCreateTime(json.getString("createTime"));
                item.setCategoryId(json.getInt("categoryId"));
                item.setCategoryName(json.getString("categoryName"));
                list.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(">>>JsonHelper.parse size:", list.size()+"");
        return list;
    }

}
