package com.java.group37.newsapp;

/**
 * Created by lenovo on 2017/9/6.
 */

public class NewsItem {
    private String id;//id
    private String title;//标题
    private String link;//文章链接
    private String date;//日期
    private String imgLink;//图片链接
    private String content;//内容
    private int newsType;//类型

    public NewsItem(String id, String title, String link, String date, String imgLink, String content, int newsType)
    {
        this.id = id;
        this.title = title;
        this.link = link;
        this.date = date;
        this.imgLink = imgLink;
        this. content = content;
        this.newsType = newsType;
    }


    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImgLink() {
        return imgLink;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString()
    {
        return "NewsItem [id=" + id + ", title=" + title + ", link=" + link + ", date=" + date + ", imgLink=" + imgLink
                + ", content=" + content + ", newsType=" + newsType + "]";
    }
}
