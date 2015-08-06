package com.learn.note.practice.ioc.common.beans;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class UserBeanTest {
    private static Logger logger = LoggerFactory.getLogger(UserBean.class);

    @Test
    public void testUserBeanSpring() {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext(new String[] {"config.xml"});
        // 取得一个实例
        UserBean user = (UserBean) applicationContext.getBean("userBean");
        logger.info(user.toString());
    }

}