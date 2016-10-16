
package com.blue.sky.mobile.manager.music.module.local;

import android.content.*;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ListView;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.Music;
import com.blue.sky.mobile.manager.music.cache.MusicDBHelper;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.module.adapter.MusicListAdapter;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.service.ApolloService;
import com.blue.sky.mobile.manager.music.service.MusicService;
import com.blue.sky.mobile.message.ActionManager;

import java.util.ArrayList;
import java.util.List;

public class MusicLoveListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final static int MENU_SHARE=1;
    private final static int MENU_DELETE=2;

    private ListView mListView;
    private MusicListAdapter listAdapter;
    private List<Song> musicList = new ArrayList<Song>();
    private Song selectedMusic;

    public MusicLoveListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.music_listview, container, false);

        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mListView.setOnCreateContextMenuListener(this);
        mListView.setOnItemClickListener(this);
        musicList.addAll(MusicDBHelper.getFavoriteMusicList(getActivity()));
        listAdapter = new MusicListAdapter(getActivity(), musicList, 1);
        mListView.setAdapter(listAdapter);

        registerMessage();

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, MENU_SHARE, 0, getResources().getString(R.string.menu_share));
        menu.add(Menu.NONE, MENU_DELETE, 1, getResources().getString(R.string.delete));
        AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        selectedMusic = musicList.get(mi.position);
        menu.setHeaderTitle(selectedMusic.getName());
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_SHARE:

                break;
            case MENU_DELETE: {
                MusicDBHelper.deleteFavorite(getActivity(),Integer.valueOf(selectedMusic.getId()));
                UIHelp.showToast(getActivity(), "删除成功!");
                musicList.remove(selectedMusic);
                listAdapter.notifyDataSetChanged();
                break;
            }
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MusicService.MUSIC.init(musicList, musicList.get(position), position);
        getActivity().sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_PLAY));    }

    @Override
    public void onDestroy(){
        getActivity().unregisterReceiver(mMediaStatusReceiver);
        getActivity().unregisterReceiver(musicBroadcastReceiver);
        super.onDestroy();
    }

    private void registerMessage(){

        IntentFilter filter = new IntentFilter();
        filter.addAction(ApolloService.META_CHANGED);
        filter.addAction(ApolloService.PLAYSTATE_CHANGED);
        getActivity().registerReceiver(mMediaStatusReceiver, filter);

        IntentFilter musicFilter = new IntentFilter();
        musicFilter.addAction(ActionManager.ACTION_MSG_MUSIC_CHANGED);
        getActivity().registerReceiver(musicBroadcastReceiver, musicFilter);

    }

    private BroadcastReceiver musicBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            ContentValues value = intent.getParcelableExtra("music");
            Song m = new Song();
            m.setId(value.getAsInteger("id") + "");
            m.setName(value.getAsString("name"));
            m.setArtist(value.getAsString("thumbPath"));
            m.setSingleList(value.getAsString("filePath"), "");
            musicList.add(0,m);

            listAdapter.notifyDataSetChanged();
        }
    };

    private final BroadcastReceiver mMediaStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListView != null) {
                listAdapter.notifyDataSetChanged();
            }
        }
    };
}
