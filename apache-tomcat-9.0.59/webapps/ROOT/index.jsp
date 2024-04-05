<%@ page import="java.util.Scanner" %><%--
  Created by IntelliJ IDEA.
  User: 71764
  Date: 27.11.2022
  Time: 17:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Yury!!!</h1>
<%@ page import="java.util.Date, ru.ReaderFile" %>
<%@ page import="java.util.List" %>

<%
    ReaderFile read = new ReaderFile();
    List<String> list = read.readFile();
    for (int i = 0; i < list.size(); i++) {
        out.print("<p>" + list.get(i) + "</p>");
        
    }

%>

</body>
</html>
