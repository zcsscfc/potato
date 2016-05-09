package com.potato.api.service;

import com.potato.api.model.Person;
import org.springframework.stereotype.Service;

/**
 * Created by zhangcs on 2016/4/22.
 */
@Service
public class HomeSerivce {

    public Person getPerson() {

        Person p = new Person();
        p.setFirstName("victor");
        p.setLastName("zhang");
        return p;
    }
}
