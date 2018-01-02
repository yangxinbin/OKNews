package com.baihui.yangxb.startapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baihui.yangxb.R;
import com.baihui.yangxb.mainpage.activity.MainpageActivity;
import com.github.glomadrian.grav.GravView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

public class BubbleStartActivity extends AppCompatActivity {


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
    FrameLayout loginLayout;
    @Bind(R.id.registerLayout)
    FrameLayout registerLayout;
    @Bind(R.id.mobilelogin)
    TextView mobilelogin;
    @Bind(R.id.grav)
    GravView grav;
    private SharedPreferences isOk;
    private String uname;

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
        SharedPreferences settings = getSharedPreferences("Re_password",MODE_PRIVATE);
        String strJudge = settings.getString("judgeText", "no");// 选中状态
        SharedPreferences loginsettings = getSharedPreferences("Re_login",MODE_PRIVATE);
        String loginstrJudge = loginsettings.getString("loginjudgeText", "no");// 选中状态
        SharedPreferences isOk = getSharedPreferences("isOk",MODE_PRIVATE);
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
            if(ok.equals("yes")) {
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
                Log.v("yxbbb","---------"+!(etUsername.getText().toString().isEmpty())+"===="+!(etPassword.getText().toString().isEmpty()));
                if (arg1 == true) {//勾选时，存入EditText中的用户名密码
                    if (!(etUsername.getText().toString().isEmpty()) && !(etPassword.getText().toString().isEmpty())) {
                        settings.edit().putString("judgeText", "yes")
                                .putString("userNameText", etUsername.getText().toString())
                                .putString("passwordText", etPassword.getText().toString())
                                .commit();
                    }
                }else {//不勾选，存入空String对象
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
                }else {//不勾选，存入空String对象
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

    @OnClick({R.id.loginLayout, R.id.registerLayout})
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
                            showToast("用户名：" + username+" 登录成功!");
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
                    startActivityForResult(new Intent(this, RegisterActivity.class),0);
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
}
