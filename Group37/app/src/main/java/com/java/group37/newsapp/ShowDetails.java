package com.java.group37.newsapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static android.os.Environment.getExternalStorageDirectory;

import com.iflytek.cloud.*;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShowDetails extends AppCompatActivity {

    private SpeechSynthesizer mTts;
    private SynthesizerListener listener;
    private String rawJSONString;
    private int picNum = 0;
    private boolean isUsingLocalPics;
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private int isSpeaking = 0;
    private List<News> newsList = new ArrayList<News>();
    private news_adapter newsAdapter;

    private List<News> RecommendList = new ArrayList<News>();
    String searchKeywords;
    RecommendUrlThread recommendThread;
    OneRecommendThread oneRecommendThread;
    jsonAnalyserList analyser;
    private ACache mCache;
    jsonAnalyserOne recommendAnalyser;
    String newsIdRecommend;


    private int getPixelsFromDp(int size){
        DisplayMetrics metrics =new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;
    }

    //to recommend news
    public void modifyCache(News singleNews)
    {
        Log.e("modifyCache", Integer.toString(singleNews.Keywords.size()));
        String nowFileString = mCache.getAsString("FileOfWordsToRecommend");
        List<String> wordList = new ArrayList<String>();
        List<Double> wordScoreList = new ArrayList<Double>();
        if (nowFileString != null)
        {
            mCache.remove("FileOfWordsToRecommend");
            String[] wordListFile = nowFileString.split(" ");
            for (int i = 0; i < wordListFile.length; i++)
            {
                String currentWord = wordListFile[i];
                if (!currentWord.equals(""))
                {
                    String currentWordScoreString = mCache.getAsString(currentWord);
                    if (currentWordScoreString != null) {
                        mCache.remove(currentWord);
                        wordList.add(currentWord);
                        wordScoreList.add(Double.valueOf(currentWordScoreString) / 2.0);
                    }
                }
            }
        }
        for (int i = 0; i < singleNews.Keywords.size(); i++)
        {
            if (i >= 10) break;
            String currentWord = singleNews.Keywords.get(i);
            Double currentWordScore = singleNews.Keyword_score.get(i);
            if (!currentWord.equals(""))
            {
                int flag = 0;
                for (int j = 0; j < wordList.size(); j++)
                    if (currentWord.equals(wordList.get(j)))
                    {
                        wordScoreList.set(j, wordScoreList.get(j) + currentWordScore);
                        flag = 1;
                    }
                if (flag == 0)
                {
                    wordList.add(currentWord);
                    wordScoreList.add(currentWordScore);
                }
            }
        }
        for (int i = 0; i < wordList.size(); i++)
            for (int j = 0; j < i; j++)
                if (wordScoreList.get(j) < wordScoreList.get(j+1)) {
                    Collections.swap(wordList, j, j+1);
                    Collections.swap(wordScoreList, j, j+1);
                }
        String toCacheString = "";
        searchKeywords = "";
        if (wordList.size() > 0) {
            toCacheString = wordList.get(0);
            searchKeywords = wordList.get(0);
        }
        for (int i = 1; i < wordList.size(); i++){
            if (i >= 10)    break;
            if (i < 3)  searchKeywords += wordList.get(i);
            toCacheString += " " + wordList.get(i);
            mCache.put(wordList.get(i), Double.toString(wordScoreList.get(i)));
        }
        mCache.put("FileOfWordsToRecommend", toCacheString);
        //Log.e("ToCacheString", toCacheString);
        searchRecommend();
    }

    public void searchRecommend(){
        recommendThread=new RecommendUrlThread();
        recommendThread.start();
        try {
            recommendThread.join();
            RecommendList = new ArrayList<News>();
            for (int i = 0; i < analyser.newsList.size(); i++)
            {
                if (i >= 3) break;
                newsIdRecommend = analyser.newsList.get(i).news_ID;
                //Log.e("recommend"+i, newsIdRecommend);
                oneRecommendThread = new OneRecommendThread();
                oneRecommendThread.start();
                oneRecommendThread.join();
                RecommendList.add(recommendAnalyser.news);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getIntent().getStringExtra("Headline"));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        String details = getIntent().getStringExtra("Details");
        if (details.length()>50){
            details = details.substring(0,49)+"...";
        }
        oks.setText(details);
        if (picNum>0){
            final String[] pictureURLs = getIntent().getStringArrayExtra("PictureList");
            oks.setImageUrl(pictureURLs[0]);
        }
        // url仅在微信（包括好友和朋友圈）中使用
        jsonAnalyserOne oneAnalyser = new jsonAnalyserOne(getIntent().getStringExtra("rawJSONstring"));
        News singleNews = oneAnalyser.news;
        oks.setUrl(singleNews.news_URL);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("你的评论");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(singleNews.news_URL);

        // 启动分享GUI
        oks.show(this);
    }

    public void fixListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        int totalHeight = 0;
        if (listAdapter == null) {return;}
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listViewItem = listAdapter.getView(i , null, listView);
            listViewItem.measure(0, 0);
            totalHeight += listViewItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCache = ACache.get(MainActivity.mactivity);
        rawJSONString = getIntent().getStringExtra("rawJSONstring");
        isUsingLocalPics = getIntent().getBooleanExtra("isUsingLocalPictures",false);

        //设置TTS
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=59b6a41a");
        mTts= SpeechSynthesizer.createSynthesizer(this, null);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");
        mTts.setParameter(SpeechConstant.SPEED, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "80");
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);

        listener = new SynthesizerListener() {

            @Override
            public void onSpeakResumed() {

            }

            @Override
            public void onSpeakProgress(int arg0, int arg1, int arg2) {

            }

            @Override
            public void onSpeakPaused() {

            }

            @Override
            public void onSpeakBegin() {

            }

            @Override
            public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {

            }

            @Override
            public void onCompleted(SpeechError arg0) {
                isSpeaking = 0;
            }

            @Override
            public void onBufferProgress(int arg0, int arg1, int arg2, String arg3) {

            }
        };

        //设置标题栏
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

        //设置图片栏
        final ImageView headerPicture = (ImageView) findViewById(R.id.HeaderPicture);
        HorizontalScrollView pictureScroll = (HorizontalScrollView) findViewById(R.id.PictureScroll);
        LinearLayout ll = (LinearLayout) findViewById(R.id.Pictures);
        final String[] pictureURLs = getIntent().getStringArrayExtra("PictureList");
        if ((pictureURLs.length > 1) || !(pictureURLs[0].equals(""))) {
            picNum = pictureURLs.length;
            for (int i=0;i<pictureURLs.length;i++){
                String url = pictureURLs[i];
                if (i==0){
                    if (isUsingLocalPics){
                        File f = new File(url);
                        Glide.with(this).load(f).placeholder(R.drawable.loading).error(R.drawable.not_found).dontAnimate().into(headerPicture);
                    }
                    else{
                        Glide.with(this).load(url).placeholder(R.drawable.loading).error(R.drawable.not_found).crossFade().into(headerPicture);
                    }
                }
                ImageView iv = new ImageView(this);
                iv.setLayoutParams(new Toolbar.LayoutParams(getPixelsFromDp(150),getPixelsFromDp(100)));
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                iv.setTag(R.id.position_tag,i);
                if (isUsingLocalPics){
                    File f = new File(url);
                    Glide.with(this)
                            .load(f)
                            .crossFade()
                            .into(iv);
                }
                else {
                    Glide.with(this)
                            .load(url)
                            .crossFade()
                            .into(iv);
                }

                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ShowDetails.this,SeeOriginalPicture.class);
                        intent.putExtra("URL",pictureURLs[Integer.parseInt(view.getTag(R.id.position_tag).toString())]);
                        intent.putExtra("isUsingLocalPictures",isUsingLocalPics);
                        startActivity(intent);
                    }
                });

                ll.addView(iv);
            }
        } else {
            headerPicture.setVisibility(View.GONE);
            pictureScroll.setVisibility(View.GONE);
        }

        //设置标题与正文
        Intent intent = getIntent();
        String headlineStr = intent.getStringExtra("Headline");
        String detailsStr = intent.getStringExtra("Details");
        TextView headline = (TextView) findViewById(R.id.NewsHeadline);
        TextView details = (TextView) findViewById(R.id.NewsDetail);
        headline.setText(headlineStr);
        details.setText(detailsStr);

        //设置推荐栏
        jsonAnalyserOne oa;
        String rcjson0 = getIntent().getStringExtra("RecommendRawJsonString0");
        if (rcjson0 != null){
            oa = new jsonAnalyserOne(rcjson0);
            News rc0 = oa.news;
            newsList.add(rc0);
        }
        String rcjson1 = getIntent().getStringExtra("RecommendRawJsonString1");
        if (rcjson1 != null){
            oa = new jsonAnalyserOne(rcjson1);
            News rc1 = oa.news;
            newsList.add(rc1);
        }
        String rcjson2 = getIntent().getStringExtra("RecommendRawJsonString2");
        if (rcjson2 != null){
            oa = new jsonAnalyserOne(rcjson2);
            News rc2 = oa.news;
            newsList.add(rc2);
        }

        if (newsList.size() == 0){
            TextView rh = (TextView) findViewById(R.id.RecommendHeader);
            rh.setVisibility(View.GONE);
        }
        ListView list = (ListView) findViewById (R.id.RecommendList);
        newsAdapter = new news_adapter(this,newsList);
        Log.e("rc",String.valueOf(newsAdapter.getCount()));
        list.setAdapter(newsAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News singleNews = newsList.get(i);
                modifyCache(singleNews);
                Intent intent = new Intent(ShowDetails.this, ShowDetails.class);
                intent.putExtra("Headline", singleNews.news_Title);
                String longString = singleNews.news_Content.replaceAll("　", "\n");
                intent.putExtra("Details", longString);
                String[] tmpList;
                if (singleNews.isUsingLocalPictures == true)
                    tmpList = singleNews.LocalPictures.split("[ ;]");
                else
                    tmpList = singleNews.news_Pictures.split("[ ;]");
                if(tmpList.length == 0) {
                    tmpList = new String[1];
                    tmpList[0] = "";
                }
                intent.putExtra("PictureList", tmpList);
                intent.putExtra("rawJSONstring",singleNews.original_String);
                intent.putExtra("isUsingLocalPictures",false);
                if (RecommendList.size() > 0)
                    intent.putExtra("RecommendRawJsonString0", RecommendList.get(0).original_String);
                if (RecommendList.size() > 1)
                    intent.putExtra("RecommendRawJsonString1", RecommendList.get(1).original_String);
                if (RecommendList.size() > 2)
                    intent.putExtra("RecommendRawJsonString2", RecommendList.get(2).original_String);
                startActivity(intent);
            }
        });
        fixListViewHeight(list);
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
    protected void onDestroy() {
        super.onDestroy();
        mTts.stopSpeaking();
        isSpeaking = 0;
        mTts.destroy();
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
                    for (int i=0;i<picNum;i++){
                        deleteFile("pic"+String.valueOf(i)+"_"+fileName+".png");
                    }
                    item.setIcon(R.drawable.ic_empty_star);
                } else {
                    fileOutputStream = openFileOutput(fileName,
                            Context.MODE_PRIVATE);
                    bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(fileOutputStream));
                    bufferedWriter.write(rawJSONString + "\n");
                    bufferedWriter.write(String.valueOf(picNum) + "\n");
                    bufferedWriter.write(headline.getText().toString() + "\n");
                    String s = getIntent().getStringExtra("Details");
                    bufferedWriter.write(s);
                    bufferedWriter.close();
                    for (int i=0;i<picNum;i++){
                        fileOutputStream = openFileOutput("pic"+String.valueOf(i)+"_"+fileName+".png",
                                Context.MODE_PRIVATE);
                        LinearLayout ll = (LinearLayout) findViewById(R.id.Pictures);
                        ImageView iv = (ImageView) ll.getChildAt(i);
                        iv.setDrawingCacheEnabled(true);
                        Bitmap bm = Bitmap.createBitmap(iv.getDrawingCache());
                        iv.setDrawingCacheEnabled(false);
                        bm.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                    item.setIcon(R.drawable.ic_full_star);
                    Toast.makeText(this,"收藏成功",Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return super.onOptionsItemSelected(item);
        } else if (id == R.id.action_read){
            if (isSpeaking == 0)
            {
                TextView detail = (TextView) findViewById(R.id.NewsDetail);
                mTts.startSpeaking(detail.getText().toString(),listener);
                item.setIcon(R.drawable.ic_pause);
                isSpeaking = 1;
            }
            else if (isSpeaking == 1) {
                mTts.pauseSpeaking();
                item.setIcon(R.drawable.ic_play_arrow);
                isSpeaking = 2;
            } else if (isSpeaking == 2){
                mTts.resumeSpeaking();
                item.setIcon(R.drawable.ic_pause);
                isSpeaking = 1;
            }
        } else if (id == R.id.action_share){
            showShare();
        }
        return true;
    }

    class RecommendUrlThread extends Thread {
        @Override
        public void run()
        {
            SearchNewsAccesser accesser = new SearchNewsAccesser(0, 0, searchKeywords);
            String jsonString = accesser.stringBuilder.toString();
            //Log.i("pageNo", Integer.toString(pageNo));
            //Log.i("newsType",Integer.toString(newsType));
            Log.i("json",jsonString);
            analyser = new jsonAnalyserList(jsonString);
        }
    }

    class OneRecommendThread extends Thread {
        @Override
        public void run()
        {
            OneNewsAccesser oneAccesser = new OneNewsAccesser(newsIdRecommend);
            String jsonOneString = oneAccesser.stringBuilder.toString();
            recommendAnalyser = new jsonAnalyserOne(jsonOneString);
        }
    }

}
