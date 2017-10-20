package com.baihui.yangxb.oknews.model;

import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsListListener;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public interface ToutiaonewsModel {
    void loadNews(String url, int type, OnLoadToutiaonewsListListener listener);
    void loadNewsDetail(String url, OnLoadToutiaonewsDetailListener listener);
}
