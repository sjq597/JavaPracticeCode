package com.learn.note.practice.jdbc.dao;

import com.google.common.collect.Lists;
import com.learn.note.practice.jdbc.bean.Account;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.*;

public class MySQLConnectTest {
    private Logger logger = LoggerFactory.getLogger(MySQLConnectTest.class);

    @Test
    public void testGetAccountList() throws Exception {
        MySQLConnect mySQLConnect = new MySQLConnect();
        List<Account> accounts = mySQLConnect.getAccountList();
        for (Account account : accounts) {
            logger.info(account.toString());
        }
    }

    /**
     * 测试SQL注入
     */
    @Test
    public void testLoginValidate() throws Exception {
        List<Account> accounts = Lists.newArrayList();
        accounts.add(new Account("admin", "test"));
        accounts.add(new Account("lilei", "test"));
        accounts.add(new Account("hanmeimei", "test"));

        accounts.add(new Account("admin", "tes"));
        accounts.add(new Account("lilei", "test1"));
        accounts.add(new Account("hanmeimei", "text"));

        accounts.add(new Account("admin' or '1=1", "tes"));
        accounts.add(new Account("lilei' or '1=1", "tes1"));
        accounts.add(new Account("hanmeimei' or '1=1", "text"));

        accounts.add(new Account("admin';#", ""));  //#是注释符，这一行后面的都注销了
        accounts.add(new Account("lilei';#", ""));

        for (Account account : accounts) {
            if (MySQLConnect.loginValidate(account)) {
                logger.info("用户[{}]使用密码[{}]登陆成功!", account.getUserName(), account.getPassword());
            } else {
                logger.info("用户[{}]使用密码[{}]登陆失败!", account.getUserName(), account.getPassword());
            }
        }
    }
}