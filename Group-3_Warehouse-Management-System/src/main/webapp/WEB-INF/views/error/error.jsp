<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Error - Laptop Warehouse</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
            padding: 20px;
        }

        .error-container {
            background: #ffffff;
            padding: 50px;
            max-width: 500px;
            width: 100%;
            border-radius: 20px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            text-align: center;
        }

        .error-icon {
            width: 100px;
            height: 100px;
            margin: 0 auto 30px;
            background: #fee2e2;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            color: #dc2626;
        }

        .error-code {
            font-size: 72px;
            font-weight: 700;
            color: #dc2626;
            margin-bottom: 10px;
            line-height: 1;
        }

        .error-title {
            font-size: 24px;
            font-weight: 600;
            color: #1e293b;
            margin-bottom: 15px;
        }

        .error-message {
            font-size: 16px;
            color: #64748b;
            margin-bottom: 30px;
            line-height: 1.6;
        }

        .error-details {
            background: #f8fafc;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 30px;
            font-size: 14px;
            color: #475569;
            text-align: left;
            max-height: 150px;
            overflow-y: auto;
        }

        .error-details pre {
            margin: 0;
            white-space: pre-wrap;
            word-wrap: break-word;
        }

        .btn-group {
            display: flex;
            gap: 15px;
            justify-content: center;
            flex-wrap: wrap;
        }

        .btn {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 12px 24px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: #fff;
            text-decoration: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: 600;
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            border: none;
            cursor: pointer;
        }

        .btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 10px 20px rgba(102, 126, 234, 0.4);
        }

        .btn-secondary {
            background: #6c757d;
        }

        .btn-secondary:hover {
            background: #5a6268;
            box-shadow: 0 10px 20px rgba(108, 117, 125, 0.4);
        }

        @media (max-width: 480px) {
            .error-container {
                padding: 30px 20px;
            }

            .error-code {
                font-size: 56px;
            }

            .error-title {
                font-size: 20px;
            }

            .btn-group {
                flex-direction: column;
            }

            .btn {
                width: 100%;
            }
        }
    </style>
</head>
<body>

<div class="error-container">
    <!-- Error Icon -->
    <div class="error-icon">
        <c:choose>
            <c:when test="${statusCode == 404}">
                <i class="fas fa-search"></i>
            </c:when>
            <c:when test="${statusCode == 403}">
                <i class="fas fa-lock"></i>
            </c:when>
            <c:when test="${statusCode == 500}">
                <i class="fas fa-exclamation-triangle"></i>
            </c:when>
            <c:otherwise>
                <i class="fas fa-times-circle"></i>
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Error Code -->
    <div class="error-code">
        <c:choose>
            <c:when test="${not empty statusCode}">
                ${statusCode}
            </c:when>
            <c:when test="${not empty param.code}">
                ${param.code}
            </c:when>
            <c:otherwise>
                Error
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Error Title -->
    <div class="error-title">
        <c:choose>
            <c:when test="${statusCode == 404}">
                Page Not Found
            </c:when>
            <c:when test="${statusCode == 403}">
                Access Denied
            </c:when>
            <c:when test="${statusCode == 500}">
                Internal Server Error
            </c:when>
            <c:when test="${statusCode == 400}">
                Bad Request
            </c:when>
            <c:when test="${statusCode == 401}">
                Unauthorized
            </c:when>
            <c:otherwise>
                Oops! Something Went Wrong
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Error Message -->
    <div class="error-message">
        <c:choose>
            <c:when test="${not empty errorMessage}">
                ${errorMessage}
            </c:when>
            <c:when test="${not empty error}">
                ${error}
            </c:when>
            <c:when test="${statusCode == 404}">
                The page you are looking for doesn't exist or has been moved.
            </c:when>
            <c:when test="${statusCode == 403}">
                You don't have permission to access this resource.
            </c:when>
            <c:when test="${statusCode == 500}">
                An internal server error occurred. Please try again later.
            </c:when>
            <c:when test="${statusCode == 400}">
                The request could not be understood by the server.
            </c:when>
            <c:when test="${statusCode == 401}">
                You need to log in to access this resource.
            </c:when>
            <c:otherwise>
                We encountered an unexpected error. Our team has been notified.
            </c:otherwise>
        </c:choose>
    </div>

    <!-- Error Details (Optional - only for development) -->
    <c:if test="${not empty exception || not empty requestScope['javax.servlet.error.exception']}">
        <div class="error-details">
            <strong>Technical Details:</strong>
            <pre><c:out value="${exception != null ? exception : requestScope['javax.servlet.error.exception']}"/></pre>
        </div>
    </c:if>

    <!-- Action Buttons -->
    <div class="btn-group">
        <a href="javascript:history.back()" class="btn btn-secondary">
            <i class="fas fa-arrow-left"></i> Go Back
        </a>
        <a href="${pageContext.request.contextPath}/home" class="btn">
            <i class="fas fa-home"></i> Home
        </a>
    </div>
</div>

</body>
</html>