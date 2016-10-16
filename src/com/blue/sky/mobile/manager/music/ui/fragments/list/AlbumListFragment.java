package com.blue.sky.mobile.manager.music.ui.fragments.list;

import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.ui.adapters.list.AlbumListAdapter;
import com.blue.sky.mobile.manager.music.ui.fragments.base.ListViewFragment;

import static com.blue.sky.mobile.manager.music.Constants.TYPE_ALBUM;

public class AlbumListFragment extends ListViewFragment {

	public AlbumListFragment(Bundle args) {    
		setArguments(args);    
	}

	@Override
	public void setupFragmentData() {
        mAdapter = new AlbumListAdapter(getActivity(), R.layout.music_listview_items, null,
                								new String[] {}, new int[] {}, 0);
    	mProjection = new String[] {
                BaseColumns._ID, MediaColumns.TITLE, AudioColumns.ALBUM, AudioColumns.ARTIST
        };
        StringBuilder where = new StringBuilder();
        where.append(AudioColumns.IS_MUSIC + "=1")
        					.append(" AND " + MediaColumns.TITLE + " != ''");
        long albumId = getArguments().getLong(BaseColumns._ID);
        where.append(" AND " + AudioColumns.ALBUM_ID + "=" + albumId);
        mWhere = where.toString();        
        mSortOrder = Audio.Media.TRACK + ", " + Audio.Media.DEFAULT_SORT_ORDER;
        mUri = Audio.Media.EXTERNAL_CONTENT_URI;
        mFragmentGroupId = 89;
        mType = TYPE_ALBUM;
        mTitleColumn = MediaColumns.TITLE; 
	}

}
