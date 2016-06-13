package com.potato.model;

/**
 * Created by s1112001 on 2016/6/13.
 */
public class UserLoginRequest {
    private String log_name = "";
    private String password="";

    public void setLog_name(String log_name){
        this.log_name = log_name;
    }
    public void setPassword(String password){
        this.password=password;
    }
}
