package com.blue.sky.mobile.manager.doc.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.doc.model.FileInfo;

import java.io.File;
import java.util.*;


public class FileListAdapter extends BaseAdapter {

    private Context context;
    private List<FileInfo> list;
    private Map<Integer,Boolean> checkItems;
    private int type;

    public FileListAdapter(Context context, List<FileInfo> list, int type) {
        super();
        this.context = context;
        this.list = list;
        this.type = type;
        this.checkItems = new HashMap<Integer, Boolean>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View rootView, ViewGroup parent) {
        ViewHolder holder;
        if (rootView == null) {
            holder = new ViewHolder();
            rootView = View.inflate(context, R.layout.doc_list_item, null);
            holder.name = (TextView) rootView.findViewById(R.id.item_title);
            holder.size = (TextView) rootView.findViewById(R.id.item_size);
            holder.time = (TextView) rootView.findViewById(R.id.item_time);
            holder.path = (TextView) rootView.findViewById(R.id.item_path);
            holder.btnCheck = (CheckBox) rootView.findViewById(R.id.btn_check);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        holder.btnCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(">>>item isChecked:", isChecked+"");
                list.get(position).setChecked(isChecked);
                if(isChecked){
                    checkItems.put(position, isChecked);
                }else{
                    if(checkItems.containsKey(position)){
                        checkItems.remove(position);
                    }
                }
            }
        });

        final FileInfo fileInfo = list.get(position);
        holder.name.setText(fileInfo.getFileName());
        holder.size.setText("大小:" + fileInfo.getFileSize());
        holder.time.setText("时间:" + fileInfo.getCreateTime());
        holder.path.setText(fileInfo.getFilePath());
        holder.btnCheck.setChecked(fileInfo.isChecked());

        return rootView;
    }

    public void checkAll(boolean isChecked){
        checkItems.clear();
        for(int i=0;i<list.size();i++){
            list.get(i).setChecked(isChecked);
            if(isChecked){
                checkItems.put(i, isChecked);
            }
        }
    }

    public Map<Integer,Boolean> getCheckItems(){
        return checkItems;
    }

    class ViewHolder {
        TextView name;
        TextView size;
        TextView time;
        TextView path;
        CheckBox btnCheck;
    }

}