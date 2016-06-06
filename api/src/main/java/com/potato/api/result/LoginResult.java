package com.potato.api.result;

import java.math.BigInteger;

/**
 * Created by zhangcs on 2016/6/3.
 */
public class LoginResult {
    private String userId;
    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
