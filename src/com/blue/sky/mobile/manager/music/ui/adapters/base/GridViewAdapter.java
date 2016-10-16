
package com.blue.sky.mobile.manager.music.ui.adapters.base;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.RemoteException;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.ImageInfo;
import com.blue.sky.mobile.manager.music.cache.ImageProvider;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.views.ViewHolderGrid;

import java.lang.ref.WeakReference;

import static com.blue.sky.mobile.manager.music.Constants.SIZE_THUMB;
import static com.blue.sky.mobile.manager.music.Constants.SRC_FIRST_AVAILABLE;


public abstract class GridViewAdapter extends SimpleCursorAdapter {

    private AnimationDrawable mPeakOneAnimation, mPeakTwoAnimation;

    private WeakReference<ViewHolderGrid> holderReference;
    
    protected Context mContext;
    
    private ImageProvider mImageProvider;
    
    public String mGridType = null,  mLineOneText = null, mLineTwoText = null;
    
    public String[] mImageData = null;
    
    public long mPlayingId = 0, mCurrentId = 0;

    public GridViewAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    	mContext = context;
    	mImageProvider = ImageProvider.getInstance( (Activity) mContext );
    }  
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View view = super.getView(position, convertView, parent);
        Cursor mCursor = (Cursor) getItem(position);
        setupViewData(mCursor);
        // ViewHolderGrid
        final ViewHolderGrid viewholder;
        if ( view != null ) {
            viewholder = new ViewHolderGrid(view);
            holderReference = new WeakReference<ViewHolderGrid>(viewholder);
            view.setTag(holderReference.get());
        } else {
            viewholder = (ViewHolderGrid)convertView.getTag();
        }
        
        holderReference.get().mViewHolderLineOne.setText(mLineOneText);
        holderReference.get().mViewHolderLineTwo.setText(mLineTwoText);

        ImageInfo mInfo = new ImageInfo();
        mInfo.type = mGridType;
        mInfo.size = SIZE_THUMB;
        mInfo.source = SRC_FIRST_AVAILABLE;
        mInfo.data = mImageData;        
        mImageProvider.loadImage( viewholder.mViewHolderImage, mInfo );
        
        if ( mPlayingId == mCurrentId ) {
            holderReference.get().mPeakOne.setImageResource(R.anim.music_peak_meter_1);
            holderReference.get().mPeakTwo.setImageResource(R.anim.music_peak_meter_2);
            mPeakOneAnimation = (AnimationDrawable)holderReference.get().mPeakOne.getDrawable();
            mPeakTwoAnimation = (AnimationDrawable)holderReference.get().mPeakTwo.getDrawable();
            try {
                if ( MusicUtils.mService.isPlaying() ) {
                    mPeakOneAnimation.start();
                    mPeakTwoAnimation.start();
                } else {
                    mPeakOneAnimation.stop();
                    mPeakTwoAnimation.stop();
                }
            } catch ( RemoteException e ) {
                e.printStackTrace();
            }
        } else {
            holderReference.get().mPeakOne.setImageResource(0);
            holderReference.get().mPeakTwo.setImageResource(0);
        }

        return view;
    }
    
    public abstract void setupViewData( Cursor mCursor );
}
