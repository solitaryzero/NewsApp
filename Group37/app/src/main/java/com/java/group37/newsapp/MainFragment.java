package com.java.group37.newsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
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
    View view;
    RefreshListView list;
    ArrayList<HashMap<String, String>> mylist;
    SimpleAdapter adapter;
    private news_adapter newsAdapter;
    //******************************************
    private List<NewsItem> mDatas = new ArrayList<NewsItem>();//数据
    private ViewFlow viewFlow;
    private CircleFlowIndicator indicator;
    private List<Integer> ids;
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
        mDatas.add(new NewsItem("1","title1",null,null,null,"content-add-up",1));
        mDatas.add(new NewsItem("1","title2",null,null,null,"content-add-up",1));
        mDatas.add(new NewsItem("1","title3",null,null,null,"content-add-up",1));
        mDatas.add(new NewsItem("1","title4",null,null,null,"content-add-up",1));
        mDatas.add(new NewsItem("1","title5",null,null,null,"content-add-up",1));

        newsAdapter = new news_adapter(getActivity(),mDatas);
        list = (RefreshListView) view.findViewById (R.id.Nlistview);

       /* mylist = new ArrayList<HashMap<String, String>>();
        for(int i=0; i<10; i++){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("ItemTitle", "This is Title.....");
            map.put("ItemText", TabAdapter.TITLES[newsType]);
            mylist.add(map);
        }
        */
        //adapter = new SimpleAdapter(getActivity(), mylist, R.layout.item, new String[]{"ItemTitle", "ItemText"}, new int[]{R.id.title, R.id.content});

        list.setAdapter(newsAdapter);
        //list.setAdapter(adapter);
        list.setOnRefreshListener(this);
        list.setOnItemClickListener(this);

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
                SystemClock.sleep(1000);
//**********************************************************************
                //这里是下拉刷新的新闻
                List<NewsItem> addDatas = new ArrayList<NewsItem>();

                //update newst

                //int id, String title, String link, String date, String imgLink, String content, int newsType
               // addDatas.add(new NewsItem(1,"title",null,null,null,"content-add-up",1));
               // addDatas.add(new NewsItem(2,"title",null,null,null,"content-add-up",1));
                mDatas.add(0,new NewsItem("2","title",null,null,null,"content-add-uppppp",1));
                mDatas.add(1,new NewsItem("2","title",null,null,null,"content-add-uppp",1));
                mDatas.add(2,new NewsItem("2","title",null,null,null,"content-add-upp",1));
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
                SystemClock.sleep(1000);
//**************************************************************
                //这里是需要下滑到底部需要添加的数据：
                // update later

                mDatas.add(new NewsItem("1","title",null,null,null,"content-add-down",1));
                mDatas.add(new NewsItem("2","title",null,null,null,"content-add-down",1));

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
        Toast.makeText(getActivity(), "Click item" + position, Toast.LENGTH_SHORT).show();
        // click a item
        //
    }
}
