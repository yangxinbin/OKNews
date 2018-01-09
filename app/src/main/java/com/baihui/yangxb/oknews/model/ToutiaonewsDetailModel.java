package com.baihui.yangxb.oknews.model;

import android.content.Context;

import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaoLoopnewsListListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsListListener;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public interface ToutiaonewsDetailModel {
    void loadNewsDetail(Boolean isWechar,String url, OnLoadToutiaonewsDetailListener listener);
}
