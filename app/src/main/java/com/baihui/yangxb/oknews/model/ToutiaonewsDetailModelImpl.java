package com.baihui.yangxb.oknews.model;



import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baihui.yangxb.oknews.cacher.ACache;
import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.entity.ToutiaoLoopnewsBean;
import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaoLoopnewsListListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsListListener;
import com.baihui.yangxb.oknews.utils.NewsJsonUtils;
import com.baihui.yangxb.tools.OkHttpUtils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/11/29 0029.
 */

public class ToutiaonewsDetailModelImpl implements ToutiaonewsDetailModel {
    // 代理;
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36";
    private DetailNews detailnews;
    @Override
    public void loadNewsDetail(final Boolean isWechar, final String url, final OnLoadToutiaonewsDetailListener listener) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 收到消息后执行handler
                listener.onSuccess(detailnews);
            }
        };
        final Runnable runnable = new Runnable() {

            private StringBuffer contents;
            private Elements elementsFrom;
            private Elements elementsTime;
            private Elements elementsAuthorImg;
            private Elements elementsTitle;
            private Elements elementsPages;

            @Override
            public void run() {
                detailnews = new DetailNews();
                Connection conn = Jsoup.connect(url);
                // 修改http包中的header,伪装成浏览器进行抓取
                conn.header("User-Agent", userAgent);
                Document doc = null;
                try {
                    doc = conn.get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 获取页数的链接
                if (isWechar){
                    //elementsPages = doc.getElementsByClass("J-article article");
                    elementsTitle = doc.getElementsByClass("rich_media_title");
                    //elementsAuthorImg = doc.getElementsByClass("dfh-img");
                    elementsTime = doc.getElementsByClass("rich_media_meta rich_media_meta_text");
                    elementsFrom = doc.getElementsByClass("rich_media_meta rich_media_meta_link rich_media_meta_nickname");
                    Log.v("yxb",url+"==============="+ elementsTime + elementsAuthorImg);
                    contents = new StringBuffer();
                    Element elementcontent = doc.getElementById("js_content");
                    Elements allContents = elementcontent.getElementsByTag("span");
                    for (Element all : allContents) {
                        contents.append("        "+all.text());
                        contents.append("\n\n");
                    }
                }else {
                    //elementsPages = doc.getElementsByClass("J-article article");
                    elementsTitle = doc.getElementsByClass("title");
                    elementsAuthorImg = doc.getElementsByClass("dfh-img");
                    elementsTime = doc.getElementsByClass("dfh-information");
                    elementsFrom = doc.getElementsByClass("dfh-name");
                    Log.v("yxb",url+"==============="+ elementsTime + elementsAuthorImg);
                    contents = new StringBuffer();
                    Element elementcontent = doc.getElementById("content");
                    Elements allContents = elementcontent.getElementsByTag("p");
                    for (Element all : allContents) {
                        contents.append("        "+all.text());
                        contents.append("\n\n");
                    }
                    detailnews.setNewsAuthorImg(elementsAuthorImg.text());
                }
                detailnews.setNewsTitle(elementsTitle.text());
                detailnews.setNewsComefrom(elementsFrom.text());
                detailnews.setNewsTime(elementsTime.text());
                detailnews.setNewsContent(contents.toString());
                Message message = new Message();
                handler.sendMessage(message);
            }
        };
        new Thread(runnable).start();  // 子线程
    }
}
