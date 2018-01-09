package com.baihui.yangxb.oknews.presenter;

import android.content.Context;

import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.listener.OnLoadToutiaonewsDetailListener;
import com.baihui.yangxb.oknews.model.ToutiaonewsDetailModel;
import com.baihui.yangxb.oknews.model.ToutiaonewsDetailModelImpl;
import com.baihui.yangxb.oknews.model.ToutiaonewsModel;
import com.baihui.yangxb.oknews.model.ToutiaonewsModelImpl;
import com.baihui.yangxb.oknews.view.ToutiaonewsDetailView;


/**
 * Created by Administrator on 2016/12/7 0007.
 */

public class ToutiaonewsDetailPresenterImpl implements ToutiaonewsDetailPresenter, OnLoadToutiaonewsDetailListener {
    private ToutiaonewsDetailView toutiaonewsDetailView;
    private Context context;
    private ToutiaonewsDetailModel toutiaonewsModel;


    public ToutiaonewsDetailPresenterImpl(Context mContent, ToutiaonewsDetailView toutiaonewsDetailView) {
        this.toutiaonewsDetailView = toutiaonewsDetailView;
        this.context = mContent;
        toutiaonewsModel = new ToutiaonewsDetailModelImpl();
    }

    @Override
    public void loadNewsDetail(Boolean isWechar,String url) {
        toutiaonewsDetailView.showProgress();
        toutiaonewsModel.loadNewsDetail(isWechar,url, this);
    }

    @Override
    public void onSuccess(DetailNews detailnews) {
        if (detailnews != null) {
            toutiaonewsDetailView.showNewsDetialContent(detailnews);
        }
        toutiaonewsDetailView.hideProgress();
    }

    @Override
    public void onFailure(String msg, Exception e) {
        toutiaonewsDetailView.hideProgress();
    }
}
