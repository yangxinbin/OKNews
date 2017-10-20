package com.baihui.yangxb.startapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.baihui.yangxb.R;
import com.baihui.yangxb.mainpage.activity.MainpageActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class StartActivity extends AppCompatActivity {

    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.mobilelogin)
    TextView mobilelogin;
    @Bind(R.id.phone_username)
    EditText phoneUsername;
    @Bind(R.id.phone_password)
    EditText phonePassword;
    @Bind(R.id.checknumber_get)
    Button checknumberGet;
    @Bind(R.id.phone_go)
    Button phoneGo;
    @Bind(R.id.phone_back)
    Button phoneBack;
    @Bind(R.id.phone_cv)
    CardView phoneCv;
    private String uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.startapp_main);
        ButterKnife.bind(this);
        cv.setVisibility(View.VISIBLE);
        phoneCv.setVisibility(View.GONE);
        getSupportActionBar().hide();//隐藏掉整个ActionBar，包括下面的Tabs
        //第一：默认初始化
        Bmob.initialize(this, "0004b86d65ce3ae4ffbbd043bb3ca832");
        Intent intent = getIntent();
        uname = intent.getStringExtra("username");
        etUsername.setText(uname);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.bt_go, R.id.fab,R.id.mobilelogin, R.id.checknumber_get, R.id.phone_go, R.id.phone_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_go:
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
                BmobUser.loginByAccount(username, password, new LogInListener<User>() {
                    @Override
                    public void done(User o, BmobException e) {
                       // Log.v("yxbbbb",username+"--------"+password);
                        if (e == null) {
                            showToast("登录成功---用户名：" + username);
                            Explode explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(StartActivity.this);
                            Intent i2 = new Intent(StartActivity.this, MainpageActivity.class);
                            startActivity(i2, oc2.toBundle());
                            finish();
                        } else {
                            showToast("登录失败：code=" + e.getErrorCode() + "，错误描述：" + e.getLocalizedMessage());
                        }
                    }
                });
               /* bu2.setUsername(username);
                bu2.setPassword(password);
                bu2.login(this, new SaveListener() {
                    @Override
                    public void onSuccess() {
                        showToast("登录成功");
                        Explode explode = new Explode();
                        explode.setDuration(500);
                        getWindow().setExitTransition(explode);
                        getWindow().setEnterTransition(explode);
                        ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(StartActivity.this);
                        Intent i2 = new Intent(StartActivity.this, MainpageActivity.class);
                        startActivity(i2, oc2.toBundle());
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        showToast("登录失败：code="+i+"，错误描述："+s);
                    }
                });*/

                break;
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;
            case R.id.mobilelogin:
                cv.setVisibility(View.GONE);
                RelativeLayout.LayoutParams params_phone_cv = (RelativeLayout.LayoutParams)fab.getLayoutParams();
                params_phone_cv.addRule(RelativeLayout.ALIGN_TOP,R.id.phone_cv);
                params_phone_cv.addRule(RelativeLayout.ALIGN_END,R.id.phone_cv);//动态修改fab的布局 fab使用了RelativeLayout 布局导致
                fab.setLayoutParams(params_phone_cv); //使layout更新
                phoneCv.setVisibility(View.VISIBLE);
                break;
            case R.id.checknumber_get:
                break;
            case R.id.phone_go:
                break;
            case R.id.phone_back:
                cv.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params_cv = (RelativeLayout.LayoutParams)fab.getLayoutParams();
                params_cv.addRule(RelativeLayout.ALIGN_TOP,R.id.cv);
                params_cv.addRule(RelativeLayout.ALIGN_END,R.id.cv);
                fab.setLayoutParams(params_cv); //使layout更新
                phoneCv.setVisibility(View.GONE);
                break;
        }
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

}
