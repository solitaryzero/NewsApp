package com.java.group37.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DELL on 2017/9/10.
 */
public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ACache mCache;
    private List<News> NewsList = new ArrayList<News>();
    RefreshListView list;
    private news_adapter newsAdapter;
    jsonAnalyserList analyser;
    jsonAnalyserOne oneAnalyser;
    String newsIdSearch;
    String searchKeywords;
    int pageNo;

    SearchThread searchThread;
    OneSearchThread oneSearchThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SkyNews");
        toolbar.setSubtitle("Search Results");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mCache = ACache.get(MainActivity.mactivity);
        list = (RefreshListView) findViewById (R.id.Nlistview);
        Intent intent = getIntent();
        searchKeywords = intent.getStringExtra("Keywords");

        pageNo = 0;

        searchThread=new SearchThread();
        searchThread.start();
        try {
            searchThread.join();
            NewsList = new ArrayList<News>();
            for (int i = 0; i < analyser.newsList.size(); i++)
            {
                NewsList.add(analyser.newsList.get(i));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        newsAdapter = new news_adapter(this,NewsList);
        list.setAdapter(newsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("aa","click "+ i + " " + l);
                newsIdSearch = NewsList.get(i-1).news_ID;

                String tmpString = mCache.getAsString(newsIdSearch);
                //String tmpString = null;
                if (tmpString == null) {
                    oneSearchThread = new OneSearchThread();
                    oneSearchThread.start();
                    try {
                        oneSearchThread.join();
                        News singleNews = oneAnalyser.news;

                        mCache.put(newsIdSearch, oneAnalyser.news.original_String);

                        String nowFileString = mCache.getAsString("FileToSaveNews");
                        if (nowFileString == null)
                            nowFileString = newsIdSearch;
                        else
                        {
                            mCache.remove("FileToSaveNews");
                            nowFileString += " " + newsIdSearch;
                        }
                        mCache.put("FileToSaveNews",nowFileString);

                        Intent intent = new Intent(MainActivity.mactivity, ShowDetails.class);
                        intent.putExtra("Headline", singleNews.news_Title);
                        String longString = singleNews.news_Content.replaceAll("　", "\n");
                        intent.putExtra("Details", longString);
                        String[] tmpList;
                        tmpList = singleNews.news_Pictures.split("[ ;]");
                        if(tmpList.length == 0) {
                            tmpList = new String[1];
                            tmpList[0] = "";
                        }

                        intent.putExtra("PictureList", tmpList);
                        intent.putExtra("rawJSONstring",singleNews.original_String);
                        //Log.e("original", singleNews.original_String);
                        intent.putExtra("isUsingLocalPictures", false);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    oneAnalyser = new jsonAnalyserOne(tmpString);
                    News singleNews = oneAnalyser.news;
                    Intent intent = new Intent(MainActivity.mactivity, ShowDetails.class);
                    intent.putExtra("Headline", singleNews.news_Title);
                    String longString = singleNews.news_Content.replaceAll("　", "\n");
                    intent.putExtra("Details", longString);
                    String[] tmpList = singleNews.news_Pictures.split("[ ;]");
                    if(tmpList.length == 0) {
                        tmpList = new String[1];
                        tmpList[0] = "";
                    }
                    intent.putExtra("PictureList", tmpList);
                    intent.putExtra("rawJSONstring",singleNews.original_String);
                    //Log.e("original", singleNews.original_String);
                    intent.putExtra("isUsingLocalPictures", false);
                    startActivity(intent);
                }
            }
        });



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //startActivity(new Intent(this, LayoutActivity.class));


    }
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    class SearchThread extends Thread {
        @Override
        public void run()
        {
            SearchNewsAccesser accesser = new SearchNewsAccesser(0, 0, searchKeywords);
            String jsonString = accesser.stringBuilder.toString();
            Log.i("pageNo", Integer.toString(pageNo));
            Log.i("json",jsonString);
            analyser = new jsonAnalyserList(jsonString);
        }
    }
    class OneSearchThread extends Thread {
        @Override
        public void run()
        {
            OneNewsAccesser oneAccesser = new OneNewsAccesser(newsIdSearch);
            String jsonOneString = oneAccesser.stringBuilder.toString();
            oneAnalyser = new jsonAnalyserOne(jsonOneString);
        }
    }
}