package com.learn.note.practice.jdbc.dao;

/**
 * @author junqiangshen
 * @version v1.0.
 * @Time Created on 15-8-3.
 */
public class DBConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf8";
    public static final String USERNAME = "root";
    public static final String PASSWORD = "root0724";
    public static final String GET_ALL_USER = "select * from account";
    public static final String INSERT_ACCOUNT = "insert into account(user_name,password,role_type)values(?,?,?)";
    public static final String GET_ACCOUNT_BY_NAME = "select * from account where user_name =?";
}
