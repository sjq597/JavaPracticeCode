package com.springbbs.service;

import com.springbbs.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)     // 基于JUnit4的Spring测试框架
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})     //启动Spring容器
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testHasMatchUser() throws Exception {
        boolean b1 = userService.hasMatchUser("admin", "123456");
        boolean b2 = userService.hasMatchUser("admin", "1111");

        Assert.assertEquals(true, b1);
        Assert.assertEquals(false, b2);
    }

    @Test
    public void testFindUserByUserName() throws Exception {
        User user = userService.findUserByUserName("admin");
        Assert.assertEquals("admin", user.getUserName());
    }

    @Test
    public void testLoginSucess() throws Exception {
        User user = userService.findUserByUserName("admin");
        user.setLastIp("127.0.0.1");
        user.setLastVistit(new Date());
        userService.loginSucess(user);
    }
}