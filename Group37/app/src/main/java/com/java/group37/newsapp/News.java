package com.java.group37.newsapp;

import java.util.*;

/**
 * Created by kmy on 2017/9/7.
 */

public class News {
    public String newsClassTag;              //新闻所属的分类
    public String news_ID;                   //新闻ID
    public String news_Source;               //来源
    public String news_Title;                //标题
    public String news_Time;                 //时间
    public String news_URL;                  //新闻URL
    public String news_Author;               //新闻的作者
    public String lang_Type;                 //新闻的语言类型
    public String news_Video;                //
    public String news_Intro;                //简介
    public String news_Category;             //新闻的类别
    public String inborn_KeyWords;           //新闻关键词
    public String news_Content;              //新闻正文
    public String crawl_Source;              //爬取来源
    public String news_Journal;              //记者列表
    public String crawl_Time;                //爬取时间
    public String news_Pictures;             //新闻的图片路径
    public String repeat_ID;                 //与该条新闻重复的新闻ID
    public String original_String;
    public boolean isUsingLocalPictures;
    public String LocalPictures;

    public String seggedTitle;               //新闻分词后标题结果
    public List<String> seggedPListOfContent;//新闻正文的分词结果
    public List<String> persons;             //人物列表
    public List<String> locations;           //地点列表
    public List<String> organizations;       //组织机构列表
    public List<String> Keywords;            //新闻中关键词列表
    public List<Double> Keyword_score;

    public int wordCountOfTitle;
    public int wordCountOfContent;

    News()
    {
        newsClassTag = "";
        news_ID = "";
        news_Source = "";
        news_Title = "";
        news_Time = "";
        news_URL = "";
        news_Author = "";
        lang_Type = "";
        news_Video = "";
        news_Intro = "";
        news_Category = "";
        inborn_KeyWords = "";
        news_Content = "";
        crawl_Source = "";
        news_Journal = "";
        crawl_Time = "";
        news_Pictures = "";
        repeat_ID = "";
        seggedTitle = "";
        original_String = "";
        isUsingLocalPictures = false;
        LocalPictures = "";

        seggedPListOfContent = new ArrayList<String>();
        persons = new ArrayList<String>();
        locations = new ArrayList<String>();
        organizations = new ArrayList<String>();
        Keywords = new ArrayList<String>();
        Keyword_score = new ArrayList<Double>();

        wordCountOfTitle = 0;
        wordCountOfContent = 0;
    }

    News(News newOne)
    {
        this.newsClassTag = newOne.newsClassTag;
        this.news_ID = newOne.news_ID;
        this.news_Source = newOne.news_Source;
        this.news_Title = newOne.news_Title;
        this.news_Time = newOne.news_Time;
        this.news_URL = newOne.news_URL;
        this.news_Author = newOne.news_Author;
        this.lang_Type = newOne.lang_Type;
        this.news_Video = newOne.news_Video ;
        this.news_Intro = newOne.news_Intro;
        this.news_Category = newOne.news_Category;
        this.inborn_KeyWords = newOne.inborn_KeyWords;
        this.news_Content = newOne.news_Content;
        this.crawl_Source = newOne.crawl_Source;
        this.news_Journal = newOne.news_Journal;
        this.crawl_Time = newOne.crawl_Time;
        this.news_Pictures = newOne.news_Pictures;
        this.repeat_ID = newOne.repeat_ID;
        this.seggedTitle = newOne.seggedTitle;
        this.original_String = newOne.original_String;
        this.isUsingLocalPictures = newOne.isUsingLocalPictures;
        this.LocalPictures = newOne.LocalPictures;

        this.seggedPListOfContent = newOne.seggedPListOfContent;
        this.persons = newOne.persons;
        this.locations = newOne.locations;
        this.organizations = newOne.organizations;
        this.Keywords = newOne.Keywords;

        this.wordCountOfTitle = newOne.wordCountOfTitle ;
        this.wordCountOfContent = newOne.wordCountOfContent;
    }

    News(String rawJSONString, String[] local_pictures)
    {
        jsonAnalyserOne oneAnalyser = new jsonAnalyserOne(rawJSONString);
        News newOne = oneAnalyser.news;
        this.newsClassTag = newOne.newsClassTag;
        this.news_ID = newOne.news_ID;
        this.news_Source = newOne.news_Source;
        this.news_Title = newOne.news_Title;
        this.news_Time = newOne.news_Time;
        this.news_URL = newOne.news_URL;
        this.news_Author = newOne.news_Author;
        this.lang_Type = newOne.lang_Type;
        this.news_Video = newOne.news_Video ;
        this.news_Intro = newOne.news_Intro;
        this.news_Category = newOne.news_Category;
        this.inborn_KeyWords = newOne.inborn_KeyWords;
        this.news_Content = newOne.news_Content;
        this.crawl_Source = newOne.crawl_Source;
        this.news_Journal = newOne.news_Journal;
        this.crawl_Time = newOne.crawl_Time;
        this.news_Pictures = newOne.news_Pictures;
        this.repeat_ID = newOne.repeat_ID;
        this.seggedTitle = newOne.seggedTitle;
        this.original_String = newOne.original_String;
        this.isUsingLocalPictures = true;

        if (local_pictures.length > 0)
            this.LocalPictures = local_pictures[0];
        else
            this.LocalPictures = "";
        for (int i = 1; i < local_pictures.length; i++) {
            this.LocalPictures += " " + local_pictures[i];
        }

        this.seggedPListOfContent = newOne.seggedPListOfContent;
        this.persons = newOne.persons;
        this.locations = newOne.locations;
        this.organizations = newOne.organizations;
        this.Keywords = newOne.Keywords;

        this.wordCountOfTitle = newOne.wordCountOfTitle ;
        this.wordCountOfContent = newOne.wordCountOfContent;
    }
}
