package com.potato.model;

public class PostMainRequest {
    private long num = 0;
    private long subject_id = 0;
    private long origin_id = 0;
    private long first_post_id = 0;
    private long last_post_id = 0;

    public long getNum() {
        return num;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(long subject_id) {
        this.subject_id = subject_id;
    }

    public long getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(long origin_id) {
        this.origin_id = origin_id;
    }

    public long getFirst_post_id() {
        return first_post_id;
    }

    public void setFirst_post_id(long first_post_id) {
        this.first_post_id = first_post_id;
    }

    public long getLast_post_id() {
        return last_post_id;
    }

    public void setLast_post_id(long last_post_id) {
        this.last_post_id = last_post_id;
    }
}
