package com.baihui.yangxb.oknews.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenter;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenterImpl;
import com.baihui.yangxb.oknews.view.ToutiaonewsDetailView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class ToutiaonewsDetailActivity extends SwipeBackActivity implements ToutiaonewsDetailView {
    @Bind(R.id.iv_web_img)
    ImageView ivWebImg;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing)
    CollapsingToolbarLayout collapsing;
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.progress)
    ProgressBar progress;
    private SwipeBackLayout mSwipeBackLayout;
    private String newsurl,newsimg;
    private ToutiaonewsDetailPresenter toutiaonewsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihunews_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("新闻详情");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        mSwipeBackLayout = getSwipeBackLayout();
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);//滑动返回方向
        newsurl = (String) getIntent().getSerializableExtra("newsurl");
        newsimg = (String) getIntent().getSerializableExtra("newsimg");
        Picasso.with(this).load(newsimg).into(ivWebImg);//图片就直接加载了
        toutiaonewsDetailPresenter = new ToutiaonewsDetailPresenterImpl(getApplication(), this);
        toutiaonewsDetailPresenter.loadNewsDetail(newsurl);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void showNewsDetialContent(String url) {
        webView.loadUrl(newsurl);

    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.GONE);

    }

}
