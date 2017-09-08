package com.java.group37.newsapp;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import adapter.MyPagerAdapter;

import com.java.group37.newsapp.widget.CircleFlowIndicator;
import com.java.group37.newsapp.widget.ViewFlow;
import com.gxz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

public class LayoutActivity extends AppCompatActivity {
    private static final String TAG = LayoutActivity.class.getSimpleName();
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager pager;
    private ViewFlow viewFlow;
    private CircleFlowIndicator indicator;
    private List<Integer> ids;
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        pager = (ViewPager) findViewById(R.id.pager);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ArrayList<String> list = new ArrayList<>();
        /*for (int i = 0; i < 10; i++) {
            list.add("TAB " + i);
        }*/
        list.add("推荐");
        list.add("热点");
        list.add("社会");
        list.add("娱乐");
        list.add("科技");
        list.add("体育");
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), list));
        mPagerSlidingTabStrip.setViewPager(pager);
        pager.setCurrentItem(1);
        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), list));
        mPagerSlidingTabStrip.setViewPager(pager);
        pager.setCurrentItem(1);
        viewFlow = (ViewFlow) findViewById(R.id.viewflow);
        indicator = (CircleFlowIndicator) findViewById(R.id.viewflowindicator);
        viewFlow.setFlowIndicator(indicator);

        ids = new ArrayList<Integer>();
        ids.add(R.drawable.banner1);
        ids.add(R.drawable.banner2);
        ids.add(R.drawable.banner3);
        ids.add(R.drawable.banner4);

        viewFlow.setAdapter(new LayoutActivity.ImageAdapter());
        viewFlow.startAutoFlowTimer();

        mPagerSlidingTabStrip.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "onPageSelected:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mPagerSlidingTabStrip.setOnPagerTitleItemClickListener(new PagerSlidingTabStrip.OnPagerTitleItemClickListener() {
            @Override
            public void onSingleClickItem(int position) {
                Toast.makeText(LayoutActivity.this, "单击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDoubleClickItem(int position) {
                Toast.makeText(LayoutActivity.this, "双击", Toast.LENGTH_SHORT).show();
            }
        });
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
