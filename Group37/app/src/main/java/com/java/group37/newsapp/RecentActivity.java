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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DELL on 2017/9/10.
 */
public class RecentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ACache mCache;
    private List<News> NewsList = new ArrayList<News>();
    ListView list;
    private news_adapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SkyNews");
        toolbar.setSubtitle("Recent News");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mCache = ACache.get(MainActivity.mactivity);
        String nowFileString = mCache.getAsString("FileToSaveNews");
        String[] recentNewsList;
        if (nowFileString == null)
            recentNewsList = new String[0];
        else
            recentNewsList = nowFileString.split(" ");
        Log.i("nowFile", nowFileString);
        for (int i = 0; i < recentNewsList.length; i++)
        {
            String jsonOneString = mCache.getAsString(recentNewsList[recentNewsList.length - i - 1]);
            jsonAnalyserOne oneAnalyser = new jsonAnalyserOne(jsonOneString);
            News singleNews = oneAnalyser.news;
            NewsList.add(singleNews);
        }
        list = (ListView) findViewById (R.id.Nlistview);
        newsAdapter = new news_adapter(this,NewsList);
        list.setAdapter(newsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.e("aa","click "+ i + " " + l);
                News singleNews = NewsList.get(i);
                Intent intent = new Intent(RecentActivity.this, ShowDetails.class);
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
                intent.putExtra("isUsingLocalPictures", false);
                startActivity(intent);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //startActivity(new Intent(this, LayoutActivity.class));

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_recents);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_favorites) {
                    Intent intent = new Intent(RecentActivity.this, SeeFavorited.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_friends) {
                    Intent intent = new Intent(RecentActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_recents);
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
}