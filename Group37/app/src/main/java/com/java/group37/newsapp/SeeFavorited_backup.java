package com.java.group37.newsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeeFavorited_backup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_favorited);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Favorited");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String[] list = fileList();
        ListView fav = (ListView) findViewById(R.id.FavList);
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
        for (String name : list){
            //Log.println(Log.INFO,"",name);
            if (name.contains("FavoritedNews_")){
                try{
                    FileInputStream fis = openFileInput(name);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    Map<String,Object> m = new HashMap<String,Object>();
                    String title = br.readLine();
                    String details = "";
                    String s;
                    while ((s = br.readLine()) != null){
                        details += s+'\n';
                    }
                    m.put("title",title);
                    m.put("details",details);
                    data.add(m);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this,data,R.layout.favlistlayout,
                new String[]{"title","details"},
                new int[]{R.id.title,R.id.details});
        fav.setAdapter(adapter);

        fav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (l == -1){
                    return;
                }
                int realPosition = (int)l;
                Intent intent = new Intent(SeeFavorited_backup.this,ShowDetails.class);
                HashMap<String,Object> hm = (HashMap<String,Object>) adapterView.getItemAtPosition(realPosition);
                intent.putExtra("Headline",hm.get("title").toString());
                intent.putExtra("Details",hm.get("details").toString());
                String[] s = new String[1];
                s[0] = "";
                intent.putExtra("PictureList",s);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        String[] list = fileList();
        ListView fav = (ListView) findViewById(R.id.FavList);
        List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
        for (String name : list){
            //Log.println(Log.INFO,"",name);
            if (name.contains("FavoritedNews_")){
                try{
                    FileInputStream fis = openFileInput(name);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    Map<String,Object> m = new HashMap<String,Object>();
                    String title = br.readLine();
                    String details = "";
                    String s;
                    while ((s = br.readLine()) != null){
                        details += s+'\n';
                    }
                    m.put("title",title);
                    m.put("details",details);
                    data.add(m);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        SimpleAdapter adapter = new SimpleAdapter(this,data,R.layout.favlistlayout,
                new String[]{"title","details"},
                new int[]{R.id.title,R.id.details});
        fav.setAdapter(adapter);

        fav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (l == -1){
                    return;
                }
                int realPosition = (int)l;
                Intent intent = new Intent(SeeFavorited_backup.this,ShowDetails.class);
                HashMap<String,Object> hm = (HashMap<String,Object>) adapterView.getItemAtPosition(realPosition);
                intent.putExtra("Headline",hm.get("title").toString());
                intent.putExtra("Details",hm.get("details").toString());
                String[] s = new String[1];
                s[0] = "";
                intent.putExtra("PictureList",s);
                startActivity(intent);
            }
        });
    }
}
