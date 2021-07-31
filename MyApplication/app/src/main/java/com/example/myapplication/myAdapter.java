package com.example.myapplication;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class myAdapter extends BaseAdapter {
    private Context context;
    private List<Bean> songList;
    public int r_ID;

    public myAdapter() {
    }

    public myAdapter(Context context, int resouceID, List<Bean> songList) {
        this.context = context;
        r_ID = resouceID;
        this.songList = songList;
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public Object getItem(int i) {
        return songList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void selectItmeID(int selectItem){
        this.r_ID = selectItem;
        //该方法会自动重新条用GetView方法
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view== null) {
            //获取view对象
            view = View.inflate(context, R.layout.list_item, null);
            holder=new ViewHolder(view);
            // 关联控件
            holder.image=view.findViewById(R.id.image);
            holder.tv_singer=view.findViewById(R.id.tv_singer);
            holder.tv_song=view.findViewById(R.id.tv_song);
            //view和holder进行关联
            view.setTag(holder);

        }else{
            holder= (ViewHolder) view.getTag();
        }
        Bean bean = songList.get(i);
        if (bean.getBitmap() != null) {
            holder.image.setImageBitmap(bean.getBitmap());
        }else{
            holder.image.setImageResource(bean.getImage());
        }
        holder.tv_song.setText(bean.getSong());
        holder.tv_singer.setText(bean.getSinger());

        Bitmap bitmap = songList.get(i).getBitmap();
        if (bitmap == null) {
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }else{
            holder.image.setImageBitmap(bitmap);
        }

        return view;
    }

    public static class ViewHolder {
        public View rootView;
        public ImageView image;
        public TextView tv_song;
        public TextView tv_singer;

        public ViewHolder() {
        }

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.image = (ImageView) rootView.findViewById(R.id.image);
            this.tv_song = (TextView) rootView.findViewById(R.id.tv_song);
            this.tv_singer = (TextView) rootView.findViewById(R.id.tv_singer);
        }

    }
}
