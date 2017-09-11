package com.java.group37.newsapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.java.group37.newsapp.widget.CircleFlowIndicator;
import com.java.group37.newsapp.widget.ViewFlow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment implements OnRefreshListener,OnItemClickListener
{
    private int newsType = 0;
    private int pageNo = 0;
    View view;
    RefreshListView list;
    private news_adapter newsAdapter;

    //******************************************
    private List<News> NewsList = new ArrayList<News>();//数据
   // private ViewFlow viewFlow;
    //private CircleFlowIndicator indicator;
    //private List<Integer> ids;
    jsonAnalyserList analyser;
    jsonAnalyserOne oneAnalyser;
    String newsId;
    NewsUrlThread urlThread;
    OneUrlThread oneThread;

    private ACache mCache;

    public MainFragment()   {}
    public MainFragment(int newsType)
    {
        this.newsType = newsType;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        mCache = ACache.get(MainActivity.mactivity);
        super.onActivityCreated(savedInstanceState);
        pageNo = 0;
        urlThread=new NewsUrlThread();
        urlThread.start();
        try {
            urlThread.join();
            for (int i = 0; i < analyser.newsList.size(); i++)
                NewsList.add(analyser.newsList.get(i));

            newsAdapter = new news_adapter(getActivity(),NewsList);
            list = (RefreshListView) view.findViewById (R.id.Nlistview);
            list.setAdapter(newsAdapter);

            list.setOnRefreshListener(this);
            list.setOnItemClickListener(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.new_listview, null);
        return view;
    }

    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                //这里是下拉刷新的新闻
                pageNo = 0;
                urlThread=new NewsUrlThread();
                urlThread.start();
                try {
                    urlThread.join();
                    int flag;
                    for (int i = 0; i < analyser.newsList.size(); i++)
                    {
                        flag = 0;
                        for (int j = 0; j < NewsList.size(); j++)
                            if (analyser.newsList.get(i).news_ID.equals(NewsList.get(j).news_ID)) {
                                flag = 1;
                                break;
                            }
                        if (flag == 0)
                            NewsList.add(i, analyser.newsList.get(i));
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }
            protected void onPostExecute(Void result){
                newsAdapter.notifyDataSetChanged();
                list.hideHeaderView();
            }
        }.execute(new Void[] {});
    }

    public void onLoadingMore() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                //这里是需要下滑到底部需要添加的数据：
                pageNo = NewsList.size();
                urlThread=new NewsUrlThread();
                urlThread.start();
                try {
                    urlThread.join();
                    for (int i = 0; i < analyser.newsList.size(); i++)
                        NewsList.add(analyser.newsList.get(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }
            protected void onPostExecute(Void result) {
                newsAdapter.notifyDataSetChanged();
                list.hideFooterView();
            }
        }.execute(new Void[] {});
    }

    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        //Toast.makeText(getActivity(), "Click item" + position, Toast.LENGTH_SHORT).show();
        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setTextColor(Color.BLUE);
        newsId = NewsList.get(position-1).news_ID;

        String tmpString = mCache.getAsString(newsId);
        //String tmpString = null;
        if (tmpString == null) {
            oneThread = new OneUrlThread();
            oneThread.start();
            try {
                oneThread.join();
                News singleNews = oneAnalyser.news;
                mCache.put(newsId, oneAnalyser.news.original_String);

                String nowFileString = mCache.getAsString("FileToSaveNews");
                if (nowFileString == null)
                    nowFileString = newsId;
                else
                {
                    mCache.remove("FileToSaveNews");
                    nowFileString += " " + newsId;
                }
                mCache.put("FileToSaveNews",nowFileString);

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
            startActivity(intent);
        }
    }

    class NewsUrlThread extends Thread {
        @Override
        public void run()
        {
            LatestNewsAccesser accesser = new LatestNewsAccesser(pageNo, newsType);
            String jsonString = accesser.stringBuilder.toString();
            Log.i("pageNo", Integer.toString(pageNo));
            Log.i("newsType",Integer.toString(newsType));
            Log.i("json",jsonString);
            analyser = new jsonAnalyserList(jsonString);
        }
    }

    class OneUrlThread extends Thread {
        @Override
        public void run()
        {
            OneNewsAccesser oneAccesser = new OneNewsAccesser(newsId);
            String jsonOneString = oneAccesser.stringBuilder.toString();
            oneAnalyser = new jsonAnalyserOne(jsonOneString);
        }
    }
}
