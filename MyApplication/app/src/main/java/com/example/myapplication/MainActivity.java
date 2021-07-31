package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.util.BitmapBlur;
import com.example.myapplication.util.BitmapUtils;
import com.example.myapplication.util.ScanMusic;
import com.example.myapplication.util.SharedMediaPlayer;
import com.example.myapplication.util.TimeGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * 把昨天的listview在敲一遍
 */
public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ImageView s_imgBg;
    private ImageView s_img;
//    private RelativeLayout s_top;
    private ListView sl_list;
    private TextView s_Title;
    private TextView s_singerName;
    private ImageView s_last;
    private ImageView s_play;
    private ImageView s_next;
    private RelativeLayout s_list;
    private ImageView s_s_img;
    private TextView tv_total_time;
    private TextView tv_current_time;
    private SeekBar sb_main_progress;
    private CustomRoundAngleImageView r_b;
    List<Bean> songList = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    //private SharedMediaPlayer sharedMediaPlayer;
    private myAdapter adapter;

    private int selectPosition = -1;
    private long SysTime = 0;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //让状态栏变成透明颜色

        super.onCreate(savedInstanceState);
        makeStatusBarTransParent();
        setContentView(R.layout.activity_main);
        initView();

        //setContentView(R.layout.dialog_update);

//        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.layout.activity_main));
//        circularBitmapDrawable.setCornerRadius(15);
//        r_b.setImageDrawable(circularBitmapDrawable);

        //ImageView mImageView3 = (ImageView) findViewById(R.id.img_3); 


        if(checkPermissions() == 0){
            songList = initData(MainActivity.this);
        }

        ListView ls = findViewById(R.id.lv);
        adapter = new myAdapter(this, R.layout.list_item ,songList);
        ls.setAdapter(adapter);

        sl_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Toast.makeText(MainActivity.this, "点击播放：" +
                        songList.get(i).getSong() , Toast.LENGTH_SHORT).show();
                Uri uri = path_to_uri(i);
                selectPosition = i;
                playMusic(uri, i);
            }

        });

        s_s_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MusicPlay.class);
                startActivity(intent);
            }
        });

        s_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectPosition == -1){
                    selectPosition = 0;
                }

                if(mediaPlayer != null) {

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        s_play.setImageResource(R.mipmap.s_play);
                    } else {
                        mediaPlayer.start();
                        s_play.setImageResource(R.mipmap.s_pause);
                    }

                }else {
                    //使用公共播放器，播放音乐
                    playMusic(path_to_uri(selectPosition), selectPosition);
                    //将播放键图片替换为暂停键
                    s_play.setImageResource(R.mipmap.s_pause);
                }
            }
        });

        s_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SysTime != 0 && System.currentTimeMillis() - SysTime < 3000) {
                    Toast.makeText(MainActivity.this,"不要点击太快", Toast.LENGTH_SHORT);
                    SysTime = 0;
                    return;
                }
                SysTime = System.currentTimeMillis();
                selectPosition = selectPosition==songList.size()-1?0:selectPosition+1;
                playMusic(path_to_uri(selectPosition), selectPosition);
            }
        });

        s_last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPosition = selectPosition==0?songList.size()-1:selectPosition-1;
                playMusic(path_to_uri(selectPosition), selectPosition);
            }
        });

        //添加seekbar的拖曳功能
        sb_main_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override  //当到进度条发生改变的时候调用
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override  //开始拖动进度条的时候该方法执行
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override//停止拖动的时候该方法执行
            public void onStopTrackingTouch(SeekBar seekBar) {
                //seekto  让播放起跳到某一个位置进行播放
                if(!mediaPlayer.isPlaying()) {
                    mediaPlayer.seekTo(seekBar.getProgress());
                }else{
                    mediaPlayer.seekTo(seekBar.getProgress());
                    mediaPlayer.start();
                }
            }
        });

    }


    @SuppressLint("HandlerLeak")
    Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            int duration=mediaPlayer.getDuration();
            int currentPosition = mediaPlayer.getCurrentPosition();
            sb_main_progress.setMax(duration);
            sb_main_progress.setProgress(currentPosition);
            TimeGenerator temp = new TimeGenerator();
            tv_current_time.setText(temp.generateTime(currentPosition)+"/");
            tv_total_time.setText(temp.generateTime(duration));

            if (mediaPlayer.isPlaying()) {
                handler.sendEmptyMessageDelayed(0,500);
            }
        }
    };


    private void makeStatusBarTransParent() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //在这里设置颜色
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void update_bg(int i) {

        Bean b_t = songList.get(i);
        s_singerName.setText(b_t.getSinger());
        s_Title.setText(b_t.getSong());
        s_img.setImageBitmap(b_t.getBitmap());
        BitmapBlur b_blur = new BitmapBlur();
        s_s_img.setImageBitmap(b_t.getBitmap());
        s_imgBg.setImageBitmap(b_blur.blurBitmap(this, b_t.getBitmap(), 20));
        TimeGenerator tg = new TimeGenerator();
        String duration = tg.generateTime(mediaPlayer.getDuration());
        tv_total_time.setText(duration);
        String current_duration = tg.generateTime(mediaPlayer.getCurrentPosition()) + "/";
        tv_current_time.setText(current_duration);
        r_b.setImageBitmap(b_blur.blurBitmap(this, b_t.getBitmap(), 20));

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void playMusic(Uri uri, int i){

        try{

            if(mediaPlayer == null) {

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(MainActivity.this, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                handler.sendEmptyMessage(0);
                update_bg(i);

            }else if(mediaPlayer != null){

                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.setDataSource(MainActivity.this, uri);
                mediaPlayer.prepare();
                mediaPlayer.start();
                handler.sendEmptyMessage(0);
                update_bg(i);

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    Uri path_to_uri(int i){
        String path = songList.get(i).getS_path();
        return Uri.parse(path);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void initView() {
        s_imgBg = (ImageView) findViewById(R.id.s_imgBg);

        //模糊
//        Resources res = MainActivity.this.getResources();
//        //拿到初始图
//        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.default_bg);
//        //处理得到模糊效果的图
//        Bitmap blurBitmap = BitmapBlur.blurBitmap(this, bmp, 20f);
//        s_imgBg.setImageBitmap(blurBitmap);

        s_img = (ImageView) findViewById(R.id.preview);
        sl_list = (ListView) findViewById(R.id.lv);
        s_Title = (TextView) findViewById(R.id.s_Title);
        s_singerName = (TextView) findViewById(R.id.s_singerName);
        s_last = (ImageView) findViewById(R.id.s_last);
        s_play = (ImageView) findViewById(R.id.s_play);
        s_next = (ImageView) findViewById(R.id.s_next);
        s_list = (RelativeLayout) findViewById(R.id.s_list);
        s_s_img = (ImageView) findViewById(R.id.prev_bm);
        tv_total_time = (TextView) findViewById(R.id.tv_total_time);
        tv_current_time = (TextView) findViewById(R.id.tv_current_time);
        sb_main_progress = (SeekBar) findViewById(R.id.sb_main_progress);
        r_b = (CustomRoundAngleImageView) findViewById(R.id.status_id);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private int checkPermissions() {
        int permission = 0;
        //如果当前版本大于api23，就需要申请权限否则不需要申请权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            //检查完权限后会返回一个int类型的值
            int ps = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            permission = ps;
            //如果这个值等于我们需要的值0，就认为权限获取成功了
            if (ps == PackageManager.PERMISSION_GRANTED) {
                songList = ScanMusic.getInstance().initData(MainActivity.this);
            } else {
                //向Android手机请求权限
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                permission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else {
            songList = ScanMusic.getInstance().initData(MainActivity.this);
        }
        return permission;
    }

    private List<Bean> initData(Context context){

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
                Bitmap bitmap = BitmapUtils.getArtwork(MainActivity.this, songId, albumId, true);
                Bean bean = new Bean(title, singer, R.mipmap.ic_launcher, path, bitmap);
                System.out.println(bean.toString());
                list.add(bean);
            }
        }

        return list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}