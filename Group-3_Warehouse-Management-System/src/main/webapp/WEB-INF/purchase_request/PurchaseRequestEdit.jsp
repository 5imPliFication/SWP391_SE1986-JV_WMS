<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content" id="editModalContent">

            <form method="post"
                  action="${pageContext.request.contextPath}/purchase-request/edit"
                  id="editForm">

                <div class="modal-header">
                    <h5 class="modal-title">Edit Purchase Request</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body d-flex flex-column" style="height:70vh">

                    <input type="hidden" name="id" value="${prList.id}"/>

                    <!-- NOTE -->
                    <div class="mb-3">
                        <label class="fw-semibold">Note</label>
                        <textarea class="form-control"
                                  name="note"
                                  rows="3">${prList.note}</textarea>
                    </div>

                    <button type="button"
                            class="btn btn-primary btn-sm align-self-start"
                            onclick="addModalRow()">
                        ➕ Thêm dòng
                    </button>

                    <div class="table-responsive mt-2"
                         style="flex:1 1 auto; overflow-y:auto; min-height:0;">

                        <c:set var="tableHeader" scope="request">
                            <tr>
                                <th class="col-brand">Brand</th>
                                <th class="col-category">Category</th>
                                <th class="col-product">Product</th>
                                <th class="col-qty">Quantity</th>
                                <th class="col-qty">Unit</th>
                                <th class="col-action">Action</th>
                            </tr>
                        </c:set>


                        <c:set var="tableBody" scope="request">
                            <tbody id="modalItemsBody">

                                <c:forEach items="${items}" var="i">

                                    <tr>

                                        <!-- BRAND -->
                                        <td>
                                            <select class="form-select brand-select"
                                                    onchange="filterModalProduct(this)">
                                                <option value="">Select Brand</option>

                                                <c:forEach var="b" items="${brandName}">
                                                    <option value="${b.id}"
                                                            <c:if test="${b.name eq i.brandName}">selected</c:if>>
                                                        ${b.name}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>


                                        <!-- CATEGORY -->
                                        <td>
                                            <select class="form-select category-select"
                                                    onchange="filterModalProduct(this)">
                                                <option value="">Select Category</option>

                                                <c:forEach var="c" items="${CategoryName}">
                                                    <option value="${c.id}"
                                                            <c:if test="${c.name eq i.categoryName}">selected</c:if>>
                                                        ${c.name}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>


                                        <!-- PRODUCT -->
                                        <td>
                                            <select name="productId[]"
                                                    class="form-select product-select"
                                                    onchange="onModalProductChange(this)">

                                                <option value="">Select Product</option>

                                                <c:forEach var="p" items="${productName}">
                                                    <option value="${p.id}"
                                                            <c:if test="${p.id == i.productId}">selected</c:if>>
                                                        ${p.name}
                                                    </option>
                                                </c:forEach>

                                            </select>
                                        </td>


                                        <!-- QUANTITY -->
                                        <td>
                                            <input type="number"
                                                   name="quantity[]"
                                                   class="form-control quantity-input"
                                                   min="1"
                                                   value="${i.quantity}">
                                        </td>   

                                        <td class="unit-cell">
                                            ${i.unit}
                                        </td>

                                        <!-- ACTION -->
                                        <td class="text-center">
                                            <button type="button"
                                                    class="btn btn-outline-danger btn-sm"
                                                    onclick="removeModalRow(this)">
                                                ❌
                                            </button>
                                        </td>

                                    </tr>

                                </c:forEach>
                            </tbody>
                        </c:set>


                        <jsp:include page="/WEB-INF/common/table.jsp"/>
                    </div>

                </div>

                <div class="modal-footer">
                    <button type="button"
                            class="btn btn-secondary"
                            data-bs-dismiss="modal">
                        Cancel
                    </button>

                    <button type="submit"
                            class="btn btn-success">
                        💾 Save Changes
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>


<!-- ================= SCRIPT ================= -->
<script>
    /* ================= FILTER PRODUCT ================= */

    const allProducts = [
    <c:forEach var="p" items="${productName}" varStatus="s">
    {
    id: ${p.id},
            name: "${p.name}",
            brandId: ${p.brand.id},
            categoryId: ${p.category.id},
            unit:"${p.unit.symbol}"
    }<c:if test="${!s.last}">,</c:if>
    </c:forEach>
    ];
</script>
<script>
    function filterModalProduct(selectEl, isInit = false) {

        const row = selectEl.closest("tr");
        const brandId = Number(row.querySelector(".brand-select").value);
        const categoryId = Number(row.querySelector(".category-select").value);
        const productSelect = row.querySelector(".product-select");

        const currentValue = productSelect.value;

        productSelect.innerHTML = '<option value="">Select Product</option>';

        const qtyInput = row.querySelector(".quantity-input");
        const unitCell = row.querySelector(".unit-cell");

        if (!isInit) {
            qtyInput.value = "";
            qtyInput.disabled = true;

            if (unitCell)
                unitCell.textContent = "";
        }


        if (!brandId || !categoryId) {
            productSelect.disabled = true;
            return;
        }

        const filteredProducts = allProducts.filter(p =>
            p.brandId === brandId &&
                    p.categoryId === categoryId
        );
        if (filteredProducts.length === 0) {
            const qtyInput = row.querySelector(".quantity-input");
            const option = document.createElement("option");
            option.value = "";
            option.textContent = "⚠ Không có sản phẩm";
            option.selected = true;

            productSelect.appendChild(option);

            productSelect.disabled = true;

            qtyInput.value = "";
            qtyInput.disabled = true;

            return;
        }


        filteredProducts.forEach(p => {

            const option = document.createElement("option");
            option.value = p.id;
            option.textContent = p.name;

            if (p.id == currentValue) {
                option.selected = true;
            }

            productSelect.appendChild(option);

        });

        productSelect.disabled = false;
        refreshModalProductOptions();
    }


    /* ================= PRODUCT CHANGE ================= */

    function onModalProductChange(select) {

        const row = select.closest("tr");
        const qtyInput = row.querySelector(".quantity-input");
        const unitCell = row.querySelector(".unit-cell");

        if (select.value !== "") {

            qtyInput.disabled = false;
            qtyInput.value = "";
            qtyInput.focus();

            const product = allProducts.find(p => p.id == select.value);

            if (product && unitCell) {
                unitCell.textContent = product.unit;
            }

        } else {

            qtyInput.disabled = true;
            qtyInput.value = "";

            if (unitCell) {
                unitCell.textContent = "";
            }

        }

        refreshModalProductOptions();
    }



    /* ================= PREVENT DUPLICATE PRODUCT ================= */

    function refreshModalProductOptions() {

        const selects = document.querySelectorAll("#modalItemsBody .product-select");
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
                        selectedValues.includes(option.value) &&
                        option.value !== currentValue;
            });
        });
    }


    /* ================= ADD ROW ================= */

    function addModalRow() {

        const tbody = document.getElementById("modalItemsBody");
        const row = document.createElement("tr");
        row.innerHTML = `

<td>
<select class="form-select brand-select"
    onchange="filterModalProduct(this)">
<option value="">Select Brand</option>

    <c:forEach var="b" items="${brandName}">
    <option value="${b.id}">${b.name}</option>
    </c:forEach>

</select>
</td>

<td>
<select class="form-select category-select"
    onchange="filterModalProduct(this)">
<option value="">Select Category</option>

    <c:forEach var="c" items="${CategoryName}">
<option value="${c.id}">${c.name}</option>
    </c:forEach>

</select>
</td>

<td>
<select name="productId[]"
    class="form-select product-select"
    onchange="onModalProductChange(this)"
    disabled>

<option value="">Select Product</option>

</select>
</td>

<td>
<input type="number"
   name="quantity[]"
   class="form-control quantity-input"
   min="1"
   disabled>
</td>
<td class="unit-cell"></td>
<td class="text-center">
<button type="button"
    class="btn btn-outline-danger btn-sm"
    onclick="removeModalRow(this)">
❌
</button>
</td>

`;
        tbody.appendChild(row);
        row.scrollIntoView({behavior: "smooth", block: "end"});
    }


    function removeModalRow(btn) {
        btn.closest("tr").remove();
    }

    function initModalFilter() {

        const rows = document.querySelectorAll("#modalItemsBody tr");

        rows.forEach(row => {

            const brandSelect = row.querySelector(".brand-select");
            const categorySelect = row.querySelector(".category-select");

            if (brandSelect.value && categorySelect.value) {
                filterModalProduct(brandSelect, true);
            }

        });

    }

    document.addEventListener("DOMContentLoaded", function () {

        const modal = document.getElementById("editModal");

        modal.addEventListener("shown.bs.modal", function () {
            initModalFilter();
        });

    });



</script>
