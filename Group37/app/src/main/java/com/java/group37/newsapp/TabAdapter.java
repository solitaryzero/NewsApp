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
    { "全部","科技", "教育", "军事", "国内", "社会","文化","汽车","国际","体育","财经","健康","娱乐" };
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
