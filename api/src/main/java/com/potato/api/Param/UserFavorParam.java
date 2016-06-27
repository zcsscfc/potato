package com.potato.api.Param;

import java.math.BigInteger;

/**
 * Created by zhangcs on 2016/6/21.
 */
public class UserFavorParam {
    private BigInteger favorId;
    private String name;

    public BigInteger getFavorId() {
        return favorId;
    }

    public void setFavorId(BigInteger favorId) {
        this.favorId = favorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
