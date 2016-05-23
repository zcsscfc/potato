package com.potato.api.service;

import com.potato.api.Param.PostListParam;
import com.potato.api.Param.PostMParam;
import com.potato.api.entity.PostDEntity;
import com.potato.api.entity.PostMEntity;
import com.potato.api.framework.jdbc.dao.DataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangcs on 2016/5/9.
 */
@Service
public class PostService {

    @Autowired
    private DataAccessor dataAccessor;

    public List<PostMEntity> getPostMList(PostMParam postMParam) {
        return dataAccessor.selectList("selectPostM", postMParam);
    }


    public PostMEntity getPostMByPostId(BigInteger postId) {
        return dataAccessor.selectOne("selectPostMPostId", postId);
    }

    public PostDEntity getPostDByPostId(BigInteger postId) {
        return dataAccessor.selectOne("selectPostDPostId", postId);
    }

    public List<BigInteger> getPostIdsBySubjectId(BigInteger subjectId) {
        return dataAccessor.selectList("selectPostSubjectSubjectId", subjectId);
    }
}
