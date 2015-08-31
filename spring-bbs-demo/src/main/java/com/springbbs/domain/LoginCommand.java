package com.springbbs.domain;

import java.io.Serializable;

/**
 * @Description JavaPracticeCode
 * Created by junqiangshen on 15-8-31.
 */
public class LoginCommand implements Serializable{
    private static final long serialVersionUID = 1556475033994145521L;

    private String userName;
    private String password;

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

    @Override
    public String toString() {
        return "LoginCommand{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
