package com.java.group37.newsapp;

import org.json.*;
import java.util.*;

/**
 * Created by kmy on 2017/9/7.
 */

public class jsonAnalyserOne {
    News news = new News();
    jsonAnalyserOne (String jsonString) {
        try {
            JSONObject list = new JSONObject(jsonString.toString());
            news.original_String = jsonString;
            news.newsClassTag = list.getString("newsClassTag");
            news.news_ID = list.getString("news_ID");
            news.news_Source = list.getString("news_Source");
            news.news_Title = list.getString("news_Title");
            news.news_Time = list.getString("news_Time");
            news.news_URL = list.getString("news_URL");
            news.news_Author = list.getString("news_Author");
            news.lang_Type = list.getString("lang_Type");
            news.news_Pictures = list.getString("news_Pictures");
            news.news_Video = list.getString("news_Video");
            news.inborn_KeyWords = list.getString("inborn_KeyWords");
            news.news_Content = list.getString("news_Content");
            news.crawl_Source = list.getString("crawl_Source");
            news.news_Journal = list.getString("news_Journal");
            news.crawl_Time = list.getString("crawl_Time");
            news.repeat_ID = list.getString("repeat_ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
