package com.baihui.yangxb.oknews.listener;

/**
 * Created by Administrator on 2016/12/7 0007.
 */

public interface OnLoadToutiaonewsDetailListener {

    void onSuccess(String newsurl);

    void onFailure(String msg, Exception e);
}
