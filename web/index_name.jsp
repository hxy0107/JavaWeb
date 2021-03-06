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
<form action="index_name.jsp" method="get">
  搜索应用名:<input type="text" name="search_app_index_name">
  <input type="submit" value="提交">
</form>
<form  action="UpdateServlet" method="get" >
 输入包名: <input type="text" name="search_app_index_name_servlet">
  <input type="submit" value="检查应用更新">
</form>
<%
  request.setCharacterEncoding("GBK");
  get=request.getParameter("search_app_index_name");
  search_opt= URLDecoder.decode(get,"utf-8");
%>

<sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
                   url="jdbc:mysql://localhost/app_info"
                   user="root"  password="1"/>

<sql:query dataSource="${snapshot}" var="result" >
  SELECT * from app_info.`msp_table_copy_7.27_copy`;
</sql:query>

<c:set var="empId" scope="page" value="<%=search_opt%>" >

</c:set>

<sql:query var="search_sql" dataSource="${snapshot}">
  SELECT * FROM app_info.`msp_table_8.12_copy` WHERE app_name=?;
  <sql:param value="${empId}" />
</sql:query>

<table border="1" width="100%">
  <tr>
    <th>ID</th>
    <th>ICON</th>
    <th>PACKAGE_NAME</th>
    <th>APP_NAME</th>
    <th>APP_VERSIONCODE</th>
    <th>DECODE</th>
    <th>HAS_ALI_SDK</th>
    <th>MSP_VERSION</th>
    <th>MSP_PRO</th>
    <th>APP_URL</th>
  </tr>
  <c:forEach var="row" items="${search_sql.rows}">
    <tr>
      <td><c:out value="${row.id}"/></td>
      <td><img src="${row.app_icon}"  class="icon" width="68" height="68"/><c:out value="${row.icon}"/></td>
      <td><c:out value="${row.package_name}"/></td>
      <td><c:out value="${row.app_name}"/></td>
      <td><c:out value="${row.app_versioncode}"/></td>
      <td><c:out value="${row.decode_app}"/></td>
      <td><c:out value="${row.has_sdk}"/></td>
      <td><c:out value="${row.msp_version}"/></td>
      <td><c:out value="${row.msp_pro}"/></td>
      <td><a href="${row.app_url}"><c:out value="${row.app_url}"/></a></td>
    </tr>
  </c:forEach>
</table>

</body>
</html>