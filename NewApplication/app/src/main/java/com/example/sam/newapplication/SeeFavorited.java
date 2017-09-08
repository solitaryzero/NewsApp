package com.example.sam.newapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;

public class SeeFavorited extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_favorited);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitle("Favorited");

        String[] list = fileList();
        ListView fav = (ListView) findViewById(R.id.FavList);
        for (String name : list){
            Log.println(Log.INFO,"",name);
            if (name.contains("FavoritedNews_")){
                try{
                    FileInputStream fis = openFileInput(name);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    String s = br.readLine();
                    TextView t = new TextView(this);
                    t.setText(s);
                    ListView.LayoutParams lp = new ListView.LayoutParams(
                            ListView.LayoutParams.MATCH_PARENT,
                            ListView.LayoutParams.WRAP_CONTENT
                    );
                    fav.addView(t,lp);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

}
