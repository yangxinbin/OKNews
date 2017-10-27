package com.baihui.yangxb.oknews.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baihui.yangxb.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Administrator on 2016/11/22 0022.
 */

public class ToutiaonewsFragment extends Fragment {
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    public static final int NEWS_TYPE_TOPNEWS = 0;
    public static final int NEWS_TYPE_SOCIETY = 1;
    public static final int NEWS_TYPE_DOMESTIC = 2;
    public static final int NEWS_TYPE_INTERNATIONAL = 3;
    public static final int NEWS_TYPE_ENTERTAINMENT = 4;
    public static final int NEWS_TYPE_SPORTS = 5;
    public static final int NEWS_TYPE_MILITARY = 6;
    public static final int NEWS_TYPE_SCIENCE = 7;
    public static final int NEWS_TYPE_FINANCE = 8;
    public static final int NEWS_TYPE_FASHION = 9;
    @Bind(R.id.new_toolbar)
    Toolbar newToolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toutiaonews, null);
        ButterKnife.bind(this, view);
        ((AppCompatActivity) getActivity()).setSupportActionBar(newToolbar);
        viewpager.setOffscreenPageLimit(2);
        setupViewPager(viewpager);
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_topnews));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_society));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_domestic));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_international));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_entertainment));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_sports));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_Military));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_science));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_finance));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.tab_fashion));
//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                tabLayout.setupWithViewPager(viewpager);
//            }
//        });
        tabLayout.setupWithViewPager(viewpager);
        return view;
    }

    private void setupViewPager(ViewPager viewpager) {
        ToutiaonewsPagerAdapter adapter = new ToutiaonewsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_TOPNEWS), getString(R.string.tab_topnews));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_SOCIETY), getString(R.string.tab_society));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_DOMESTIC), getString(R.string.tab_domestic));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_INTERNATIONAL), getString(R.string.tab_international));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_ENTERTAINMENT), getString(R.string.tab_entertainment));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_SPORTS), getString(R.string.tab_sports));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_MILITARY), getString(R.string.tab_Military));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_SCIENCE), getString(R.string.tab_science));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_FINANCE), getString(R.string.tab_finance));
        adapter.addFragment(ToutiaonewsRecyclerviewFragment.newInstance(NEWS_TYPE_FASHION), getString(R.string.tab_fashion));
        viewpager.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class ToutiaonewsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public ToutiaonewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String string) {
            mFragments.add(fragment);
            mFragmentTitles.add(string);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }
}
