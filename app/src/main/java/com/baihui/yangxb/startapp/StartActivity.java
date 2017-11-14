package com.baihui.yangxb.startapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baihui.yangxb.R;
import com.baihui.yangxb.mainpage.activity.MainpageActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

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
    @Bind(R.id.checkBox_password)
    CheckBox checkBoxPassword;
    @Bind(R.id.checkBox_login)
    CheckBox checkBoxLogin;
    private String uname;
    private String phoneNum;
    private String phoneCode;
    private SharedPreferences isOk;

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
                startActivity(new Intent(StartActivity.this, MainpageActivity.class));
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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @OnClick({R.id.bt_go, R.id.fab, R.id.mobilelogin, R.id.checknumber_get, R.id.phone_go, R.id.phone_back})
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
                Log.v("yxbbbb", username + "--------" + password);
                BmobUser.loginByAccount(username, password, new LogInListener<User>() {
                    @Override
                    public void done(User o, BmobException e) {
                        // Log.v("yxbbbb",username+"--------"+password);
                        if (e == null) {
                            isOk = getSharedPreferences("isOk", MODE_PRIVATE);
                            isOk.edit().putString("isOk", "yes")
                                    .commit();
                            showToast("登录成功---用户名：" + username);
                            Explode explode = new Explode();
                            explode.setDuration(500);
                            getWindow().setExitTransition(explode);
                            getWindow().setEnterTransition(explode);
                            ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(StartActivity.this);//去掉动画效果不好
                            Intent i2 = new Intent(StartActivity.this, MainpageActivity.class);
                            startActivity(i2);
                            finish();
                        } else {
//                            isOk.edit().putString("isOk", "no")
//                                    .commit();
                            showErrorMsg(e.getErrorCode());
                            //showToast("登录失败：code=" + e.getErrorCode() + "，错误描述：" + e.getLocalizedMessage());
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
                RelativeLayout.LayoutParams params_phone_cv = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                params_phone_cv.addRule(RelativeLayout.ALIGN_TOP, R.id.phone_cv);
                params_phone_cv.addRule(RelativeLayout.ALIGN_END, R.id.phone_cv);//动态修改fab的布局 fab使用了RelativeLayout 布局导致
                fab.setLayoutParams(params_phone_cv); //使layout更新
                phoneCv.setVisibility(View.VISIBLE);
                break;
            case R.id.checknumber_get:
                //手机号码
                Log.i("smile", "===================");//用于后续的查询本次短信发送状态
                phoneNum = phoneUsername.getText().toString();
                BmobSMS.requestSMSCode(phoneNum, "template", new QueryListener<Integer>() {
                    @Override
                    public void done(final Integer smsId, BmobException ex) {
                        if (ex == null) {//验证码发送成功
                            Log.i("smile", "短信id：" + smsId);//用于后续的查询本次短信发送状态
                            new CountDownTimer(60000, 1000) {
                                @Override
                                public void onTick(long millisUntilFinished) {
                                    checknumberGet.setText(millisUntilFinished / 1000 + "秒");
                                }

                                @Override
                                public void onFinish() {
                                    checknumberGet.setClickable(true);
                                    checknumberGet.setText("重新发送");
                                }
                            }.start();
                            final BmobUser bu = new BmobUser();
                            bu.setUsername(phoneNum);
                            bu.setPassword(smsId.toString());
                            final String v = smsId.toString();
                            bu.signUp(new SaveListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if (e == null) {
                                        Log.v("yxbbb", "----------" + v);
                                        showToast("用户名：" + bu.getUsername() + "\n" + "密码：验证码");
                                    } else {
                                        showToast("注册失败：" + e);
                                    }
                                }
                            });
                        } else {
                            showToast("错误描述：" + ex);
                        }
                    }
                });
                break;
            case R.id.phone_go:
                phoneCode = phonePassword.getText().toString();
                phoneNum = phoneUsername.getText().toString();
                Log.v("yxbbb", "----------" + phoneCode + "==========" + phoneNum);
                BmobUser.signOrLoginByMobilePhone(phoneNum, phoneCode, new LogInListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (user != null) {
                            if (TextUtils.isEmpty(phoneNum)) {
                                showToast("手机号码不能为空");
                                return;
                            }
                            if (TextUtils.isEmpty(phoneCode)) {
                                showToast("验证码不能为空");
                                return;
                            }
                            startActivity(new Intent(StartActivity.this, MainpageActivity.class));
                            showToast("用户登陆成功" + phoneNum + "----" + phoneCode);
                        } else {
                            showToast("错误描述：" + e);
                        }
                    }
                });

                break;
            case R.id.phone_back:
                cv.setVisibility(View.VISIBLE);
                RelativeLayout.LayoutParams params_cv = (RelativeLayout.LayoutParams) fab.getLayoutParams();
                params_cv.addRule(RelativeLayout.ALIGN_TOP, R.id.cv);
                params_cv.addRule(RelativeLayout.ALIGN_END, R.id.cv);
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

    public void showErrorMsg(int e) {
        Snackbar snackbar = null;
            View view =getWindow().getDecorView();
            switch (e){
                case 9016:
                    snackbar = Snackbar.make(view, getResources().getString(R.string.net_fail), Snackbar.LENGTH_LONG);
                    break;
                case 101:
                    snackbar = Snackbar.make(view, getResources().getString(R.string.name_fail), Snackbar.LENGTH_LONG);
                    break;
                default:
                    break;
            }
            View snackbarview = snackbar.getView();
            snackbarview.setBackgroundColor(getResources().getColor(R.color.snackbar));
            TextView tvSnackbarText = (TextView) snackbarview.findViewById(android.support.design.R.id.snackbar_text);
            tvSnackbarText.setTextColor(Color.WHITE);
            snackbar.show();
    }

}
