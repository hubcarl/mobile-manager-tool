package com.blue.sky.mobile.manager.music.module.network.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.util.Log;
import android.widget.SeekBar;
import com.blue.sky.common.AppMain;
import com.blue.sky.mobile.message.ActionManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayer implements OnBufferingUpdateListener, OnCompletionListener,
        OnPreparedListener {

    private Context context;
    public MediaPlayer mediaPlayer; // 媒体播放器
    private SeekBar seekBar; // 拖动条
    private Timer mTimer = new Timer(); // 计时器

    // 初始化播放器
    public MusicPlayer(Context context,SeekBar seekBar) {
        super();
        this.context = context;
        this.seekBar = seekBar;
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 每一秒触发一次
        mTimer.schedule(timerTask, 0, 1000);
    }

    // 计时器
    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying() && seekBar.isPressed() == false) {
                handler.sendEmptyMessage(0); // 发送消息
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            Log.d(">>>mediaPlayer Handler"," position:" + position + " duration:" + duration);
            if (duration > 0) {
                // 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
                long pos = seekBar.getMax() * position / duration;
                Log.d(">>>mediaPlayer Handler"," pos:" + pos);
                seekBar.setProgress((int) pos);
            }
        };
    };

    public void play() {
        mediaPlayer.start();
    }

    // 暂停
    public void pause() {
        mediaPlayer.pause();
    }

    // 停止
    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    /**
     * @param url url地址
     */
    public void playUrl(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url); // 设置数据源
            mediaPlayer.prepare(); // prepare自动播放
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    /**
     * 重写MediaPlayer setOnCompletionListener
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(">>>mediaPlayer", "onCompletion");
        Intent intent = new Intent(ActionManager.ACTION_MSG_MUSIC_NETWORK_PLAY);
        intent.putExtra("from",context.getClass().getSimpleName());
        intent.putExtra("music", AppMain.MUSIC.getNextMusic());
        context.sendBroadcast(intent);
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        seekBar.setSecondaryProgress(percent);
        int currentProgress = seekBar.getMax() * mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration();
        Log.d(">>>mediaPlayer:" + currentProgress + "% play", percent + " buffer");
    }

}
