package com.blue.sky.mobile.manager.video.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.common.VideoPlayerActivity;

import java.util.List;


public class VideoListAdapter extends BaseAdapter {

    private Context context;
    private List<VideoInfo> list;
    private int type;

    public VideoListAdapter(Context context, List<VideoInfo> list, int type) {
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
            rootView = View.inflate(context, R.layout.video_list_item, null);
            holder.icon =(ImageView) rootView.findViewById(R.id.item_icon);
            holder.title = (TextView) rootView.findViewById(R.id.item_title);
            holder.size = (TextView) rootView.findViewById(R.id.item_size);
            holder.time = (TextView) rootView.findViewById(R.id.item_time);
            holder.btnPlay = (TextView) rootView.findViewById(R.id.btn_opt);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }
        final VideoInfo videoInfo = list.get(position);
        holder.icon.setImageBitmap(videoInfo.getBitmap());
        holder.title.setText(videoInfo.getTitle());
        holder.time.setText("时间：" + videoInfo.getTime());
        holder.size.setText("大小：" + videoInfo.getSize());
        holder.btnPlay.setText("播放");
        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoPlayerActivity.class);
                intent.putExtra("path", videoInfo.getPath());
                intent.putExtra("title", videoInfo.getTitle());
                context.startActivity(intent);
            }
        });

        return rootView;
    }

    class ViewHolder {
        ImageView icon;
        TextView title;
        TextView size;
        TextView time;
        TextView btnPlay;
    }

}