package com.baihui.yangxb.oknews.presenter;

import android.content.Context;

import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.model.ToutiaonewsModel;
import com.baihui.yangxb.oknews.model.ToutiaonewsModelImpl;
import com.baihui.yangxb.oknews.view.ToutiaonewsDetailView;


/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class ToutiaonewsDetailPresenterImpl implements ToutiaonewsDetailPresenter, OnLoadToutiaonewsDetailListener {
    private ToutiaonewsDetailView toutiaonewsDetailView;
    private Context context;
    private ToutiaonewsModel toutiaonewsModel;

    public ToutiaonewsDetailPresenterImpl(Context mContent, ToutiaonewsDetailView toutiaonewsDetailView) {
        this.toutiaonewsDetailView = toutiaonewsDetailView;
        this.context = mContent;
        toutiaonewsModel = new ToutiaonewsModelImpl();
    }

    @Override
    public void loadNewsDetail(String url) {
        toutiaonewsDetailView.showProgress();
        toutiaonewsModel.loadNewsDetail(url, this);
    }

    @Override
    public void onSuccess(String newsurl) {
        if (newsurl != "") {
            toutiaonewsDetailView.showNewsDetialContent(newsurl);
        }
        toutiaonewsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        toutiaonewsDetailView.hideProgress();
    }
}
