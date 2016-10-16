
package com.blue.sky.mobile.manager.music.module.network.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import com.blue.sky.common.fragment.BaseFragment;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.service.MusicService;
import com.blue.sky.mobile.message.ActionManager;

public class MusicBottomFragment extends BaseFragment implements OnClickListener {

    private SeekBar musicProgress;
    private TextView txtTitle;
    private TextView txtDuration;
    private ImageButton txtPrev;
    private ImageButton txtPlayOrPause;
    private ImageButton txtNext;
    private boolean isPlaying = false;
    private int position = 0;
    private int duration = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View root = inflater.inflate(R.layout.music_network_bottom_fragment, container);

        txtTitle = (TextView)root.findViewById(R.id.item_title);
        txtDuration = (TextView)root.findViewById(R.id.item_time);
        txtPrev = (ImageButton)root.findViewById(R.id.prev);
        txtPlayOrPause = (ImageButton)root.findViewById(R.id.playOrPause);
        txtNext = (ImageButton)root.findViewById(R.id.next);

        txtPrev.setOnClickListener(this);
        txtPlayOrPause.setOnClickListener(this);
        txtNext.setOnClickListener(this);

        musicProgress = (SeekBar) root.findViewById(R.id.music_progress);
        musicProgress.setOnSeekBarChangeListener(new SeekBarChangeEvent());

        IntentFilter commandFilter = new IntentFilter();
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PLAY);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_CHANGED);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PLAY_CACHE);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PLAY_PROGRESS);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PAUSE);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PREVIOUS_PLAY);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_NEXT_PLAY);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_STOP_TRACKING);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_CLOSE_NOTIFICATION);
        getActivity().registerReceiver(musicPlayReceiver, commandFilter);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(musicPlayReceiver);
        super.onDestroy();
    }

    private void updateUI(Song song){
        this.txtTitle.setText(song.getName());
        this.txtDuration.setText(song.getDuration());
        this.txtPlayOrPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.apollo_holo_light_pause));
        this.isPlaying = true;
    }

    private final BroadcastReceiver musicPlayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(">>>MusicBottomFragment musicPlayReceiver", action);
            if(ActionManager.ACTION_MUSIC_PLAY.equals(action)) {
                updateUI(MusicService.MUSIC.getPlayMusic());
                txtPlayOrPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.apollo_holo_light_pause));
            }else if(ActionManager.ACTION_MUSIC_CHANGED.equals(action)){
                updateUI(MusicService.MUSIC.getPlayMusic());
            }else if(ActionManager.ACTION_MUSIC_PLAY_CACHE.equals(action)){
                int percent = intent.getIntExtra("percent",0);
                musicProgress.setSecondaryProgress(percent);
            }else if(ActionManager.ACTION_MUSIC_PLAY_PROGRESS.equals(action)) {
                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                position = intent.getIntExtra("position", 0);
                duration = intent.getIntExtra("duration", 1);
                long pos = musicProgress.getMax() * position / duration;
                musicProgress.setProgress((int) pos);
            }else if(ActionManager.ACTION_MUSIC_PREVIOUS_PLAY.equals(action)){
                updateUI(MusicService.MUSIC.getPrevMusic());
            }else  if(ActionManager.ACTION_MUSIC_NEXT_PLAY.equals(action)){
                updateUI(MusicService.MUSIC.getNextMusic());
            }else  if(ActionManager.ACTION_MUSIC_PAUSE.equals(action)){
                isPlaying = false;
                txtPlayOrPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.apollo_holo_light_play));
            }else  if(ActionManager.ACTION_MUSIC_STOP_TRACKING.equals(action)){
                isPlaying = false;
                txtPlayOrPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.apollo_holo_light_play));
            }else  if(ActionManager.ACTION_MUSIC_CLOSE_NOTIFICATION.equals(action)){
                isPlaying = false;
                txtPlayOrPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.apollo_holo_light_play));
            }
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prev:
                if(!MusicService.MUSIC.isEmpty()){
                    updateUI(MusicService.MUSIC.getPrevMusic());
                    getActivity().sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_PREVIOUS_PLAY));
                }
                break;
            case R.id.playOrPause:
                if(!MusicService.MUSIC.isEmpty()){
                    if(isPlaying){
                        isPlaying = false;
                        txtPlayOrPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.apollo_holo_light_play));
                        getActivity().sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_PAUSE));
                    }else{
                        isPlaying = true;
                        Intent intent = new Intent(ActionManager.ACTION_MUSIC_PLAY);
                        intent.putExtra("isNotification", true);
                        txtPlayOrPause.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.apollo_holo_light_pause));
                        getActivity().sendBroadcast(intent);
                    }
                }
                break;
            case R.id.next:
                if(!MusicService.MUSIC.isEmpty()) {
                    updateUI(MusicService.MUSIC.getNextMusic());
                    getActivity().sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_NEXT_PLAY));
                }
                break;
        }
    }

    class SeekBarChangeEvent implements SeekBar.OnSeekBarChangeListener {
        int progress;

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            // 原本是(progress/seekBar.getMax())*player.mediaPlayer.getDuration()
            this.progress = progress * duration/ seekBar.getMax();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            // seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
            Intent intent = new Intent(ActionManager.ACTION_MUSIC_STOP_TRACKING);
            intent.putExtra("progress", progress);
            getActivity().sendBroadcast(intent);
        }

    }

}
