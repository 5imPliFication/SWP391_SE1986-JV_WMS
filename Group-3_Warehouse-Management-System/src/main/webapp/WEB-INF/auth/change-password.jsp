<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Change Password</title>
    <link rel="stylesheet" href="<c:url value='/static/css/change-password.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/bootstrap.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/bootstrap-grid.css'/>">
    <link rel="stylesheet" href="<c:url value='/static/css/bootstrap-reboot.css'/>">
</head>
<body>

<div class="container py-5">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">

            <div class="profile-header d-flex align-items-center">
                <div class="rounded-circle bg-white text-primary d-flex align-items-center justify-content-center me-3 mr-3"
                     style="width: 60px; height: 60px; font-size: 24px; font-weight: bold; color: #7a58bf !important;">
                    S
                </div>
                <div>
                    <h4 class="mb-0">Thay đổi mật khẩu</h4>
                    <small>${user.email}</small>
                </div>
            </div>

            <div class="card p-4">
                <form method="POST" action="${pageContext.request.contextPath}/change-password">

                    <c:if test="${not empty error}">
                        <div class="error-message">
                            <i class="bi bi-exclamation-triangle-fill"></i> ${error}
                        </div>
                    </c:if>

                    <div class="mb-4">
                        <label for="currentPassword" class="form-label">Mật khẩu hiện tại</label>
                        <input type="password" id="currentPassword" name="currentPassword"
                               class="form-control" placeholder="Nhập mật khẩu cũ" required>
                    </div>

                    <div class="mb-4">
                        <label for="newPassword" class="form-label">Mật khẩu mới</label>
                        <input type="password" id="newPassword" name="newPassword"
                               class="form-control" placeholder="Nhập mật khẩu mới" required>
                        <small class="text-muted" style="font-style: italic; font-size: 0.8rem;">
                            Mật khẩu nên có ít nhất 8 ký tự bao gồm chữ và số.
                        </small>
                    </div>

                    <div class="mt-5">
                        <button type="submit" class="btn btn-update">Cập nhật mật khẩu</button>
                    </div>

                    <div class="text-center mt-3">
                        <a href="${pageContext.request.contextPath}/user/profile" style="color: #7a58bf;">Quay lại</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>