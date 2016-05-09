package com.potato.api.controller;

import com.potato.api.entity.PostD;
import com.potato.api.entity.PostM;
import com.potato.api.framework.bean.Response;
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

    @RequestMapping(value = "postm", method = RequestMethod.GET)
    public Response postm() {
        List<PostM> postMs=postService.getPostMList();
        return new Response().success(postMs);
    }

    @RequestMapping(value = "postm/{postId}",method = RequestMethod.GET)
    public Response postm(@PathVariable BigInteger postId){
        PostM postM=postService.getPostMByPostId(postId);
        return new Response().success(postM);
    }

    @RequestMapping(value = "postd/{postId}",method = RequestMethod.GET)
    public Response postd(@PathVariable BigInteger postId){
        PostD postD=postService.getPostDByPostId(postId);
        return new Response().success(postD);
    }

}
