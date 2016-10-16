
package com.blue.sky.mobile.manager.music.module.local;

import android.content.*;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.blue.sky.common.fragment.BaseFragment;
import com.blue.sky.common.utils.EnumUtil;
import com.blue.sky.common.utils.SharedPreferenceUtil;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.Music;
import com.blue.sky.mobile.manager.music.cache.MusicDBHelper;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.module.adapter.MusicListAdapter;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.service.MusicService;
import com.blue.sky.mobile.message.ActionManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MusicAllListFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    private final static int ADD_TO_LOVE=11;
    private final static int MENU_DELETE=12;

    private ListView mListView;
    private MusicListAdapter listAdapter;
    private List<Song> musicList = new ArrayList<Song>();
    private Song selectedMusic;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:
                    musicList.add((Song) msg.obj);
                    listAdapter.notifyDataSetChanged();
                    break;
                case MSG_FINISHED:
                    SharedPreferenceUtil.getInstance(getActivity()).putInt(EnumUtil.Navigation.Music.toString(), musicList.size());
                    getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                    break;
            }
        }
    };

    public MusicAllListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.music_listview, container, false);

        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mListView.setOnCreateContextMenuListener(this);
        mListView.setOnItemClickListener(this);
        listAdapter = new MusicListAdapter(getActivity(), musicList, 1);
        mListView.setAdapter(listAdapter);


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadMusic();
            }
        }).start();

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.FIRST, ADD_TO_LOVE, 0, getResources().getString(R.string.add_to_love));
        menu.add(Menu.FIRST, MENU_DELETE, 1, getResources().getString(R.string.delete));
        AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedMusic = musicList.get(mi.position);
        menu.setHeaderTitle(selectedMusic.getName());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case ADD_TO_LOVE:
                ContentValues value = new ContentValues();
                value.put("id", selectedMusic.getId());
                value.put("name", selectedMusic.getName());
                value.put("filePath", selectedMusic.getPlayUrl());
                value.put("thumbPath",selectedMusic.getArtist());
                value.put("createDate", TimeUtil.dateToStrLong(new Date()));
                Log.i(">>>music", value.toString());
                MusicDBHelper.addFavoriteMusic(getActivity(), value);
                UIHelp.showToast(getActivity(), "添加成功");

                Intent intent = new Intent();
                Bundle mBundle = new Bundle();
                mBundle.putParcelable("music", value);
                intent.putExtras(mBundle);
                intent.setAction(ActionManager.ACTION_MSG_MUSIC_CHANGED);
                getActivity().sendBroadcast(intent);
                break;

            case MENU_DELETE:
                getActivity().getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID + "=" + selectedMusic.getId(),null);
                File selectedFile = new File(selectedMusic.getPlayUrl());
                selectedFile.delete();
                musicList.remove(selectedMusic);
                listAdapter.notifyDataSetChanged();
                UIHelp.showToast(getActivity(),"[" + selectedMusic.getName() + "]删除成功");
                SharedPreferenceUtil.getInstance(getActivity()).putInt(EnumUtil.Navigation.Music.toString(), musicList.size());
                getActivity().sendBroadcast(new Intent(ActionManager.ACTION_FILE_CHANGED));
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicService.MUSIC.init(musicList, musicList.get(position), position);
        getActivity().sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_PLAY));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public void loadMusic(){
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{ MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.DURATION,    //音频文件播放时长
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DATA,
                        MediaStore.Audio.Media.ALBUM_ID,
                        MediaStore.Audio.Media.ALBUM},        //音频文件路径
                null, null, null
        );

        if(cursor.moveToFirst()){
            do{
                Song m = new Song();
                m.setId(cursor.getLong(0) + "");
                m.setName(cursor.getString(1));
                m.setMDuration(cursor.getLong(2));
                m.setArtist(cursor.getString(3));
                m.setSingleList(cursor.getString(4), cursor.getString(3));
                m.setAlbumId(cursor.getString(5));
                m.setAlbumName(cursor.getString(6));
                sendMessage(handler, MSG_REFRESH, m);
            }while(cursor.moveToNext());
            sendMessage(handler, MSG_FINISHED, null);
        }
        cursor.close();
    }
}
