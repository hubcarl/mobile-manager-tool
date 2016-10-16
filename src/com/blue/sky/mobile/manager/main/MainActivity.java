package com.blue.sky.mobile.manager.main;

import android.app.DownloadManager;
import android.content.*;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.blue.sky.common.activity.BaseActivity;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.ApkMainActivity;
import com.blue.sky.mobile.manager.assistant.AssistantActivity;
import com.blue.sky.mobile.manager.doc.DocumentMainActivity;
import com.blue.sky.mobile.manager.file.FileManagerActivity;
import com.blue.sky.mobile.manager.game.GameListActivity;
import com.blue.sky.mobile.manager.music.module.MusicMainActivity;
import com.blue.sky.mobile.manager.picture.activity.ImageMainActivity;
import com.blue.sky.mobile.manager.center.setting.ProfileCenterMainActivity;
import com.blue.sky.mobile.manager.video.VideoMainActivity;
import com.blue.sky.mobile.message.ActionManager;


public class MainActivity extends BaseActivity {

    private GridView gridView;
    private NavigationAdapter navigationAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        initGridView();
        registerReceiver();
	}

    private void initGridView(){
        navigationAdapter = new NavigationAdapter(this);
        gridView = (GridView) findViewById(R.id.gridView);
        gridView.setNumColumns(2);
        gridView.setVerticalSpacing(5);
        gridView.setHorizontalSpacing(5);
        gridView.setAdapter(navigationAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        startActivity(AssistantActivity.class);
                        break;
                    case 1:
                        startActivity(FileManagerActivity.class);
                        break;
                    case 2 :
                        startActivity(ApkMainActivity.class);
                        break;
                    case 3:
                        startActivity(ImageMainActivity.class);
                        break;
                    case 4 :
                        startActivity(MusicMainActivity.class);
                        break;
                    case 5 :
                        startActivity(VideoMainActivity.class);
                        break;
                    case 6:
                        startActivity(DocumentMainActivity.class);
                        break;
                    case 7:
                        startActivity(GameListActivity.class);
                        break;
                }
            }
        });

        gridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 隐藏软键盘
                if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
                    if (getCurrentFocus() != null)
                        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }





    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(">>>downloadCompleteReceiver:", "test");
            String title = intent.getStringExtra("title");
            String author = intent.getStringExtra("author");
            String path = intent.getStringExtra("path");
            String mineType = intent.getStringExtra("mineType");
            Log.d(">>>downloadCompleteReceiver title:", title);
            Log.d(">>>downloadCompleteReceiver author:", author);
            Log.d(">>>downloadCompleteReceiver path:", path);
            Log.d(">>>downloadCompleteReceiver mineType:", mineType);
            addMusicToMediaStore(title, author, mineType,path);
        }
    };

    protected void addMusicToMediaStore(String title, String author, String mineType, String path)
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.TITLE, title);
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (System.currentTimeMillis() / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, mineType);
        values.put(MediaStore.Audio.Media.ARTIST, author);
        values.put(MediaStore.Audio.Media.DATA, path);
        values.put(MediaStore.Audio.Media.IS_MUSIC, 1);
        Uri newUri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
    }



    private void registerReceiver(){
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        registerReceiver(downloadCompleteReceiver, filter);

        IntentFilter filterReceiver = new IntentFilter(ActionManager.ACTION_FILE_CHANGED);
        registerReceiver(fileChangeReceiver, filterReceiver);
    }

    private BroadcastReceiver fileChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            navigationAdapter.refresh();
        }
    };


    @Override
    protected void onDestroy(){

        if(downloadCompleteReceiver!=null){
            unregisterReceiver(downloadCompleteReceiver);
        }
        if(fileChangeReceiver!=null){
            unregisterReceiver(fileChangeReceiver);
        }
        super.onDestroy();
    }
}
