package com.baihui.yangxb.oknews.view;



import com.baihui.yangxb.oknews.entity.ToutiaoLoopnewsBean;
import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public interface ToutiaonewsView {

    void showProgress();

    void addNews(List<ToutiaonewsBean> newsList);

    void addLoopNews(List<ToutiaoLoopnewsBean> newsList);

    void hideProgress();

    void showLoadFailMsg();
}
