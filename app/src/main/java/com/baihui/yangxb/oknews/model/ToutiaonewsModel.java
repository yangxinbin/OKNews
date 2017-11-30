package com.baihui.yangxb.oknews.model;

import android.content.Context;

import com.baihui.yangxb.oknews.listener.OnLoadToutiaoLoopnewsListListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsListListener;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public interface ToutiaonewsModel {
    void loadNews(Boolean isRefresh,Context context,String url, int type, OnLoadToutiaonewsListListener listener);//isRefresh 刷新重新写入缓存
    void loadLoopNews(Boolean isRefresh,Context context,String url, int type, OnLoadToutiaoLoopnewsListListener listener);//isRefresh 刷新重新写入缓存
    void loadNewsDetail(String url, OnLoadToutiaonewsDetailListener listener);
}
