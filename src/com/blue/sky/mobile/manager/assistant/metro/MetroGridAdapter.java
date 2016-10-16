package com.blue.sky.mobile.manager.assistant.metro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import com.blue.sky.common.utils.ImageLoadUtil;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.picture.control.MyImageView;
import com.blue.sky.mobile.manager.picture.control.NativeImageLoader;
import com.blue.sky.mobile.manager.picture.entity.ImageBean;

import java.util.List;

public class MetroGridAdapter extends BaseAdapter{
    private List<CityMetro> list;
    private Point mPoint = new Point(0, 0);
    private GridView mGridView;
    protected LayoutInflater mInflater;

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

    public MetroGridAdapter(Context context, List<CityMetro> list, GridView mGridView){
        this.list = list;
        this.mGridView = mGridView;
        mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        CityMetro metro = list.get(position);
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.assistant_metro_grid_item, null);
            viewHolder.mImageView = (MyImageView) convertView.findViewById(R.id.imageUrl);
            viewHolder.mTextViewTitle = (TextView) convertView.findViewById(R.id.name);
            viewHolder.mImageView.setOnMeasureListener(new MyImageView.OnMeasureListener() {

                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width, height);
                }
            });
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mTextViewTitle.setText(metro.getCityName());
        ImageLoadUtil.loadImage(viewHolder.mImageView, metro.getImageUrl(), ImageLoadUtil.ImageStyle.BIG_LOGO);

        return convertView;
    }

    public void refresh(){
        this.notifyDataSetChanged();
    }


    public static class ViewHolder{
        public MyImageView mImageView;
        public TextView mTextViewTitle;
    }


}
