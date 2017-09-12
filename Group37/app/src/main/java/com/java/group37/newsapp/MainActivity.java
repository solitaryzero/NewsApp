package com.java.group37.newsapp;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.java.group37.newsapp.adapter.TitleFragmentAdapter;
import com.java.group37.newsapp.baseactivity.BaseActivity;
import com.java.group37.newsapp.bean.MyChannel;
import com.java.group37.newsapp.bgarefreshlayoutviewholder.DefineBAGRefreshWithLoadView;
import com.java.group37.newsapp.recycleradapter.RecyclerViewAdapter;
import com.java.group37.newsapp.util.GsonUtil;
import com.java.group37.newsapp.widget.CircleFlowIndicator;
import com.java.group37.newsapp.widget.ViewFlow;
import com.gxz.PagerSlidingTabStrip;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.trs.channellib.channel.channel.helper.ChannelDataHelepr;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.refreshlayout.BGARefreshLayout;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class MainActivity extends BaseActivity
        implements  BGARefreshLayout.BGARefreshLayoutDelegate,NavigationView.OnNavigationItemSelectedListener {
    private BGARefreshLayout mBGARefreshLayout;
    public static MainActivity mactivity;
    public static Context mainContext;
    public static int i = 0;
    private ACache mCache;
    private RecyclerView mRecyclerView;
    private Context mContext;
    /** title */
    private TextView mTitle;
    /** 数据 */
    private List<String> mListData = new ArrayList<String>();
    /** 一次加载数据的条数 */
    private int DATASIZE = 10;
    /** 数据填充adapter */
    private RecyclerViewAdapter mRecyclerViewAdapter = null;
    /** 设置一共请求多少次数据 */
    private int ALLSUM = 0;
    /** 设置刷新和加载 */
    private DefineBAGRefreshWithLoadView mDefineBAGRefreshWithLoadView = null;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager pager;

    private ViewFlow viewFlow;
    private CircleFlowIndicator indicator;
    private List<Integer> ids;
    private PagerSlidingTabStrip mIndicator;
    private ViewPager mViewPager;
    private TabAdapter mAdapter ;
    private BottomNavigationView butttom;
    NavigationView navigationView;
    String menutitle1;
    String menutitle2 = "切换到无图模式";
    View switch_view;
    //TitleFragmentAdapter adapter;
    //List<MyChannel> myChannels;
    //ChannelDataHelepr<MyChannel> dataHelepr;
    private int needShowPosition=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        if (i % 2 == 0){
            menutitle1 = "切换到夜间模式";
        }
        else{
            menutitle1 = "切换到日间模式";
        }
        mactivity=this;
        mCache = ACache.get(MainActivity.mactivity);
        mCache.put("IsHavingPictures","true");
        mainContext = this.getBaseContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mIndicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);//导航栏
        mViewPager = (ViewPager) findViewById(R.id.pager);//导航栏下的page
        mAdapter = new TabAdapter(getSupportFragmentManager());//导航栏
        mViewPager.setAdapter(mAdapter);//导航栏适配器

        //mAdapter.removeFragment("财经");


        mIndicator.setViewPager(mViewPager);

        switch_view = findViewById(R.id.iv_subscibe);
        switch_view.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                startActivityForResult(intent, 1357);
            }
        });

        //dataHelepr = new ChannelDataHelepr(this, (ChannelDataHelepr.ChannelDataRefreshListenter) this, findViewById(R.id.top_bar));
        //dataHelepr.setSwitchView(switch_view);
        //myChannels = new ArrayList<>();
        //adapter = new TitleFragmentAdapter(getSupportFragmentManager(), myChannels);
        //loadData();



        //startActivity(new Intent(this, LayoutActivity.class));

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_friends);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_favorites) {
                    Intent intent = new Intent(MainActivity.this, SeeFavorited.class);
                    startActivity(intent);
                }
                if (tabId == R.id.tab_recents) {
                    Intent intent = new Intent(MainActivity.this, RecentActivity.class);
                    startActivity(intent);
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu mn = navigationView.getMenu();
        MenuItem menuItem = mn.findItem(R.id.nav_slideshow);
        menuItem.setTitle(menutitle1);
        menuItem = mn.findItem(R.id.nav_gallery);
        menuItem.setTitle(menutitle2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//最顶栏
        toolbar.setTitle("SkyNews");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1357 && resultCode == 2468)
        {
            String added_channels = data.getStringExtra("result");
            mAdapter.modify(added_channels);
            mViewPager.setAdapter(mAdapter);
            mIndicator.setViewPager(mViewPager);
        }
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_friends);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);//最顶栏
        toolbar.setTitle("SkyNews");
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

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
                convertView = getLayoutInflater().inflate(R.layout.viewflow_imageviewitem, null);
            }
            ImageView imageView = (ImageView) convertView.findViewById(R.id.banner_img);
            imageView.setImageDrawable(getResources().getDrawable(ids.get(position)));
            return convertView;
        }
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        final SearchView searchView = (SearchView)menu.findItem(R.id.ab_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            //输入完成后，点击回车或是完成键
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 0) {
                    Log.e(query,"我是点击回车按钮");
                    Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    intent.putExtra("Keywords", query);
                    startActivity(intent);
                }
                return true;
            }

            //查询文本框有变化时事件
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {
            Menu mn = navigationView.getMenu();
            MenuItem menuItem = mn.findItem(R.id.nav_gallery);
            if (menuItem.getTitle().equals("切换到无图模式")){
                String isPic = mCache.getAsString("IsHavingPictures");
                if (isPic != null)
                    mCache.remove("IsHavingPictures");
                isPic = "false";
                //Log.e("Setispic", isPic);
                mCache.put("IsHavingPictures",isPic);

                menuItem.setTitle("切换到有图模式");
            }else{
                String isPic = mCache.getAsString("IsHavingPictures");
                if (isPic != null)
                    mCache.remove("IsHavingPictures");
                isPic = "true";
                menuItem.setTitle("切换到无图模式");
            }
        } else if (id == R.id.nav_slideshow) {
            if (i % 2 == 0){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            i++;}
            else{
                i++;
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            recreate();
        }  else if (id == R.id.nav_share) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            final View layout = inflater.inflate(R.layout.dialog, null);//获取自定义布局
            builder.setView(layout);
            //builder.setIcon(R.drawable.ic_launcher);
            //builder.setMessage("浏览wuyudong的博客?");
            TextView textView = (TextView)layout.findViewById(R.id.nav_gallery);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    /** 进入页面首次加载数据 */
    // @Override
    /*protected void onStart() {
        super.onStart();
        mBGARefreshLayout.beginRefreshing();
        onBGARefreshLayoutBeginRefreshing(mBGARefreshLayout);
    }*/

    private void initView() {
        //mTitle = (TextView) findViewById(R.id.fml_title);
        //mTitle.setText("自定义刷新和加载更多样式");
        //mRecyclerView = (RecyclerView) findViewById(R.id.define_bga_refresh_with_load_recycler);
        //得到控件
        //mBGARefreshLayout = (BGARefreshLayout) findViewById(R.id.define_bga_refresh_with_load);
        //设置刷新和加载监听
        //mBGARefreshLayout.setDelegate(this);
    }
    /**
     * 设置 BGARefreshLayout刷新和加载
     * */
    private void setBgaRefreshLayout() {
        mDefineBAGRefreshWithLoadView = new DefineBAGRefreshWithLoadView(mContext , true , true);
        //设置刷新样式
        mBGARefreshLayout.setRefreshViewHolder(mDefineBAGRefreshWithLoadView);
        mDefineBAGRefreshWithLoadView.updateLoadingMoreText("自定义加载更多");
    }
    /** 设置RecyclerView的布局方式 */
    private void setRecyclerView(){
        //垂直listview显示方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }
    /** 模拟请求网络数据 */
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    mListData.clear();
                    setData();
                    mBGARefreshLayout.endRefreshing();
                    break;
                case 1:
                    setData();
                    mBGARefreshLayout.endLoadingMore();
                    break;
                case 2:
                    mBGARefreshLayout.endLoadingMore();
                    break;
                default:
                    break;

            }
        }
    };
    /**
     * 添加假数据
     * */
    private void setData() {
        for(int i = 0 ; i < DATASIZE ; i++){
            mListData.add("第" + (ALLSUM * 10 + i) +"条数据");
        }
        if(ALLSUM == 0){
            setRecyclerCommadapter();
        }else{
            mRecyclerViewAdapter.notifyDataSetChanged();
        }
    }
    /** 数据填充 */
    private void setRecyclerCommadapter() {
        mRecyclerViewAdapter = new RecyclerViewAdapter(mContext , mListData);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        //点击事件
        mRecyclerViewAdapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(mContext, "onclick  " + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View v, int position) {
                Toast.makeText(mContext, "onlongclick  " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /** 刷新 */
    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        mDefineBAGRefreshWithLoadView.updateLoadingMoreText("自定义加载更多");
        mDefineBAGRefreshWithLoadView.showLoadingMoreImg();
        ALLSUM = 0;
        handler.sendEmptyMessageDelayed(0 , 2000);
    }
    /** 加载 */
    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        if(ALLSUM == 2){
            /** 设置文字 **/
            mDefineBAGRefreshWithLoadView.updateLoadingMoreText("没有更多数据");
            /** 隐藏图片 **/
            mDefineBAGRefreshWithLoadView.hideLoadingMoreImg();
            handler.sendEmptyMessageDelayed(2 , 2000);
            return true;
        }
        ALLSUM++;
        handler.sendEmptyMessageDelayed(1 , 2000);
        return true;
    }
}
