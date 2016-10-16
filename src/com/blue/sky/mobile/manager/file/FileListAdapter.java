package com.blue.sky.mobile.manager.file;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.file.common.FileInfo;
import com.blue.sky.mobile.manager.file.common.FileType;

import java.util.List;


public class FileListAdapter extends BaseAdapter {

    private Context context;
    private List<FileInfo> list;
    private ItemListener listener;

    public FileListAdapter(Context context, List<FileInfo> list, final ItemListener listener) {
        super();
        this.context = context;
        this.list = list;
        this.listener = listener;
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
    public View getView(int position, View rootView, ViewGroup parent) {
        ViewHolder holder;
        if (rootView == null) {
            holder = new ViewHolder();
            rootView = View.inflate(context, R.layout.search_result_gridview_item, null);
            holder.item =(RelativeLayout) rootView.findViewById(R.id.search_item);
            holder.icon =(ImageView) rootView.findViewById(R.id.icon);
            holder.name = (TextView) rootView.findViewById(R.id.name);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }
        final FileInfo fileInfo = list.get(position);
        holder.icon.setImageResource(FileType.getResId(fileInfo.getType()));
        holder.name.setText(fileInfo.getName());
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v,fileInfo);
            }
        });
        return rootView;
    }

    class ViewHolder {
        RelativeLayout item;
        ImageView icon;
        TextView name;
    }

    public interface ItemListener{
        void onClick(View v, FileInfo fileInfo);
    }
}