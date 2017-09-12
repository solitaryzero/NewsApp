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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by DELL on 2017/9/10.
 */
public class SearchActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ACache mCache;
    private List<News> NewsList = new ArrayList<News>();
    ListView list;
    private news_adapter newsAdapter;
    jsonAnalyserList analyser;
    jsonAnalyserOne oneAnalyser;
    jsonAnalyserOne recommendAnalyser;
    String newsIdSearch;
    String searchKeywords;
    int pageNo;
    String newsIdRecommend;

    SearchThread searchThread;
    OneSearchThread oneSearchThread;
    RecommendUrlThread recommendThread;
    OneRecommendThread oneRecommendThread;
    private List<News> RecommendList = new ArrayList<News>();

    //to recommend news
    public void modifyCache(News singleNews)
    {
        Log.e("modifyCache", Integer.toString(singleNews.Keywords.size()));
        String nowFileString = mCache.getAsString("FileOfWordsToRecommend");
        List<String> wordList = new ArrayList<String>();
        List<Double> wordScoreList = new ArrayList<Double>();
        if (nowFileString != null)
        {
            mCache.remove("FileOfWordsToRecommend");
            String[] wordListFile = nowFileString.split(" ");
            for (int i = 0; i < wordListFile.length; i++)
            {
                String currentWord = wordListFile[i];
                if (!currentWord.equals(""))
                {
                    String currentWordScoreString = mCache.getAsString(currentWord);
                    if (currentWordScoreString != null) {
                        mCache.remove(currentWord);
                        wordList.add(currentWord);
                        wordScoreList.add(Double.valueOf(currentWordScoreString) / 2.0);
                    }
                }
            }
        }
        for (int i = 0; i < singleNews.Keywords.size(); i++)
        {
            if (i >= 10) break;
            String currentWord = singleNews.Keywords.get(i);
            Double currentWordScore = singleNews.Keyword_score.get(i);
            if (!currentWord.equals(""))
            {
                int flag = 0;
                for (int j = 0; j < wordList.size(); j++)
                    if (currentWord.equals(wordList.get(j)))
                    {
                        wordScoreList.set(j, wordScoreList.get(j) + currentWordScore);
                        flag = 1;
                    }
                if (flag == 0)
                {
                    wordList.add(currentWord);
                    wordScoreList.add(currentWordScore);
                }
            }
        }
        for (int i = 0; i < wordList.size(); i++)
            for (int j = 0; j < i; j++)
                if (wordScoreList.get(j) < wordScoreList.get(j+1)) {
                    Collections.swap(wordList, j, j+1);
                    Collections.swap(wordScoreList, j, j+1);
                }
        String toCacheString = "";
        searchKeywords = "";
        if (wordList.size() > 0) {
            toCacheString = wordList.get(0);
            searchKeywords = wordList.get(0);
        }
        for (int i = 1; i < wordList.size(); i++){
            if (i >= 10)    break;
            if (i < 3)  searchKeywords += wordList.get(i);
            toCacheString += " " + wordList.get(i);
            mCache.put(wordList.get(i), Double.toString(wordScoreList.get(i)));
        }
        mCache.put("FileOfWordsToRecommend", toCacheString);
        //Log.e("ToCacheString", toCacheString);
        searchRecommend();
    }

    public void searchRecommend(){
        recommendThread=new RecommendUrlThread();
        recommendThread.start();
        try {
            recommendThread.join();
            RecommendList = new ArrayList<News>();
            for (int i = 0; i < analyser.newsList.size(); i++)
            {
                if (i >= 3) break;
                newsIdRecommend = analyser.newsList.get(i).news_ID;
                //Log.e("recommend"+i, newsIdRecommend);
                oneRecommendThread = new OneRecommendThread();
                oneRecommendThread.start();
                oneRecommendThread.join();
                RecommendList.add(recommendAnalyser.news);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mCache = ACache.get(MainActivity.mactivity);
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
        list = (ListView) findViewById (R.id.Nlistview);
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
                newsIdSearch = NewsList.get(i).news_ID;

                String tmpString = mCache.getAsString(newsIdSearch);
                //String tmpString = null;
                if (tmpString == null) {
                    oneSearchThread = new OneSearchThread();
                    oneSearchThread.start();
                    try {
                        oneSearchThread.join();
                        News singleNews = oneAnalyser.news;

                        modifyCache(singleNews);

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
                        if (RecommendList.size() > 0)
                            intent.putExtra("RecommendRawJsonString0", RecommendList.get(0).original_String);
                        if (RecommendList.size() > 1)
                            intent.putExtra("RecommendRawJsonString1", RecommendList.get(1).original_String);
                        if (RecommendList.size() > 2)
                            intent.putExtra("RecommendRawJsonString2", RecommendList.get(2).original_String);
                        startActivity(intent);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    oneAnalyser = new jsonAnalyserOne(tmpString);
                    News singleNews = oneAnalyser.news;
                    modifyCache(singleNews);
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
                    if (RecommendList.size() > 0)
                        intent.putExtra("RecommendRawJsonString0", RecommendList.get(0).original_String);
                    if (RecommendList.size() > 1)
                        intent.putExtra("RecommendRawJsonString1", RecommendList.get(1).original_String);
                    if (RecommendList.size() > 2)
                        intent.putExtra("RecommendRawJsonString2", RecommendList.get(2).original_String);
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

    class RecommendUrlThread extends Thread {
        @Override
        public void run()
        {
            SearchNewsAccesser accesser = new SearchNewsAccesser(0, 0, searchKeywords);
            String jsonString = accesser.stringBuilder.toString();
            //Log.i("pageNo", Integer.toString(pageNo));
            //Log.i("newsType",Integer.toString(newsType));
            Log.i("json",jsonString);
            analyser = new jsonAnalyserList(jsonString);
        }
    }

    class OneRecommendThread extends Thread {
        @Override
        public void run()
        {
            OneNewsAccesser oneAccesser = new OneNewsAccesser(newsIdRecommend);
            String jsonOneString = oneAccesser.stringBuilder.toString();
            recommendAnalyser = new jsonAnalyserOne(jsonOneString);
        }
    }
}