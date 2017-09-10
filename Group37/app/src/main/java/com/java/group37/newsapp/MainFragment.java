package com.java.group37.newsapp;

import android.content.Intent;
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
    ArrayList<HashMap<String, String>> mylist;
    SimpleAdapter adapter;
    private news_adapter newsAdapter;

    //******************************************
    private List<News> NewsList = new ArrayList<News>();//数据
    private ViewFlow viewFlow;
    private CircleFlowIndicator indicator;
    private List<Integer> ids;
    jsonAnalyserList analyser;
    jsonAnalyserOne oneAnalyser;
    String newsId;
    NewsUrlThread urlThread;
    OneUrlThread oneThread;

    public MainFragment()   {}
    public MainFragment(int newsType)
    {
        this.newsType = newsType;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewFlow = (ViewFlow) view.findViewById(R.id.viewflow);
        indicator = (CircleFlowIndicator) view.findViewById(R.id.viewflowindicator);
        viewFlow.setFlowIndicator(indicator);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.banner1);
        ids.add(R.drawable.banner2);
        ids.add(R.drawable.banner3);
        ids.add(R.drawable.banner4);

        viewFlow.setAdapter(new ImageAdapter());
        viewFlow.startAutoFlowTimer();

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
    private class ImageAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return ids.size();
        }
        @Override
        public Object getItem(int position) {
            return ids.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.viewflow_imageviewitem, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.banner_img);
            imageView.setImageDrawable(getResources().getDrawable(ids.get(position)));
            return convertView;
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       view = inflater.inflate(R.layout.new_listview, null);
        return view;
    }

    public void onDownPullRefresh() {
        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                //SystemClock.sleep(1000);
//**********************************************************************
                //这里是下拉刷新的新闻
                pageNo = 0;
                urlThread=new NewsUrlThread();
                urlThread.start();
                try {
                    urlThread.join();
                    if (!analyser.newsList.get(0).equals(NewsList.get(0)))
                        for (int i = 0; i < analyser.newsList.size(); i++)
                            if (!analyser.newsList.get(i).equals(NewsList.get(0)))
                                NewsList.add(i, analyser.newsList.get(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//**********************************************************************
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
                //SystemClock.sleep(1000);
//**************************************************************
                //这里是需要下滑到底部需要添加的数据：
                // update later
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

//**************************************************************
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
        newsId = NewsList.get(position-1).news_ID;
        oneThread=new OneUrlThread();
        oneThread.start();
        try {
            oneThread.join();
            News singleNews = oneAnalyser.news;
            Intent intent = new Intent(MainActivity.mactivity, ShowDetails.class);
            intent.putExtra("Headline", singleNews.news_Title);
            String longString = singleNews.news_Content.replaceAll("　", "\n");
            intent.putExtra("Details", longString);
            startActivity(intent);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
