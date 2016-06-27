package com.potato.api.framework.security.impl;


import com.potato.api.framework.security.TokenManager;
import com.potato.api.framework.util.CodecUtil;
import com.potato.api.framework.util.StringUtil;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认令牌管理器
 *
 * @author huangyong
 * @since 1.0.0
 */
public class DefaultTokenManager implements TokenManager {

    private static Map<String, BigInteger> tokenMap = new ConcurrentHashMap<>();

    @Override
    public String createToken(BigInteger userId) {
        String token = CodecUtil.createUUID();
        tokenMap.put(token, userId);
        return token;
    }

    @Override
    public boolean checkToken(String token) {
        return !StringUtil.isEmpty(token) && tokenMap.containsKey(token);
    }

    @Override
    public BigInteger getTokenValue(String token) {
        return tokenMap.get(token);
    }
}
