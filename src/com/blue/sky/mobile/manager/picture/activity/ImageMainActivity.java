package com.blue.sky.mobile.manager.picture.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.picture.entity.ImageBean;
import com.blue.sky.mobile.manager.picture.adapter.GroupAdapter;
import com.blue.sky.mobile.message.ActionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ImageMainActivity extends BaseActivity{

    private final static int SCAN_OK = 1;
    private final static int DEL_OK = 2;
    private List<String> group = new ArrayList<String>();
    private List<List<String>> child = new ArrayList<List<String>>();
    private List<ImageBean> list = new ArrayList<ImageBean>();

    private ProgressDialog mProgressDialog;
    private GroupAdapter adapter;
    private GridView mGroupGridView;

    private int imageCount = 0;
    private int selectedGroupIndex;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN_OK:
                    //关闭进度条
                    mProgressDialog.dismiss();
                    subGroupOfImage();
                    adapter = new GroupAdapter(ImageMainActivity.this, list, mGroupGridView);
                    mGroupGridView.setAdapter(adapter);
                    setIntCache(EnumUtil.Navigation.Image.toString(), imageCount);
                    sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                    break;
                case DEL_OK:
                    adapter.refresh();
                    showToast("图片删除成功!");
                    setIntCache(EnumUtil.Navigation.Image.toString(), imageCount);
                    sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity_main);

        initGridView();
        initImages();
        UIHelp.setHeaderMenuView(this,"图片管理");
    }

   private void initGridView(){
       mGroupGridView = (GridView) findViewById(R.id.main_grid);
       mGroupGridView.setOnItemClickListener(new OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Intent mIntent = new Intent(ImageMainActivity.this, ShowImageActivity.class);
               mIntent.putExtra("folderName", list.get(position).getFolderName());
               mIntent.putStringArrayListExtra("fileList", (ArrayList<String>)child.get(position));
               startActivity(mIntent);
           }
       });
       mGroupGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               return false;
           }
       });
       registerForContextMenu(mGroupGridView);
   }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 1, 1, getResources().getString(R.string.delete));
        AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedGroupIndex = mi.position;
        Log.i(">>>selectedGroupIndex", selectedGroupIndex+"");
        menu.setHeaderTitle("图片管理");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 1:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(String path : child.get(selectedGroupIndex)){
                            getContentResolver().delete(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                    MediaStore.Images.ImageColumns.DATA + "=?", new String[]{path});
                        }
                        imageCount = imageCount - child.get(selectedGroupIndex).size();
                        group.remove(selectedGroupIndex);
                        child.remove(selectedGroupIndex);
                        list.remove(selectedGroupIndex);
                        mHandler.sendEmptyMessage(DEL_OK);
                    }
                }).start();
                break;

        }
        return super.onContextItemSelected(item);
    }



    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
     */
    private void initImages() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
            return;
        }

        //显示进度条
        mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

        new Thread(new Runnable() {

            @Override
            public void run() {

                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = ImageMainActivity.this.getContentResolver();

//                String str[] = { MediaStore.Images.Media._ID,
//                        MediaStore.Images.Media.DISPLAY_NAME,
//                        MediaStore.Images.Media.DATA};
//                Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, str, null, null, null);
                //只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[] { "image/jpeg", "image/png" }, MediaStore.Images.Media.DATE_MODIFIED);

                while (mCursor.moveToNext()) {
                    //获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    //获取该图片的父路径名
                    File file=new File(path);
                    String parentName = file.getName();
                    if(file.getParentFile()!=null){
                        parentName= file.getParentFile().getName();
                    }

                    //根据父路径名将图片放入到mGruopMap中
                    int index = group.indexOf(parentName);
                    if (index>-1) {
                        child.get(index).add(path);
                        imageCount++;
                    } else {
                        group.add(parentName);
                        List<String> childList = new ArrayList<String>();
                        childList.add(path);
                        child.add(childList);
                    }
                }

                mCursor.close();

                //通知Handler扫描图片完成
                mHandler.sendEmptyMessage(SCAN_OK);

            }
        }).start();

    }


    /**
     * 组装分组界面GridView的数据源，因为我们扫描手机的时候将图片信息放在HashMap中
     * 所以需要遍历HashMap将数据组装成List
     *
     * @return
     */
    private List<ImageBean> subGroupOfImage(){
        for(int i=0;i<group.size();i++){
            ImageBean mImageBean = new ImageBean();
            mImageBean.setFolderName(group.get(i));
            mImageBean.setImageCounts(child.get(i).size());
            //获取该组的第一张图片
            mImageBean.setTopImagePath(child.get(i).get(0));
            list.add(mImageBean);
        }
        return list;
    }
}
