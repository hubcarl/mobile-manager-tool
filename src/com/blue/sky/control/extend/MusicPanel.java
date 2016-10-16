package com.blue.sky.control.extend;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.*;
import com.blue.sky.common.AppMain;
import com.blue.sky.common.component.util.PackageUtils;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.control.download.DownloadHelper;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.Music;
import com.blue.sky.mobile.manager.music.module.network.activity.MusicOnlineListActivity;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.utils.PopMenu;
import com.blue.sky.mobile.manager.music.service.MusicService;
import com.blue.sky.mobile.manager.video.common.VideoPlayerActivity;
import com.blue.sky.mobile.message.ActionManager;

import java.util.List;


/**
 * Created by Administrator on 2014/7/27.
 */
public class MusicPanel extends LinearLayout{

    protected final Context ctx;
    protected LayoutInflater mInflater;
    protected View rootView;
    protected TextView btnMore;
    protected LinearLayout myContainer;
    private String categoryId;
    private String categoryName;
    private PopupWindow popupWindow;
    private Song selectedSong;

    public MusicPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.ctx = context;
        this.mInflater = LayoutInflater.from(context);
        rootView = mInflater.inflate(R.layout.sky_activity_common_panel_extend, this);
        btnMore = (TextView) rootView.findViewById(R.id.btn_more);
        myContainer = (LinearLayout) rootView.findViewById(R.id.myContainer);

        btnMore.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, MusicOnlineListActivity.class);
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
    public void refresh(final List<Song> tempList) {
        if (tempList != null && tempList.size() > 0) {
            myContainer.removeAllViews();
            for (int i = 0; i < tempList.size(); i++) {
                View convertView = LayoutInflater.from(this.ctx).inflate(R.layout.music_panel_list_item, null);
                ViewHolder holder = new ViewHolder();
                holder.index = (TextView) convertView.findViewById(R.id.item_index);
                holder.title = (TextView) convertView.findViewById(R.id.item_title);
                holder.item_time = (TextView) convertView.findViewById(R.id.item_time);
                holder.item_hitCount = (TextView) convertView.findViewById(R.id.item_hitCount);
                holder.btnOpt = (TextView) convertView.findViewById(R.id.btn_opt);

                final Song entity = tempList.get(i);
                holder.title.setText(entity.getName());
                holder.item_time.setText("时长:" + entity.getDuration());
                if (entity.getMv() != null) {
                    holder.item_hitCount.setText("热度:" + entity.getPickCount() + "    MV");
                } else {
                    holder.item_hitCount.setText("热度:" + entity.getPickCount());
                }
                holder.index.setText(i + "");

                convertView.findViewById(R.id.list_item).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPopMenu(v, tempList);
//                        Intent intent = new Intent(ActionManager.ACTION_MSG_MUSIC_NETWORK_PLAY);
//                        intent.putExtra("from",ctx.getClass().getSimpleName());
//                        intent.putExtra("music",s);
//                        ctx.sendBroadcast(intent);
//                        AppMain.MUSIC.init(tempList, s, index);
//                        Intent intent = new Intent(ctx, VideoPlayerActivity.class);
//                        intent.putExtra("path", s.getMv().getUrl());
//                        intent.putExtra("title", s.getName());
//                        ctx.startActivity(intent);
//                        if(entity.getMv()!=null){
//                            Log.i(">>>url mv:", s.getMv().getUrl());
//
//                        }else{
//                            Intent intent = new Intent();
//                            Uri uri = Uri.parse(s.getUrlList().get(0).getUrl());
//                            intent.setDataAndType(uri, "audio/*");
//                            intent.putExtra("path", s.getUrlList().get(0).getUrl());
//                            intent.putExtra("title", s.getName());
//                            intent.setAction(Intent.ACTION_VIEW);
//                            ctx.startActivity(intent);
//                        }
                    }
                });
                holder.btnOpt.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        TextView txtIndex  = (TextView)((RelativeLayout)v.getParent()).findViewById(R.id.item_index);
                        int position = Integer.valueOf(txtIndex.getText().toString());
                        MusicService.MUSIC.init(tempList, tempList.get(position), position);
                        ctx.sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_PLAY));
                    }

                });
                myContainer.addView(convertView);
            }
        }
    }

    private void showPopMenu(View v, List<Song> tempList) {
        TextView tx = (TextView) v.findViewById(R.id.item_index);
        int index = Integer.valueOf(tx.getText().toString());
        selectedSong = tempList.get(index);
        PopMenu.showMusicPopWindow(ctx, v, selectedSong);
    }

    class ViewHolder {
        TextView index;
        ImageView icon;
        TextView title;
        TextView item_time;
        TextView item_hitCount;
        TextView btnOpt;
    }

    public void setTitle(String title) {
        ((TextView) rootView.findViewById(R.id.title)).setText(title);
    }

    public void init(String categoryName, String categoryId) {
        setTitle(categoryName);
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public void setMoreVisible(int visible) {
        btnMore.setVisibility(visible);
    }

    public String getCategoryId(){
        return this.categoryId;
    }
}
