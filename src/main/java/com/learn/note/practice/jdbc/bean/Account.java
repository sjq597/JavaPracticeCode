package com.learn.note.practice.jdbc.bean;

import java.io.Serializable;

/**
 * @author junqiangshen
 * @version v1.0.
 * @Time Created on 15-8-3.
 */
public class Account implements Serializable {

    private static final long serialVersionUID = -2650762086865218778L;
    private String userName;
    private String password;
    private int roleType;

    public Account() {
    }

    public Account(String userName, String password, int roleType) {
        this.userName = userName;
        this.password = password;
        this.roleType = roleType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    @Override
    public String toString() {
        return "{ userName:" + userName + "," + "password:" + password
        + "roleType:" + roleType + "}";
    }
}
