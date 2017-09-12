package com.java.group37.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeeFavorited extends AppCompatActivity {

    private ACache mCache;
    private List<News> NewsList = new ArrayList<News>();
    ListView list;
    private news_adapter newsAdapter;
    public static SeeFavorited sfactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sfactivity = this;
        setContentView(R.layout.recent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SkyNews");
        toolbar.setSubtitle("Favorited News");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_favorites);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_recents) {
                    Intent intent = new Intent(SeeFavorited.this, RecentActivity.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_friends) {
                    Intent intent = new Intent(SeeFavorited.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NewsList.clear();
        String[] fl = fileList();
        for (String name : fileList()){
            if (name.startsWith("FavoritedNews_")){
                try{
                    FileInputStream fis = openFileInput(name);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String rawJSONString = br.readLine();
                    int picNum = Integer.parseInt(br.readLine());
                    String[] picURLs = new String[picNum];
                    for (int i=0;i<picNum;i++){
                        picURLs[i] = getFilesDir()+"/pic"+String.valueOf(i)+"_"+name+".png";
                    }
                    News n = new News(rawJSONString,picURLs);
                    NewsList.add(n);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        list = (ListView) findViewById (R.id.Nlistview);
        newsAdapter = new news_adapter(this,NewsList);
        list.setAdapter(newsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News singleNews = NewsList.get(i);
                Intent intent = new Intent(SeeFavorited.this, ShowDetails.class);
                intent.putExtra("Headline", singleNews.news_Title);
                String longString = singleNews.news_Content.replaceAll("　", "\n");
                intent.putExtra("Details", longString);
                String[] tmpList;
                if (singleNews.isUsingLocalPictures == true)
                    tmpList = singleNews.LocalPictures.split("[ ;]");
                else
                    tmpList = singleNews.news_Pictures.split("[ ;]");
                if(tmpList.length == 0) {
                    tmpList = new String[1];
                    tmpList[0] = "";
                }
                intent.putExtra("PictureList", tmpList);
                intent.putExtra("rawJSONstring",singleNews.original_String);
                intent.putExtra("isUsingLocalPictures",true);
                startActivity(intent);
            }
        });
    }
}
