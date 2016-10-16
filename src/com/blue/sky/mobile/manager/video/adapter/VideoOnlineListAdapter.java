package com.blue.sky.mobile.manager.video.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.common.utils.ImageLoadUtil;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.network.VideoDetailActivity;

import java.util.List;


public class VideoOnlineListAdapter extends BaseAdapter {

    private Context context;
    private List<VideoInfo> list;
    private int type;

    public VideoOnlineListAdapter(Context context, List<VideoInfo> list, int type) {
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
            rootView = View.inflate(context, R.layout.video_online_list_item, null);
            holder.icon = (ImageView) rootView.findViewById(R.id.item_icon);
            holder.title = (TextView) rootView.findViewById(R.id.item_title);
            holder.item_time = (TextView) rootView.findViewById(R.id.item_time);
            holder.item_hitCount = (TextView) rootView.findViewById(R.id.item_hitCount);
            rootView.setTag(holder);
        } else {
            holder = (ViewHolder) rootView.getTag();
        }
        final VideoInfo videoInfo = list.get(position);
        ImageLoadUtil.loadImage(holder.icon, videoInfo.getImageUrl(), ImageLoadUtil.ImageStyle.PHOTO);
        holder.title.setText(videoInfo.getTitle());
        holder.item_time.setText("热度:" + videoInfo.getTime());
        holder.item_hitCount.setText("时长:" + TimeUtil.parseTime(videoInfo.getPlayTimes()));

        rootView.findViewById(R.id.list_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("video", videoInfo);
                context.startActivity(intent);
            }
        });

        return rootView;
    }

    class ViewHolder {
        TextView index;
        ImageView icon;
        TextView title;
        TextView item_time;
        TextView item_hitCount;
    }


}