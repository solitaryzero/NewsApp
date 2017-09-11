package com.java.group37.newsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.*;
/**
 * Created by kmy on 2017/9/11.
 */

public class TabAdapter extends FragmentPagerAdapter
{
    /*添加新闻分类栏目*/
    private List<String> mTitles;
    private FragmentManager fm;
    public static final String[] titles = new String[]
    { "全部","科技", "教育", "军事", "国内", "社会","文化","汽车","国际","体育","财经","健康","娱乐" };
    private ACache mCache;
    //public  String[] TITLES;
    public TabAdapter(FragmentManager fm)
    {
        super(fm);
        this.fm = fm;
        mTitles = new ArrayList<String>();
        mCache = ACache.get(MainActivity.mactivity);
        String tmpTitles = mCache.getAsString("TitlesSavedInCache");
        //if (tmpTitles == null)
        if(true)
        {
            for (int i = 0; i < titles.length; i++)
                mTitles.add(titles[i]);
            tmpTitles = titles[0];
            for (int i = 1; i < titles.length; i++)
                tmpTitles += " " + titles[i];
            mCache.put("TitlesSavedInCache",tmpTitles);
        }
        else
        {
            String listTitles[] = tmpTitles.split(" ");
            for (int i = 0; i < listTitles.length; i++)
                mTitles.add(listTitles[i]);
        }
    }
    public Fragment getItem(int arg0) {
        MainFragment fragment = new MainFragment(arg0);
        return fragment;
    }
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position % mTitles.size());
    }
    public int getCount(){
        return mTitles.size();
    }
    public void addFragment(String title){
        mTitles.add(title);
        notifyDataSetChanged();
        String tmpTitles = mCache.getAsString("TitlesSavedInCache");
        tmpTitles += " " + title;
        mCache.remove("TitlesSavedInCache");
        mCache.put("TitlesSavedInCache", tmpTitles);
    }

    public void removeFragment(String title){
        int index = mTitles.indexOf(title);
        if (index == -1)
            return;
        mTitles.remove(index);
        notifyDataSetChanged();
        String tmpTitles = mTitles.get(0);
        for (int i = 1; i < mTitles.size(); i++)
            tmpTitles += " " + mTitles.get(i);
        mCache.remove("TitlesSavedInCache");
        mCache.put("TitlesSavedInCache",tmpTitles);
    }
}
