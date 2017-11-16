package com.baihui.yangxb.startapp;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by yangxb on 17-10-20.
 */

public class User extends BmobUser implements Serializable { //implements Serializable 用于传递User
    private BmobFile avatar;

    public BmobFile getAvatar() {
        return avatar;
    }

    public void setAvatar(BmobFile avatar) {
        this.avatar = avatar;
    }
}
