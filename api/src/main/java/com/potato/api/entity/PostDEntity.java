package com.potato.api.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by zhangcs on 2016/5/9.
 */
public class PostDEntity {
    private BigInteger postId;
    private String detail;
    private Date createT;

    public BigInteger getPostId() {
        return postId;
    }

    public void setPostId(BigInteger postId) {
        this.postId = postId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreateT() {
        return createT;
    }

    public void setCreateT(Date createT) {
        this.createT = createT;
    }
}
