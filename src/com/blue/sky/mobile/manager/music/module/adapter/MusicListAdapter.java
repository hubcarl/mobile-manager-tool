package com.blue.sky.mobile.manager.music.module.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.blue.sky.common.utils.MediaUtil;
import com.blue.sky.common.utils.Strings;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.cache.ImageInfo;
import com.blue.sky.mobile.manager.music.cache.ImageProvider;
import com.blue.sky.mobile.manager.music.cache.Music;
import com.blue.sky.mobile.manager.music.helpers.utils.MusicUtils;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.service.MusicService;
import com.blue.sky.mobile.manager.music.views.ViewHolderList;

import java.util.ArrayList;
import java.util.List;

import static com.blue.sky.mobile.manager.music.Constants.SIZE_THUMB;
import static com.blue.sky.mobile.manager.music.Constants.SRC_FIRST_AVAILABLE;
import static com.blue.sky.mobile.manager.music.Constants.TYPE_ARTIST;


public class MusicListAdapter extends BaseAdapter {

    private Context context;
    private ImageProvider mImageProvider;
    private AnimationDrawable mPeakOneAnimation, mPeakTwoAnimation;
    private List<Song> list = new ArrayList<Song>();
    private int type;
    private int left, top;

    public long mPlayingId = 0;

    public boolean showContextEnabled = true;

    /**
     * Used to quickly show our the ContextMenu
     */
    private final View.OnClickListener showContextMenu = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            v.showContextMenu();
        }
    };

    public MusicListAdapter(Context context, List<Song> list, int type) {
        super();
        this.context = context;
        this.list = list;
        this.type = type;
        // Helps center the text in the Playlist/Genre tab
        left = context.getResources().getDimensionPixelSize(
                R.dimen.listview_items_padding_left_top);
        top = context.getResources().getDimensionPixelSize(
                R.dimen.listview_items_padding_gp_top);
        mImageProvider = ImageProvider.getInstance((Activity) context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderList viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.music_listview_items, null);
            viewHolder = new ViewHolderList(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderList) convertView.getTag();
        }

        final Song m = list.get(position);
        viewHolder.mViewHolderLineOne.setText(m.getName());
        String desc = "<unknown>".equals(m.getArtist()) ? m.getAlbumName() : m.getArtist() ;
        if(m.getMDuration()>0){
            viewHolder.mViewHolderLineTwo.setText(desc + " " + MediaUtil.formatTime(m.getMDuration()));
        }else{
            viewHolder.mViewHolderLineTwo.setText(desc);
        }
        viewHolder.mQuickContext.setOnClickListener(showContextMenu);
        viewHolder.mViewHolderImage.setImageResource(R.drawable.music_default);

        Song currentPlayMusic = MusicService.MUSIC.getPlayMusic();
        if (currentPlayMusic != null && currentPlayMusic.getId().equals(m.getId())) {
            viewHolder.mPeakOne.setImageResource(R.anim.music_peak_meter_1);
            viewHolder.mPeakTwo.setImageResource(R.anim.music_peak_meter_2);
            mPeakOneAnimation = (AnimationDrawable) viewHolder.mPeakOne.getDrawable();
            mPeakTwoAnimation = (AnimationDrawable) viewHolder.mPeakTwo.getDrawable();
            if (MusicService.getInstance() != null && MusicService.getInstance().isPlaying()) {
                mPeakOneAnimation.start();
                mPeakTwoAnimation.start();
            } else {
                mPeakOneAnimation.stop();
                mPeakTwoAnimation.stop();
            }
        } else {
            viewHolder.mPeakOne.setImageResource(0);
            viewHolder.mPeakTwo.setImageResource(0);
        }

        return convertView;
    }
}