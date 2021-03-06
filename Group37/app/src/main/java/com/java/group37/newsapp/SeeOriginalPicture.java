package com.java.group37.newsapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import java.io.File;

public class SeeOriginalPicture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_original_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("OriginalImage");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageView img = (ImageView) findViewById(R.id.Image);
        String url = getIntent().getStringExtra("URL");
        boolean isUsingLocalPictures = getIntent().getBooleanExtra("isUsingLocalPictures",false);
        if (isUsingLocalPictures){
            File f = new File(url);
            Glide.with(this).load(f).placeholder(R.drawable.loading).error(R.drawable.not_found).dontAnimate().into(img);
        } else {
            Glide.with(this).load(url).placeholder(R.drawable.loading).error(R.drawable.not_found).dontAnimate().into(img);
        }
    }

}
