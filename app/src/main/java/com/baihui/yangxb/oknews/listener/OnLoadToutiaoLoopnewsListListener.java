package com.baihui.yangxb.oknews.listener;

import com.baihui.yangxb.oknews.entity.ToutiaoLoopnewsBean;
import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;

import java.util.List;

/**
 * Created by yangxb on 17-11-29.
 */

public interface OnLoadToutiaoLoopnewsListListener {
    void onLoopNewSuccess(List<ToutiaoLoopnewsBean> list);

    void onLoopNewFailure(String msg, Exception e);
}
