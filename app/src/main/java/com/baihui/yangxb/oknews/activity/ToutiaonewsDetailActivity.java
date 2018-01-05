package com.baihui.yangxb.oknews.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.entity.DetailNews;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenter;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsDetailPresenterImpl;
import com.baihui.yangxb.oknews.view.ToutiaonewsDetailView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//    @Bind(R.id.web_view)
//    WebView webView;
    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @Bind(R.id.floatingActionButton)
    FloatingActionButton floatingActionButton;
    private String newsurl, newsimg;
    private ToutiaonewsDetailPresenter toutiaonewsDetailPresenter;
   // private WebSettings mSetting;

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
//        urlToString();
    }

/*    private void urlToString() {
        mSetting = webView.getSettings();
        mSetting.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "java_obj");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Toast.makeText(ToutiaonewsDetailActivity.this, "onPageFinished", Toast.LENGTH_SHORT).show();
                view.loadUrl("javascript:window.java_obj.getSource(document.documentElement.outerHTML);void(0)");
                super.onPageFinished(view, url);
            }


        });
    }*/

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
                if(Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()){
                    toolbar.setTitleTextColor(getResources().getColor(R.color.white));
                    collapsing.setTitle("新闻详情");
                }else{
                    collapsing.setTitle("");
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //接入分享接口
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void showNewsDetialContent(DetailNews detailnews) {

/*        webView.loadUrl(newsurl);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());*/
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
    }

/*    //自己定义的类
    public final class InJavaScriptLocalObj {
        //一定也要加上这个注解,否则没有用
        @JavascriptInterface
        public void getSource(String html) {
            //取出HTML中P标签的文本内容,利用正则表达式匹配.
            Pattern pattern=Pattern.compile("<p class=\"section txt\">(.*?)</p>");
            Matcher matcher = pattern.matcher(html);
            StringBuffer sb=new StringBuffer();
            while (matcher.find())
            {
                sb.append(matcher.group(2));
            }
            //mHtmlText = sb.toString();
            Log.v("yxb", sb.toString());
            Toast.makeText(ToutiaonewsDetailActivity.this,sb.toString(), Toast.LENGTH_LONG).show();
        }
    }*/

}
