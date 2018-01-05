package com.baihui.yangxb.oknews.view;

import com.baihui.yangxb.oknews.entity.DetailNews;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public interface ToutiaonewsDetailView {

    void showNewsDetialContent(DetailNews detailnews);

    void showProgress();

    void hideProgress();
}
