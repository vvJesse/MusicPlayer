package com.example.myapplication.util;

import android.media.MediaPlayer;

public class SharedMediaPlayer {
    private static MediaPlayer mediaPlayer;

    private SharedMediaPlayer(){}

    public static synchronized MediaPlayer getInstance() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        return mediaPlayer;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }
}
