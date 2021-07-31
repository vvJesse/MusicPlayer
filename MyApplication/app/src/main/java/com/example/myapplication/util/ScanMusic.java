package com.example.myapplication.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import com.example.myapplication.Bean;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.List;

public class ScanMusic {
    private static volatile ScanMusic scanner;
    List<Bean> songList=new ArrayList<>();

    private ScanMusic(){}

    public static synchronized ScanMusic getInstance() {
        if (scanner.songList == null) {
            scanner =new ScanMusic();
        }
        return scanner;
    }

    public List<Bean> getSongList() {
        return songList;
    }

    public List<Bean> initData(Context context){

        String[] message = new String[]{
                MediaStore.Audio.Media.TITLE,      //歌曲名字
                MediaStore.Audio.Media.DURATION,    //歌曲的播放时间
                MediaStore.Audio.Media.ARTIST,      //歌曲的歌手
                MediaStore.Audio.Media._ID,         //歌曲的id
                MediaStore.Audio.Media.ALBUM,       //歌曲的专辑
                MediaStore.Audio.Media.DISPLAY_NAME,//歌曲的专辑名
                MediaStore.Audio.Media.DATA,        //歌曲的路径
                MediaStore.Audio.Media.ALBUM_ID,    //获取歌曲的图片缩略图
        };

        List<Bean> list = new ArrayList<Bean>();
        // 媒体库查询语句（写一个工具类MusicUtils）
        Cursor query = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                message,
                null,
                null,
                null);

        if (query != null) {
            //我们不知道query里面有多少数据
            while (query.moveToNext()) {
                String title = query.getString(0);
                Long durition = query.getLong(1);
                String singer = query.getString(2);
                int songId = query.getInt(3);
                String path = query.getString(6);
                long albumId = query.getLong(7);

                //调用工具类把图片生成 BitmapUtils 工具类可以直接copy
                Bitmap bitmap = BitmapUtils.getArtwork(context, songId, albumId, true);
                Bean bean = new Bean(title, singer, R.mipmap.ic_launcher, path, bitmap);
                System.out.println(bean.toString());
                list.add(bean);
            }
        }

        return list;
    }

}
