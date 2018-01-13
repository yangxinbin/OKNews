package com.baihui.yangxb.oknews.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenter;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenterImpl;
import com.baihui.yangxb.oknews.utils.SpeechUtils;
import com.baihui.yangxb.oknews.view.ToutiaonewsDetailView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

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
/*    @Bind(R.id.fab_share)
    FloatingActionButton fabShare;*/
    @Bind(R.id.fab_read)
    FloatingActionButton fabRead;
    @Bind(R.id.titile)
    TextView titile;
    @Bind(R.id.texttime)
    TextView texttime;
    @Bind(R.id.textfrom)
    TextView textfrom;
    @Bind(R.id.fab_menu)
    FloatingActionsMenu fabMenu;
    @Bind(R.id.imageViewauthor)
    CircleImageView imageViewauthor;
    @Bind(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.imageView3)
    ImageView imageView3;
    private String newsurl, newsimg;
    private ToutiaonewsDetailPresenter toutiaonewsDetailPresenter;
    private Boolean isWechar;
    private StringBuffer videoText;
    private SpeechUtils speechUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihunews_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        videoText = new StringBuffer();
        Log.v("yxb","=======on======");
        speechUtils = SpeechUtils.getInstance(ToutiaonewsDetailActivity.this);
        newsurl = (String) getIntent().getSerializableExtra("newsurl");
        isWechar = getIntent().getBooleanExtra("iswechar", false);
        toutiaonewsDetailPresenter = new ToutiaonewsDetailPresenterImpl(getApplication(), this);
        toutiaonewsDetailPresenter.loadNewsDetail(this, isWechar, newsurl);//yxb
    }
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 收到消息后执行handler
            if (speechUtils != null){
                Log.v("yxb","=======on======"+videoText);
                speechUtils.speakText(videoText.toString());
            }
        }
    };

    private void initView() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        newsimg = (String) getIntent().getSerializableExtra("newsimg");
        Glide.with(ToutiaonewsDetailActivity.this)
                .load(newsimg)
                .placeholder(R.drawable.beginimg)//图片加载出来前，显示的图片
                .into(ivWebImg);
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
        Log.v("yxb","=======onDestroy======");
        speechUtils.reMOVETTL();
        ButterKnife.unbind(this);
    }
    @Override
    public void showNewsDetialContent(DetailNews detailnews) {
        if (detailnews == null) {
            return;
        }
        if (detailnews.getNewsTitle() != null) {
            titile.setText(detailnews.getNewsTitle());
        }
        if (detailnews.getNewsComefrom() != null) {
            textfrom.setText(detailnews.getNewsComefrom());
        }
        if (detailnews.getNewsTime() != null) {
            texttime.setText(detailnews.getNewsTime());
        }
        imageView3.setVisibility(View.VISIBLE);//显示分割线
        Resources resources = getResources();
        int i;
        for (i = 0; i < detailnews.getNewsContentAndImg().size(); i++) {
            if (detailnews.getNewsContentAndImg().get(i).startsWith("http") || detailnews.getNewsContentAndImg().get(i).endsWith("jpeg") || detailnews.getNewsContentAndImg().get(i).endsWith("jpg") || detailnews.getNewsContentAndImg().get(i).endsWith("png")) {
                ImageView contentImg = new ImageView(this);
                Glide.with(ToutiaonewsDetailActivity.this)
                        .load(detailnews.getNewsContentAndImg().get(i))
                        .placeholder(R.drawable.beginimg)//图片加载出来前，显示的图片
                        .error(R.drawable.imgerror)//图片加载失败后，显示的图片
                        .into(contentImg);
                content.addView(contentImg);
                TextView textN = new TextView(this);
                textN.setText("\n");
                content.addView(textN);
                continue;
            }
            if (detailnews.getNewsContentAndImg().get(i).endsWith("gif")) {
                ImageView gif = new ImageView(this);
                Glide.with(ToutiaonewsDetailActivity.this)
                        .load(detailnews.getNewsContentAndImg().get(i))
                        .asGif()
                        .placeholder(R.drawable.beginimg)//图片加载出来前，显示的图片
                        .error(R.drawable.imgerror)//图片加载失败后，显示的图片
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(gif);
                content.addView(gif);
                TextView textN = new TextView(this);
                textN.setText("\n");
                content.addView(textN);
                continue;
            }
            TextView textContent = new TextView(this);
            textContent.setTextColor(resources.getColor(R.color.textblack, null));
            textContent.setTextSize(17);
            textContent.setText("        " + detailnews.getNewsContentAndImg().get(i) + "\n");
            videoText.append(detailnews.getNewsContentAndImg().get(i)+"    ");
            content.addView(textContent);
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

    @OnClick({/*R.id.fab_share,*/ R.id.fab_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            /*case R.id.fab_share:
                break;*/
            case R.id.fab_read:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (videoText != null){
                            Message message = new Message();
                            handler.sendMessage(message);
                        }
                    }
                }).start();
                break;
            default:
                break;
        }
    }
}
