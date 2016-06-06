package com.potato.api.framework.security.impl;

import com.potato.api.framework.security.TokenManager;
import com.potato.api.framework.util.CodecUtil;
import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 基于 Redis 的令牌管理器
 *
 * @author huangyong
 * @since 1.0.0
 */
public class RedisTokenManager implements TokenManager {

    private static final int DEFAULT_DATABASE = 0;
    private static final int DEFAULT_SECONDS = 0;

    private JedisPool jedisPool;
    private int database = DEFAULT_DATABASE;
    private int seconds = DEFAULT_SECONDS;

    public void setJedisPool(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    @Override
    public String createToken(String username) {
        String token = CodecUtil.createUUID();
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database);
            if (seconds != 0) {
                jedis.setex(token, seconds, username);
            } else {
                jedis.set(token, username);
            }
        }
        return token;
    }

    @Override
    public boolean checkToken(String token) {
        boolean result;
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.select(database);
            result = jedis.exists(token);
            if (seconds != 0) {
                jedis.expire(token, seconds);
            }
        }
        return result;
    }
}
