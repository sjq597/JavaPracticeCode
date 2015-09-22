### 用spring实现一个论坛基本功能

#### 运行环境
Linux：Ubun 14.04 64bit
IDE：IntelliJ IDEA 14.03
JDK：1.7.40
MySQL：5.5.44
Tomcat：7.0.47
​Maven：3.0.5

#### 展现层

使用`Spring-MVC`,具体步骤如下：
先配置web.xml文件，在`webapp`的`WEB-INF`目录下
```xml
<web-app version="2.5"
         xmlns="http://java.sun.com/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://java.sun.com/xml/ns/javaee
         http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

    <!--从类的路径下加载Spring的配置文件，classpath管架子特指类路径-->
    <!--多个文件用空格或者逗号可以隔开，建议使用逗号-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>

    <!--负责自动Spring容器的监听器，它将引用上面定义的上下文恩参数获得配置文件地址-->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!--Spring MVC的主控Servlet-->
    <servlet>
        <servlet-name>baobaotao</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <load-on-startup>2</load-on-startup>
    </servlet>
    <!--Spring MVC处理的URL-->
    <servlet-mapping>
        <servlet-name>baobaotao</servlet-name>
        <url-pattern>*.html</url-pattern>
    </servlet-mapping>

  <display-name>Archetype Created Web Application</display-name>
</web-app>

```

配置文件
```xml
<servlet>
    <servlet-name>baobaotao</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <load-on-startup>2</load-on-startup>
</servlet>
```
虽然这个servlet名字可以随意取，但是一般有个约定，就是还必须有一个<servlet名>-servlet.xml的配置文件，即在同目录下
还需要有一个`baobaotao-servlet.xml`的Spring-mvc配置文件。不过不需要再定义在web.xml文件中，Spring MVC会自动将这些文件
加载。

`<url-pattern>*.html</url-pattern>`指定拦截*.html的文件，一般喜欢使用*.do和*.action的后缀，但是使用.html后缀有几个好处。
第一：用户不能根据url判断我们使用的是什么技术
第二：*.html是静态网页后缀，可以骗过搜索引擎，增加被收录的概率
同时对于那些真正的静态网页可以使用*.htm加以区分

Spring MVC
这个东西主要负责控制业务逻辑，即后台代码的逻辑页面跳转等控制，我们需要写一个Controller来进行控制
先写一个`LoginController`
```java
@Controller //标注成为一个Spring MVC的Controller 处理http请求
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
```
**NOTE:** `@Controller`标注的类也是一个`Bean`，所以在Controller里进行注入，控制器中可以映射多个不同的http请求路径，通过
`@RequestMapping`指定路径。其中请求的参数会自动的绑定到响应方法的入参中，这个绑定是按照名字来绑定的，请求响应方法可以返回一个
`ModelAndView`或者字符串，然后`Spring MVC`会自动解析做相应跳转。并且`ModelAndView`对象不仅包括了视图信息，也包含了渲染所需模型数据

#### Spring MVC配置文件
光写一个`Controller`还不够，还需要在`springbbs-servlet.xml`文件中申明我们写的控制器，扫描web路径，指定`Spring MVC`的
视图解析器，代码如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:p="http://www.springframework.org/schema/p"
       xmlns:contex="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <!--1 扫描web包，应用Spring注解-->
    <contex:component-scan base-package="com.springbbs.controller"/>

    <!--2 配置视图解析器，将ModelAndView及字符串解析为具体页面-->
    <bean class="org.springframework.web.servlet.view.InternalResourceViewResolver"
          p:viewClass="org.springframework.web.servlet.view.JstlView"
          p:prefix="/WEB-INF/jsp/"
          p:suffix=".jsp"/>
</beans>
```
Spring MVC如何将视图逻辑名解析成具体视图？上面的bean即定义解析规则，可以有多种选择，这里使用的是`InternalResourceViewResolver`
它的工作方式就是为视图逻辑名添加前后缀的方式进行解析。举个例子，逻辑名为`login`,对应的解析为`/WEB-INF/jsp/login.jsp`。

### JSP视图页面
两个页面分别为登陆页面`login.jsp`以及`main.jsp`。

* login.jsp
```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: junqiangshen
  Date: 15-9-16
  Time: 下午11:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Spring样例演示论坛登陆界面</title>
</head>
    <body>
        <c:if test="${!empty error}">
            <front color="red"><c:out value="${error}"/></front>
        </c:if>

        <form action="<c:url value="/loginCheck.html"/>" method="post">
            用户名：
            <input type="text" name="userName"/>
            <br>
            密码：
            <input type="password" name="password"/>
            <br>
            <input type="submit" value="登陆"/>
            <input type="reset" value="重置"/>
        </form>
    </body>
</html>
```

登陆成功界面
```jsp
<%--
  Created by IntelliJ IDEA.
  User: junqiangshen
  Date: 15-9-16
  Time: 下午11:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1">
    <title>Spring 测试论坛</title>
</head>
<body>
    ${user.userName},欢迎进入Spring 样例论坛,您当前积分为${user.credits};
</body>
</html>
```