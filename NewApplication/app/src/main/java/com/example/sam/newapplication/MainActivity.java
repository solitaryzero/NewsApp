package com.example.sam.newapplication;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.*;

public class MainActivity extends AppCompatActivity {

    private TextView newsList = null;
    private Handler messageHandler;
    private Handler oneNewsMessageHandler;
    private Looper looper;
    String newsId = "";
    News singleNews;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsList = (TextView) findViewById(R.id.news_list);
        looper = Looper.myLooper();
        messageHandler = new MessageHandler(looper);
        new Thread(new Runnable(){
            @Override
            public void run() {
                String jsonString = "";
                LatestNewsAccesser accesser = new LatestNewsAccesser();
                jsonString = accesser.stringBuilder.toString();
                jsonAnalyserList analyser = new jsonAnalyserList(jsonString);
                String output = "";

                for (int i = 0; i < analyser.newsList.size(); i++)
                {
                    output += analyser.newsList.get(i).news_Title + "\n";
                    newsId = analyser.newsList.get(i).news_ID;
                }

                //output += str;
                //output += jsonString;
                //output += newsId;
                //output += oneAnalyser.news.news_Content;
                //output += tmpId;

                //output += jsonString;
                Message message = Message.obtain();
                message.obj = output;
                messageHandler.sendMessage(message);
            }
        }).start();
    }

    public void btOnClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                oneNewsMessageHandler = new OneNewsMessageHandler(looper);
                OneNewsAccesser oneAccesser = new OneNewsAccesser(newsId);
                String jsonOneString = oneAccesser.stringBuilder.toString();
                jsonAnalyserOne oneAnalyser = new jsonAnalyserOne(jsonOneString);
                singleNews = oneAnalyser.news;
                Message message = Message.obtain();
                message.obj = singleNews;
                oneNewsMessageHandler.sendMessage(message);
            }
        }).start();
    }

    public void favOnClick(View v) {
        Intent intent = new Intent(MainActivity.this,SeeFavorited.class);
        startActivity(intent);
    }

    class MessageHandler extends Handler {
        public MessageHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            newsList.append((String) msg.obj);
        }
    }

    class OneNewsMessageHandler extends Handler{
        public OneNewsMessageHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Intent intent = new Intent(MainActivity.this, ShowDetails.class);
            News tmp = (News)msg.obj;
            intent.putExtra("Headline", tmp.news_Title);
            String longString = tmp.news_Content;
            intent.putExtra("Details", longString);
            startActivity(intent);
        }
    }
}
