package com.potato.api.framework.security.impl;


import com.potato.api.framework.security.TokenManager;
import com.potato.api.framework.util.CodecUtil;
import com.potato.api.framework.util.StringUtil;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认令牌管理器
 *
 * @author huangyong
 * @since 1.0.0
 */
public class DefaultTokenManager implements TokenManager {

    private static Map<String, String> tokenMap = new ConcurrentHashMap<>();

    @Override
    public String createToken(String username) {
        String token = CodecUtil.createUUID();
        tokenMap.put(token, username);
        return token;
    }

    @Override
    public boolean checkToken(String token) {
        return !StringUtil.isEmpty(token) && tokenMap.containsKey(token);
    }

    @Override
    public String getTokenValue(String token) {
        return tokenMap.get(token);
    }
}
