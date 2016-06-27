package com.potato.api.entity;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by zhangcs on 2016/6/21.
 */
public class UserFavorEntity {
    private BigInteger favorId;
    private BigInteger userId;
    private String name;
    private Date createT;

    public BigInteger getFavorId() {
        return favorId;
    }

    public void setFavorId(BigInteger favorId) {
        this.favorId = favorId;
    }

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateT() {
        return createT;
    }

    public void setCreateT(Date createT) {
        this.createT = createT;
    }
}
