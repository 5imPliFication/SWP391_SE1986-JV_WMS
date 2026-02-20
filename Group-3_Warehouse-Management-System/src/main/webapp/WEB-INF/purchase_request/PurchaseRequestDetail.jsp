<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Purchase Request</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
              rel="stylesheet">
    </head>

    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <!-- ===== ROLE & STATUS ===== -->
        <c:set var="isManager" value="${user.role.name eq 'Manager'}"/>
        <c:set var="isPending" value="${prList != null && prList.status eq 'PENDING'}"/>

        <main class="main-content container mt-4" style="max-width:1100px">

            <!-- ================= HEADER ================= -->
            <h3 class="mb-3">
                Purchase Request Detail
                <span class="badge bg-secondary">${prList.status}</span>
            </h3>

            <form method="post"
                  action="${pageContext.request.contextPath}/purchase-request/edit">

                <input type="hidden" name="id" value="${prList.id}"/>

                <!-- ================= NOTE ================= -->
                <div class="mb-3">
                    <label class="fw-semibold">Note</label>
                    <textarea class="form-control"
                              name="note"
                              rows="3"
                              <c:if test="${isManager || !isPending}">readonly</c:if>>
                        ${prList.note}
                    </textarea>
                </div>

                <!-- ================= ADD ROW ================= -->
                <c:if test="${!isManager && isPending}">
                    <button type="button"
                            class="btn btn-primary btn-sm mb-2"
                            onclick="addRow()">
                        ➕ Thêm dòng
                    </button>
                </c:if>

                <!-- ================= ITEMS TABLE ================= -->
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead class="table-primary text-center">
                            <tr>
                                <th>Product</th>
                                <th>Product Name</th>
                                <th>Brand</th>
                                <th>Category</th>
                                <th>Qty</th>
                                <th>Action</th>
                            </tr>
                        </thead>

                        <tbody id="itemsBody">
                            <c:forEach items="${items}" var="i" varStatus="st">
                                <tr>
                                    <!-- PRODUCT -->
                                    <td>
                                        <select name="productId[]"
                                                class="form-select product-select"
                                                onchange="onProductChange(this)"
                                                <c:if test="${isManager || !isPending}">disabled</c:if>>
                                                    <option value="">-New Product-</option>
                                                <c:forEach var="p" items="${productName}">
                                                    <option value="${p.id}"
                                                            <c:if test="${p.id == i.productId}">selected</c:if>>
                                                        ${p.name}
                                                    </option>
                                                </c:forEach>
                                        </select>
                                    </td>

                                    <!-- PRODUCT NAME -->
                                    <td>
                                        <input type="text"
                                               name="productName[]"
                                               class="form-control product-name"
                                               value="${i.productName}"
                                               <c:if test="${isManager || not empty i.productId || !isPending}">
                                                   readonly
                                               </c:if>>
                                    </td>

                                    <!-- BRAND -->
                                    <td>
                                        <select name="brandId[]"
                                                class="form-select brand"
                                                <c:if test="${isManager || not empty i.productId || !isPending}">
                                                    disabled
                                                </c:if>>
                                            <option value="">-- Select Brand --</option>
                                            <c:forEach var="b" items="${BrandName}">
                                                <option value="${b.name}"
                                                        <c:if test="${b.name == i.brandName}">selected</c:if>>
                                                    ${b.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>

                                    <!-- CATEGORY -->
                                    <td>
                                        <select name="categoryId[]"
                                                class="form-select category"
                                                <c:if test="${isManager || not empty i.productId || !isPending}">
                                                    disabled
                                                </c:if>>
                                            <option value="">-- Select Category --</option>
                                            <c:forEach var="c" items="${CategoryName}">
                                                <option value="${c.name}"
                                                        <c:if test="${c.name == i.categoryName}">selected</c:if>>
                                                    ${c.name}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>

                                    <!-- QTY -->
                                    <td>
                                        <input type="number"
                                               name="quantity[]"
                                               class="form-control quantity text-center"
                                               min="1"
                                               value="${i.quantity}"
                                               <c:if test="${isManager || !isPending}">readonly</c:if>>
                                        </td>

                                        <!-- ACTION -->
                                        <td class="text-center">
                                        <c:if test="${!isManager && isPending}">
                                            <button type="button"
                                                    onclick="removeRow(this)"
                                                    class="btn btn-outline-danger btn-sm">
                                                ❌
                                            </button>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty items}">
                                <tr id="emptyRow">
                                    <td colspan="6" class="text-center text-muted">No items</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- ================= ACTION BUTTONS ================= -->
                <div class="mt-3 d-flex gap-2">
                    <!-- SAVE -->
                    <c:if test="${!isManager && isPending}">
                        <button type="submit" class="btn btn-success">
                            💾 Save
                        </button>
                    </c:if>
                </div>
            </form>
            <div class="mt-3 d-flex gap-2">
                <!-- CANCEL -->
                <c:if test="${!isManager && isPending}">
                    <form action="${pageContext.request.contextPath}/purchase-request/detail"
                          method="post"
                          onsubmit="return confirm('Cancel this purchase request?')">
                        <input type="hidden" name="id" value="${prList.id}"/>
                        <input type="hidden" name="action" value="cancel">
                        <button class="btn btn-danger">❌ Cancel</button>
                    </form>
                </c:if>

                <!-- APPROVE / REJECT (MANAGER) -->
                <c:if test="${isManager && isPending}">
                    <form method="post"
                          action="${pageContext.request.contextPath}/purchase-request/detail">
                        <input type="hidden" name="id" value="${prList.id}">
                        <input type="hidden" name="action" value="approve">
                        <button class="btn btn-success">Approve</button>
                    </form>

                    <form method="post"
                          action="${pageContext.request.contextPath}/purchase-request/detail">
                        <input type="hidden" name="id" value="${prList.id}">
                        <input type="hidden" name="action" value="reject">
                        <button class="btn btn-danger">Reject</button>
                    </form>


                </c:if>

            </div>    
            <div class="mt-3 d-flex gap-2">

                <a href="${pageContext.request.contextPath}/purchase-request/list"
                   class="btn btn-secondary">
                    ← Back
                </a>
            </div>    

        </main>

        <!-- ================= JS ================= -->
        <script>
            function onProductChange(selectEl) {
                const row = selectEl.closest("tr");
                const productName = row.querySelector(".product-name");
                const brand = row.querySelector(".brand");
                const category = row.querySelector(".category");

                if (selectEl.value === "") {
                    productName.readOnly = false;
                    brand.disabled = false;
                    category.disabled = false;
                } else {
                    productName.value = "";
                    productName.readOnly = true;
                    brand.value = "";
                    brand.disabled = true;
                    category.value = "";
                    category.disabled = true;
                }
            }

            function addRow() {
                const tbody = document.getElementById("itemsBody");
                const emptyRow = document.getElementById("emptyRow");
                if (emptyRow)
                    emptyRow.remove();

                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>
                        <select name="productId[]" class="form-select product-select"
                                onchange="onProductChange(this)">
                            <option value="">-New Product-</option>
            <c:forEach var="p" items="${productName}">
                                <option value="${p.id}">${p.name}</option>
            </c:forEach>
                        </select>
                    </td>
                    <td><input type="text" name="productName[]" class="form-control product-name"></td>
                    <td>
                        <select name="brandId[]" class="form-select brand">
                            <option value="">-- Select Brand --</option>
            <c:forEach var="b" items="${BrandName}">
                                <option value="${b.name}">${b.name}</option>
            </c:forEach>
                        </select>
                    </td>
                    <td>
                        <select name="categoryId[]" class="form-select category">
                            <option value="">-- Select Category --</option>
            <c:forEach var="c" items="${CategoryName}">
                                <option value="${c.name}">${c.name}</option>
            </c:forEach>
                        </select>
                    </td>
                    <td><input type="number" name="quantity[]" min="1" class="form-control text-center"></td>
                    <td class="text-center">
                        <button type="button" class="btn btn-outline-danger btn-sm"
                                onclick="removeRow(this)">❌</button>
                    </td>`;
                tbody.appendChild(row);
            }

            function removeRow(btn) {
                const tbody = document.getElementById("itemsBody");
                btn.closest("tr").remove();
                if (tbody.children.length === 0) {
                    tbody.innerHTML = `<tr id="emptyRow">
                        <td colspan="6" class="text-center text-muted">No items</td>
                    </tr>`;
                }
            }
        </script>
    </body>
</html>
