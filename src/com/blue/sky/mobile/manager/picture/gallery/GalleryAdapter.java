package com.blue.sky.mobile.manager.picture.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Gallery的适配器类，主要用于加载图片
 * @author sky
 *
 */
public class GalleryAdapter extends BaseAdapter {

	private Context context;

	/*图片素材*/
	private ArrayList<String> images;

	public GalleryAdapter(Context context, ArrayList<String> images) {
		this.context = context;
        this.images = images;
	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

        /*产生ImageView对象*/
        ImageView view = new ImageView(context);
        /*设定图片给imageView对象*/
        Bitmap bm = BitmapFactory.decodeFile(images.get(position));

        view.setImageBitmap(bm);
        /*重新设定图片的宽高*/
        //view.setScaleType(ImageView.ScaleType.FIT_XY);
        /*重新设定Layout的宽高*/
        view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

        return view;
	}

}
