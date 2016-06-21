package com.android.potato;

import android.content.Context;
import android.content.SharedPreferences;

public class UserInfoShared {
    private SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    private String userName = null;
    private String userId = null;
    private String token = null;
    private String nickName = null;

    public UserInfoShared(Context context) {
        sharedPreferences = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
    }

    public void edit() {
        if (editor == null) {
            editor = sharedPreferences.edit();
        }
    }

    public boolean commit() {
        if (editor != null) {
            return editor.commit();
        }
        return false;
    }

    public String getUserName() {
        return sharedPreferences.getString("user_name", "");
    }

    public void setUserName(String userName) {
        if (editor != null) {
            editor.putString("user_name", userName);
        }
    }

    public String getUserId() {
        return sharedPreferences.getString("user_id", "");
    }

    public void setUserId(String userId) {
        if (editor != null) {
            editor.putString("user_id", userId);
        }
    }

    public String getToken() {
        return sharedPreferences.getString("token", "");
    }

    public void setToken(String token) {
        if (editor != null) {
            editor.putString("token", token);
        }
    }

    public String getNickName() {
        return sharedPreferences.getString("nick_name", "");
    }

    public void setNickName(String nickName) {
        if (editor != null) {
            editor.putString("nick_name", nickName);
        }
    }
}
