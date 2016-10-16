package com.blue.sky.mobile.manager.assistant.bus.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.blue.sky.common.adapter.SimpleCommonListAdapter;
import com.blue.sky.common.entity.Item;
import com.blue.sky.common.fragment.BaseFragment;
import com.blue.sky.common.http.HttpAsyncClient;
import com.blue.sky.common.http.HttpResponseCallback;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.assistant.api.AiBangAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SiteFragment extends BaseFragment{


    private View rootView;
    private ListView listView;
    private List<Item> list = new ArrayList<Item>();
    private SimpleCommonListAdapter listAdapter;

    private EditText txtKeyword;
    private TextView btnSearch;
    private String city="广州";

    public SiteFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.assistant_bus_path, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        txtKeyword = (EditText) rootView.findViewById(R.id.txtKeyword);
        btnSearch = (TextView)rootView.findViewById(R.id.btn_opt);
        btnSearch.setOnClickListener(this);
        return rootView;
    }

    private void updateListView() {
        if (listAdapter == null) {
            listAdapter = new SimpleCommonListAdapter(getActivity(), list, 1);
            listView.setAdapter(listAdapter);
        } else {
            listAdapter.notifyDataSetChanged();
        }
    }

    private void parse(String response) {
        try {
            JSONObject json = new JSONObject(response);
            JSONObject lines = json.getJSONObject("stats");
            JSONArray jArray = lines.getJSONArray("stat");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject obj = jArray.getJSONObject(i);
                Item item = new Item();
                item.setItemOne(obj.optString("name"));
                item.setItemThree(obj.optString("xy"));
                item.setItemFive(obj.optString("line_names"));
                list.add(item);
            }
        } catch (JSONException e) {
            Log.e(">>>parse", e.toString());
        }
    }

    private void queryPath(String keyword){
        String url = AiBangAPI.STAT_URL + "&city=" + city + "&q=" + keyword;
        new HttpAsyncClient(url, null, new HttpResponseCallback() {

            @Override
            public void onSuccess(String response) {
                Log.i(">>>queryPath", response);
                list.clear();
                parse(response);
                updateListView();
                UIHelp.closeLoading();
            }

            @Override
            public void onError(String response) {
                Log.e(">>>queryPath onError:", response);
            }
        }).execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_opt:
                if(Strings.isNotEmpty(txtKeyword.getText().toString())){
                    UIHelp.showLoading(getActivity(),"正在查询,请稍后...");
                    queryPath(txtKeyword.getText().toString());
                    UIHelp.hideSoftInputFromWindow(txtKeyword);
                }
                break;
        }
    }
}