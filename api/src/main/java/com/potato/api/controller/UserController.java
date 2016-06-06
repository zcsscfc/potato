package com.potato.api.controller;

import com.potato.api.Param.LoginParam;
import com.potato.api.Param.UserRegParam;
import com.potato.api.framework.bean.Response;
import com.potato.api.framework.security.IgnoreSecurity;
import com.potato.api.model.ServiceResult;
import com.potato.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangcs on 2016/6/3.
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @IgnoreSecurity
    @RequestMapping(value = "/reg", method = RequestMethod.POST)
    public Response reg(@RequestBody UserRegParam userRegParam) {

        ServiceResult serviceResult = userService.userReg(userRegParam);
        if (serviceResult.isSuccess()) {
            return new Response().success();
        }
        return new Response().failure(serviceResult.getMessage());
    }

    @IgnoreSecurity
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Response login(@RequestBody LoginParam loginParam) {
        ServiceResult serviceResult=userService.userLogin(loginParam);
        if(serviceResult.isSuccess()){
            return new Response().success(serviceResult.getData());
        }else {
            return new Response().failure(serviceResult.getMessage());
        }
    }
}
