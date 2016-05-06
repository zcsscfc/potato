package com.potato.api.dao;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;


/**
 * Created by zhangcs on 2016/5/5.
 */
@Repository
public class TestDao {

    @Resource
    private SqlSessionTemplate sqlSession;

    public void test(){
        sqlSession.selectList("");
    }
}
