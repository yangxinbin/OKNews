package com.baihui.yangxb.oknews.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenter;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenterImpl;
import com.baihui.yangxb.oknews.view.ToutiaonewsDetailView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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
    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @Bind(R.id.fab_share)
    FloatingActionButton fabShare;
    @Bind(R.id.fab_read)
    FloatingActionButton fabRead;
    @Bind(R.id.titile)
    TextView titile;
    @Bind(R.id.texttime)
    TextView texttime;
    @Bind(R.id.textfrom)
    TextView textfrom;
    @Bind(R.id.textcontent)
    TextView textcontent;
    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;
    @Bind(R.id.imageViewauthor)
    CircleImageView imageViewauthor;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    private String newsurl, newsimg;
    private ToutiaonewsDetailPresenter toutiaonewsDetailPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihunews_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        newsurl = (String) getIntent().getSerializableExtra("newsurl");
        toutiaonewsDetailPresenter = new ToutiaonewsDetailPresenterImpl(getApplication(), this);
        toutiaonewsDetailPresenter.loadNewsDetail(newsurl);
    }

    private void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        newsimg = (String) getIntent().getSerializableExtra("newsimg");
        Picasso.with(this).load(newsimg).into(ivWebImg);//图片就直接加载了
        collapsing.setTitle("");
        collapsing.setCollapsedTitleTextColor(getResources().getColor(R.color.white));
        collapsing.setExpandedTitleColor(getResources().getColor(R.color.white));
        collapsing.setExpandedTitleColor(Color.TRANSPARENT);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                    collapsing.setTitle("新闻详情");
                } else {
                    collapsing.setTitle("");
                }
            }
        });
/*        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接入分享接口
            }
        });*/

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void showNewsDetialContent(DetailNews detailnews) {
        titile.setText(detailnews.getNewsTitle());
        if (detailnews.getNewsAuthorImg() != null){
      //      Picasso.with(this).load(detailnews.getNewsAuthorImg()).into(imageViewauthor);
        }
        textfrom.setText(detailnews.getNewsComefrom());
        texttime.setText(detailnews.getNewsTime());
        textcontent.setText(detailnews.getNewsContent());
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

    @OnClick({R.id.fab_share, R.id.fab_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_share:
                break;
            case R.id.fab_read:
                break;
        }
    }
}
