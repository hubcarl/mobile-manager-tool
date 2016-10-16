package com.blue.sky.mobile.manager.music.module.network.utils;

import android.content.Context;
import com.blue.sky.mobile.manager.music.module.network.entity.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2014/11/9.
 */
public class MusicPlayController {

    private List<Song> playMusicList = new ArrayList<Song>();
    private Song currentMusic;
    private int playIndex;

    public void init(List<Song> tempList, Song playMusic,int index){
        playMusicList.clear();
        playMusicList.addAll(tempList);
        currentMusic = playMusic;
        playIndex = index;
    }

    public Song getPlayMusic(){
        return currentMusic;
    }

    public Song getPrevPlayMusic(){
        if(playMusicList.size()>0 && playMusicList.size()>1){
            if(playIndex == 0){
                playIndex = playMusicList.size()-1;
            }else {
                playIndex = playIndex-1;
            }
            currentMusic = playMusicList.get(playIndex) ;
            return currentMusic;
        }else{
            return null;
        }
    }

    public Song getPrevMusic(){
        if(playMusicList.size()>0 && playMusicList.size()>1){
            if(playIndex == 0){
                return playMusicList.get(playMusicList.size()-1);
            }else {
                return playMusicList.get(playIndex-1);
            }

        }else{
            return null;
        }
    }

    public Song getNextPlayMusic() {
        if (playMusicList.size() > 0 && playMusicList.size() > 1) {
            if (playIndex == playMusicList.size() - 1) {
                playIndex = 0;
            } else {
                playIndex = playIndex + 1;
            }
            currentMusic = playMusicList.get(playIndex) ;
            return currentMusic;
        } else {
            return null;
        }
    }

    public Song getNextMusic() {
        if (playMusicList.size() > 0 && playMusicList.size() > 1) {
            if (playIndex == playMusicList.size() - 1) {
                return playMusicList.get(0) ;
            } else {
                return playMusicList.get(playIndex + 1) ;
            }
        } else {
            return null;
        }
    }

    public  boolean isEmpty(){
        return playMusicList.isEmpty();
    }

    public void clear(){
        playMusicList = null;
        currentMusic = null;
    }
}
