package com.baihui.yangxb.oknews.presenter;

import android.content.Context;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public interface ToutiaonewsPresenter {
    void loadNews(int type,Context context,boolean isRefresh);//刷新动作加载新闻数据
}
