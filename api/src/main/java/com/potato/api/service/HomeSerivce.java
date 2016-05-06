package com.potato.api.service;

import com.potato.api.model.Person;
import com.potato.api.model.PostM;
import org.apache.ibatis.session.*;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by zhangcs on 2016/4/22.
 */
@Service
public class HomeSerivce {

    public Person getPerson() throws Exception {

        Person p = new Person();
        p.setFirstName("victor");
        p.setLastName("zhang");
        return p;
    }
}
