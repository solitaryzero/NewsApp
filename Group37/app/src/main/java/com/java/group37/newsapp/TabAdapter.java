package com.java.group37.newsapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

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
        if (tmpTitles == null)
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

    @Override
    public int getItemPosition(Object object) {
        return TabAdapter.POSITION_NONE;
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
    public void modify(String nowTitles){
        Log.e ("added_channels", nowTitles);
        mTitles = new ArrayList<String>();
        String[] TitlesList = nowTitles.split(" ");
        for (int i = 0; i < TitlesList.length; i++)
            mTitles.add(TitlesList[i]);

        notifyDataSetChanged();

        mCache.remove("TitlesSavedInCache");
        mCache.put("TitlesSavedInCache", nowTitles);
    }
}
