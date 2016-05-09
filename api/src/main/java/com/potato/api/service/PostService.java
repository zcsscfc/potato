package com.potato.api.service;

import com.potato.api.entity.PostM;
import com.potato.api.framework.jdbc.dao.DataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by zhangcs on 2016/5/9.
 */
@Service
public class PostService {

    @Autowired
    private DataAccessor dataAccessor;

    public List<PostM> getPostList(){
        return dataAccessor.selectList("selectPostM");
    }
}
