package com.springbbs.controller;

import com.springbbs.domain.LoginCommand;
import com.springbbs.domain.User;
import com.springbbs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @Description JavaPracticeCode
 * Created by junqiangshen on 15-8-31.
 */
@Controller //标注成为一个Spring MVC的Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 负责处理/index.html的请求
     */
    @RequestMapping(value = "/index.html")
    public String loginPage() {
        return "login";
    }

    /**
     * 登陆验证视图
     * @param loginCommand 扥路信息
     */
    @RequestMapping(value = "/loginCheck.html")
    public ModelAndView loginCheck(HttpServletRequest request, LoginCommand loginCommand) {
        boolean isValidUser = userService.hasMatchUser(
                loginCommand.getUserName(), loginCommand.getPassword());

        if (!isValidUser) {
            return new ModelAndView("login", "error", "用户名或密码错误");
        } else {
            User user = userService.findUserByUserName(loginCommand.getUserName());
            user.setLastIp(request.getLocalAddr());
            user.setLastVistit(new Date());
            userService.loginSucess(user);

            request.getSession().setAttribute("user", user);
            return new ModelAndView("main");
        }
    }
}
