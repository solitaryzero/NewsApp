package com.java.group37.newsapp;

import org.json.*;
import java.util.*;

/**
 * Created by kmy on 2017/9/6.
 */

public class jsonAnalyserList{
    public List<News> newsList = new ArrayList<News>();
    jsonAnalyserList (String jsonString) {
        try {
            JSONObject list = new JSONObject(jsonString.toString());
            JSONArray infArray = list.getJSONArray("list");
            for (int i = 0; i < infArray.length(); i++) {
                JSONObject inf_Array = infArray.getJSONObject(i);
                News tmp = new News();
                tmp.newsClassTag = inf_Array.getString("newsClassTag");
                tmp.news_ID = inf_Array.getString("news_ID");
                tmp.news_Source = inf_Array.getString("news_Source");
                tmp.news_Title = inf_Array.getString("news_Title");
                tmp.news_Time = inf_Array.getString("news_Time");
                tmp.news_URL = inf_Array.getString("news_URL");
                tmp.news_Author = inf_Array.getString("news_Author");
                tmp.lang_Type = inf_Array.getString("lang_Type");
                tmp.news_Pictures = inf_Array.getString("news_Pictures");
                tmp.news_Video = inf_Array.getString("news_Video");
                tmp.news_Intro = inf_Array.getString("news_Intro");
                newsList.add(tmp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
