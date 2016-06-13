package com.potato.model;

/**
 * Created by s1112001 on 2016/6/13.
 */
public class UserRegRequest {
    private String log_name = "";
    private String wechat="";
    private String password="";
    private String nick_name="";
    private String mobile = "";

    public void setLog_name(String log_name){
        this.log_name = log_name;
    }
    public void setPassword(String password){
        this.password=password;
    }
}
