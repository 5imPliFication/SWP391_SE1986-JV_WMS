<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Create Purchase Request</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
              rel="stylesheet">


    </head>

    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>
        <main class="main-content">

            <h2>Create Purchase Request</h2>

            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <form action="${pageContext.request.contextPath}/purchase-request/create"
                  method="post"
                  id="purchaseForm"
                  style="max-width: 1000px;">
                <!-- NOTE -->
                <div class="mb-3">
                    <label for="note" class="form-label fw-semibold">
                        Note
                    </label>
                    <textarea id="note"
                              name="note"
                              rows="3"
                              class="form-control"
                              placeholder="Nhập ghi chú..."></textarea>
                </div>

                <div class="d-flex mb-3">
                    <button type="button"
                            onclick="addRow()"
                            class="btn btn-primary btn-sm">
                        ➕ Thêm dòng
                    </button>
                </div>



                <div class="table-responsive">
                    <table class="table table-bordered mb-0">
                        <thead class="table-primary text-center">
                            <tr>
                                <th>Product</th>
                                <th>Product Name</th>
                                <th>Brand</th>
                                <th>Category</th>
                                <th>Qty</th>
                                <th >Action</th>
                            </tr>
                        </thead>

                        <tbody id="itemsBody">
                            <tr id="emptyRow">
                                <td colspan="6" class="text-center text-muted">
                                    Chưa có item nào. Vui lòng bấm <b>➕ Thêm dòng</b> để thêm sản phẩm
                                </td>
                            </tr>

                        </tbody>

                    </table>
                </div>

                <button type="submit" class="btn btn-success mt-3">
                    Create Purchase Request
                </button>

            </form>
        </main>

        <!-- ================= JAVASCRIPT ================= -->
        <script>
            function onProductChange(selectEl) {
                toggleNewProductFields(selectEl);
                refreshProductOptions();
            }

// Disable / enable new product fields
            function toggleNewProductFields(selectEl) {
                const row = selectEl.closest("tr");
                const fields = row.querySelectorAll(".new-product-field");

                if (selectEl.value) {
                    fields.forEach(f => {
                        f.value = "";
                        f.disabled = true;
                        f.classList.add("disabled-field");
                    });
                } else {
                    fields.forEach(f => {
                        f.disabled = false;
                        f.classList.remove("disabled-field");
                    });
                }
            }

// Hide selected products in other rows
            function refreshProductOptions() {
                const selects = document.querySelectorAll(".product-select");

                const selectedValues = Array.from(selects)
                        .map(s => s.value)
                        .filter(v => v !== "");

                selects.forEach(select => {
                    const currentValue = select.value;

                    Array.from(select.options).forEach(option => {
                        if (option.value === "")
                            return;

                        option.disabled =
                                selectedValues.includes(option.value) &&
                                option.value !== currentValue;
                    });
                });
            }

// ➕ Add row
            function addRow() {
                const tbody = document.getElementById("itemsBody");

                // remove empty row if exists
                const emptyRow = document.getElementById("emptyRow");
                if (emptyRow) {
                    emptyRow.remove();
                }

                const row = document.createElement("tr");
                row.innerHTML = `
        <td>
            <select name="productId[]" onchange="onProductChange(this)">
                <option value="">-- New Product --</option>
            <c:forEach var="p" items="${productName}">
                    <option value="${p.id}">${p.name}</option>
            </c:forEach>
            </select>
        </td>

        <td>
            <input type="text" name="productName[]" class="new-product-field">
        </td>

        <td>
            <select name="brandId[]" class="new-product-field">
                <option value="">-- Select Brand --</option>
            <c:forEach var="b" items="${BrandName}">
                    <option value="${b.id}">${b.name}</option>
            </c:forEach>
            </select>
        </td>

        <td>
            <select name="categoryId[]" class="new-product-field">
                <option value="">-- Select Category --</option>
            <c:forEach var="c" items="${CategoryName}">
                    <option value="${c.id}">${c.name}</option>
            </c:forEach>
            </select>
        </td>

        <td>
            <input type="number" name="quantity[]" min="1" required>
        </td>

        <td>
          <button type="button" onclick="removeRow(this)"
        class="btn btn-outline-danger btn-sm">
    ❌
</button>

        </td>
    `;

                tbody.appendChild(row);
                refreshProductOptions();
            }

// ❌ Remove row
            function removeRow(btn) {
                const tbody = document.getElementById("itemsBody");
                btn.closest("tr").remove();

                // if no rows left → show empty row again
                if (tbody.children.length === 0) {
                    tbody.innerHTML = `
            <tr id="emptyRow">
                <td colspan="6" style="text-align:center; color:#888;">
                    Chưa có item nào. Vui lòng bấm <b>➕ Thêm dòng</b> để thêm sản phẩm
                </td>
            </tr>
        `;
                }

                refreshProductOptions();
            }
        </script>
    </body>
</html>
