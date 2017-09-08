package com.java.group37.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class news_adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<NewsItem> mDatas;


    public news_adapter(Context context, List<NewsItem> datas){
        this.mDatas = datas;
        mInflater = LayoutInflater.from(context);
        //****************************************
    }


    public void addAll(List<NewsItem> mDatas){
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

        NewsItem newsItem = mDatas.get(position);
        holder.mTitle.setText(newsItem.getTitle());
        holder.mContent.setText(newsItem.getContent());
        holder.mDate.setText(newsItem.getDate());

        //******************************************************
        //处理文章是否有无图片链接
        //******************************************************
        if(newsItem.getImgLink() != null){
            holder.mImg.setVisibility(View.VISIBLE);
            //**********************
        }
        else{
            holder.mImg.setVisibility(View.GONE);
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


