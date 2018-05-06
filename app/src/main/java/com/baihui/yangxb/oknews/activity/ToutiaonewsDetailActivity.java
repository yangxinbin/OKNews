package com.baihui.yangxb.oknews.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
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
import com.baihui.yangxb.oknews.view.ToutiaonewsDetailView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.Locale;
import java.util.concurrent.ConcurrentLinkedQueue;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.onekeyshare.OnekeyShare;
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
    private TextToSpeech tts;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhihunews_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
        videoText = new StringBuffer();
        newsurl = (String) getIntent().getSerializableExtra("newsurl");
        isWechar = getIntent().getBooleanExtra("iswechar", false);
        toutiaonewsDetailPresenter = new ToutiaonewsDetailPresenterImpl(getApplication(), this);
        toutiaonewsDetailPresenter.loadNewsDetail(this, isWechar, newsurl);//yxb
    }

    private void initTTS() {
        ConcurrentLinkedQueue<String> mBufferedMessages=new ConcurrentLinkedQueue<String>();;//消息队列
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Log.v("yxbbb","---SUCCESS----");
                    //下面这句代码是主要的，设置语言，如果是英文的话，就用Locale.ENGLISH
                    int result = tts.setLanguage(Locale.CHINA);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("yxbb", "not use");
                    } else {
                        Log.e("yxbb", "speak use");
                        tts.speak(videoText.toString(), TextToSpeech.QUEUE_FLUSH, null ,"read");
                    }
                }

            }
        });
        //进度监听器，有点粗糙，但够用了，记得在done后将mpeech shutdown
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                Log.v("yxbbb","---onStart----");
            }

            @Override
            public void onError(String utteranceId) {
                Log.v("yxbbb","---onError----");

            }

            @Override
            public void onDone(String utteranceId) {
                Log.v("yxbbb","---onDone----");

                tts.stop(); // 不管是否正在朗读TTS都被打断
                tts.shutdown();
            }
        });
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0x116:
                    //进入页面延迟1秒自动播放
                    initTTS();
                    if (tts != null) {
                        if (!tts.isSpeaking()) {//播报中
                            tts.setPitch(1.2f);// 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
                            tts.setSpeechRate(0.9f);//调整语速
                            Log.v("yxbbb","-------"+videoText.toString());
                            tts.speak(videoText.toString(), TextToSpeech.QUEUE_FLUSH, null ,"read");
                        }
                    }
                    break;
                    default:
                    break;
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
        ButterKnife.unbind(this);
        if (tts != null) {
            tts.stop(); // 不管是否正在朗读TTS都被打断
            tts.shutdown(); // 关闭，释放资源
        }
    }
    @Override
    public void showNewsDetialContent(DetailNews detailnews) {
        if (detailnews == null) {
            return;
        }
        if (detailnews.getNewsTitle() != null) {
            title = detailnews.getNewsTitle();
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

    @OnClick({R.id.fab_share, R.id.fab_read})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fab_share:
                newShare();
                break;
            case R.id.fab_read:
                handler.sendEmptyMessage(0x116);
        /*
        * 开启一个线程，执行完之后立刻销毁
        * 延迟1秒在播放语音。TextToSpeech的初始化需要时间
        * */
               /* new Thread() {
                    @Override
                    public void run() {
                        try {
                            //延迟一秒，开始自动播放
                            sleep(1000);
                            handler.sendEmptyMessage(0x116);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();*/
                break;
            default:
                break;
        }
    }

    private void newShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        //oks.setTitle(title);
        // titleUrl QQ和QQ空间跳转链接
        //oks.setTitleUrl(newsurl);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(title);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(newsimg);//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl(newsurl);
        // comment是我对这条分享的评论，仅在人人网使用
        oks.setComment("评论");
        // 启动分享GUI
        oks.show(this);
    }
}
