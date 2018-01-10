package com.baihui.yangxb.oknews.model;



import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
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
import java.util.ArrayList;
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
    public void loadNewsDetail(Context context,final Boolean isWechar, final String url, final OnLoadToutiaonewsDetailListener listener) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                // 收到消息后执行handler
                listener.onSuccess(detailnews);
            }
        };
        final Runnable runnable = new Runnable() {

            private String timeAndFrom;
            private String title;
            private StringBuffer contents;
            private Elements elementsFrom;
            private Elements elementsTime;
            private Elements elementsAuthorImg;
            private Elements elementsTitle;
            private Elements elementsPages;
            private List<String> imagesList;

            @Override
            public void run() {
                detailnews = new DetailNews();
                imagesList = new ArrayList<String>();
                contents = new StringBuffer();
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
                    elementsTitle = doc.getElementsByClass("rich_media_title");
                    //elementsAuthorImg = doc.getElementsByClass("dfh-img");
                    elementsTime = doc.getElementsByClass("rich_media_meta rich_media_meta_text");
                    elementsFrom = doc.getElementsByClass("rich_media_meta rich_media_meta_link rich_media_meta_nickname");
                    Element elementcontent = doc.getElementById("js_content");
                    Elements allContents = elementcontent.getElementsByTag("p");
                    for (Element all : allContents) {
                        if (all.text() == ""){
                            continue;
                        }
                        contents.append("        "+all.text()+"\n\n");
                        all.remove();
                    }
                    title = elementsTitle.text();
                    timeAndFrom = elementsTime.text();
                    detailnews.setNewsComefrom(elementsFrom.text());
/*                    doc.getAllElements();
                    // 返回所有的Element
                    Elements eles = doc.getElementsByTag("body");
                    Log.v("yxb","=====all.text()====="+eles);
                    // 遍历所有的文档
                    for(Element ele : eles){
                        if (ele.tagName() == "p"){
                            if (ele.text() == ""){
                                continue;
                            }
                            contents.append("        "+ele.text()+"\n\n");
                            ele.remove();
                        }
                        if (ele.tagName() == "title"){
                            title = ele.text();
                        }
                        if (ele.tagName() == "span"){
                            timeAndFrom = ele.text();
                        }
                        String tagName = ele.tagName();
                        //Log.v("yxb","=====tagName====="+tagName);
                    }*/
                }else {
/*                    elementsTitle = doc.getElementsByClass("title");
                    elementsTime = doc.getElementsByClass("src");
                    elementsFrom = doc.getElementsByClass("dfh-name");
                    Log.v("yxb",url+"==============="+ elementsTime + elementsAuthorImg);
                    contents = new StringBuffer();
                    Element elementcontent = doc.getElementById("content");
                    Elements allContents = elementcontent.getElementsByTag("p");
                    for (Element all : allContents) {
                        if (all.text() == ""){
                            continue;
                        }
                        contents.append("        "+all.text());
                        contents.append("\n\n");
                        all.remove();
                    }
                    Elements jpgs=doc.select("img[src]"); //　查找扩展名是jpg的图片
                    Log.v("yxb",url+"=======jpgs========"+jpgs);
                    for(int i=0;i<jpgs.size();i++){
                        Element jpg=jpgs.get(i);
                        Log.v("yxb",url+"=======imagesList========"+ jpg.toString());
                        imagesList.add(jpg.toString());
                        detailnews.setNewsImages(imagesList);
                        if(i==5){
                            break;
                        }
                    }*/
                    doc.getAllElements();
                    // 返回所有的Element
                    Elements eles = doc.getAllElements();
                    Log.v("yxb","=====all.text()====="+eles);
                    // 遍历所有的文档
                    for(Element ele : eles){
                        if (ele.tagName() == "p"){
                                Log.v("yxb","=====all.text()====="+ele);
                                contents.append("        "+ele.text()+"\n\n");
                            ele.remove();
                        }
                        if (ele.tagName() == "title"){
                            title = ele.text();
                        }
                        if (ele.tagName() == "span"){
                            timeAndFrom = ele.text();
                        }
                        String tagName = ele.tagName();
                        Log.v("yxb","=====tagName====="+tagName);
                    }
                }
                detailnews.setNewsTitle(title);
                //detailnews.setNewsComefrom(timeAndFrom);
                detailnews.setNewsTime(timeAndFrom);
                detailnews.setNewsContent(contents.toString());
                Message message = new Message();
                handler.sendMessage(message);
            }
        };
        if (isNetworkAvailable(context)){
            new Thread(runnable).start();  // 子线程
        }
    }
    // 判断是否有可用的网络连接
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        else {   // 获取所有NetworkInfo对象
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Network[] networks = cm.getAllNetworks();
                NetworkInfo networkInfo;
                for (Network mNetwork : networks) {
                    networkInfo = cm.getNetworkInfo(mNetwork);
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        return true;
                    }
                }
            }
            else {
                //否则调用旧版本方法
                    NetworkInfo[] info = cm.getAllNetworkInfo();
                    if (info != null) {
                        for (NetworkInfo anInfo : info) {
                            if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                                return true;
                            }
                        }
                    }
            }
        }
        return false;
    }
}
