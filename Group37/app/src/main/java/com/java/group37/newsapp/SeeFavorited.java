package com.java.group37.newsapp;

import android.content.Intent;
import android.os.Bundle;
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
    RefreshListView list;
    private news_adapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        String[] fl = fileList();
        for (String name : fileList()){
            if (name.startsWith("FavoritedNews_")){
                try{
                    FileInputStream fis = openFileInput(name);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String rawJSONString = br.readLine();
                    int picNum = Integer.parseInt(br.readLine());
                    if (picNum == 0){
                        News n = new News(rawJSONString,new String[0]);
                    }
                    else{
                        String[] picURLs = new String[picNum];
                        Log.println(Log.ERROR,"error",name);
                        Log.println(Log.ERROR,"error",String.valueOf(picNum));
                        for (int i=0;i<picNum;i++){
                            picURLs[i] = "pic"+String.valueOf(i)+"_"+name;
                        }
                        News n = new News(rawJSONString,picURLs);
                        NewsList.add(n);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        list = (RefreshListView) findViewById (R.id.Nlistview);
        newsAdapter = new news_adapter(this,NewsList);
        list.setAdapter(newsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.e("aa","click "+ i + " " + l);
                News singleNews = NewsList.get(i-1);
                Intent intent = new Intent(SeeFavorited.this, ShowDetails.class);
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
                intent.putExtra("isUsingLocalPictures",true);
                startActivity(intent);
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
                        picURLs[i] = "pic"+String.valueOf(i)+"_"+name;
                    }
                    News n = new News(rawJSONString,picURLs);
                    NewsList.add(n);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
        list = (RefreshListView) findViewById (R.id.Nlistview);
        newsAdapter = new news_adapter(this,NewsList);
        list.setAdapter(newsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Log.e("aa","click "+ i + " " + l);
                News singleNews = NewsList.get(i-1);
                Intent intent = new Intent(SeeFavorited.this, ShowDetails.class);
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
                intent.putExtra("isUsingLocalPictures",true);
                startActivity(intent);
            }
        });
    }
}
