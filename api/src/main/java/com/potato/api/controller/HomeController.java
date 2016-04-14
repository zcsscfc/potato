package com.potato.api.controller;

import com.potato.api.framework.Response;
import com.potato.api.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangcs on 2016/4/14.
 */
@Controller
public class HomeController {

    @RequestMapping(value = "/Home/index", method = RequestMethod.GET)
    public
    @ResponseBody
    Response index() {
        Person p = new Person();
        p.setFirstName("victor");
        p.setLastName("zhang");
        return new Response().success(p);
    }
}
