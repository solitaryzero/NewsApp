package com.java.group37.newsapp;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

public class news_adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<News> mDatas;

    public news_adapter(Context context, List<News> datas){
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        //****************************************
    }


    public void addAll(List<News> mDatas){
        this.mDatas.addAll(mDatas);
    }

    public int getCount(){
        return mDatas.size();
    }
    public Object getItem(int postion){
        return mDatas.get(postion);
    }
    public long getItemId(int position){
        return position;
    }
    public View getView(int position, View view, ViewGroup parent){
        ViewHolder holder= null;
        if(view == null){
            View tempView = mInflater.inflate(R.layout.new_item,null);
            //view = mInflater.inflate(R.layout.item,null);
            view = tempView;
            holder = new ViewHolder();
            holder.mContent = (TextView) view.findViewById(R.id.content);
            holder.mTitle = (TextView) view.findViewById(R.id.title);
            holder.mDate = (TextView) view.findViewById(R.id.date);
            holder.mImg = (ImageView) view.findViewById(R.id.img);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder) view.getTag();
        }

        News newsItem = mDatas.get(position);
        holder.mTitle.setText(newsItem.news_Title+"                               ");
        holder.mContent.setText(newsItem.news_Content);
        holder.mDate.setText(newsItem.news_Time);

        //******************************************************
        //处理文章是否有无图片链接
        //******************************************************
        String[] tmpPictures = newsItem.news_Pictures.split("[ ;]");
        if(newsItem.isUsingLocalPictures == true)
        {
            tmpPictures = newsItem.LocalPictures.split("[ ;]");
            holder.mImg.setVisibility(View.VISIBLE);
            String tmpPicture = tmpPictures[0];
            File file = new File(tmpPicture);
            //加载图片
            Glide.with(MainActivity.mactivity).load(file).placeholder(R.drawable.loading).error(R.drawable.not_found).dontAnimate().into(holder.mImg);
        }
        else if(!newsItem.news_Pictures.equals("")&&tmpPictures.length != 0){
            holder.mImg.setVisibility(View.VISIBLE);
            String tmpPicture = tmpPictures[0];
            Glide.with(MainActivity.mactivity).load(tmpPicture).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.loading).error(R.drawable.not_found).dontAnimate().into(holder.mImg);
            //Glide.with(MainActivity.mactivity).load(tmpPictures).diskCacheStrategy(DiskCacheStrategy.ALL).dontAnimate().into(holder.mImg);
            //**********************
        }
        else{
            holder.mImg.setVisibility(View.VISIBLE);
            Glide.with(MainActivity.mactivity).load(R.drawable.not_found).dontAnimate().into(holder.mImg);
        }
        return view;
    }

    private final class ViewHolder{
        TextView mTitle;
        TextView mContent;
        ImageView mImg;
        TextView mDate;
    }
}


