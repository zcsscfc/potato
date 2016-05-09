package com.potato.api.service;

import com.potato.api.entity.PostD;
import com.potato.api.entity.PostM;
import com.potato.api.framework.jdbc.dao.DataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by zhangcs on 2016/5/9.
 */
@Service
public class PostService {

    @Autowired
    private DataAccessor dataAccessor;

    public List<PostM> getPostMList() {
        return dataAccessor.selectList("selectPostM");
    }


    public PostM getPostMByPostId(BigInteger postId) {
        return dataAccessor.selectOne("selectPostMPostId", postId);
    }

    public PostD getPostDByPostId(BigInteger postId) {
        return dataAccessor.selectOne("selectPostDPostId",postId);
    }
}
