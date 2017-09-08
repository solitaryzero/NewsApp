package com.java.group37.newsapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/9/6 0006.
 */

public class Single_activity extends Activity {
    private ImageButton backbutton;
    private ImageButton getbutton;
    private TextView stitle;
    private TextView stime;
    private TextView ssource;
    private TextView scontent;

    private String geturl;
    private myHandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_new);

        backbutton=(ImageButton)findViewById(R.id.single_topback);
        getbutton=(ImageButton)findViewById(R.id.single_topget);

        stitle=(TextView)findViewById(R.id.single_title);
        stime=(TextView)findViewById(R.id.single_time);
        ssource=(TextView)findViewById(R.id.single_source);
        scontent=(TextView)findViewById(R.id.single_content);

        Bundle getintent=getIntent().getExtras();
        geturl=getintent.getString("get_single_url");

        handler=new myHandler();
    }

    class Getthread extends Thread {
        @Override
        public void run()
        {
            HttpURLConnection conn=null;
            InputStream is=null;
            String result="";
            try{
                URL url=new URL(geturl);
                conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                if (conn.getResponseCode()==200)
                {
                    is = conn.getInputStream();
                    BufferedReader isr=new BufferedReader(new InputStreamReader(is));
                    result=isr.readLine();
                }

                Bundle bundle=new Bundle();
                bundle.putString("res",result);
                Message msg=handler.obtainMessage();
                msg.setData(bundle);
                handler.sendMessage(msg);

            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }

    class myHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg)
        {
            Bundle bundle=msg.getData();
            String result=bundle.get("res").toString();

            try {
                JSONObject jsr = new JSONObject(result);

                stitle.setText(jsr.getString("news_Title"));
                stime.setText(jsr.getString("news_Time"));
                ssource.setText(jsr.getString("Author"));
                scontent.setText(jsr.getString("news_Content"));
            }catch(JSONException e){
                e.printStackTrace();
            }

        }
    }

    public void Backfromsingle(View v)
    {
        Toast.makeText(getApplicationContext(), "you click back", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void Getsingle(View v)
    {
        Toast.makeText(getApplicationContext(), "you click get", Toast.LENGTH_SHORT).show();
    }


}
