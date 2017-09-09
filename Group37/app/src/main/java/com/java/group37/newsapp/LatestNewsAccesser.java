package com.java.group37.newsapp;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kmy on 2017/9/7.
 */

public class LatestNewsAccesser{
    public static final String HTTP_GET = "GET";
    public static final String CHARSET_UTF_8 = "utf-8";
    public static final String CONTENT_TYPE_TEXT_HTML = "text/xml";
    public static final String CONTENT_TYPE_FORM_URL = "application/x-www-form-urlencoded";
    public static final int SEND_REQUEST_TIME_OUT = 50000;
    public static final int READ_TIME_OUT = 50000;
    public StringBuilder stringBuilder;
    LatestNewsAccesser (int newsNum, int category){
        stringBuilder = new StringBuilder();
        try {
            int pageNo = (newsNum + 20) / 20;
            String UrlString = "http://166.111.68.66:2042/news/action/query/latest?pageNo="+pageNo;
            if (category != 0)
                UrlString += "&category="+category;
            URL url = new URL(UrlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(SEND_REQUEST_TIME_OUT);
            conn.setReadTimeout(READ_TIME_OUT);
            conn.setUseCaches(false);
            conn.setRequestProperty("Accept-Charset", CHARSET_UTF_8);
            conn.setRequestProperty("Content-Type", CONTENT_TYPE_FORM_URL);
            conn.setRequestMethod(HTTP_GET);
            InputStream is = conn.getInputStream();
            InputStreamReader streamReader = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
