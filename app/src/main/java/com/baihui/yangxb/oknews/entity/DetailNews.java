package com.baihui.yangxb.oknews.entity;

import java.util.List;

/**
 * Created by yangxb on 18-1-5.
 */

public class DetailNews {
    private String newsTitle;   //新闻标题
    private String newsTime;     //新闻时间
    private String newsComefrom;  //新闻来源
    private String newsContent;    //新闻内容
    private List<String> newsImages;    //新闻图片

    public DetailNews() {
    }

    public DetailNews(String newsTitle, String newsTime, String newsComefrom, String newsContent, List<String> newsImages) {
        this.newsTitle = newsTitle;
        this.newsTime = newsTime;
        this.newsComefrom = newsComefrom;
        this.newsContent = newsContent;
        this.newsImages = newsImages;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(String newsTime) {
        this.newsTime = newsTime;
    }

    public String getNewsComefrom() {
        return newsComefrom;
    }

    public void setNewsComefrom(String newsComefrom) {
        this.newsComefrom = newsComefrom;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }

    public List<String> getNewsImages() {
        return newsImages;
    }

    public void setNewsImages(List<String> newsImages) {
        this.newsImages = newsImages;
    }
}
