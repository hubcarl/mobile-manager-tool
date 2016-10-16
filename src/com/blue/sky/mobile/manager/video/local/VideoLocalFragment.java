package com.blue.sky.mobile.manager.video.local;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.blue.sky.common.fragment.BaseFragment;
import com.blue.sky.common.ui.DialogListener;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.SharedPreferenceUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.apk.model.AppInfo;
import com.blue.sky.mobile.manager.video.adapter.VideoListAdapter;
import com.blue.sky.mobile.manager.video.common.VideoInfo;
import com.blue.sky.mobile.manager.video.common.VideoPlayerActivity;
import com.blue.sky.mobile.message.ActionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/10/14.
 */
public class VideoLocalFragment extends BaseFragment {

    private View rootView;
    private ListView listView;
    private List<VideoInfo> videoList = new ArrayList<VideoInfo>();
    private VideoListAdapter videoListAdapter;
    private LinearLayout loading;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    videoList.add((VideoInfo) msg.obj);
                    videoListAdapter.notifyDataSetChanged();
                    break;
                case MSG_FINISHED:
                    loading.setVisibility(View.GONE);
                    SharedPreferenceUtil.getInstance(getActivity()).putInt(EnumUtil.Navigation.Video.toString(), videoList.size());
                    getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));

                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.sky_activity_common_list, container, false);

        loading = (LinearLayout) rootView.findViewById(R.id.loading);

        listView = (ListView) rootView.findViewById(R.id.list_view);
        videoListAdapter = new VideoListAdapter(getActivity(), videoList, 1);
        listView.setAdapter(videoListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoInfo video = videoList.get(position);
                Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
                intent.putExtra("path", video.getPath());
                intent.putExtra("title", video.getTitle());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
             @Override
             public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                 final VideoInfo videoInfo = videoList.get(position);
                 UIHelp.confirmDialog(getActivity(), "温馨提示", "确认删除视频["+videoInfo.getTitle()+"]吗?", "确认", "取消", true, new DialogListener(){

                     @Override
                     public void onConfirm(Dialog dialog, View v) {
                         getActivity().getContentResolver().delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,MediaStore.Video.Media._ID + "=?",new String[]{videoInfo.getId()});
                         File selectedFile = new File(videoInfo.getPath());
                         selectedFile.delete();
                         videoList.remove(position);
                         videoListAdapter.notifyDataSetChanged();
                         UIHelp.showToast(getActivity(), "删除成功!");
                         SharedPreferenceUtil.getInstance(getActivity()).putInt(EnumUtil.Navigation.Video.toString(), videoList.size());
                         getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));

                         super.onConfirm(dialog, v);
                     }

                     @Override
                     public void onCancel(Dialog dialog, View v) {
                         super.onCancel(dialog, v);
                     }

                 });
                return true;
             }
          }


        );

        initVideoList();

        return rootView;
    }

    private void initVideoList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadVideo();
            }
        }).start();
    }

    public void loadVideo() {
        // MediaStore.Video.Thumbnails.DATA:视频缩略图的文件路径
        String[] thumbColumns = {MediaStore.Video.Thumbnails.DATA,
                MediaStore.Video.Thumbnails.VIDEO_ID};

        // MediaStore.Video.Media.DATA：视频文件路径；
        // MediaStore.Video.Media.DISPLAY_NAME : 视频文件名，如 testVideo.mp4
        // MediaStore.Video.Media.TITLE: 视频标题 : testVideo
        String[] mediaColumns = {MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DISPLAY_NAME};

        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Video.Media.TITLE,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.ARTIST,
                        MediaStore.Video.Media._ID,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.MIME_TYPE,
                        MediaStore.Video.Media.DISPLAY_NAME},
                null, null, null
        );

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        ContentResolver contentResolver = getActivity().getContentResolver();

        if (cursor.moveToFirst()) {
            do {
                VideoInfo info = new VideoInfo();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                Cursor thumbCursor = getActivity().managedQuery(MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
                        thumbColumns, MediaStore.Video.Thumbnails.VIDEO_ID + "=" + id, null, null);
                if (thumbCursor.moveToFirst()) {
                    info.setmImageUrl(thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Video.Thumbnails.DATA)));
                }
                info.setId(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)));
                info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                info.setBitmap(MediaStore.Video.Thumbnails.getThumbnail(contentResolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, options));
                info.setMineType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
                sendMessage(handler, MSG_REFRESH, info);
            } while (cursor.moveToNext());
            sendMessage(handler, MSG_FINISHED, null);
        }
    }
}