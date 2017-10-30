package com.baihui.yangxb.oknews.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baihui.yangxb.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

import static android.content.Context.MODE_PRIVATE;


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
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_toutiaonews, null);
        ButterKnife.bind(this, view);
        newToolbar.setTitle(R.string.nav_baihuinews);
        ((AppCompatActivity) getActivity()).setSupportActionBar(newToolbar);//不加这句 toolbar menu 不显示。
        DrawerLayout drawerLayout =(DrawerLayout)getActivity().findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,newToolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);//增加抽屉监听
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
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_new, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_exit){
            BmobUser.logOut();   //清除缓存用户对象
            SharedPreferences isOk = getActivity().getSharedPreferences("isOk",MODE_PRIVATE);
            isOk.edit().putString("isOk", "no")
                    .commit();
            exitDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void exitDialog() {
        // 创建退出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.drawable.exit);
        // 设置对话框标题
        builder.setTitle("系统提示");
        // 设置对话框消息
        builder.setMessage("确定要退出吗");
        //监听下方button点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        // 显示对话框
        builder.show();
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
