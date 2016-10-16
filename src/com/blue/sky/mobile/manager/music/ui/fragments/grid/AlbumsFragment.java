
package com.blue.sky.mobile.manager.music.ui.fragments.grid;

import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AlbumColumns;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.ui.adapters.grid.AlbumAdapter;
import com.blue.sky.mobile.manager.music.ui.fragments.base.GridViewFragment;

import static com.blue.sky.mobile.manager.music.Constants.TYPE_ALBUM;

public class AlbumsFragment extends GridViewFragment {

    public void setupFragmentData(){
    	mAdapter = new AlbumAdapter(getActivity(), R.layout.music_gridview_items, null,
                					new String[] {}, new int[] {}, 0); 
    	mProjection = new String []{
                BaseColumns._ID, AlbumColumns.ALBUM, AlbumColumns.ARTIST, AlbumColumns.ALBUM_ART
        };
        mUri = Audio.Albums.EXTERNAL_CONTENT_URI;
        mSortOrder = Audio.Albums.DEFAULT_SORT_ORDER;
        mFragmentGroupId = 2;
        mType = TYPE_ALBUM;
    }
    
}
