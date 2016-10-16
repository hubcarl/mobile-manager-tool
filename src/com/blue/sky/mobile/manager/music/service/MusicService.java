package com.blue.sky.mobile.manager.music.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import com.blue.sky.mobile.manager.R;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;
import com.blue.sky.mobile.manager.music.module.network.utils.MusicPlayController;
import com.blue.sky.mobile.message.ActionManager;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2014/10/11.
 */
public class MusicService extends Service implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener {

    public static MusicPlayController MUSIC = new MusicPlayController();

    private MediaPlayer mediaPlayer;

    private Timer mTimer = new Timer(); // 计时器

    private NotificationManager mNotificationManager;

    private Notification notification;

    private static MusicService  musicService;


    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
            mediaPlayer.setOnBufferingUpdateListener(this);
            mediaPlayer.setOnPreparedListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        IntentFilter commandFilter = new IntentFilter();
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PLAY);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PAUSE);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_PREVIOUS_PLAY);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_NEXT_PLAY);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_STOP_TRACKING);
        commandFilter.addAction(ActionManager.ACTION_MUSIC_CLOSE_NOTIFICATION);

        registerReceiver(musicPlayReceiver, commandFilter);

        // 每一秒触发一次
        mTimer.schedule(timerTask, 0, 1000);

        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        musicService = this;
    }

    public static MusicService getInstance(){
        return musicService;
    }

    // 计时器
    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {
            if (mediaPlayer == null)
                return;
            if (mediaPlayer.isPlaying()) {
                handler.sendEmptyMessage(0); // 发送消息
            }
        }
    };

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int position = mediaPlayer.getCurrentPosition();
            int duration = mediaPlayer.getDuration();
            Log.d(">>>mediaPlayer Handler", " position:" + position + " duration:" + duration);
            if (duration > 0) {
                Intent intent = new Intent(ActionManager.ACTION_MUSIC_PLAY_PROGRESS);
                intent.putExtra("position", position);
                intent.putExtra("duration", duration);
                getApplicationContext().sendBroadcast(intent);
            }
        }

        ;
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private boolean isPause = false;

    private final BroadcastReceiver musicPlayReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            boolean isNotification = intent.getBooleanExtra("isNotification", false);
            Log.d(">>>MusicService", action);
            if (ActionManager.ACTION_MUSIC_PLAY.equals(action)) {
                if (isNotification) {
                    if (isPause) {
                        play();
                        notification.contentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                            notification.bigContentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
                        }
                    } else {
                        pause();
                        notification.contentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
                        if (android.os.Build.VERSION.SDK_INT >= 16) {
                            notification.bigContentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
                        }
                        sendBroadcast(new Intent(ActionManager.ACTION_MUSIC_PAUSE));
                    }
                    NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    mManager.notify(2015123456, notification);
                } else {
                    startPlay(MUSIC.getPlayMusic().getPlayUrl());
                    showCustomizeNotification(MUSIC.getPlayMusic(), false);
                }
            } else if (ActionManager.ACTION_MUSIC_PREVIOUS_PLAY.equals(action)) {

                startPlay(MUSIC.getPrevPlayMusic().getPlayUrl());
                showCustomizeNotification(MUSIC.getPlayMusic(), false);

            } else if (ActionManager.ACTION_MUSIC_NEXT_PLAY.equals(action)) {

                startPlay(MUSIC.getNextPlayMusic().getPlayUrl());
                showCustomizeNotification(MUSIC.getPlayMusic(), false);

            } else if (ActionManager.ACTION_MUSIC_PAUSE.equals(action)) {
                pause();
                notification.contentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    notification.bigContentView.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_play);
                }
                NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mManager.notify(2015123456, notification);

            } else if (ActionManager.ACTION_MUSIC_STOP_TRACKING.equals(action)) {

                int progress = intent.getIntExtra("progress", 0);
                mediaPlayer.seekTo(progress);

            } else if (ActionManager.ACTION_MUSIC_CLOSE_NOTIFICATION.equals(action)) {

                pause();
                mNotificationManager.cancel(2015123456);

            }
        }
    };

    //自定义显示的通知 ，创建RemoteView对象
    private void showCustomizeNotification(Song song, boolean isStop) {

        Log.i(">>>showCustomizeNotification", "enter");
        Log.i(">>>showCustomizeNotification", isStop ? "stop" : "play");

        RemoteViews views = new RemoteViews(getPackageName(), R.layout.music_status_bar);
        RemoteViews bigViews = new RemoteViews(getPackageName(), R.layout.music_status_bar_expanded);

        views.setViewVisibility(R.id.status_bar_icon, View.GONE);
        views.setViewVisibility(R.id.status_bar_album_art, View.VISIBLE);
        views.setImageViewResource(R.id.status_bar_album_art, R.drawable.ic_launcher);
        bigViews.setImageViewResource(R.id.status_bar_album_art, R.drawable.ic_launcher);


        Intent intent = new Intent();
        intent.putExtra("isNotification", true);
        intent.setAction(ActionManager.ACTION_MUSIC_PLAY);

        views.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
        bigViews.setImageViewResource(R.id.status_bar_play, R.drawable.apollo_holo_dark_pause);
        PendingIntent mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        views.setOnClickPendingIntent(R.id.status_bar_play, mediaPendingIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_play, mediaPendingIntent);

        intent.setAction(ActionManager.ACTION_MUSIC_NEXT_PLAY);
        mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 2, new Intent(ActionManager.ACTION_MUSIC_NEXT_PLAY), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.status_bar_next, mediaPendingIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_next, mediaPendingIntent);

        intent.setAction(ActionManager.ACTION_MUSIC_PREVIOUS_PLAY);
        mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 3, new Intent(ActionManager.ACTION_MUSIC_PREVIOUS_PLAY), PendingIntent.FLAG_UPDATE_CURRENT);
        bigViews.setOnClickPendingIntent(R.id.status_bar_prev, mediaPendingIntent);

        intent.setAction(ActionManager.ACTION_MUSIC_CLOSE_NOTIFICATION);
        mediaPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 4, new Intent(ActionManager.ACTION_MUSIC_CLOSE_NOTIFICATION), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.status_bar_collapse, mediaPendingIntent);
        bigViews.setOnClickPendingIntent(R.id.status_bar_collapse, mediaPendingIntent);

        views.setTextViewText(R.id.status_bar_track_name, song.getName());
        bigViews.setTextViewText(R.id.status_bar_track_name, song.getName());
        views.setTextViewText(R.id.status_bar_artist_name, song.getSingerName());
        bigViews.setTextViewText(R.id.status_bar_artist_name, song.getSingerName());

        bigViews.setTextViewText(R.id.status_bar_album_name, song.getAlbumName());

        notification = new NotificationCompat.Builder(this).build();

        notification.contentView = views;
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            notification.bigContentView = bigViews;
        }
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.icon = R.drawable.music_stat_notify_music;
        notification.flags = Notification.FLAG_ONGOING_EVENT;

        mNotificationManager.notify(2015123456, notification);

    }

    public void startPlay(String url) {
        isPause = false;
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

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void play() {
        isPause = false;
        mediaPlayer.start();
    }

    // 暂停
    public void pause() {
        isPause = true;
        mediaPlayer.pause();
    }

    // 停止
    public void stop() {
        isPause = false;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    /**
     * 重写MediaPlayer setOnCompletionListener
     *
     * @param mp
     */
    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d(">>>mediaPlayer", "onCompletion");
        startPlay(MUSIC.getNextMusic().getPlayUrl());
        Intent intent = new Intent(ActionManager.ACTION_MUSIC_CHANGED);
        getApplicationContext().sendBroadcast(intent);
    }

    /**
     * 缓冲更新
     */
    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        Intent intent = new Intent(ActionManager.ACTION_MUSIC_PLAY_CACHE);
        intent.putExtra("percent", percent);
        getApplicationContext().sendBroadcast(intent);
    }

}
