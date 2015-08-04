### JDBC使用demo

需要使用到jdbc的mysql驱动，并且使用到了slf4j日志服务。
所有依赖的jar包通过maven管理
在pom.xml文件中配置如下
```xml
  <dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.10</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>5.1.21</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.5</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.0.13</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-core</artifactId>
        <version>1.0.13</version>
    </dependency>
    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>18.0</version>
    </dependency>
  </dependencies>
```
最后需要用的jar包如下
>
junit:4.10  
mysql-connector-java:5.1.21  
slf4j-api:1.7.5  
logback-classic:1.0.13  
logback-core:1.0.13  
guava:18.0

测试用例在test文件夹里`~/src/test/java/com/learn/note/practice/jdbc/dao/MySQLConnectTest.java`

#### SQL注入攻击
首先写一个登陆验证的函数`public static boolean loginValidate(Account account)`
```java
    /**
     * 登陆验证
     * @param account 需要被验证的账户
     * @return 登陆成功返回true
     *         登陆失败返回false
     */
    public static boolean loginValidate(Account account) {
        Connection connection = null;
        Statement statement = null;
        String sql = "select * from account where user_name='" + account.getUserName() + "' and password='" + account.getPassword() + "'";
        logger.info("登陆验证时查询的sql语句是：{}", sql);
        try {
            connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USERNAME, DBConfig.PASSWORD);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.info("获取数据库库连接或者执行sql语句或者获取数据错误", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    logger.info("Statement关闭异常", e);
                }
            }

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    logger.info("关闭数据库连接异常", e);
                }
            }
        }
        return false;
    }
```

测试代码在`MySQLConnectTest.java`中
```java
    /**
     * 测试SQL注入
     */
    @Test
    public void testLoginValidate() throws Exception {
        List<Account> accounts = Lists.newArrayList();
        accounts.add(new Account("admin", "test"));
        accounts.add(new Account("lilei", "test"));
        accounts.add(new Account("hanmeimei", "test"));

        accounts.add(new Account("admin", "tes"));
        accounts.add(new Account("lilei", "test1"));
        accounts.add(new Account("hanmeimei", "text"));

        accounts.add(new Account("admin' or '1=1", "tes"));
        accounts.add(new Account("lilei' or '1=1", "tes1"));
        accounts.add(new Account("hanmeimei' or '1=1", "text"));

        accounts.add(new Account("admin';#", ""));  //#是注释符，这一行后面的都注销了
        accounts.add(new Account("lilei';#", ""));

        for (Account account : accounts) {
            if (MySQLConnect.loginValidate(account)) {
                logger.info("用户[{}]登陆成功!", account.getUserName());
            } else {
                logger.info("用户]{}]登陆失败!", account.getUserName());
            }
        }
    }
```
每个用户在数据库中的密码都是`test`，看看注入测试结果：
>
17:23:02.454 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='admin' and password='test'  
17:23:02.827 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[admin]使用密码[test]登陆成功!  
17:23:02.828 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='lilei' and password='test'  
17:23:02.850 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[lilei]使用密码[test]登陆成功!  
17:23:02.851 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='hanmeimei' and password='test'  
17:23:02.873 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[hanmeimei]使用密码[test]登陆成功!  
17:23:02.874 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='admin' and password='tes'  
17:23:02.907 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[admin]使用密码[tes]登陆失败!  
17:23:02.908 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='lilei' and password='test1'  
17:23:02.929 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[lilei]使用密码[test1]登陆失败!  
17:23:02.929 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='hanmeimei' and password='text'  
17:23:02.948 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[hanmeimei]使用密码[text]登陆失败!  
17:23:02.949 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='admin' or '1=1' and password='tes'  
17:23:02.968 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[admin' or '1=1]使用密码[tes]登陆成功!  
17:23:02.969 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='lilei' or '1=1' and password='tes1'  
17:23:02.998 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[lilei' or '1=1]使用密码[tes1]登陆成功!  
17:23:02.999 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='hanmeimei' or '1=1' and password='text'  
17:23:03.017 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[hanmeimei' or '1=1]使用密码[text]登陆成功!  
17:23:03.018 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='admin';#' and password=''  
17:23:03.035 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[admin';#]使用密码[]登陆成功!  
17:23:03.035 [main] INFO  c.l.n.practice.jdbc.dao.MySQLConnect - 登陆验证时查询的sql语句是：select * from account where user_name='lilei';#' and password=''  
17:23:03.052 [main] INFO  c.l.n.p.jdbc.dao.MySQLConnectTest - 用户[lilei';#]使用密码[]登陆成功!
