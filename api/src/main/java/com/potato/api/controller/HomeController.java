package com.potato.api.controller;

import com.potato.api.framework.bean.Response;
import com.potato.api.framework.security.IgnoreSecurity;
import com.potato.api.model.Person;
import com.potato.api.service.HomeSerivce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangcs on 2016/4/14.
 */
@RestController
@RequestMapping(value = "/home")
public class HomeController {

    @Autowired
    private HomeSerivce homeSerivce;

    @IgnoreSecurity
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public Response index() {

        Person p = homeSerivce.getPerson();
        return new Response().success(p);
    }
}
