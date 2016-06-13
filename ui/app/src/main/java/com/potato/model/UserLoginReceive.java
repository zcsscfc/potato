package com.potato.model;

/**
 * Created by s1112001 on 2016/6/13.
 */
public class UserLoginReceive {
    public Meta meta;
    public Data data;

    public class Meta{
        public String success;
        public String message;
    }

    public class Data{
        public String user_id;
        public String token;
    }
}
