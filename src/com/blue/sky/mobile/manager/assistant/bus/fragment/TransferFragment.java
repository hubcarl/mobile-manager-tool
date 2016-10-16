package com.blue.sky.mobile.manager.assistant.bus.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.blue.sky.common.adapter.SimpleCommonListAdapter;
import com.blue.sky.common.component.util.FileUtils;
import com.blue.sky.common.entity.Item;
import com.blue.sky.common.fragment.BaseFragment;
import com.blue.sky.common.http.HttpAsyncClient;
import com.blue.sky.common.http.HttpResponseCallback;
import com.blue.sky.common.ui.DialogListener;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.utils.FileUtil;
import com.blue.sky.mobile.manager.assistant.api.AiBangAPI;
import com.blue.sky.mobile.manager.assistant.bus.activity.PathDetailActivity;
import com.blue.sky.mobile.manager.doc.adapter.FileListAdapter;
import com.blue.sky.mobile.manager.doc.model.FileInfo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.*;


public class TransferFragment extends BaseFragment implements View.OnClickListener {

    private View rootView;
    private ListView listView;
    private List<Item> list = new ArrayList<Item>();
    private SimpleCommonListAdapter listAdapter;

    private EditText edit_start;
    private EditText edit_end;

    private TextView btnSearch;
    private String city = "广州";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.assistant_bus_transfer, container, false);
        listView = (ListView) rootView.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(PathDetailActivity.class,"item", list.get(position));
            }
        });
        edit_start = (EditText) rootView.findViewById(R.id.edit_start);
        edit_end = (EditText) rootView.findViewById(R.id.edit_end);
        btnSearch = (TextView) rootView.findViewById(R.id.btn_opt);
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
            JSONObject lines = json.getJSONObject("buses");
            JSONArray jArray = lines.getJSONArray("bus");
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject seg = jArray.getJSONObject(i);
                JSONObject segment = seg.getJSONObject("segments");
                JSONArray segments = segment.getJSONArray("segment");
                for(int j=0;j<segments.length();j++){
                    JSONObject obj = segments.getJSONObject(j);
                    Item item = new Item();
                    item.setItemOne(obj.optString("line_name"));
                    item.setItemThree("总距离: " + seg.optString("dist")
                            + "米&nbsp;&nbsp;&nbsp;&nbsp;耗费时间: " + seg.optString("time") + "分");
                    item.setItemFive(obj.optString("stats"));
                    list.add(item);
                }
            }
        } catch (JSONException e) {
            Log.e(">>>parse", e.toString());
        }
    }

    private void queryPath(String start, String end) {
        String url = AiBangAPI.TRANSFER_URL + "&city=" + city
                + "&start_addr=" + start
                + "&end_addr=" + end;
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
        switch (v.getId()) {
            case R.id.btn_opt:
                if (Strings.isNotEmpty(edit_start.getText().toString())
                        && Strings.isNotEmpty(edit_end.getText().toString())) {
                    UIHelp.showLoading(getActivity(), "正在查询,请稍后...");
                    queryPath(edit_start.getText().toString(), edit_end.getText().toString());
                    UIHelp.hideSoftInputFromWindow(edit_end);
                }
                break;
        }
    }
}