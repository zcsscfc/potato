package com.potato.api.controller;

import com.potato.api.framework.Response;
import com.potato.api.model.Person;
import com.potato.api.service.HomeSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangcs on 2016/4/14.
 */
@Controller
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private HomeSerivce homeSerivce;

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public @ResponseBody Response index() throws Exception {

        Person p=homeSerivce.getPerson();
        return new Response().success(p);
    }
}
