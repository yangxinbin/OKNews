package com.baihui.yangxb;

import android.app.Application;

import com.mob.MobSDK;

/**
 * Created by Administrator on 2018/4/17 0017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
    }
}
