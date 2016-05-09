package com.potato.api.framework.jdbc;

import com.potato.api.framework.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 数据库连接上下文
 *
 * @author huangyong
 * @since 1.0.0
 */
public class ConnectionContext {

    private static Logger logger = LoggerFactory.getLogger(ConnectionContext.class);

    private static final String CONFIG = "config.properties";
    private static final String DRIVER = "jdbc.driver";
    private static final String URL = "jdbc.url";
    private static final String USERNAME = "jdbc.username";
    private static final String PASSWORD = "jdbc.password";
    private static final String MAX_TOTAL = "jdbc.max_total";
    private static final String MAX_IDLE = "jdbc.max_idle";
    private static final String MIN_IDLE = "jdbc.min_idle";
    private static final String TEST_ON_BORROW = "jdbc.test_on_borrow";
    private static final String TEST_ON_RETURN = "jdbc.test_on_return";

    private static ThreadLocal<Connection> connectionHolder = new ThreadLocal<>();

    private static BasicDataSource dataSource = new BasicDataSource();

    static {
        Properties config = PropsUtil.loadProps(CONFIG);
        dataSource.setDriverClassName(PropsUtil.getString(config, DRIVER));
        dataSource.setUrl(PropsUtil.getString(config, URL));
        dataSource.setUsername(PropsUtil.getString(config, USERNAME));
        dataSource.setPassword(PropsUtil.getString(config, PASSWORD));
        if (PropsUtil.containsKey(config, MAX_TOTAL)) {
            dataSource.setMaxTotal(PropsUtil.getInt(config, MAX_TOTAL));
        }
        if (PropsUtil.containsKey(config, MAX_IDLE)) {
            dataSource.setMaxIdle(PropsUtil.getInt(config, MAX_IDLE));
        }
        if (PropsUtil.containsKey(config, MIN_IDLE)) {
            dataSource.setMinIdle(PropsUtil.getInt(config, MIN_IDLE));
        }
        if (PropsUtil.containsKey(config, TEST_ON_BORROW)) {
            dataSource.setTestOnBorrow(PropsUtil.getBoolean(config, TEST_ON_BORROW));
        }
        if (PropsUtil.containsKey(config, TEST_ON_RETURN)) {
            dataSource.setTestOnReturn(PropsUtil.getBoolean(config, TEST_ON_RETURN));
        }
    }

    public static Connection connect() {
        Connection connection = connectionHolder.get();
        if (connection == null) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                logger.error("get connection failure", e);
                throw new RuntimeException(e);
            }
            if (connection != null) {
                connectionHolder.set(connection);
            }
        }
        return connection;
    }

    public static void release() {
        Connection connection = connectionHolder.get();
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                logger.error("release connection failure", e);
                throw new RuntimeException(e);
            }
            connectionHolder.remove();
        }
    }
}
