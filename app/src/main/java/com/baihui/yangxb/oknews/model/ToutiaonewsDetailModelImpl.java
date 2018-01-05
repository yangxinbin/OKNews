package com.baihui.yangxb.oknews.model;



import android.content.Context;

import com.baihui.yangxb.oknews.cacher.ACache;
import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.entity.ToutiaoLoopnewsBean;
import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaoLoopnewsListListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsListListener;
import com.baihui.yangxb.oknews.utils.NewsJsonUtils;
import com.baihui.yangxb.tools.OkHttpUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29 0029.
 */

public class ToutiaonewsDetailModelImpl implements ToutiaonewsDetailModel {
    @Override
    public void loadNewsDetail(String url, OnLoadToutiaonewsDetailListener listener) {
        DetailNews detailnews = new DetailNews();
        listener.onSuccess(detailnews);//判断网络加载URL
    }
}
