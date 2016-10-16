
package com.blue.sky.mobile.manager.music.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.blue.sky.mobile.manager.R;

public class AlbumArtFragment extends Fragment {
	public ImageView albumArt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View root = inflater.inflate(R.layout.music_nowplaying_album_art, null);
        albumArt = (ImageView)root.findViewById(R.id.audio_player_album_art);
        return root;
    }
}
