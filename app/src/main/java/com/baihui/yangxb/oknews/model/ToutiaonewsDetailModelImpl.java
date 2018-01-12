package com.baihui.yangxb.oknews.model;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

            private StringBuffer timeAndFrom;
            private String title;
            private List<String> textAndImg;
            private Boolean firstImg = true;

            @Override
            public void run() {
                detailnews = new DetailNews();
                textAndImg = new ArrayList<String>();
                timeAndFrom = new StringBuffer();
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
                    doc.getAllElements();
                    // 返回所有的Element
                    Elements eles = doc.getAllElements();
                    // 遍历所有的文档
                    for(Element ele : eles){
                        if (ele.tagName() == "title"){
                            title = ele.text();
                        }
                        if (ele.tagName() == "em"){
                            timeAndFrom.append(ele.text()+"    ");
                        }
                        if (ele.tagName() == "p"){
                            if (ele.text() == ""){
                                continue;
                            }
                            textAndImg.add(ele.text());
                            ele.remove();
                        }
                        if (ele.tagName() == "img"){
                            textAndImg.add(ele.attr("abs:data-src"));
                        }
                        if (ele.tagName() == "img"){
                            if (firstImg && ele.attr("abs:src").startsWith("http")){
                                firstImg = false;
                                continue;
                            }
                            textAndImg.add(ele.attr("abs:src"));
                        }
                    }
                }else {
                    doc.getAllElements();
                    // 返回所有的Element
                    Elements eles = doc.getAllElements();
                    // 遍历所有的文档
                    for(Element ele : eles){
                        if (ele.tagName() == "title"){
                            title = ele.text();
                        }
                        if (ele.tagName() == "span"){
                            timeAndFrom.append(ele.text()+"    ");
                        }
                        if (ele.tagName() == "p"){
                            if (ele.text() == ""){
                                continue;
                            }
                            textAndImg.add(ele.text());
                            ele.remove();
                        }
                        if (ele.tagName() == "figure"){
                            textAndImg.add(ele.child(0).attr("abs:data-href"));
                        }
                    }
                }
                detailnews.setNewsTitle(title);
                detailnews.setNewsTime(timeAndFrom.toString());
                detailnews.setNewsContentAndImg(textAndImg);
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
