package com.java.group37.newsapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SimpleAdapter;

import com.roughike.bottombar.BottomBar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DELL on 2017/9/10.
 */
public class RecentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ACache mCache;

    RefreshListView list;
    private news_adapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SkyNews");
        toolbar.setSubtitle("Recent News");
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        mCache = ACache.get(MainActivity.mactivity);
        String nowFileString = mCache.getAsString("FileToSaveNews");
        if (nowFileString == null)
            nowFileString = "NULL";
        Log.i("nowFile", nowFileString);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //startActivity(new Intent(this, LayoutActivity.class));

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.selectTabWithId(R.id.tab_recents);
        list = (RefreshListView) findViewById (R.id.Nlistview);

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