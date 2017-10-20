package com.baihui.yangxb.oknews.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.adapter.ToutiaonewsAdapter;
import com.baihui.yangxb.oknews.entity.ToutiaonewsBean;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsPresenter;
import com.baihui.yangxb.oknews.presenter.ToutiaonewsPresenterImpl;
import com.baihui.yangxb.oknews.view.ToutiaonewsView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ToutiaonewsRecyclerviewFragment extends Fragment implements ToutiaonewsView,OnRefreshListener {

    @Bind(R.id.recycle_view)
    RecyclerView recycleView;
    @Bind(R.id.smart_refresh_widget)
    RefreshLayout smartRefreshWidget;
    private ToutiaonewsPresenter mNewsPresenter;
    private int mType = ToutiaonewsFragment.NEWS_TYPE_TOPNEWS;
    private List<ToutiaonewsBean> mDataall;
    private List<ToutiaonewsBean> mData;
    private LinearLayoutManager mLayoutManager;
    private ToutiaonewsAdapter adapter;


    public static ToutiaonewsRecyclerviewFragment newInstance(int type) {
        Bundle bundle = new Bundle();
        ToutiaonewsRecyclerviewFragment fragment = new ToutiaonewsRecyclerviewFragment();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsPresenter = new ToutiaonewsPresenterImpl(this);
        mType = getArguments().getInt("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toutiaonews_recyclerview, null);
        ButterKnife.bind(this, view);
       // swipeRefreshWidget.setColorSchemeResources(R.color.colorPrimary,
       //         R.color.colorPrimaryDark, R.color.colorPrimary,
       //         R.color.colorPrimaryDark);
       // swipeRefreshWidget.setOnRefreshListener(this);
        smartRefreshWidget.setOnRefreshListener(this);
        recycleView.setHasFixedSize(true);//固定宽高
        mLayoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(mLayoutManager);
        recycleView.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        adapter = new ToutiaonewsAdapter(getActivity().getApplicationContext());
        //adapter.setOnItemnewsClickListener(mOnItemClickListener);
        recycleView.setAdapter(adapter);
        recycleView.addOnScrollListener(mOnScrollListener);
        onRefresh(smartRefreshWidget);
        return view;
    }

   /* private ToutiaonewsAdapter.OnItemnewsClickListener mOnItemClickListener = new ToutiaonewsAdapter.OnItemnewsClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if (mData.size() <= 0) {
                return;
            }
            String newsurl = adapter.getItem(position).getResult().getData().get(position).getUrl();
            String newsimg = adapter.getItem(position).getResult().getData().get(position).getThumbnail_pic_s();
            Intent intent = new Intent(getActivity(), ToutiaonewsDetailActivity.class);
            intent.putExtra("newsurl", newsurl);//传输内容
            intent.putExtra("newsimg", newsimg);//传输图片
            View transitionView = view.findViewById(R.id.item_news_img);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            transitionView, getString(R.string.transition_news_img));
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    }; */
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();//可见的最后一个item
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == adapter.getItemCount()
                    && adapter.isShowFooter()) {//加载判断条件 手指离开屏幕 到了footeritem
                //加载更多
                //mNewsPresenter.loadNews(mType);//加载最后报错
                int count = adapter.getItemCount();
                int i;
                for (i = count; i < count + 5; i++) {
                    if (mDataall == null){
                        break;//一开始断网报空指针的情况
                    }
                    Log.v("yxbbbb",i+"--------"+mDataall.size());
                    if (i >= (mDataall.size())){//比如一共30条新闻 这个条件当为29时还是可以把30那条新闻加上去的
                        noMoreMsg();
                        break;
                    }
                    adapter.addItem(mDataall.get(i));//addItem里面记得要notifyDataSetChanged 否则第一次加载不会显示数据
                }
                if (mDataall !=null && i >= (mDataall.size())){//到最后
                    adapter.isFooter(false);
                    noMoreMsg();
                }
            }
        }
    };

    @Override
    public void showProgress() {
        smartRefreshWidget.finishRefresh(true);
    }

    @Override
    public void addNews(List<ToutiaonewsBean> newsList) {
        Log.v("yxbbbb",newsList.size()+"--------");
        if(newsList.size()==0){
            Toast.makeText(getActivity(), "服务器坑爹，只能请求100次", Toast.LENGTH_SHORT).show();
            return;
        }
        adapter.isShowFooter(true);
        if (mData == null && mDataall == null) {
            mDataall = new ArrayList<ToutiaonewsBean>();
            mData = new ArrayList<ToutiaonewsBean>();
        }
        mDataall.addAll(newsList);//一次加载所有  给后面的加载用
        for (int i= 0 ;i<5;i++){
            mData.add(mDataall.get(i));
        }
        adapter.setmDate(mData);
    }

    @Override
    public void hideProgress() {
        if (smartRefreshWidget != null){
            smartRefreshWidget.finishRefresh(false);//报错因为在onDestroy() 使用了ButterKnife.unbind(this) swipeRefreshWidget id找不到;
        }
    }

    @Override
    public void showLoadFailMsg() {
        View view = getActivity() == null ? recycleView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        if (isAdded()) {
            Snackbar snackbar = Snackbar.make(view, getResources().getString(R.string.load_fail), Snackbar.LENGTH_SHORT);
            View snackbarview = snackbar.getView();
            snackbarview.setBackgroundColor(getResources().getColor(R.color.snackbar));
            TextView tvSnackbarText = (TextView) snackbarview.findViewById(android.support.design.R.id.snackbar_text);
            tvSnackbarText.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    public void noMoreMsg() {
        View view = getActivity() == null ? recycleView.getRootView() : getActivity().findViewById(R.id.drawer_layout);
        if (isAdded()) {
            final Snackbar snackbar = Snackbar.make(view, getResources().getString(R.string.no_more), Snackbar.LENGTH_SHORT);
            View snackbarview = snackbar.getView();
            snackbarview.setBackgroundColor(getResources().getColor(R.color.snackbar));
            TextView tvSnackbarText = (TextView) snackbarview.findViewById(android.support.design.R.id.snackbar_text);
            tvSnackbarText.setTextColor(Color.WHITE);
//            snackbar.setAction("click", new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    snackbar.dismiss();
//                }
//            });
            snackbar.show();
            //adapter.isShowFooter(false);//关闭加载更多... 字符串
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        if (mData != null && mDataall != null) {
            mDataall.clear();//一定要加上否则会报越界异常 不执行代码加载的if判断
            mData.clear();
        }
        mNewsPresenter.loadNews(mType);
    }
}
