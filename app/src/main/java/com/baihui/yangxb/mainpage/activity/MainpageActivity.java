package com.baihui.yangxb.mainpage.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ProviderInfo;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baihui.yangxb.aboutauthor.AboutAuthor;
import com.baihui.yangxb.authorMessage.Author;
import com.baihui.yangxb.howtouser.HowToUser;
import com.baihui.yangxb.mainpage.presenter.MainpagePresenter;
import com.baihui.yangxb.mainpage.presenter.MainpagePresenterImpl;
import com.baihui.yangxb.mainpage.view.MainpageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

import com.baihui.yangxb.R;
import com.baihui.yangxb.oknews.activity.ToutiaonewsFragment;
import com.baihui.yangxb.weathernews.activity.WeathernewsFragment;
import com.baihui.yangxb.weathernews.selectcity.activity.SelectCityMainActivity;

import java.io.File;

public class MainpageActivity extends AppCompatActivity implements MainpageView,View.OnClickListener {

    @Bind(R.id.nav_view)
    NavigationView navView;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    private MainpagePresenter mainpagePresenter;
    private ColorStateList csl;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private String addr;
    private TextView locatName;

    private ImageView mHeader_iv;
    private LinearLayout linearLayoutAuthor;
    private String userName;
    private Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        ButterKnife.bind(this);
        View headerLayout = navView.inflateHeaderView(R.layout.nav_header);
        TextView tName = (TextView) headerLayout.findViewById(R.id.autorName);
        locatName = (TextView) headerLayout.findViewById(R.id.located_city);
        linearLayoutAuthor = (LinearLayout) headerLayout.findViewById(R.id.author_message);
        linearLayoutAuthor.setOnClickListener(this);
        mHeader_iv = (ImageView) headerLayout.findViewById(R.id.profile_image);
        //默认图片
        Resources res=getResources();
        image= BitmapFactory.decodeResource(res, R.drawable.picture);//drawable转为Bitmap
        mHeader_iv.setImageBitmap(image);
        //TextView tName = (TextView) navView.getHeaderView(0).findViewById(R.id.autorName);
        //TextView tName = (TextView) findViewById(R.id.autorName);
        if (tName != null && BmobUser.getCurrentUser() != null) {
            userName = BmobUser.getCurrentUser().getUsername();
            tName.setText(userName);//获得当前用户名
        }
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
        setLocatInNavHeader();
        setupDrawerContent(navView);
        mainpagePresenter = new MainpagePresenterImpl(this);
        selectBaihuinews();
    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //完成主界面更新,拿到数据
                    locatName.setText(addr);
                    break;
                case 1:
                    //完成主界面更新,拿到数据
                    locatName.setText("定位失败");
                    break;
                case 2:
                    mHeader_iv.setImageBitmap(image);
                    //也可以进行一些保存、压缩等操作后上传
//                    String path = saveImage("crop", image);

                    break;
                case 3:
                    mHeader_iv.setImageResource(R.drawable.picture);
                    break;
                default:
                    break;
            }
        }
    };
    private void setLocatInNavHeader() {
        mLocationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);
        //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        //可选，是否需要地址信息，默认为不需要，即参数为false
        //如果开发者需要获得当前点的地址信息，此处必须为true
        mLocationClient.setLocOption(option);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        //更多LocationClientOption的配置，请参照类参考中LocationClientOption类的详细说明
        mLocationClient.start();
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
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new ToutiaonewsFragment()).commitAllowingStateLoss();
    }
    @Override
    public void selectWeathernews() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new WeathernewsFragment()).commitAllowingStateLoss();
    }

/*  @Override
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
    public void selectEnjoygame() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new EnjoygameFragment()).commit();
        toolbar.setTitle(R.string.nav_enjoygame);
    }    */

    @Override
    public void selectUserhelp() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new AboutAuthor()).commitAllowingStateLoss();
    }

    @Override
    public void selectAboutauthor() {
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, new HowToUser()).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
    switch (view.getId()) {
        case R.id.author_message:
            Intent intentAuthor = new Intent(MainpageActivity.this, Author.class);
            Bundle b = new Bundle();
            Log.v("yxb","-------image------"+image);
            b.putParcelable("authorimg", image);
            b.putCharSequence("authorname", userName);
            intentAuthor.putExtras(b);
            startActivityForResult(intentAuthor, 0);//前面不加getActivity().  要不拿不到结果。
            drawerLayout.closeDrawers();
            break;
        default:
            break;
        }
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(final BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            //获取详细地址信息
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    addr = location.getAddrStr();
                    Log.v("yxbbbb","-----Listener-------"+addr);
                    if (addr != null){
                        mHandler.sendEmptyMessage(0);

                    }else {
                        mHandler.sendEmptyMessage(1);
                    }
                }
            }.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);//一定要加 否则fragment 里面的无效
        switch (requestCode) {
            case 0:   //调用个人中心后返回
                if (resultCode == RESULT_OK) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            Bundle b = intent.getExtras();
                            image = (Bitmap) b.getParcelable("backauthorimg");
                            if (userName != null) {
                                mHandler.sendEmptyMessage(2);
                            } else {
                                mHandler.sendEmptyMessage(3);
                            }
                        }
                    }.start();
                }
                break;
        }
    }
}
