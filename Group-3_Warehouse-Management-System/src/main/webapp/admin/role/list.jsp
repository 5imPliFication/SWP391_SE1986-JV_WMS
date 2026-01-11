<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<h2>Role List</h2>

<table border="1">
    <tr>
        <th>ID</th>
        <th>Name</th>
        <th>Description</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

    <c:forEach items="${roles}" var="r">
        <tr>
            <td>${r.id}</td>
            <td>${r.name}</td>
            <td>${r.description}</td>
            <td>${r.status ? "Active" : "Inactive"}</td>
            <td>
                <a href="${pageContext.request.contextPath}/admin/role?action=detail&id=${r.id}">Detail</a> |
                <a href="role?action=toggle&id=${r.id}&status=${!r.status}">
                        ${r.status ? "Deactivate" : "Activate"}
                </a>
            </td>
        </tr>
    </c:forEach>
</table>
