package com.baihui.yangxb.oknews.entity;

import java.util.List;

/**
 * Created by yangxb on 18-1-5.
 */

public class DetailNews {
    private String newsTitle;   //新闻标题
    private String newsAuthorImg;   //新闻作者图标
    private String newsTime;     //新闻时间
    private String newsComefrom;  //新闻来源
    private String newsContent;    //新闻内容
    private List<String> newsImages;    //新闻图片
    private List<String> newsContentAndImg;    //新闻内容+图片

    public DetailNews() {
    }

    public DetailNews(String newsTitle, String newsAuthorImg, String newsTime, String newsComefrom, String newsContent, List<String> newsImages, List<String> newsContentAndImg) {
        this.newsTitle = newsTitle;
        this.newsAuthorImg = newsAuthorImg;
        this.newsTime = newsTime;
        this.newsComefrom = newsComefrom;

        this.newsContent = newsContent;
        this.newsImages = newsImages;
        this.newsContentAndImg = newsContentAndImg;
    }

    public List<String> getNewsContentAndImg() {
        return newsContentAndImg;
    }

    public void setNewsContentAndImg(List<String> newsContentAndImg) {
        this.newsContentAndImg = newsContentAndImg;
    }

    public String getNewsAuthorImg() {
        return newsAuthorImg;
    }

    public void setNewsAuthorImg(String newsAuthorImg) {
        this.newsAuthorImg = newsAuthorImg;
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
