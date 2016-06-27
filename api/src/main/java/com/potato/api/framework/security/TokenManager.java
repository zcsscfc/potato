package com.potato.api.framework.security;

import java.math.BigInteger;

/**
 * 令牌管理器
 *
 * @author huangyong
 * @since 1.0.0
 */
public interface TokenManager {

    String createToken(BigInteger userId);

    boolean checkToken(String token);
    BigInteger getTokenValue(String token);
}
