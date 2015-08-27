### 用spring实现一个论坛基本功能

#### 运行环境
Linux：Ubun 14.04 64bit
IDE：IntelliJ IDEA 14.03
JDK：1.7.40
MySQL：5.5.44
Tomcat：7.0.47
​Maven：3.0.5

#### 具体步骤
* 新建一个Webapp工程，名字就叫spring-bbs-demo
项目的目录结构如下：
```
├── pom.xml
├── README.MD
├── spring-bbs-demo.iml
└── src
    └── main
        ├── resources
        └── webapp
            ├── index.jsp
            └── WEB-INF
                └── web.xml

```
* 在mysql里创建两张表，数据库名为`sampledb`
```sql
DROP DATABASE IF EXISTS sampledb;
CREATE DATABASE sampledb DEFAULT CHARACTER SET utf8mb4;
USE sampledb;

-- 创建用户表
CREATE TABLE t_user (
  user_id INT AUTO_INCREMENT NOT NULL COMMENT '用户id',
  user_name VARCHAR(30) NOT NULL DEFAULT '' COMMENT '用户名',
  credits INT NOT NULL DEFAULT 0 COMMENT '论坛积分',
  password VARCHAR(32) NOT NULL DEFAULT ''COMMENT '用户密码',
  last_visit TIMESTAMP NOT NULL DEFAULT 0 COMMENT '最后访问时间',
  last_ip VARCHAR(23) NOT NULL DEFAULT '0.0.0.0' COMMENT '最后访问ip',
  PRIMARY KEY (user_id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '用户表';

-- 创建用户登陆日志表
CREATE TABLE t_login_log (
  login_log_id INT AUTO_INCREMENT NOT NULL COMMENT '日志id',
  user_id INT NOT NULL DEFAULT 0 COMMENT '用户id',
  ip VARCHAR(23) NOT NULL DEFAULT '0.0.0.0' COMMENT '访问ip',
  login_datetime TIMESTAMP NOT NULL DEFAULT 0 COMMENT '访问时间',
  PRIMARY KEY (login_log_id)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT '用户登陆日志表';

INSERT INTO t_user (user_name,password) VALUES ('admin','123456');
```

#### 最终项目目录结构

```
├── pom.xml
├── README.MD
├── spring-bbs-demo.iml
└── src
    └── main
        ├── java
        │   └── com
        │       └── springbbs
        │           ├── dao
        │           ├── domain
        │           └── service
        ├── resources
        │   └── sql
        │       └── user_table_init.sql
        └── webapp
            ├── index.jsp
            └── WEB-INF
                └── web.xml

```
**NOTE:**`java`为代码根目录，如果不是，可以在`java`文件夹上右击`->Mark Directory As->Resources Root`。  
同理，`resources`为资源根目录，`webapp`为`web`根目录,`test`目录下的`java`文件夹为`TestR Resources Root`目录

#### 文件及文件夹相关函数

文件 | 解释
-----| --------------
applicationContext.xml | Spring容器配置文件
domain | 领域对象(实体类)存放文件夹,往往拥有对应的数据库表，一般要实现`Serializable`接口，以便序列化
dao | 访问实体类的接口，一般一个实体类都会有一个dao与之对应，里面有一些对应的方法
jdbcTemplate | Spring对jdbc的简单封装，可以轻松完成大部分的数据库操作而不必频繁的重复对数据库的打开，获取连接，查询，关闭等操作
jdbcTemplate#query() | `query(String sql,Object[] args,RowCallbackHandler rch)`.第一个参数不解释了;第二个是占位符(?)对应的参数数据;第三个是查询结果的处理回调接口，该回调接口有一个方法`processRow(ResultSet resultSet)`负责将查询结果从`ResultSet`装载到类似于实例类对象的实例中。一般都是使用匿名内部类的方式来调用`RowCallbackHandler`接口

**NOTE:**在DAO文件中写的`sql`语句比较长，多行衔接要注意空格，不然会连在一起，具体小技巧是每一行最后加个空格。

### Spring中配置
以上两个DAO实现类中并没有打开/和释放`Connection`，到底如何访问数据库呢？答案是`jdbc`被`spring`封装起来了，`JdbcTemplate`需要一个`dataSource`，从数据源中获取或返回连接。所以在`UserDao`和`LoginLogDao`中都提供了一个带`@Autowired`注解的`jdbcTemplate`对象。
所以我们需要先申明一个数据源，然后再定义一个`JdbcTemplate Bean`，通过`Spring`的容器上下文自动绑定机制进行`Bean`的注入。配置文件在`Resources`文件夹下，名字叫`applicationContext.xml`，内容如下：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <!-- 1 加载配置文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" ignore-unresolvable="true"/>
    <!--2 扫描类包，将标注Spring注解的类自动转化成Bean，同时完成Bean的注入-->
    <context:component-scan base-package="com.springbbs.dao"/>

    <!--3 定义一个使用DBCP实现的数据源-->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close"
          <!--如果加载不了配置文件，此处手动改为对应的值-->
          p:driverClassName="${jdbc.driver}"
          p:url="${jdbc.url}"
          p:username="${jdbc.username}"
          p:password="${jdbc.password}"/>

    <!--4 定义jdbc模板Bean-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate"
          p:dataSource-ref="dataSource"/>
</beans>
```
##### 配置说明
`jdbc.properties`文件为mysql配置文件，内容如下：
> 
jdbc.driver=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/sampledb
jdbc.username=root
jdbc.password=root0724  

在配置文件中加载mysql配置文件，在定义数据源时需要使用到配置文件中的信息

#### 业务层
在这个登陆实例中，仅有一个业务类，即`UserService`，它负责将持久层的`UserDao`和`LoginDao`组织起来完成用户/密码认证、登陆日志记录等操作。`loginSucess`将两个`DAO`组织起来共同完成一个事务性工作：更新两个表，虽然没有事务操作的影子，但是通过`Spring`事务配置即可。
关于几个注解的解释:
注解 | 作用
-----|------
`@Service` | 将`UserService`标注为一个服务层的`Bean`
`@Autowired` | 注入`userDao`和`loginLogDao`这两个`DAO`层的`Bean`

#### 在Spring中装配Service
我们必须告诉`Spring`哪些业务类需要工作于事务环境下及事务的规则等内容，以便`Spring`根据这些信息自动为目标业务类添加事务管理功能。对`applicationContext.xml`更改：
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!--1 引入aop及tx命名空间所对应的Schema文件-->
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:p="http://www.springframework.org/schema/p" xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd">

    <!-- 加载配置文件 -->
    <context:property-placeholder location="classpath:jdbc.properties" ignore-unresolvable="true"/>

    <!--扫描类包，将标注Spring注解的类自动转化成Bean，同时完成Bean的注入-->
    <context:component-scan base-package="com.springbbs.dao"/>

    <!--扫描service类包，应用Spring的注解配置-->
    <context:component-scan base-package="com.springbbs.service"/>

    <!--配置事务管理器-->
    <bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
          p:dataSource-ref="dataSource"/>

    <!--通过AOP配置提供事务增强，让service包下所有的Bean的所有方法拥有事务-->
    <aop:config proxy-target-class="true">
        <aop:pointcut id="serviceMethod" expression="execution(* com.springbbs.service..*(..))"/>
        <aop:advisor pointcut-ref="serviceMethod" advice-ref="txAdvice"/>
    </aop:config>

    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>
    <!--定义一个使用DBCP实现的数据源-->
    <bean id="dataSource" class="org.apache.commons.dbcp.BasicDataSource"
          destroy-method="close"
          <!--如果加载不了配置文件，此处手动改为对应的值-->
          p:driverClassName="${jdbc.driver}"
          p:url="${jdbc.url}"
          p:username="${jdbc.username}"
          p:password="${jdbc.password}"/>

    <!--定义jdbc模板Bean-->
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate"
          p:dataSource-ref="dataSource"/>
</beans>
```
**NOTE:** 配置文件解释
* 命名空间
```xml
<!--添加`aop`和`tx`命名空间，这样就可以在`xml`配置文件中使用这两个空间下的标签了-->
xmlns:aop="http://www.springframework.org/schema/aop" xmlns:tx="http://www.springframework.org/schema/tx"
```
 * Service注解扫描目录
 ```xml
 <context:component-scan base-package="com.springbbs.service"/>
 ```
* 事务管理器
```xml
<bean id="transactionManager"
          class="org.springframework.jdbc.datasource.DataSourceTransactionManager"
          p:dataSource-ref="dataSource"/>
```
* aop事务增强
关于aop(面向切片的编程后面会细讲，目前可以理解为拦截和代理即可),`execution(* com.springbbs.service.. *(..))`的意思就是任意返回值的service包及子包下的任何参数的任何方法都切入进行事务管理。
```xml
    <!--通过AOP配置提供事务增强，让service包下所有的Bean的所有方法拥有事务-->
    <aop:config proxy-target-class="true">
        <aop:pointcut id="serviceMethod" expression="execution(* com.springbbs.service..*(..))"/>
        <aop:advisor pointcut-ref="serviceMethod" advice-ref="txAdvice"/>
    </aop:config>

    <tx:advice id="txAdvice" transaction-manager="transactionManager">
        <tx:attributes>
            <tx:method name="*"/>
        </tx:attributes>
    </tx:advice>
```

#### 单元测试
依赖
```xml
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-test</artifactId>
    <scope>test</scope>
</dependency>
```
创建单元测试的方法：光标放在在需要测试的类名字上(本例中是`UserService`)，按住`Ctrl+Shift+T`，`Junit4`单元测试，勾选3个方法即可。
测试类`UserServiceTest`代码如下：
```java
package com.springbbs.service;

import com.springbbs.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)     // 基于JUnit4的Spring测试框架
@ContextConfiguration(locations = {"classpath:applicationContext.xml"})     //启动Spring容器
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testHasMatchUser() throws Exception {
        boolean b1 = userService.hasMatchUser("admin", "123456");
        boolean b2 = userService.hasMatchUser("admin", "1111");

        Assert.assertEquals(true, b1);
        Assert.assertEquals(false, b2);
    }

    @Test
    public void testFindUserByUserName() throws Exception {
        User user = userService.findUserByUserName("admin");
        Assert.assertEquals("admin", user.getUserName());
    }

    @Test
    public void testLoginSucess() throws Exception {
        User user = userService.findUserByUserName("admin");
        user.setLastIp("127.0.0.1");
        user.setLastVistit(new Date());
        userService.loginSucess(user);
    }
}
```
**NOTE:**`Spring`测试框架可以和`Junit4`整合，通过`Junit4`的`@RunWith`注解指定`SpringJUnit4ClassRunner.class`的测试运行器，该运行器是Spring提供的，可以将`Spring`容器和`Junit4`测试框架整合。`@ContextConfiguration`也是`Spring`提供的注解，它用于制定`Spring`的配置文件。
这里需要注意的是，因为我的resources文件夹被指定为资源根目录，所以使用的 `classpath`路径来加载，即最终在类的根目录下。
在`UserServiceTest`上右键->`Run UserServiceTest`即可运行单元测试。最后取数据库中查询登陆日志可以发现成功插入一条记录。