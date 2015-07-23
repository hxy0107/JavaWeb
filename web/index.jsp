<%--
  Created by IntelliJ IDEA.
  User: xianyu.hxy
  Date: 2015/7/20
  Time: 16:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html" pageEncoding="utf-8" language="java"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<html>
<head>
  <title>MSP查询首页</title>
</head>
<body>
<form action="index.jsp" method="get">
  <input type="submit" value="回到MSP查询首页">
</form>
<form action="search_app.jsp" method="get">
  <input type="submit" value="回到应用列表首页">
</form><br/>
<form action="index_name.jsp" method="get">
  搜索应用名   : <input type="text" name="search_app_index_name">
  <input type="submit" value="提交">
</form>
<form action="index_pac.jsp" method="get">
  搜索应用包名:<input type="text" name="search_app_index_pac">
  <input type="submit" value="提交">
</form>
<form action="index_msp.jsp" method="get">
  搜索应用MSP :<input type="text" name="search_app_index_msp">
  <input type="submit" value="提交">
</form>
<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
                   url="jdbc:mysql://localhost/app_info"
                   user="root"  password="1"/>

<sql:query dataSource="${snapshot}" var="result">
  SELECT * from app_info.`msp_table_copy`;
</sql:query>

<table border="1" width="100%">
  <tr>
    <th>ID</th>
    <th>ICON</th>
    <th>PACKAGE_NAME</th>
    <th>APP_NAME</th>
    <th>APP_VERSION</th>
    <th>APP_VERSIONCODE</th>
    <th>APP_SIZE</th>
    <th>MSP_VERSION</th>
    <th>APP_URL</th>
  </tr>
  <c:forEach var="row" items="${result.rows}">
    <tr>
      <td><c:out value="${row.id}"/></td>
      <td><img src="${row.app_icon}"  class="icon" width="68" height="68"/><c:out value="${row.icon}"/></td>
      <td><c:out value="${row.package_name}"/></td>
      <td><c:out value="${row.app_name}"/></td>
      <td><c:out value="${row.app_version}"/></td>
      <td><c:out value="${row.app_versioncode}"/></td>
      <td><c:out value="${row.app_size}"/></td>
      <td><c:out value="${row.msp_version}"/></td>
      <td><a href="${row.app_url}"><c:out value="${row.app_url}"/></a></td>
    </tr>
  </c:forEach>
</table>

</body>
</html>