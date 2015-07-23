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
<%@ page import="java.net.URLDecoder" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>

<html>
<head>
  <title>SELECT Operation</title>
  <%!
    public  String get;
    public  String search_opt ;
  %>
</head>
<body>
<form action="index.jsp" method="get">
  <input type="submit" value="回到MSP查询首页">
</form>
<form action="search_app.jsp" method="get">
  <input type="submit" value="回到应用列表首页">
</form><br/>
<form action="search_app_pac.jsp" method="get">
  搜索应用:<input type="text" name="search_app_pac">
  <input type="submit" value="提交">
</form>
<%
  get=request.getParameter("search_app_pac");
  search_opt= URLDecoder.decode(get,"utf-8");
%>

<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
                   url="jdbc:mysql://localhost/app_info"
                   user="root"  password="1"/>

<sql:query dataSource="${snapshot}" var="result" >
  SELECT * from app_info.`pack_only_copy_7.22`;
</sql:query>

<c:set var="empId" scope="application" value="<%=get%>" >

</c:set>

<sql:query var="search_sql" dataSource="${snapshot}">
  SELECT * FROM app_info.`pack_only_copy_7.22` WHERE package_name=?;
  <sql:param value="${empId}" />
</sql:query>

<table border="1" width="100%">
  <tr>
    <th>ID</th>
    <th>ICON</th>
    <th>PACKAGE_NAME</th>
    <th>APP_NAME</th>
    <th>APP_VERSION</th>
    <th>APP_MD5</th>
    <th>APP_ID</th>
    <th>APP_SIZE</th>
    <th>ALIPAY_SDK_VERSION</th>
    <th>APP_URL</th>
  </tr>
  <c:forEach var="row" items="${search_sql.rows}">
    <tr>
      <td><c:out value="${row.id}"/></td>
      <td><img src="${row.app_icon}"  class="icon" width="68" height="68"/><c:out value="${row.icon}"/></td>
      <td><c:out value="${row.package_name}"/></td>
      <td><c:out value="${row.app_name}"/></td>
      <td><c:out value="${row.app_version}"/></td>
      <td><c:out value="${row.app_md5}"/></td>
      <td><c:out value="${row.app_id}"/></td>
      <td><c:out value="${row.app_size}"/></td>
      <td><c:out value="${row.sdk_version}"/></td>
      <td><a href="${row.app_url}"><c:out value="${row.app_url}"/></a></td>
    </tr>
  </c:forEach>
</table>

</body>
</html>