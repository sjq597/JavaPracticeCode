package com.springbbs.service;

import com.springbbs.dao.LoginLogDao;
import com.springbbs.dao.UserDao;
import com.springbbs.domain.LoginLog;
import com.springbbs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 业务层，负责调用DAO层
 * Created by junqiangshen on 15-8-27.
 */
@Service    // 将UserService标注为一个服务层的Bean
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginLogDao loginLogDao;

    public boolean hasMatchUser(String userName, String password) {
        int matchCount = userDao.getMatchCount(userName, password);

        return matchCount > 0;
    }

    public User findUserByUserName(String userName) {
        return userDao.findUserByUserName(userName);
    }

    public void loginSucess(User user) {
        user.setCredits(5+ user.getCredits());
        LoginLog loginLog = new LoginLog();
        loginLog.setUserId(user.getUserId());
        loginLog.setIp(user.getLastIp());
        loginLog.setLoginDate(user.getLastVistit());

        userDao.updateLoginInfo(user);
        loginLogDao.insertLoginLog(loginLog);
    }
}
