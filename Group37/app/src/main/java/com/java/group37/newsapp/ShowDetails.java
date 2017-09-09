package com.java.group37.newsapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.*;
import java.security.MessageDigest;
import java.util.Arrays;

public class ShowDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Details");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        String headlineStr = intent.getStringExtra("Headline");
        String detailsStr = intent.getStringExtra("Details");
        TextView headline = (TextView) findViewById(R.id.NewsHeadline);
        TextView details = (TextView) findViewById(R.id.NewsDetail);
        headline.setText(headlineStr);
        details.setText(detailsStr);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_favorite);
        TextView headline = (TextView) findViewById(R.id.NewsHeadline);
        String fileName = "FavoritedNews_" + toMD5(headline.getText().toString());
        if (Arrays.asList(fileList()).contains(fileName)){
            item.setIcon(R.drawable.ic_full_star);
        } else {
            item.setIcon(R.drawable.ic_empty_star);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_details, menu);
        return true;
    }

    private String toMD5(String source){
        StringBuffer sb = new StringBuffer(200);

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(source.getBytes("utf-8"));

            for (int i = 0; i < array.length; i++) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).toUpperCase().substring(1, 3));
            }
        } catch (Exception e) {
            return null;
        }

        return sb.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_favorite) {
            FileOutputStream fileOutputStream;
            BufferedWriter bufferedWriter;
            TextView headline = (TextView) findViewById(R.id.NewsHeadline);
            TextView detail = (TextView) findViewById(R.id.NewsDetail);
            String fileName = "FavoritedNews_" + toMD5(headline.getText().toString());

            try {
                if (Arrays.asList(fileList()).contains(fileName)){
                    //Toast.makeText(this,"Already favorited",Toast.LENGTH_LONG).show();
                    deleteFile(fileName);
                    item.setIcon(R.drawable.ic_empty_star);
                } else {
                    fileOutputStream = openFileOutput(fileName,
                            Context.MODE_PRIVATE);
                    bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(fileOutputStream));
                    bufferedWriter.write(headline.getText().toString() + "\n");
                    bufferedWriter.write(detail.getText().toString());
                    bufferedWriter.close();
                    item.setIcon(R.drawable.ic_full_star);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
