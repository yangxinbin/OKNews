package com.baihui.yangxb.oknews.model;



import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsListListener;
import com.baihui.yangxb.oknews.utils.NewsJsonUtils;
import com.baihui.yangxb.tools.OkHttpUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29 0029.
 */

public class ToutiaonewsModelImpl implements ToutiaonewsModel {
    /**
     * 加载新闻列表
     *
     * @param url
     * @param listener
     */
    @Override
    public void loadNews(String url, final int type, final OnLoadToutiaonewsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback = new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                List<ToutiaonewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, "data");//data是json字段获得data的值即对象数组
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                listener.onFailure("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url, loadNewsCallback);
    }

    @Override
    public void loadNewsDetail(String url, OnLoadToutiaonewsDetailListener listener) {
        listener.onSuccess(url);//判断网络加载URL

    }


    /**
     * 获取ID
     * @param type
     * @return
     */
   /* private String getID(int type) {
        String id;
        switch (type) {
            case ToutiaonewsFragment.NEWS_TYPE_TOPNEWS:
                id = Urls.TOP_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_SOCIETY:
                id = Urls.SHEHUI_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_DOMESTIC:
                id = Urls.GUOMEI_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_INTERNATIONAL:
                id = Urls.GUOJI_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_ENTERTAINMENT:
                id = Urls.YULE_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_SPORTS:
                id = Urls.TIYU_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_MILITARY:
                id = Urls.JUNSHI_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_SCIENCE:
                id = Urls.KEJI_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_FINANCE:
                id = Urls.CAIJING_ID;
                break;
            case ToutiaonewsFragment.NEWS_TYPE_FASHION:
                id = Urls.SHISHANG_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }*/
}
