package com.java.group37.newsapp;

import android.content.Context;
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
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ShowDetails extends AppCompatActivity {

    private SpeechSynthesizer mTts;
    private SynthesizerListener listener;
    private String rawJSONString;
    private int picNum = 0;
    private boolean isUsingLocalPics;
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    private int isSpeaking = 0;

    private int getPixelsFromDp(int size){

        DisplayMetrics metrics =new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        return(size * metrics.densityDpi) / DisplayMetrics.DENSITY_DEFAULT;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        /*
        //设置TTS
        tts = new TextToSpeech(this,new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int status) {
                if (status==TextToSpeech.SUCCESS) {
                    int supported = tts.setLanguage(Locale.CHINESE);
                    if ((supported!=TextToSpeech.LANG_AVAILABLE)&&(supported!=TextToSpeech.LANG_COUNTRY_AVAILABLE)) {
                        Toast.makeText(ShowDetails.this, "不支持当前语言！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        */


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
        }
        return true;
    }
}
