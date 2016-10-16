
package com.blue.sky.mobile.manager.music.ui.fragments.list;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.content.Loader;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.helpers.AddIdCursorLoader;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.ui.adapters.list.RecentlyAddedAdapter;
import com.blue.sky.mobile.manager.music.ui.fragments.base.ListViewFragment;

import static com.blue.sky.mobile.manager.music.Constants.NUMWEEKS;

public class RecentlyAddedFragment extends ListViewFragment{

    public void setupFragmentData(){
        mAdapter = new RecentlyAddedAdapter(getActivity(), R.layout.music_listview_items,
                null, new String[] {}, new int[] {}, 0);
    	mProjection = new String[] {
                BaseColumns._ID, MediaColumns.TITLE, AudioColumns.ALBUM, AudioColumns.ARTIST
        };
        StringBuilder where = new StringBuilder();
        int X = MusicUtils.getIntPref(getActivity(), NUMWEEKS, 5) * 3600 * 24 * 7;
        where.append(MediaColumns.TITLE + " != ''");
        where.append(" AND " + AudioColumns.IS_MUSIC + "=1");
        where.append(" AND " + MediaColumns.DATE_ADDED + ">"
                + (System.currentTimeMillis() / 1000 - X));
        mWhere = where.toString();
        mSortOrder = MediaColumns.DATE_ADDED + " DESC";
        mUri = Audio.Media.EXTERNAL_CONTENT_URI;
        mTitleColumn = MediaColumns.TITLE;       
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {    
        return new AddIdCursorLoader(getActivity(), mUri, mProjection, mWhere, null, mSortOrder);
    }
}
