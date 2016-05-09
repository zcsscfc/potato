package com.potato.api.framework.jdbc.paging;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.RowBounds;

import java.sql.Connection;

/**
 * 基于 MySQL 的 MyBatis 分页拦截器实现
 *
 * @author huangyong
 * @since 1.0.0
 */
@Intercepts(
    @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})
)
public class MySQLPagingInterceptor extends AbstractPagingInterceptor {

    @Override
    public String getPagingSql(String sql, RowBounds rowBounds) {
        int offset = rowBounds.getOffset();
        int limit = rowBounds.getLimit();
        if (limit == 0) {
            return sql;
        }
        return sql + String.format(" LIMIT %d, %d", offset, limit);
    }

    @Override
    public String getCountSql(String sql) {
        int index = sql.indexOf("LIMIT");
        if (index != -1) {
            sql = sql.substring(0, index).trim();
        }
        return "SELECT count(0) FROM (" + sql + ") temp";
    }
}
