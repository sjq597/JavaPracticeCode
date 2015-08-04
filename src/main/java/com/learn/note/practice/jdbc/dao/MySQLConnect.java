package com.learn.note.practice.jdbc.dao;

import com.google.common.collect.Lists;
import com.learn.note.practice.jdbc.bean.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Collections;
import java.util.List;

/**
 * @author junqiangshen
 * @version v1.0.
 * @Time Created on 15-8-3.
 */
public class MySQLConnect {
    private static Logger logger = LoggerFactory.getLogger(MySQLConnect.class);

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException e) {
            logger.info("数据库驱动加载失败！", e);
        } catch (InstantiationException e) {
            logger.info("试图通过newInstance()方法实例化异常", e);
        } catch (IllegalAccessException e) {
            logger.info("安全权限异常", e);
        }
    }

    public List<Account> getAccountList() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(DBConfig.GET_ALL_USER);
            List result = Lists.newArrayList();

            while (resultSet.next()) {
                String account = resultSet.getString("user_name");
                String password = resultSet.getString("password");
                int roleType = resultSet.getInt("role_type");
                result.add(new Account(account, password, roleType));
            }
            statement.close();
            return result;

        } catch (SQLException e) {
            logger.info("获取数据库库连接或者执行sql语句或者获取数据错误", e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.info("关闭数据库连接异常", e);
                }
            }
        }
        return Collections.emptyList();
    }
}
