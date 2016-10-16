package com.blue.sky.mobile.manager.picture.activity;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.picture.control.crop.CropHandler;
import com.blue.sky.mobile.manager.picture.control.crop.CropHelper;
import com.blue.sky.mobile.manager.picture.control.crop.CropParams;
import com.blue.sky.mobile.manager.picture.gallery.GalleryAdapter;
import com.blue.sky.mobile.manager.picture.gallery.QGallery;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

public class GalleryActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "GalleryActivity";
    //屏幕的宽度
    public static int screenWidth;

    //屏幕的高度
    public static int screenHeight;

    private QGallery gallery;

    private ArrayList<String> list;
    private int image_position;
    private TextView image_progress;
    private Bitmap bmp = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_gallery);
        initGallery();
        initEvent();
    }

    private void initGallery() {
        list = getIntent().getStringArrayListExtra("image_list");
        image_position = getIntent().getIntExtra("image_position", 0);

        image_progress = (TextView) findViewById(R.id.image_progress);

        gallery = (QGallery) findViewById(R.id.gallery);
        gallery.setVerticalFadingEdgeEnabled(false);
        // 设置view在水平滚动时，水平边不淡出。
        gallery.setHorizontalFadingEdgeEnabled(false);
        gallery.setAdapter(new GalleryAdapter(this, list));
        gallery.setSelection(image_position);
        gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                image_progress.setText((position + 1) + "/" + list.size());
                image_position= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        image_progress.setText((image_position + 1) + "/" + list.size());
    }

    private void initEvent() {
        findViewById(R.id.btn_share).setOnClickListener(this);
        findViewById(R.id.btn_crop).setOnClickListener(this);
        findViewById(R.id.btn_background).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_share:
                break;
            case R.id.btn_crop:
                startPhotoZoom(Uri.fromFile(new File(list.get(image_position))));
                break;
            case R.id.btn_background:
                //http://www.cnblogs.com/zgz345/archive/2013/01/08/2851204.html
                UIHelp.showLoading(this,"正在设置...");
                BitmapFactory.Options bfOptions = new BitmapFactory.Options();
                bfOptions.inDither = false;
                bfOptions.inPurgeable = true;
                bfOptions.inTempStorage = new byte[12 * 1024];
                bfOptions.inJustDecodeBounds = false;// true 只生成边框
                File file = new File(list.get(image_position));
                FileInputStream fs = null;
                try {
                    fs = new FileInputStream(file);
                    if (fs != null){
                        //bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
                        bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
                        if(bmp==null){
                            Log.e(">>>WallPaper1", "decodeFileDescriptor failed, bmp is null");
                            showToast("设置壁纸失败,换张图片试试吧!");
                        }else{
                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                            try {
                                wallpaperManager.setBitmap(bmp);
                                showToast("成功设置为壁纸");
                            } catch (IOException e) {
                                Log.e(">>>WallPaper2", e.toString());
                                showToast("设置壁纸失败,换张图片试试吧!");
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.e(">>>WallPaper3", e.toString());
                    showToast("设置壁纸失败,换张图片试试吧!");
                }
                UIHelp.closeLoading();
                break;
        }
    }

    @Override
    protected void onDestroy(){
        if(bmp!=null && !bmp.isRecycled()){
            bmp.recycle();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // 如果是直接从相册获取
            case 1:
                if (data != null) {
                    setPicToView(data);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 1);
    }

    /**
     * 保存裁剪之后的图片数据
     */
    private void setPicToView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            saveBitmap(photo, TimeUtil.dateToStrLong(new Date()) + ".jpg");
            /**
             * 下面注释的方法是将裁剪之后的图片以Base64Coder的字符方式上
             * 传到服务器，QQ头像上传采用的方法跟这个类似
             */

            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 60, stream);
            byte[] b = stream.toByteArray();
            // 将图片流以字符串形式存储下来

            tp = new String(Base64Coder.encodeLines(b));
            这个地方大家可以写下给服务器上传图片的实现，直接把tp直接上传就可以了，
            服务器处理的方法是服务器那边的事了，吼吼

            如果下载到的服务器的数据还是以Base64Coder的形式的话，可以用以下方式转换
            为我们可以用的图片类型就OK啦...吼吼
            Bitmap dBitmap = BitmapFactory.decodeFile(tp);
            Drawable drawable = new BitmapDrawable(dBitmap);
            */

        }
    }

    /**
     * 保存方法
     */
    public void saveBitmap(Bitmap bitmap, String fileName) {

        String firstDir = "MobileManager";
        String secondDir = "Crop";
        if (createDir(firstDir, secondDir)) {
            try {
                File f = new File(Environment.getExternalStorageDirectory() +
                        File.separator + firstDir + File.separator + secondDir + File.separator, fileName);
                FileOutputStream out = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                showToast("裁剪成功!");
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(f)));
            } catch (FileNotFoundException e) {
                showToast("裁剪失败!");
                Log.e(TAG, "保存失败:" + e.toString());
            } catch (IOException e) {
                showToast("裁剪失败!");
                Log.e(TAG, "保存失败:" + e.toString());
            }
        } else {
            Log.e(TAG, "裁剪失败");
        }
    }

    private boolean createDir(String firstDir, String secondDir) {

        //检查手机上是否有外部存储卡
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if (!sdCardExist) {//如果不存在SD卡，进行提示
            Toast.makeText(this, "请插入外部SD存储卡", Toast.LENGTH_SHORT).show();
            return false;
        } else {//如果存在SD卡，判断文件夹目录是否存在
            //一级目录和二级目录必须分开创建

            File dirFirstFile = new File(Environment.getExternalStorageDirectory() +
                    File.separator + firstDir + File.separator);//新建一级主目录
            if (!dirFirstFile.exists()) {//判断文件夹目录是否存在
                dirFirstFile.mkdir();//如果不存在则创建
            }
            File dirSecondFile = new File(Environment.getExternalStorageDirectory() +
                    File.separator + firstDir + File.separator + secondDir + File.separator);//新建二级主目录
            if (!dirSecondFile.exists()) {//判断文件夹目录是否存在
                dirSecondFile.mkdir();//如果不存在则创建
            }
            return true;
        }
    }
}