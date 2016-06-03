package com.potato.api.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by zhangcs on 2016/6/3.
 */
public class UserEntity {
    private BigInteger userId;
    private String logName;
    private String wechat;
    private String password;
    private String nickName;
    private String mobile;
    private Date createT;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

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

    public Date getCreateT() {
        return createT;
    }

    public void setCreateT(Date createT) {
        this.createT = createT;
    }
}
