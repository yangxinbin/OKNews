package com.baihui.yangxb.mainpage.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.baihui.yangxb.mainpage.presenter.MainpagePresenter;
import com.baihui.yangxb.mainpage.presenter.MainpagePresenterImpl;
import com.baihui.yangxb.mainpage.view.MainpageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.activity.ToutiaonewsFragment;

public class MainpageActivity extends AppCompatActivity implements MainpageView {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MainpagePresenter mainpagePresenter;
    private ColorStateList csl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        ButterKnife.bind(this);
        TextView tName = (TextView) navView.getHeaderView(0).findViewById(R.id.autorName);
        //TextView tName = (TextView) findViewById(R.id.autorName);
        tName.setText(BmobUser.getCurrentUser().getUsername());
        /*start DrawLayout item 选中字体颜色变化*/
        Resources resource=getBaseContext().getResources();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//版本问题控制 API23以上
            csl = resource.getColorStateList(R.color.navigation_menu_item_color, null);
        }else {
            csl = resource.getColorStateList(R.color.navigation_menu_item_color);
        }
        navView.setItemTextColor(csl);
        /*end*/
        navView.setItemIconTintList(null);//传入一个null参数，这样原本的彩色图标就可以显示出来了
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        setupDrawerContent(navView);
        mainpagePresenter = new MainpagePresenterImpl(this);
        selectBaihuinews();
    }

    private void setupDrawerContent(NavigationView navView) {
        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mainpagePresenter.switchNavigation(menuItem.getItemId());
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });
    }

    @Override
    public void selectBaihuinews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new ToutiaonewsFragment()).commit();
        Log.v("yxbbbb","===========b");
        toolbar.setTitle(R.string.nav_baihuinews);
    }

/*    @Override
    public void selectAuthormanage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new AuthormanagerFragment()).commit();
        toolbar.setTitle(R.string.nav_authormanager);
    }


    @Override
    public void selectMeipaivideo() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new MeipaivideoFragment()).commit();
        toolbar.setTitle(R.string.nav_meipaivideo);
    }

    @Override
    public void selectComicnews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new ComicnewsFragment()).commit();
        toolbar.setTitle(R.string.nav_comicnews);
    }

    @Override
    public void selectBaihuimap() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new BaihuimapFragment()).commit();
        toolbar.setTitle(R.string.nav_baihuimap);
    }

    @Override
    public void selectWeathernews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new WeathernewsFragment()).commit();
        toolbar.setTitle(R.string.nav_weathernews);
    }

    @Override
    public void selectEnjoygame() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new EnjoygameFragment()).commit();
        toolbar.setTitle(R.string.nav_enjoygame);
    }

    @Override
    public void selectUserhelp() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new UserhelpFragment()).commit();
        toolbar.setTitle(R.string.nav_userhelp);
    }

    @Override
    public void selectAboutauthor() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new AboutauthorFragment()).commit();
        toolbar.setTitle(R.string.nav_aboutauthor);
    }
    */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if (id == R.id.action_exit){
            BmobUser.logOut();   //清除缓存用户对象
            SharedPreferences isOk = getSharedPreferences("isOk",MODE_PRIVATE);
            isOk.edit().putString("isOk", "no")
                    .commit();
            exitDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            exitDialog();
        }

        return false;
    }

    private void exitDialog() {
        // 创建退出对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.exit);
        // 设置对话框标题
        builder.setTitle("系统提示");
        // 设置对话框消息
        builder.setMessage("确定要退出吗");
        //监听下方button点击事件
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
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
}
