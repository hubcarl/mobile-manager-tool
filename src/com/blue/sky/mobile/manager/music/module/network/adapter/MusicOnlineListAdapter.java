package com.blue.sky.mobile.manager.music.module.network.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.common.AppMain;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.service.MusicService;
import com.blue.sky.mobile.message.ActionManager;

import java.util.List;


public class MusicOnlineListAdapter extends BaseAdapter {

    private Context context;
    private List<Song> list;
    private int type;

    public MusicOnlineListAdapter(Context context, List<Song> list, int type) {
        super();
        this.context = context;
        this.list = list;
        this.type = type;
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
            rootView = View.inflate(context, R.layout.music_panel_list_item, null);
            holder.title = (TextView) rootView.findViewById(R.id.item_title);
            holder.item_time = (TextView) rootView.findViewById(R.id.item_time);
            holder.item_hitCount = (TextView) rootView.findViewById(R.id.item_hitCount);
            holder.btnOpt = (TextView) rootView.findViewById(R.id.btn_opt);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }

        final Song entity = list.get(position);
        holder.title.setText(entity.getName());
        holder.item_time.setText("时长:" + entity.getDuration());
        if (entity.getMv() != null) {
            holder.item_hitCount.setText("热度:" + entity.getPickCount() + "    MV");
        } else {
            holder.item_hitCount.setText("热度:" + entity.getPickCount());
        }

        final int index = position;
        holder.btnOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicService.MUSIC.init(list, list.get(index), index);
                context.sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_PLAY));
            }
        });
        return rootView;
    }

    class ViewHolder {
        TextView title;
        TextView item_time;
        TextView item_hitCount;
        TextView btnOpt;
    }


}