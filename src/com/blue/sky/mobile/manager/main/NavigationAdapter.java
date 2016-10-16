package com.blue.sky.mobile.manager.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.mobile.manager.R;

import java.util.ArrayList;

/**
 * Created by bluesky on 2014/9/15.
 */
public class NavigationAdapter extends BaseAdapter {

    private ArrayList<ImageInfo> imageList; // 菜单数据
    private Context context;

    public NavigationAdapter(Context context) {
        this.context = context;
        imageList = new ArrayList<ImageInfo>();
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.Assistant, R.drawable.icon_assistant, R.drawable.icon_bg01));
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.File, R.drawable.icon_file, R.drawable.icon_bg01));
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.App, R.drawable.icon_app, R.drawable.icon_bg02));
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.Image, R.drawable.icon_picture, R.drawable.icon_bg02));
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.Music, R.drawable.icon_music, R.drawable.icon_bg02));
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.Video, R.drawable.icon_video, R.drawable.icon_bg02));
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.Doc, R.drawable.icon_doc, R.drawable.icon_bg02));
        imageList.add(new ImageInfo(context, EnumUtil.Navigation.Game, R.drawable.icon_game_nav, R.drawable.icon_bg02));
    }

    public void refresh(){
        for(ImageInfo info : imageList){
            info.refresh(context);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.adapter_navigation_gridview_item, null);

        ImageView iv = (ImageView) rootView.findViewById(R.id.imageView1);
        RelativeLayout relativeLayout = (RelativeLayout) rootView.findViewById(R.id.relativeLayout);
        TextView txtName = (TextView) rootView.findViewById(R.id.name);
        //TextView txtDesc = (TextView) rootView.findViewById(R.id.desc);

        ImageInfo imageInfo = imageList.get(position);
        iv.setImageResource(imageInfo.imageId);

        if (Strings.isEmpty(imageInfo.desc) || "0".equals(imageInfo.desc)) {
            txtName.setText(imageInfo.navigation.name);
        } else {
            txtName.setText(imageInfo.navigation.name + "("+imageInfo.desc+")");
        }

        relativeLayout.setBackgroundResource(imageInfo.bgId);
        relativeLayout.getBackground().setAlpha(225);

        return rootView;
    }

}
