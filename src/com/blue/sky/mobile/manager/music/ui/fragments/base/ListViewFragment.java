
package com.blue.sky.mobile.manager.music.ui.fragments.base;

import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.*;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.blue.sky.common.utils.TimeUtil;
import com.blue.sky.common.utils.UIHelp;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.MusicDBHelper;
import com.blue.sky.mobile.manager.music.helpers.RefreshableFragment;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.service.ApolloService;
import com.blue.sky.mobile.manager.music.ui.adapters.base.ListViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.blue.sky.mobile.manager.music.Constants.INTENT_ADD_TO_PLAYLIST;
import static com.blue.sky.mobile.manager.music.Constants.INTENT_PLAYLIST_LIST;


public abstract class ListViewFragment extends RefreshableFragment implements LoaderCallbacks<Cursor>,
        OnItemClickListener {

    // Adapter
    protected ListViewAdapter mAdapter;

    // ListView
    protected ListView mListView;

    // Cursor
    protected Cursor mCursor;

    // Selected position
    protected int mSelectedPosition;

    // Used to set ringtone
    protected long mSelectedId;

    // Options
    protected final int PLAY_SELECTION = 0;

    protected final int USE_AS_RINGTONE = 1;

    protected final int ADD_TO_PLAYLIST = 2;

    protected final int SEARCH = 3;

    protected final int ADD_TO_LOVE = 4;

    protected final int DELETE = 5;
    
    protected int mFragmentGroupId = 0;

    protected String mCurrentId, mSortOrder = null, 
    		mWhere = null, mType = null,
    		mTitleColumn = null;
    
    protected String[] mProjection = null;
    
    protected Uri mUri = null;

    // Bundle
    public ListViewFragment() {
    }

    public ListViewFragment(Bundle args) {
        setArguments(args);
    }

    /*
     * To be overrode in child classes to setup fragment data
     */
    public abstract void setupFragmentData();
    
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupFragmentData();
        mListView.setOnCreateContextMenuListener(this);
        mListView.setOnItemClickListener(this);
        mListView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void refresh() {
        // The data need to be refreshed
        if( mListView != null ) {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.music_listview, container, false);
        mListView = (ListView)root.findViewById(android.R.id.list);
        return root;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
    	return new CursorLoader(getActivity(), mUri, mProjection, mWhere, null, mSortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Check for database errors
        if (data == null) {
            return;
        }
        if(mCursor!=null)
        	mCursor.close();
        mAdapter.changeCursor(data);
        mListView.invalidateViews();
        mCursor = data;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (mAdapter != null)
            mAdapter.changeCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putAll(getArguments() != null ? getArguments() : new Bundle());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        MusicUtils.playAll(getActivity(), mCursor, position);
    }

    /**
     * Update the list as needed
     */
    private final BroadcastReceiver mMediaStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mListView != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ApolloService.META_CHANGED);
        filter.addAction(ApolloService.PLAYSTATE_CHANGED);
        getActivity().registerReceiver(mMediaStatusReceiver, filter);
    }

    @Override
    public void onStop() {
        getActivity().unregisterReceiver(mMediaStatusReceiver);
        super.onStop();
    }
}
