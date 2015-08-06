#### Spring学习笔记

#### 第一课，依赖注入，也称IOC
java里反射是个非常强大的东西，Spring之所以能够实现也多亏java的反射机制
先看一个简单的例子，来看看Spring是如何工作的。

#### 配置
* 新建三个包`ioc.common.beans`,`resources`，在`beans`目录下新建一个`UserBean`类，内容如下
```java
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
```

在`resources`目录下新建一个`config.xml`文件，内容如下
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
        "http://www.springframework.org/dtd/spring-beans.dtd">
<beans>
    <bean id="userBean" class="com.learn.note.practice.ioc.common.beans.UserBean">
        <constructor-arg index="0" value="Fuck" />
        <constructor-arg index="1" value="hehehe"/>
    </bean>
</beans>
```
然后创建一个单元测试`UserBeanTest.java`
```java
package com.learn.note.practice.ioc.common.beans;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.*;

public class UserBeanTest {

    @Test
    public void testUserBeanSpring() {
        ApplicationContext applicationContext =
                new ClassPathXmlApplicationContext(new String[] {"config.xml"});
        // 取得一个实例
        UserBean user = (UserBean) applicationContext.getBean("userBean");
        System.out.println(user);
    }
}

```
注意最终的目录结构是这样的
```
.
├── JavaPracticeCode.iml
├── pom.xml
├── README.MD
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── learn
│   │   │           └── note
│   │   │               └── practice
│   │   │                   └── ioc
│   │   │                       ├── common
│   │   │                       │   └── beans
│   │   │                       │       └── UserBean.java
│   │   │                       └── README.MD
│   │   └── resources
│   │       └── config.xml
│   └── test
│       └── java
│           └── com
│               └── learn
│                   └── note
│                       └── practice
│                           └── ioc
│                               └── common
│                                   └── beans
│                                       └── UserBeanTest.java

```

**NOTE:**关于在idea中把文件夹标注为制定文件夹根目录的方法，在文件夹上`右键->Mark Directory As`
具体有四个选项，分别是
* `Sources Root` 源代码java文件根目录，本例中`src/main/java`文件夹就是默认的
* `Test Sources Root` 测试代码根目录，本例中`test/java`目录是
* `Resources Root`资源文件根目录，本例中的`resources`目录
* `Excluded`编译的类`.class`文件根目录，本例中是`target`，对于所有`java`目录下的资源，编译时都会拷贝到对应的`Excluded`文件夹下

我们可以在`JavaPracticeCode.iml`文件中看到被标记的情况
```xml
<?xml version="1.0" encoding="UTF-8"?>
<module org.jetbrains.idea.maven.project.MavenProjectsManager.isMavenModule="true" type="JAVA_MODULE" version="4">
  <component name="NewModuleRootManager" inherit-compiler-output="false">
    <output url="file://$MODULE_DIR$/target/classes" />
    <output-test url="file://$MODULE_DIR$/target/test-classes" />
    <content url="file://$MODULE_DIR$">
      <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />
      <sourceFolder url="file://$MODULE_DIR$/src/test/java" isTestSource="true" />
      <sourceFolder url="file://$MODULE_DIR$/src/main/resources" type="java-resource" />
      <excludeFolder url="file://$MODULE_DIR$/target" />
    </content>
    <orderEntry type="inheritedJdk" />
    <orderEntry type="sourceFolder" forTests="false" />
    <orderEntry type="library" scope="TEST" name="Maven: junit:junit:3.8.1" level="project" />
    <orderEntry type="library" scope="TEST" name="Maven: junit:junit:4.10" level="project" />
    <orderEntry type="library" scope="TEST" name="Maven: org.hamcrest:hamcrest-core:1.1" level="project" />
    <orderEntry type="library" name="Maven: org.slf4j:slf4j-api:1.7.5" level="project" />
    <orderEntry type="library" name="Maven: ch.qos.logback:logback-classic:1.0.13" level="project" />
    <orderEntry type="library" name="Maven: ch.qos.logback:logback-core:1.0.13" level="project" />
    <orderEntry type="library" name="Maven: org.springframework:spring-context:4.0.0.RELEASE" level="project" />
    <orderEntry type="library" name="Maven: org.springframework:spring-aop:4.0.0.RELEASE" level="project" />
    <orderEntry type="library" name="Maven: aopalliance:aopalliance:1.0" level="project" />
    <orderEntry type="library" name="Maven: org.springframework:spring-beans:4.0.0.RELEASE" level="project" />
    <orderEntry type="library" name="Maven: org.springframework:spring-core:4.0.0.RELEASE" level="project" />
    <orderEntry type="library" name="Maven: commons-logging:commons-logging:1.1.1" level="project" />
    <orderEntry type="library" name="Maven: org.springframework:spring-expression:4.0.0.RELEASE" level="project" />
  </component>
</module>
```
注意这几行
```xml
<content url="file://$MODULE_DIR$">
    <sourceFolder url="file://$MODULE_DIR$/src/main/java" isTestSource="false" />
    <sourceFolder url="file://$MODULE_DIR$/src/test/java" isTestSource="true" />
    <sourceFolder url="file://$MODULE_DIR$/src/main/resources" type="java-resource" />
    <excludeFolder url="file://$MODULE_DIR$/target" />
</content>
```
以后如果发现单元测试无法创建或者运行，或者资源文件加载不上，自行加标记。
单元测试的输出结果如下：
> 00:20:20.204 [main] INFO  c.l.n.p.ioc.common.beans.UserBean - UserBean{userName='Fuck', userPassword='hehehe'}