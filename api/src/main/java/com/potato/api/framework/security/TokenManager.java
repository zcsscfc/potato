package com.potato.api.framework.security;

/**
 * 令牌管理器
 *
 * @author huangyong
 * @since 1.0.0
 */
public interface TokenManager {

    String createToken(String username);

    boolean checkToken(String token);
    String getTokenValue(String token);
}
