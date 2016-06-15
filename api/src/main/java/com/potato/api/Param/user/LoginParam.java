package com.potato.api.Param.user;

import com.potato.api.framework.validation.NotEmpty;

/**
 * Created by zhangcs on 2016/6/3.
 */
public class LoginParam {
    private String logName;
    @NotEmpty
    private String password;

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
