package com.potato.api.framework;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Created by zhangcs on 2016/4/22.
 */
public class DataAccess {
    @Autowired
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    public DataAccess(){
        try {
            SqlSession sqlSession=sqlSessionFactoryBean.getObject().openSession(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
