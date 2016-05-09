package com.potato.api.framework.jdbc.dao;


import com.potato.api.framework.jdbc.paging.Paging;

import java.util.List;
import java.util.Map;

/**
 * 数据访问器接口
 *
 * @author huangyong
 * @since 1.0.0
 */
public interface DataAccessor {

    <T> T selectOne(String statementId);

    <T> T selectOne(String statementId, Object sqlParam);

    // ----------------------------------------------------------------------------------------------------

    <T> List<T> selectList(String statementId);

    <T> List<T> selectList(String statementId, Object sqlParam);

    // ----------------------------------------------------------------------------------------------------

    <K, V> Map<K, V> selectMap(String statementId, String mapKey);

    <K, V> Map<K, V> selectMap(String statementId, Object sqlParam, String mapKey);

    // ----------------------------------------------------------------------------------------------------

    <T> Paging<T> selectPaging(String statementId, int pageNumber, int pageSize);

    <T> Paging<T> selectPaging(String statementId, Object sqlParam, int pageNumber, int pageSize);

    // ----------------------------------------------------------------------------------------------------

    int insert(String statementId);

    int insert(String statementId, Object sqlParam);

    // ----------------------------------------------------------------------------------------------------

    int update(String statementId);

    int update(String statementId, Object sqlParam);

    // ----------------------------------------------------------------------------------------------------

    int delete(String statementId);

    int delete(String statementId, Object sqlParam);
}
