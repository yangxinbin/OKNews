package com.baihui.yangxb.startapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baihui.yangxb.R;
import com.baihui.yangxb.mainpage.activity.MainpageActivity;
import com.github.glomadrian.grav.GravView;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class BubbleStartActivity extends AppCompatActivity implements PlatformActionListener {


    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.login)
    LinearLayout login;
    @Bind(R.id.checkBox_password)
    CheckBox checkBoxPassword;
    @Bind(R.id.checkBox_login)
    CheckBox checkBoxLogin;
    @Bind(R.id.loginLayout)
    RippleView loginLayout;
    @Bind(R.id.registerLayout)
    RippleView registerLayout;
    @Bind(R.id.mobilelogin)
    TextView mobilelogin;
    @Bind(R.id.grav)
    GravView grav;
    @Bind(R.id.textView6)
    TextView textView6;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.textView7)
    TextView textView7;
    private SharedPreferences isOk;
    private String uname;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(BubbleStartActivity.this, "授权登陆成功", Toast.LENGTH_SHORT).show();
                    Platform platform = (Platform) msg.obj;
                    String userId = platform.getDb().getUserId();//获取用户账号
                    String userName = platform.getDb().getUserName();//获取用户名字
                    String userIcon = platform.getDb().getUserIcon();//获取用户头像
                    String userGender = platform.getDb().getUserGender(); //获取用户性别，m = 男, f = 女，如果微信没有设置性别,默认返回null
                    Toast.makeText(BubbleStartActivity.this, "用户名：" + userName + "  性别：" + userGender, Toast.LENGTH_SHORT).show();
                    //下面就可以利用获取的用户信息登录自己的服务器或者做自己想做的事啦!
                    //。。。
                    startActivity(new Intent(BubbleStartActivity.this, MainpageActivity.class));
                    break;
                case 2:
                    Toast.makeText(BubbleStartActivity.this, "授权登陆失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(BubbleStartActivity.this, "授权登陆取消", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bubble);
        ButterKnife.bind(this);
        //第一：默认初始化
        Bmob.initialize(this, "0004b86d65ce3ae4ffbbd043bb3ca832");
        checkListen();
        chectFun();
    }

    private void chectFun() {
        //从配置文件中取用户名密码的键值对
        //若第一运行，则取出的键值对为所设置的默认值
        SharedPreferences settings = getSharedPreferences("Re_password", MODE_PRIVATE);
        String strJudge = settings.getString("judgeText", "no");// 选中状态
        SharedPreferences loginsettings = getSharedPreferences("Re_login", MODE_PRIVATE);
        String loginstrJudge = loginsettings.getString("loginjudgeText", "no");// 选中状态
        SharedPreferences isOk = getSharedPreferences("isOk", MODE_PRIVATE);
        String ok = isOk.getString("isOk", "no");// 用户名
        String strUserName = settings.getString("userNameText", "");// 用户名
        String strPassword = settings.getString("passwordText", "");// 密码
        if (strJudge.equals("yes")) {
            checkBoxPassword.setChecked(true);
            etUsername.setText(strUserName);
            etPassword.setText(strPassword);
        } else {
            checkBoxPassword.setChecked(false);
            etUsername.setText("");
            etPassword.setText("");
        }
        if (loginstrJudge.equals("yes")) {
            etUsername.setText(strUserName);
            etPassword.setText(strPassword);
            checkBoxLogin.setChecked(true);
            if (ok.equals("yes")) {
                startActivity(new Intent(BubbleStartActivity.this, MainpageActivity.class));
                finish();
            }
        } else {
            checkBoxLogin.setChecked(false);
        }
    }

    private void checkListen() {
        checkBoxPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                SharedPreferences settings = getSharedPreferences("Re_password", MODE_PRIVATE);
                Log.v("yxbbb", "---------" + !(etUsername.getText().toString().isEmpty()) + "====" + !(etPassword.getText().toString().isEmpty()));
                if (arg1 == true) {//勾选时，存入EditText中的用户名密码
                    if (!(etUsername.getText().toString().isEmpty()) && !(etPassword.getText().toString().isEmpty())) {
                        settings.edit().putString("judgeText", "yes")
                                .putString("userNameText", etUsername.getText().toString())
                                .putString("passwordText", etPassword.getText().toString())
                                .commit();
                    }
                } else {//不勾选，存入空String对象
                    settings.edit().putString("judgeText", "no")
                            .commit();
                }
            }
        });
        checkBoxLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                SharedPreferences loginsettings = getSharedPreferences("Re_login", MODE_PRIVATE);
                if (arg1 == true) {//勾选时，存入EditText中的用户名密码
                    checkBoxPassword.setChecked(true);
                    checkBoxPassword.setClickable(false);
                    loginsettings.edit().putString("loginjudgeText", "yes")
                            .commit();
                } else {//不勾选，存入空String对象
                    //checkBoxPassword.setChecked(false);
                    checkBoxPassword.setClickable(true);
                    loginsettings.edit().putString("loginjudgeText", "no")
                            .commit();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    Toast mToast;

    public void showToast(String text) {
        if (!TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT);
            } else {
                mToast.setText(text);
            }
            mToast.show();
        }
    }

    public void showErrorMsg(int e) {
        Snackbar snackbar = null;
        View view = getWindow().getDecorView();
        View snackbarview = null;
        switch (e) {
            case 9016:
                snackbar = Snackbar.make(view, getResources().getString(R.string.net_fail), Snackbar.LENGTH_LONG);
                break;
            case 101:
                snackbar = Snackbar.make(view, getResources().getString(R.string.name_fail), Snackbar.LENGTH_LONG);
                break;
            default:
                break;
        }
        if (snackbar != null) {
            snackbarview = snackbar.getView();
            snackbarview.setBackgroundColor(getResources().getColor(R.color.snackbar));
            TextView tvSnackbarText = (TextView) snackbarview.findViewById(android.support.design.R.id.snackbar_text);
            tvSnackbarText.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    @OnClick({R.id.loginLayout, R.id.registerLayout,R.id.textView7})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.loginLayout:
                //    BmobUser bu2 = new BmobUser();
                final String username = etUsername.getText().toString();
                final String password = etPassword.getText().toString();
                if (TextUtils.isEmpty(username)) {
                    showToast("账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    showToast("密码不能为空");
                    return;
                }
                Log.v("yxbbbb", username + "--------" + password);
                BmobUser.loginByAccount(username, password, new LogInListener<User>() {
                    @Override
                    public void done(User o, BmobException e) {
                        // Log.v("yxbbbb",username+"--------"+password);
                        if (e == null) {
                            isOk = getSharedPreferences("isOk", MODE_PRIVATE);
                            isOk.edit().putString("isOk", "yes")
                                    .commit();
                            showToast("登录成功");
                            Explode explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            //  ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(StartActivity.this);//去掉动画效果不好
                            Intent i2 = new Intent(BubbleStartActivity.this, MainpageActivity.class);
                            startActivity(i2);
                            finish();
                        } else {
                            showErrorMsg(e.getErrorCode());
                        }
                    }
                });
                break;
            case R.id.registerLayout:
                startActivityForResult(new Intent(this, RegisterActivity.class), 0);
                break;
            case R.id.textView7:
                Log.v("yxbbbb","==========");
                loginByWeibo();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);//
        switch (requestCode) {
            case 0:   //调用个人中心后返回
                if (resultCode == RESULT_OK) {
                    uname = intent.getStringExtra("username");
                    etUsername.setText(uname);
                }
                break;
            default:
                break;
        }
    }
/**
 * 微博登陆
 */
        private void loginByWeibo(){
            Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
            sinaWeibo.setPlatformActionListener(this);
            sinaWeibo.SSOSetting(false);
            //sinaWeibo.SSOSetting(true); // true表示不使用SSO方式授权
            if (!sinaWeibo.isClientValid()) {
                Toast.makeText(this, "新浪微博未安装,请先安装新浪微博", Toast.LENGTH_SHORT).show();
            }
            authorize(sinaWeibo);
        }
    /**
     * 授权
     *
     * @param platform
     */
    private void authorize(Platform platform) {
        if (platform == null) {
            return;
        }
        if (platform.isAuthValid()) {  //如果授权就删除授权资料
            platform.removeAccount(true);
        }

        platform.showUser(null); //授权并获取用户信息
    }

    /**
     * 授权成功的回调
     *
     * @param platform
     * @param i
     * @param hashMap
     */
    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message message = Message.obtain();
        message.what = 1;
        message.obj = platform;
        mHandler.sendMessage(message);
    }

    /**
     * 授权错误的回调
     *
     * @param platform
     * @param i
     * @param throwable
     */
    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message message = Message.obtain();
        message.what = 2;
        message.obj = platform;
        mHandler.sendMessage(message);
    }

    /**
     * 授权取消的回调
     *
     * @param platform
     * @param i
     */
    @Override
    public void onCancel(Platform platform, int i) {
        Message message = Message.obtain();
        message.what = 3;
        message.obj = platform;
        mHandler.sendMessage(message);
    }
}
