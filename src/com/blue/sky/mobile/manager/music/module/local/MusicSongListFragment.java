
package com.blue.sky.mobile.manager.music.module.local;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.MusicDBHelper;
import com.blue.sky.mobile.manager.music.ui.adapters.list.ApolloSongListAdapter;
import com.blue.sky.mobile.manager.music.ui.fragments.base.ListViewFragment;
import com.blue.sky.mobile.message.ActionManager;

import java.io.File;
import java.util.Date;

import static com.blue.sky.mobile.manager.music.Constants.TYPE_SONG;

public class MusicSongListFragment extends ListViewFragment{

    public void setupFragmentData(){
        mAdapter = new ApolloSongListAdapter(getActivity(), R.layout.music_listview_items, null,
                										new String[] {}, new int[] {}, 0);
    	mProjection = new String[] {
                BaseColumns._ID,
                MediaColumns.TITLE,
                AudioColumns.ALBUM,
                AudioColumns.ARTIST,
                AudioColumns.DATA
        };

        StringBuilder where = new StringBuilder();
        where.append(AudioColumns.IS_MUSIC).append(" =1 AND ").append( MediaColumns.TITLE).append(" != ''");
        mWhere = where.toString();
        mSortOrder = Audio.Media.DEFAULT_SORT_ORDER;
        mUri = Audio.Media.EXTERNAL_CONTENT_URI;
        mFragmentGroupId = 3;
        mType = TYPE_SONG;
        mTitleColumn = MediaColumns.TITLE;
        // 随机播放
//        View shuffle_temp = View.inflate(getActivity(), R.layout.shuffle_all, null);
//        mListView.addHeaderView(shuffle_temp);
//    	RelativeLayout  shuffle = (RelativeLayout)shuffle_temp.findViewById(R.id.shuffle_wrapper);
//    	shuffle.setVisibility(View.VISIBLE);
//    	shuffle.setOnClickListener(new RelativeLayout.OnClickListener() {
//            public void onClick(View v)
//            {
//                Uri uri = Audio.Media.EXTERNAL_CONTENT_URI;
//                String[] projection = new String[] {
//                    BaseColumns._ID
//                };
//                String selection = AudioColumns.IS_MUSIC + "=1";
//                String sortOrder = "RANDOM()";
//                Cursor cursor = MusicUtils.query(getActivity(), uri, projection, selection, null, sortOrder);
//                if (cursor != null) {
//                    MusicUtils.shuffleAll(getActivity(), cursor);
//                    cursor.close();
//                    cursor = null;
//                }
//            }
//         });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if( mFragmentGroupId != 0 ){
            menu.add(mFragmentGroupId, ADD_TO_LOVE, 0, getResources().getString(R.string.add_to_love));
            menu.add(mFragmentGroupId, DELETE, 1, getResources().getString(R.string.delete));
            AdapterView.AdapterContextMenuInfo mi = (AdapterView.AdapterContextMenuInfo)menuInfo;
            mSelectedPosition = mi.position;
            mCursor.moveToPosition(mSelectedPosition);
            mCurrentId = mCursor.getString(mCursor.getColumnIndexOrThrow(BaseColumns._ID));
            try {
                mSelectedId = Long.parseLong(mCurrentId);
            } catch (IllegalArgumentException ex) {
                mSelectedId = mi.id;
            }
            String title = mCursor.getString(mCursor.getColumnIndexOrThrow(mTitleColumn));
            menu.setHeaderTitle(title);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if( item.getGroupId() == mFragmentGroupId ){
            switch (item.getItemId()) {
                case ADD_TO_LOVE:
                    //MusicUtils.playAll(getActivity(), mCursor, mSelectedPosition);
                    ContentValues value = new ContentValues();
                    value.put("id", mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)));
                    value.put("name", mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
                    value.put("filePath", mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)));
                    value.put("thumbPath", mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ARTIST)));
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
                case DELETE: {
                    getActivity().getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,MediaStore.Audio.Media._ID + "=" +
                            mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns._ID)),null);
                    refresh();
                    UIHelp.showToast(getActivity(),"[" + mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)) + "]删除成功");
                    int pathColumnIndex = mCursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                    if(pathColumnIndex>-1){
                        String path =  mCursor.getString(pathColumnIndex);
                        File selectedFile = new File(path);
                        selectedFile.delete();
                        refresh();
                    }
                    break;
                }
                default:
                    break;
            }
        }
        return super.onContextItemSelected(item);
    }


}
