package com.example.myapplication;

import android.graphics.Bitmap;

public class Bean {

    private String song;
    private String singer;
    private int image;
    private String s_path;
    Bitmap bitmap;

    public Bean(String title, String singer, int ic_launcher, String path, Bitmap bitmap) {
        song = title;
        this.singer = singer;
        image = ic_launcher;
        s_path = path;
        this.bitmap = bitmap;
    }


    @Override
    public String toString() {
        return "Bean{" +
                "song='" + song + '\'' +
                ", singer='" + singer + '\'' +
                '}';
    }

    public Bean() {
    }

    public Bean(String song, String singer, int image) {
        this.song = song;
        this.singer = singer;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public String getS_path() {
        return s_path;
    }

    public void setS_path(String s_path) {
        this.s_path = s_path;
    }
}
