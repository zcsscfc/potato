package com.potato.api.Param;

import com.potato.api.framework.validation.NotEmpty;

/**
 * Created by zhangcs on 2016/6/3.
 */
public class UserRegParam {
    @NotEmpty
    private String logName;
    private String wechat;
    @NotEmpty
    private String password;
    private String nickName;
    private String mobile;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getWechat() {
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
