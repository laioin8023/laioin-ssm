package com.ssm.service.test.user;

import com.ssm.service.entry.User;
import com.ssm.service.services.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:mybatis-spring.xml"})
public class TestUser {

    @Autowired
    private IUserService userService;

    @Test
    public void findById(){
        User dnUser = userService.queryById(1);
        System.out.println(dnUser);
    }
}
