<%-- 
    Document   : login
    Created on : 18-ago-2020, 15:03:03
    Author     : andre
--%>

<%@page import="Principales.BaseDeDatos"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        En este momento se llama a el jsp y funciona
        <%
            BaseDeDatos a = new BaseDeDatos();
            String lista = a.verUsuarios();
            
            String nick = request.getParameter("nick");
            String contraseña = request.getParameter("password");
            
        %>
        <h1>Bienvenido a la Biblioteca <%=nick%></h1>
        <p><%=lista%></p>
    </body>
</html>
