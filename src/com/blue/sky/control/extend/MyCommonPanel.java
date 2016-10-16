package com.blue.sky.control.extend;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.blue.sky.common.utils.ImageLoadUtil;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.network.VideoDetailActivity;
import com.blue.sky.mobile.manager.video.network.VideoOnlineListActivity;

import java.util.List;


/**
 * Created by Administrator on 2014/7/27.
 */
public class MyCommonPanel extends LinearLayout {

    protected final Context ctx;
    protected LayoutInflater mInflater;
    protected View rootView;
    protected TextView btnMore;
    protected LinearLayout myContainer;
    private String categoryId;
    private String categoryName;


    public MyCommonPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        this.mInflater = LayoutInflater.from(context);
        rootView = mInflater.inflate(R.layout.sky_activity_common_panel_extend, this);
        btnMore  = (TextView)rootView.findViewById(R.id.btn_more);
        myContainer  = (LinearLayout)rootView.findViewById(R.id.myContainer);

        btnMore.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, VideoOnlineListActivity.class);
                intent.putExtra("categoryId", categoryId);
                intent.putExtra("categoryName", categoryName);
                ctx.startActivity(intent);
            }
        });
    }

    /*
     LayoutInflater 相当于一个“布局加载器”，有三种方式可以从系统中获取到该布局加载器对象，如：

    方法一： LayoutInflater.from(this);

    方法二： (LayoutInflater)this.getSystemService(this.LAYOUT_INFLATER_SERVICE);

    方法三： this.getLayoutInflater();
     */
    public void refresh(final List<VideoInfo> tempList)
    {
        if (tempList != null && tempList.size() > 0) {
            myContainer.removeAllViews();
            for(int i=0;i<tempList.size();i++){
                View convertView = LayoutInflater.from(this.ctx).inflate(R.layout.sky_panel_list_item, null);
                ViewHolder holder = new ViewHolder();
                holder.index = (TextView) convertView.findViewById(R.id.item_index);
                holder.icon =(ImageView) convertView.findViewById(R.id.item_icon);
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.item_time = (TextView) convertView.findViewById(R.id.item_time);
                holder.item_hitCount = (TextView) convertView.findViewById(R.id.item_hitCount);
                VideoInfo entity = tempList.get(i);

                ImageLoadUtil.loadImage(holder.icon, entity.getImageUrl(), ImageLoadUtil.ImageStyle.PHOTO);
                holder.title.setText(entity.getTitle());
                holder.item_time.setText("热度:" + entity.getTime());
                holder.item_hitCount.setText("时长:" + TimeUtil.parseTime(entity.getPlayTimes()));
                holder.index.setText(i+"");

                convertView.findViewById(R.id.list_item).setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        TextView tx = (TextView)v.findViewById(R.id.item_index);
                        int index = Integer.valueOf(tx.getText().toString());
                        Intent intent = new Intent(ctx, VideoDetailActivity.class);
                        intent.putExtra("video", tempList.get(index));
                        ctx.startActivity(intent);
                    }
                });
                myContainer.addView(convertView);
            }
        }
    }

    class ViewHolder {
        TextView index;
        ImageView icon;
        TextView title;
        TextView item_time;
        TextView item_hitCount;
    }

    public void setTitle(String title){
        ((TextView)rootView.findViewById(R.id.title)).setText(title);
    }

    public void init(String categoryName, String categoryId){
        setTitle(categoryName);
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public void setMoreVisible(int visible)
    {
        btnMore.setVisibility(visible);
    }
}
