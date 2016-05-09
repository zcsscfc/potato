package com.potato.api.framework.redis;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * JedisPool 工厂
 *
 * @author huangyong
 * @since 1.0.0
 */
public class JedisPoolFactoryBean implements FactoryBean<JedisPool>, InitializingBean {

    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 6379;
    private static final int DEFAULT_TIMEOUT = 2000;
    private static final int DEFAULT_MAX_TOTAL = 100;
    private static final int DEFAULT_MAX_IDLE = 20;
    private static final int DEFAULT_MIN_IDLE = 5;
    private static final boolean DEFAULT_TEST_ON_BORROW = true;
    private static final boolean DEFAULT_TEST_ON_RETURN = true;

    private JedisPool pool;

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private int timeout = DEFAULT_TIMEOUT;
    private String password;
    private int maxTotal = DEFAULT_MAX_TOTAL;
    private int maxIdle = DEFAULT_MAX_IDLE;
    private int minIdle = DEFAULT_MIN_IDLE;
    private boolean testOnBorrow = DEFAULT_TEST_ON_BORROW;
    private boolean testOnReturn = DEFAULT_TEST_ON_RETURN;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    @Override
    public JedisPool getObject() throws Exception {
        return pool;
    }

    @Override
    public Class<?> getObjectType() {
        return JedisPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        pool = new JedisPool(config, host, port, timeout, password);
    }
}
