<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Create Purchase Request</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
              rel="stylesheet">
        <style>

            .table-fixed{
                table-layout: fixed;
                width: 100%;
            }

            .table-fixed th,
            .table-fixed td{
                vertical-align: middle;
            }

            .col-brand{
                width: 18%;
            }
            .col-category{
                width: 18%;
            }
            .col-product{
                width: 34%;
            }
            .col-qty{
                width: 15%;
            }
            .col-action{
                width: 10%;
            }

        </style>

    </head>

    <body>

        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <main class="main-content">

            <h2>Create Purchase Request</h2>

            <c:if test="${not empty error}">
                <p class="text-danger">${error}</p>
            </c:if>

            <form action="${pageContext.request.contextPath}/purchase-request/create"
                  method="post">

                <div class="mb-3">
                    <label class="form-label fw-semibold">Note</label>
                    <textarea name="note"
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

                    <table class="table table-bordered">

                        <thead class="table-primary text-center">
                            <tr>
                                <th class="col-brand">Brand</th>
                                <th class="col-category">Category</th>
                                <th class="col-product">Product</th>
                                <th class="col-qty">Quanity</th>
                                <th class="col-action">Action</th>
                            </tr>
                        </thead>

                        <tbody id="itemsBody">

                            <tr id="emptyRow">
                                <td colspan="5" class="text-center text-muted">
                                    Chưa có item nào. Bấm <b>➕ Thêm dòng</b>
                                </td>
                            </tr>

                        </tbody>

                    </table>

                </div>

                <div class="d-flex gap-2 mt-3">

                    <button type="submit"
                            class="btn btn-success">
                        Create Purchase Request
                    </button>

                    <a href="${pageContext.request.contextPath}/purchase-request/list"
                       class="btn btn-secondary ms-auto">
                        ← Back
                    </a>
                </div>
            </form>
        </main>

        <!-- ================= PRODUCT DATA ================= -->

        <script>

            const allProducts = [
            <c:forEach var="p" items="${productName}" varStatus="s">
            {
            id: ${p.id},
                    name: "${p.name}",
                    brandId: ${p.brand.id},
                    categoryId: ${p.category.id}
            }<c:if test="${!s.last}">,</c:if>
            </c:forEach>
            ];
            console.log(allProducts);
        </script>


        <!-- ================= MAIN SCRIPT ================= -->

        <script>

            // ================= ADD ROW =================

            function addRow() {

                const tbody = document.getElementById("itemsBody");

                const emptyRow = document.getElementById("emptyRow");
                if (emptyRow)
                    emptyRow.remove();

                const row = document.createElement("tr");

                row.innerHTML = `

        <td>
        <select class="form-control brand-select"
                onchange="filterProduct(this)">
        <option value="">Select Brand</option>

            <c:forEach var="b" items="${brands}">
        <option value="${b.id}">
                ${b.name}
        </option>
            </c:forEach>

        </select>
        </td>

        <td>
        <select class="form-control category-select"
                onchange="filterProduct(this)">
        <option value="">Select Category</option>

            <c:forEach var="c" items="${categories}">
        <option value="${c.id}">
                ${c.name}
        </option>
            </c:forEach>

        </select>
        </td>

        <td>
        <select name="productId[]"
        class="form-control product-select"
        onchange="onProductChange(this)"
        disabled>


        <option value="">Select Product</option>

        </select>
        </td>

        <td>
<input class="form-control quantity-input"
       type="number"
       name="quantity[]"
       min="1"
       required
       disabled>


        </td>

        <td class="text-center">
        <button type="button"
                onclick="removeRow(this)"
                class="btn btn-outline-danger btn-sm">
        ❌
        </button>
        </td>

        `;

                tbody.appendChild(row);
            }


            // ================= REMOVE ROW =================

            function removeRow(btn) {

                const tbody = document.getElementById("itemsBody");

                btn.closest("tr").remove();

                if (tbody.children.length === 0) {

                    tbody.innerHTML = `
        <tr id="emptyRow">
        <td colspan="5"
        class="text-center text-muted">
        Chưa có item nào. Bấm <b>➕ Thêm dòng</b>
        </td>
        </tr>
        `;

                }

                refreshProductOptions();
            }


            // ================= FILTER PRODUCT =================

            function filterProduct(selectEl) {

                const row = selectEl.closest("tr");

                const brandId = Number(
                        row.querySelector(".brand-select").value
                        );

                const categoryId = Number(
                        row.querySelector(".category-select").value
                        );

                const productSelect =
                        row.querySelector(".product-select");

                productSelect.innerHTML =
                        '<option value="">Select Product</option>';

                // disable nếu chưa chọn đủ
                if (!brandId || !categoryId) {
                    productSelect.disabled = true;
                    return;
                }

                const filteredProducts =
                        allProducts.filter(p =>
                            p.brandId === brandId &&
                                    p.categoryId === categoryId
                        );

                // nếu không có sản phẩm
                if (filteredProducts.length === 0) {

                    const qtyInput = row.querySelector(".quantity-input");

                    const option = document.createElement("option");

                    option.value = "";
                    option.textContent = "⚠ Không có sản phẩm";
                    option.selected = true;

                    productSelect.appendChild(option);

                    productSelect.disabled = true;

                    // disable quantity
                    qtyInput.value = "";
                    qtyInput.disabled = true;

                    return;
                }




                // có sản phẩm
                filteredProducts.forEach(p => {

                    const option = document.createElement("option");

                    option.value = p.id;
                    option.textContent = p.name;

                    productSelect.appendChild(option);

                });

                productSelect.disabled = false;

                refreshProductOptions();
            }




            // ================= PREVENT DUPLICATE PRODUCT =================

            function onProductChange(select) {

                const row = select.closest("tr");
                const qtyInput = row.querySelector(".quantity-input");

                if (select.value !== "") {
                    qtyInput.disabled = false;
                    qtyInput.focus();   // tự nhảy vào ô quantity
                } else {
                    qtyInput.disabled = true;
                    qtyInput.value = "";
                }

                refreshProductOptions();
            }



            function refreshProductOptions() {

                const selects =
                        document.querySelectorAll(".product-select");

                const selectedValues =
                        Array.from(selects)
                        .map(s => s.value)
                        .filter(v => v !== "");

                selects.forEach(select => {

                    const currentValue = select.value;

                    Array.from(select.options).forEach(option => {

                        if (option.value === "")
                            return;

                        option.disabled =
                                selectedValues.includes(option.value)
                                && option.value !== currentValue;

                    });

                });

            }
        </script>

    </body>
</html>
