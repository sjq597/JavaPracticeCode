package com.springbbs.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description User实体类
 * Created by junqiangshen on 15-8-26.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 3628238945647868396L;

    private int userId;
    private String userName;
    private String password;
    private int credits;
    private String lastIp;
    private Date lastVistit;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getLastIp() {
        return lastIp;
    }

    public void setLastIp(String lastIp) {
        this.lastIp = lastIp;
    }

    public Date getLastVistit() {
        return lastVistit;
    }

    public void setLastVistit(Date lastVistit) {
        this.lastVistit = lastVistit;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", credits=" + credits +
                ", lastIp='" + lastIp + '\'' +
                ", lastVistit=" + lastVistit +
                '}';
    }
}
