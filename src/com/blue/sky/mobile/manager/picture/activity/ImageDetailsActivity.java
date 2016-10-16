package com.blue.sky.mobile.manager.picture.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.picture.control.ZoomImageView;

/**
 * 查看大图的Activity界面。
 *
 */
public class ImageDetailsActivity extends Activity {

	/**
	 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
	 */
	private ZoomImageView zoomImageView;

	/**
	 * 待展示的图片
	 */
	private Bitmap bitmap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view_details);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		// 取出图片路径，并解析成Bitmap对象，然后在ZoomImageView中显示
		String imagePath = getIntent().getStringExtra("image_path");
		bitmap = BitmapFactory.decodeFile(imagePath);
		zoomImageView.setImageBitmap(bitmap);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 记得将Bitmap对象回收掉
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

}