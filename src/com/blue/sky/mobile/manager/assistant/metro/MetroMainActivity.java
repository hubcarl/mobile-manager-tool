package com.blue.sky.mobile.manager.assistant.metro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.adapter.SimpleCommonListAdapter;
import com.blue.sky.common.entity.Item;
import com.blue.sky.common.http.*;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.assistant.cache.MetroDBHelper;
import com.blue.sky.mobile.manager.music.cache.MusicDBHelper;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.utils.MusicJsonUtil;
import com.blue.sky.mobile.manager.picture.activity.ShowImageActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MetroMainActivity extends BaseActivity {


    private GridView mGroupGridView;
    private List<CityMetro> metroList = new ArrayList<CityMetro>();
    private MetroGridAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.assistant_metro_activity_main);
        UIHelp.setHeaderMenuView(this, "地铁线路");
        initView();
        loadMetroList();
    }

    private void initView() {
        mGroupGridView = (GridView) findViewById(R.id.gridView);
        mGroupGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent mIntent = new Intent(MetroMainActivity.this, MetroMapViewActivity.class);
                mIntent.putExtra("id", metroList.get(position).getCityId());
                mIntent.putExtra("name", metroList.get(position).getCityName());
                mIntent.putExtra("imageUrl", metroList.get(position).getImageUrl());
                startActivity(mIntent);
            }
        });
    }

    private void loadMetroList() {
        final String url = "http://skyapi.sinaapp.com/api/metro/citymetro.php";
//        new HttpAsyncClient(url, null, new HttpResponseCallback() {
//            @Override
//            public void onSuccess(String response) {
//                //Log.i(">>>response:", response);
//                parse(response);
//                updateListView();
//            }
//
//            @Override
//            public void onError(String response) {
//                Log.e(">>>loadOnlineMusic onError:", response);
//            }
//        }).execute();

        new HttpCacheClient(this, new IHttpCache<CityMetro>() {

            @Override
            public List<CityMetro> loadCache(Object... params) {
                return MetroDBHelper.getCityMetroList(MetroMainActivity.this);
            }

            @Override
            public List<CityMetro> loadHttp(Object... params) {
                String response = HttpSyncClient.httpGet(url, null);
                Log.i(">>>response:", response);
                return parse(response);
            }

            @Override
            public void cache(Object obj) {
                MetroDBHelper.addCityMetro(getApplicationContext(),(List<CityMetro>)obj);
            }

            @Override
            public void response(Object obj, int type) {
                List<CityMetro> metros = (List<CityMetro>)obj;
                if(metros.size()>0){
                    metroList.clear();
                    metroList.addAll(metros);
                    updateListView();
                }
            }

        }).execute();
    }

    private void updateListView() {
        if (listAdapter == null) {
            listAdapter = new MetroGridAdapter(this, metroList, mGroupGridView);
            mGroupGridView.setAdapter(listAdapter);
        } else {
            listAdapter.notifyDataSetChanged();
        }
    }

    private List<CityMetro> parse(String response) {

        List<CityMetro> metros = new ArrayList<CityMetro>();

        try {
            JSONArray jArray = new JSONArray(response);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                CityMetro metro = new CityMetro();
                metro.setCityId(obj.getInt("cityId"));
                metro.setCityName(obj.getString("cityName"));
                metro.setImageUrl(obj.getString("imageUrl"));
                metros.add(metro);

            }
        } catch (JSONException e) {

        }

        return metros;
    }


}
