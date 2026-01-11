<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Update Role</title>
</head>
<body>

<h2>Update Role</h2>

<form method="post" action="${pageContext.request.contextPath}/admin/role">

    <!-- Role ID -->
    Role ID<br>
    <input type="text" name="id" value="${role.id}" readonly><br><br>

    <!-- Role Name -->
    Role Name<br>
    <input type="text" name="name" value="${role.name}"><br><br>


    Permission<br>
    <select>
        <option>Yes</option>
        <option>No</option>

    </select>
    <br>
    <small>Modify; Create; Delete</small>
    <br><br>

    <!-- Description -->
    Description<br>
    <textarea name="description" rows="5" cols="40">
        ${role.description}
    </textarea>
    <br><br>

    <!-- Status -->
    <button type="submit">
        Active
    </button>

</form>

<br>

<a href="${pageContext.request.contextPath}/admin/role">
    Cancel
</a>

</body>
</html>
