package com.potato.api.controller;

import com.potato.api.Param.PostMParam;
import com.potato.api.entity.PostDEntity;
import com.potato.api.entity.PostMEntity;
import com.potato.api.framework.bean.Response;
import com.potato.api.framework.security.IgnoreSecurity;
import com.potato.api.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by zhangcs on 2016/5/9.
 */
@RestController
public class PostController {

    @Autowired
    private PostService postService;

    @IgnoreSecurity
    @RequestMapping(value = "postm", method = RequestMethod.POST)
    public Response postm(@RequestBody PostMParam postMParam) {
        List<PostMEntity> postMs = postService.getPostMList(postMParam);
        return new Response().success(postMs);
    }

    @IgnoreSecurity
    @RequestMapping(value = "postm/{postId}", method = RequestMethod.GET)
    public Response postm(@PathVariable BigInteger postId) {
        PostMEntity postM = postService.getPostMByPostId(postId);
        return new Response().success(postM);
    }

    @IgnoreSecurity
    @RequestMapping(value = "postd/{postId}", method = RequestMethod.GET)
    public Response postd(@PathVariable BigInteger postId) {
        PostDEntity postD = postService.getPostDByPostId(postId);
        return new Response().success(postD);
    }

}
