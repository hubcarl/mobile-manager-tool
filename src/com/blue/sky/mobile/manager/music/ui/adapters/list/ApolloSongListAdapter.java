
package com.blue.sky.mobile.manager.music.ui.adapters.list;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.ui.adapters.base.ListViewAdapter;

import static com.blue.sky.mobile.manager.music.Constants.TYPE_ARTIST;

public class ApolloSongListAdapter extends ListViewAdapter {

    public ApolloSongListAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public void setupViewData( Cursor mCursor ){
    	mLineOneText = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaColumns.TITLE));
    	
    	mLineTwoText = mCursor.getString(mCursor.getColumnIndexOrThrow(AudioColumns.ARTIST));
    	
        mImageData = new String[]{ mLineTwoText };
        
        mPlayingId = MusicUtils.getCurrentAudioId();
        mCurrentId = mCursor.getLong(mCursor.getColumnIndexOrThrow(BaseColumns._ID));
        
        mListType = TYPE_ARTIST;   	
    }

}
