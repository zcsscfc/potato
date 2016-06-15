package com.potato.model;

/**
 * Created by s1112001 on 2016/6/15.
 */
public class AddToReadRequest {
    private String post_id;
    private String user_id;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }


    public void setUser_id(String user_id){
        this.user_id = user_id;
    }
}
