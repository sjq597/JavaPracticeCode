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
