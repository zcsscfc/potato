package com.potato.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangcs on 2016/4/14.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/Home/index",method = RequestMethod.GET)
    public @ResponseBody
    String index(){
        return "hello world";
    }
}
