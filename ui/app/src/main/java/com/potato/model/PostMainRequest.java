package com.potato.model;

public class PostMainRequest {
    private int num = 0;
    private int subject_id = 0;
    private int origin_id = 0;
    private int first_post_id = 0;
    private int last_post_id = 0;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getSubject_id() {
        return subject_id;
    }

    public void setSubject_id(int subject_id) {
        this.subject_id = subject_id;
    }

    public int getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(int origin_id) {
        this.origin_id = origin_id;
    }

    public int getFirst_post_id() {
        return first_post_id;
    }

    public void setFirst_post_id(int first_post_id) {
        this.first_post_id = first_post_id;
    }

    public int getLast_post_id() {
        return last_post_id;
    }

    public void setLast_post_id(int last_post_id) {
        this.last_post_id = last_post_id;
    }
}
