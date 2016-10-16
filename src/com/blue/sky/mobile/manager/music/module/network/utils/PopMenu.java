package com.blue.sky.mobile.manager.music.module.network.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.control.download.DownloadHelper;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.video.common.VideoPlayerActivity;

/**
 * Project: IntelliJ IDEA.
 * User: Sky
 * Date: 2014/12/13
 * Time: 15:30
 * Desc:
 */
public class PopMenu {


    public static void showMusicPopWindow(final Context context, View parent, final Song selectedSong) {

        int[] location = new int[2];
        //location [0]--->x坐标,location [1]--->y坐标
        parent.getLocationInWindow(location);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.music_list_menu_item, null);
        // 创建一个PopuWidow对象
        final PopupWindow popupWindow = new PopupWindow(view, windowManager.getDefaultDisplay().getWidth(), UIHelp.dip2px(context, 56));

        TextView music_download;
        TextView music_mv;
        TextView music_share;

        music_download = (TextView) view.findViewById(R.id.music_download);
        music_mv = (TextView) view.findViewById(R.id.music_mv);
        music_share = (TextView) view.findViewById(R.id.music_share);

        if (selectedSong.getMv() != null) {
            music_mv.setVisibility(View.VISIBLE);
        } else {
            music_mv.setVisibility(View.GONE);
        }

        music_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadHelper.startDownload(context.getApplicationContext(), selectedSong);
                popupWindow.dismiss();
            }
        });
        music_mv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSong.getMv()!=null){
                    Intent intent = new Intent(context, VideoPlayerActivity.class);
                    intent.putExtra("path", selectedSong.getMv().getUrl());
                    intent.putExtra("title", selectedSong.getName());
                    context.startActivity(intent);
                }
                popupWindow.dismiss();
            }
        });
        music_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        popupWindow.showAsDropDown(parent, location[0], 0);
    }
}
