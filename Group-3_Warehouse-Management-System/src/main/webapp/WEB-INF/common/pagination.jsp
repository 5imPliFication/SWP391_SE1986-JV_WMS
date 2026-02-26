<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    Reusable Pagination Component
    
    Required Parameters:
    - pageNo: Current page number
    - totalPages: Total number of pages
    - baseUrl: Base URL for pagination links (e.g., "/user/list", "/coupons", "/salesman/orders")
    
    Optional Parameters (pass as queryParams):
    - searchName: Search query
    - sortName: Sort field
    - roleId: Filter by role
    - status: Filter by status
    - Any other custom filters
    
    Usage Example:
    <c:set var="queryParams" value="searchName=${param.searchName}&sortName=${param.sortName}&roleId=${selectedRoleId}"/>
    <jsp:include page="/WEB-INF/common/pagination.jsp">
        <jsp:param name="pageNo" value="${pageNo}"/>
        <jsp:param name="totalPages" value="${totalPages}"/>
        <jsp:param name="baseUrl" value="${pageContext.request.contextPath}/user/list"/>
        <jsp:param name="queryParams" value="${queryParams}"/>
    </jsp:include>
--%>

<c:if test="${param.totalPages > 1}">
    <nav class="mt-4 mb-4">
        <ul class="pagination justify-content-center">
            <%-- Previous Page Button --%>
            <li class="page-item ${param.pageNo == 1 ? 'disabled' : ''}">
                <a class="page-link" 
                   href="${param.baseUrl}?pageNo=${param.pageNo - 1}&${param.queryParams}"
                   aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                    <span class="sr-only">Previous</span>
                </a>
            </li>

            <%-- Page Numbers --%>
            <c:set var="left" value="${param.pageNo - 2}"/>
            <c:set var="right" value="${param.pageNo + 2}"/>

            <c:forEach begin="1" end="${param.totalPages}" var="i">
                <c:choose>
                    <%-- Always display first page --%>
                    <c:when test="${i == 1}">
                        <li class="page-item ${i == param.pageNo ? 'active' : ''}">
                            <a class="page-link" 
                               href="${param.baseUrl}?pageNo=${i}&${param.queryParams}">
                                ${i}
                                <c:if test="${i == param.pageNo}">
                                    <span class="sr-only">(current)</span>
                                </c:if>
                            </a>
                        </li>
                    </c:when>

                    <%-- Always display last page --%>
                    <c:when test="${i == param.totalPages}">
                        <li class="page-item ${i == param.pageNo ? 'active' : ''}">
                            <a class="page-link" 
                               href="${param.baseUrl}?pageNo=${i}&${param.queryParams}">
                                ${i}
                                <c:if test="${i == param.pageNo}">
                                    <span class="sr-only">(current)</span>
                                </c:if>
                            </a>
                        </li>
                    </c:when>

                    <%-- Display pages near current page --%>
                    <c:when test="${i >= left && i <= right}">
                        <li class="page-item ${i == param.pageNo ? 'active' : ''}">
                            <a class="page-link" 
                               href="${param.baseUrl}?pageNo=${i}&${param.queryParams}">
                                ${i}
                                <c:if test="${i == param.pageNo}">
                                    <span class="sr-only">(current)</span>
                                </c:if>
                            </a>
                        </li>
                    </c:when>

                    <%-- Display ellipsis for hidden pages --%>
                    <c:when test="${i == left - 1 || i == right + 1}">
                        <li class="page-item disabled">
                            <span class="page-link">...</span>
                        </li>
                    </c:when>
                </c:choose>
            </c:forEach>

            <%-- Next Page Button --%>
            <li class="page-item ${param.pageNo == param.totalPages ? 'disabled' : ''}">
                <a class="page-link" 
                   href="${param.baseUrl}?pageNo=${param.pageNo + 1}&${param.queryParams}"
                   aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                    <span class="sr-only">Next</span>
                </a>
            </li>
        </ul>
    </nav>
</c:if>
