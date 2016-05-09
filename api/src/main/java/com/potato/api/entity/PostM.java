package com.potato.api.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by zhangcs on 2016/5/5.
 */
public class PostM {
    private BigInteger postId;
    private String title;
    private String digest;
    private String thumb;
    private String fromUrl;

    public String getFromUrl() {
        return fromUrl;
    }

    public void setFromUrl(String fromUrl) {
        this.fromUrl = fromUrl;
    }

    public BigInteger getPostId() {
        return postId;
    }

    public void setPostId(BigInteger postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public BigInteger getOriginId() {
        return originId;
    }

    public void setOriginId(BigInteger originId) {
        this.originId = originId;
    }

    public Date getCreateT() {
        return createT;
    }

    public void setCreateT(Date createT) {
        this.createT = createT;
    }

    private BigInteger originId;
    private Date createT;
}
