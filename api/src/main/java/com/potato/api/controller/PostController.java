package com.potato.api.controller;

import com.potato.api.entity.PostM;
import com.potato.api.framework.bean.Response;
import com.potato.api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangcs on 2016/5/9.
 */
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping(value = "post", method = RequestMethod.GET)
    public Response post() {
        List<PostM> postMs=postService.getPostList();
        return new Response().success(postMs);
    }
}
