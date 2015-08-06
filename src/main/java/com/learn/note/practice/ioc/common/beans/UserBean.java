package com.learn.note.practice.ioc.common.beans;

/**
 * @author junqiangshen
 * @version v1.0.
 * @Time Created on 15-8-6.
 */
public class UserBean {

    private String userName;
    private String userPassword;

    public UserBean() {
    }

    public UserBean(String userName, String userPassword) {
        this.userName = userName;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "userName='" + userName + '\'' +
                ", userPassword='" + userPassword + '\'' +
                '}';
    }
}
