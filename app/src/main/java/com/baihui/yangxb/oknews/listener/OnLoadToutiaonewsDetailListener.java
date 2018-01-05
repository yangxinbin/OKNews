package com.baihui.yangxb.oknews.listener;

import com.baihui.yangxb.oknews.entity.DetailNews;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public interface OnLoadToutiaonewsDetailListener {

    void onSuccess(DetailNews detailnews);

    void onFailure(String msg, Exception e);
}
