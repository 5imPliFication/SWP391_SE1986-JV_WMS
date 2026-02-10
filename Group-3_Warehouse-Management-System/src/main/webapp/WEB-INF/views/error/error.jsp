<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 2/5/2026
  Time: 7:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f6f8;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
        }
        .error-container {
            background: #ffffff;
            padding: 40px;
            width: 420px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            text-align: center;
        }
        .error-code {
            font-size: 64px;
            font-weight: bold;
            color: #dc3545;
        }
        .error-message {
            font-size: 18px;
            margin: 15px 0 25px;
            color: #333;
        }
        .btn {
            display: inline-block;
            padding: 10px 18px;
            background-color: #0d6efd;
            color: #fff;
            text-decoration: none;
            border-radius: 5px;
            font-size: 14px;
        }
        .btn:hover {
            background-color: #0b5ed7;
        }
    </style>
</head>
<body>

<div class="error-container">
    <div class="error-code">
        <%= request.getAttribute("javax.servlet.error.status_code") != null
                ? request.getAttribute("javax.servlet.error.status_code")
                : "Error" %>
    </div>

    <div class="error-message">
        <%
            Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
            if (statusCode != null) {
                switch (statusCode) {
                    case 404:
                        System.out.print("The page you are looking for does not exist.");
                        break;
                    case 403:
                        System.out.print("You do not have permission to access this page.");
                        break;
                    case 500:
                        System.out.print("An internal server error occurred.");
                        break;
                    default:
                        System.out.print("Something went wrong.");
                }
            } else {
                System.out.print("Unexpected system error.");
            }
        %>
    </div>

    <a href="<%= request.getContextPath() %>/dashboard" class="btn">
        Go Back to Dashboard
    </a>
</div>

</body>
</html>
