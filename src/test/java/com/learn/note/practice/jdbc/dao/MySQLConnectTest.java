package com.learn.note.practice.jdbc.dao;

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
}