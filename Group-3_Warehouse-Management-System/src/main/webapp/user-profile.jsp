<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 1/9/2026
  Time: 10:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>User Profile</title>
    </head>
    <body>
        <h2>User Profile</h2>

        <form action="<%= request.getContextPath() %>/user/profile" method="post">
            <table>
                <tr>
                    <td>Full Name</td>
                    <td>
                        <input type="text"
                               name="fullName"
                               value="${user.fullName}"
                               required />
                    </td>
                </tr>

                <tr>
                    <td>Email</td>
                    <td>
                        <input type="email"
                               name="email"
                               value="${user.email}"
                               />
                    </td>
                </tr>

                <tr>
                    <td>Role</td>
                    <td>
                        <input type="text"
                               value="${user.role.name}"
                               disabled />
                    </td>
                </tr>

                <tr>
                    <td colspan="2">
                        <button type="submit">Update Profile</button>
                    </td>
                </tr>
            </table>
        </form>

        <%
       String success = request.getParameter("success");
       if ("true".equals(success)) {
        %>
        <p style="color: green;">Cập nhật thành công!</p>
        <%
            }else{
        %>   
        <p style="color: red">Cập nhật không thành công</p>
        <%
     }
        %>
    </body>
</html>
