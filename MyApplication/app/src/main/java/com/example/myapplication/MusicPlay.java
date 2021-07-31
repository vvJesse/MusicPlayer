package com.example.myapplication;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.ScanMusic;

import java.util.List;

public class MusicPlay extends AppCompatActivity {

    private ImageView pre_bg;
    private ImageView album_pic;
    private TextView song;
    private TextView singer;
    private SeekBar seekBar;
    private ImageView last;
    private ImageView pause_or_play;
    private ImageView next;
    private List<Bean> songList;
    private MediaPlayer mediaPlayer;
    private int current_position;


    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        songList = ScanMusic.getInstance().getSongList();
        initView();
    }

    private void makeStatusBarTransParent() {

        //当版本号高于21   需要通过java代码来设置状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            //清除flag
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //添加flag
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 设置透明色
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            //给decorView设置uiflag
            getWindow().getDecorView().setSystemUiVisibility(uiFlags);
        }

    }

    private void initView() {
        pre_bg = findViewById(R.id.layout_bg);
        album_pic = findViewById(R.id.prev_music);
        song = findViewById(R.id.music_song);
        singer = findViewById(R.id.music_singer);
        seekBar = findViewById(R.id.sb_music_progress);
        last = findViewById(R.id.last);
        pause_or_play = findViewById(R.id.s_play);
        next = findViewById(R.id.music_next);

    }

    public void setPre_bg(ImageView pre_bg) {
        this.pre_bg = pre_bg;
    }

    public void setAlbum_pic(ImageView album_pic) {
        this.album_pic = album_pic;
    }

    public void setSong(TextView song) {
        this.song = song;
    }

    public void setSinger(TextView singer) {
        this.singer = singer;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public void setLast(ImageView last) {
        this.last = last;
    }

    public void setPause_or_play(ImageView pause_or_play) {
        this.pause_or_play = pause_or_play;
    }

    public void setNext(ImageView next) {
        this.next = next;
    }

    public ImageView getPre_bg() {
        return pre_bg;
    }

    public ImageView getAlbum_pic() {
        return album_pic;
    }

    public TextView getSong() {
        return song;
    }

    public TextView getSinger() {
        return singer;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public ImageView getLast() {
        return last;
    }

    public ImageView getPause_or_play() {
        return pause_or_play;
    }

    public ImageView getNext() {
        return next;
    }


}
