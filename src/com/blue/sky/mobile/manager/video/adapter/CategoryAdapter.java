package com.blue.sky.mobile.manager.video.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.common.Category;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.common.VideoPlayerActivity;

import java.util.List;


public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> list;

    public CategoryAdapter(Context context,List<Category> list) {
        super();
        this.context = context;
        this.list = list;
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
            rootView = View.inflate(context, R.layout.category_item, null);
            holder.name = (TextView) rootView.findViewById(R.id.name);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }
        final Category category = list.get(position);
        holder.name.setText(category.getCategoryName());
        return rootView;
    }

    class ViewHolder {
        TextView name;
    }

}