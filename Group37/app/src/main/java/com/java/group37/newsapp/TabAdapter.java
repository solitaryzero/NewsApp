package com.java.group37.newsapp;

/**
 * Created by HQW on 2017/9/5.
 */
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class TabAdapter extends FragmentPagerAdapter
{
    /*添加新闻分类栏目*/
    public static final String[] TITLES = new String[]
            { "要闻", "体育", "娱乐", "游戏", "学习","test1","test2","test3","test4" };
    //public  String[] TITLES;
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }
    public Fragment getItem(int arg0) {
        MainFragment fragment = new MainFragment(arg0);
        return fragment;
    }
    public CharSequence getPageTitle(int position) {
        return TITLES[position % TITLES.length];
    }
    public int getCount(){
        return TITLES.length;
    }
}
