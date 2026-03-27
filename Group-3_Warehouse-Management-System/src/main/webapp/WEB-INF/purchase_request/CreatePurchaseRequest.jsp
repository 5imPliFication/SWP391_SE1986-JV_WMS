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
            <div class="row bg-white m-2 p-2 rounded-4 shadow-sm h-75">

                <!-- LEFT: Filters -->
                <div class="col-md-4">
                    <h2>Filter Product</h2>
                    <div class="mb-2">
                        <label class="form-label fw-semibold mb-1">Brand</label>
                        <select class="form-select form-select-sm brand-select" onchange="filterProduct()">
                            <option value="">Select Brand</option>
                            <c:forEach var="b" items="${brands}">
                                <option value="${b.name}">${b.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-2">
                        <label class="form-label fw-semibold mb-1">Category</label>
                        <select class="form-select form-select-sm category-select" onchange="filterProduct()" disabled>
                            <option value="">Select Category</option>
                            <c:forEach var="c" items="${categories}">
                                <option value="${c.name}">${c.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-2">
                        <label class="form-label fw-semibold mb-1">Model</label>
                        <select class="form-select form-select-sm model-select" onchange="filterProduct()" disabled>
                            <option value="">Select Model</option>

                            <c:forEach var="m" items="${models}">
                                <option value="${m.name}" data-brand="${m.brand.name}">
                                    ${m.name}
                                </option>
                            </c:forEach>

                        </select>

                    </div>

                    <div class="mb-2">
                        <label class="form-label fw-semibold mb-1">Chip</label>
                        <select class="form-select form-select-sm chip-select" onchange="filterProduct()" disabled>
                            <option value="">Select Chip</option>
                            <c:forEach var="c" items="${chips}">
                                <option value="${c.name}">${c.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-2">
                        <label class="form-label fw-semibold mb-1">Ram</label>
                        <select class="form-select form-select-sm ram-select" onchange="filterProduct()" disabled>
                            <option value="">Select Ram</option>
                            <c:forEach var="r" items="${rams}">
                                <option value="${r.size}">${r.size}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-2">
                        <label class="form-label fw-semibold mb-1">Storage</label>
                        <select class="form-select form-select-sm storage-select" onchange="filterProduct()" disabled>
                            <option value="">Select Storage</option>
                            <c:forEach var="s" items="${storages}">
                                <option value="${s.size}">${s.size}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-2">
                        <label class="form-label fw-semibold mb-1">Size</label>
                        <select class="form-select form-select-sm size-select" onchange="filterProduct()" disabled>
                            <option value="">Select Size</option>
                            <c:forEach var="s" items="${sizes}">
                                <option value="${s.size}">${s.size}</option>
                            </c:forEach>
                        </select>
                    </div>

                </div>


                <!-- RIGHT: Table -->
                <div class="col-md-8">
                    <div>
                        <c:set var="tableHeader" scope="request">
                            <tr>
                                <th class="col-product">Product</th>
                                <th class="col-action">Action</th>
                            </tr>
                        </c:set>
                        <c:set var="tbodyId" value="productTableBody" scope="request"/>

                        <c:set var="tableBody" scope="request">

                            <c:forEach var="p" items="${productName}">
                                <tr
                                    data-brand="${p.brand.name}"
                                    data-category="${p.category.name}"
                                    data-model="${p.model.name}"
                                    data-chip="${p.chip.name}"
                                    data-ram="${p.ram.size}"
                                    data-storage="${p.storage.size}"
                                    data-size="${p.size.size}"
                                    >
                                    <td>${p.name}</td>
                                    <td>
                                        <button type="button"
                                                class="btn btn-sm btn-primary add-btn"
                                                data-id="${p.id}"
                                                data-name="${p.name}"
                                                data-unit="${p.unit.name}">
                                            Add
                                        </button>
                                    </td>
                                </tr>
                            </c:forEach>

                            <tr id="noDataRow" style="display:none">
                                <td colspan="2" class="text-center text-muted">
                                    Vui lòng chọn bên trái
                                </td>
                            </tr>

                        </c:set>
                        <jsp:include page="/WEB-INF/common/table.jsp"/>

                    </div>
                </div>

            </div>

            <div class="my-5">
                <c:if test="${not empty error}">
                    <p class="text-danger">${error}</p>
                </c:if>
            </div>   

            <div class="row bg-white m-2 p-2 rounded-4 shadow-sm">
                <h2>Create Purchase Request</h2>

                <form action="${pageContext.request.contextPath}/purchase-request/create"
                      method="post">

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Note</label>
                        <textarea name="note"
                                  rows="3"
                                  class="form-control"
                                  placeholder="Nhập ghi chú..."></textarea>
                    </div>

                    <div class="table-responsive">

                        <c:set var="tableHeader" scope="request">
                            <tr>
                                <th class="col-product">Product</th>
                                <th class="col-qty">Quantity</th>
                                <th class="col-qty">Unit</th>
                                <th class="col-action">Action</th>
                            </tr>
                        </c:set>

                        <c:set var="tbodyId" value="itemsBody" scope="request"/>

                        <c:set var="tableBody" scope="request">
                            <tr id="emptyRow">
                                <td colspan="4" class="text-center text-muted">
                                    No Data
                                </td>
                            </tr>
                        </c:set>


                        <jsp:include page="/WEB-INF/common/table.jsp"/>

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
            </div>
        </main>

        <script>

            document.addEventListener("DOMContentLoaded", function () {

                filterProduct();

                // FIX: add button event
                document.querySelectorAll(".add-btn").forEach(btn => {
                    btn.addEventListener("click", function () {

                        const id = this.dataset.id;
                        const name = this.dataset.name;
                        const unit = this.dataset.unit;

                        addProduct(id, name, unit);
                    });
                });

            });


            document.addEventListener("DOMContentLoaded", function () {

                filterProduct();

                document.querySelector(".brand-select").addEventListener("change", function () {

                    let brand = this.value;

                    let categorySelect = document.querySelector(".category-select");
                    let modelSelect = document.querySelector(".model-select");
                    let chipSelect = document.querySelector(".chip-select");
                    let ramSelect = document.querySelector(".ram-select");
                    let storageSelect = document.querySelector(".storage-select");
                    let sizeSelect = document.querySelector(".size-select");

                    // reset tất cả select phía sau
                    categorySelect.value = "";
                    modelSelect.value = "";
                    chipSelect.value = "";
                    ramSelect.value = "";
                    storageSelect.value = "";
                    sizeSelect.value = "";

                    let options = modelSelect.querySelectorAll("option");

                    options.forEach(opt => {

                        let modelBrand = opt.dataset.brand;

                        if (!modelBrand)
                            return;

                        if (!brand || modelBrand === brand) {
                            opt.style.display = "";
                        } else {
                            opt.style.display = "none";
                        }

                    });

                    filterProduct();
                });


            });


            function filterProduct() {

                let brandSelect = document.querySelector(".brand-select");
                let categorySelect = document.querySelector(".category-select");
                let modelSelect = document.querySelector(".model-select");
                let chipSelect = document.querySelector(".chip-select");
                let ramSelect = document.querySelector(".ram-select");
                let storageSelect = document.querySelector(".storage-select");
                let sizeSelect = document.querySelector(".size-select");

                let brand = brandSelect.value;
                let category = categorySelect.value;
                let model = modelSelect.value;
                let chip = chipSelect.value;
                let ram = ramSelect.value;
                let storage = storageSelect.value;
                let size = sizeSelect.value;

                // Nếu chưa chọn brand → không hiển thị data
                // Nếu chưa chọn brand → reset toàn bộ filter
                if (!brand) {

                    categorySelect.value = "";
                    modelSelect.value = "";
                    chipSelect.value = "";
                    ramSelect.value = "";
                    storageSelect.value = "";
                    sizeSelect.value = "";

                    categorySelect.disabled = true;
                    modelSelect.disabled = true;
                    chipSelect.disabled = true;
                    ramSelect.disabled = true;
                    storageSelect.disabled = true;
                    sizeSelect.disabled = true;

                    document.querySelectorAll("#productTableBody tr[data-brand]")
                            .forEach(row => row.style.display = "none");

                    let noDataRow = document.getElementById("noDataRow");

                    if (noDataRow) {
                        noDataRow.style.display = "";
                    }

                    return;
                }


                // ENABLE SELECT
                categorySelect.disabled = !brand;
                modelSelect.disabled = !category;
                chipSelect.disabled = !model;
                ramSelect.disabled = !chip;
                storageSelect.disabled = !ram;
                sizeSelect.disabled = !storage;

                let rows = document.querySelectorAll("#productTableBody tr[data-brand]");
                let visibleCount = 0;

                rows.forEach(row => {

                    let show = true;

                    if (brand && row.dataset.brand !== brand)
                        show = false;
                    if (category && row.dataset.category !== category)
                        show = false;
                    if (model && row.dataset.model !== model)
                        show = false;
                    if (chip && row.dataset.chip !== chip)
                        show = false;
                    if (ram && row.dataset.ram !== ram)
                        show = false;
                    if (storage && row.dataset.storage !== storage)
                        show = false;
                    if (size && row.dataset.size !== size)
                        show = false;

                    row.style.display = show ? "" : "none";

                    if (show)
                        visibleCount++;

                });

                let noDataRow = document.getElementById("noDataRow");

                if (noDataRow) {
                    noDataRow.style.display = visibleCount === 0 ? "" : "none";
                }
            }


            function addProduct(id, name, unit) {

                const table = document.querySelector("#itemsBody");

                if (!table) {
                    console.log("itemsBody not found");
                    return;
                }

                const emptyRow = document.getElementById("emptyRow");
                if (emptyRow)
                    emptyRow.remove();

                const existing = table.querySelector('input[name="productId[]"][value="' + id + '"]');

                if (existing) {

                    const qtyInput = existing.closest("tr").querySelector('input[name="quantity[]"]');
                    qtyInput.value = parseInt(qtyInput.value) + 1;
                    return;
                }

                const row = document.createElement("tr");

                row.innerHTML =
                        '<td>' + name +
                        '<input type="hidden" name="productId[]" value="' + id + '">' +
                        '</td>' +
                        '<td>' +
                        '<input type="number" name="quantity[]" class="form-control form-control-sm" value="1" min="1">' +
                        '</td>' +
                        '<td>' + unit + '</td>' +
                        '<td>' +
                        '<button type="button" class="btn btn-sm btn-danger" onclick="removeRow(this)">Remove</button>' +
                        '</td>';

                table.appendChild(row);
            }

            function removeRow(btn) {

                const table = document.querySelector("#itemsBody");

                btn.closest("tr").remove();

                if (table.children.length === 0) {

                    const row = document.createElement("tr");
                    row.id = "emptyRow";

                    row.innerHTML =
                            '<td colspan="4" class="text-center text-muted">No Data</td>';

                    table.appendChild(row);
                }
            }

        </script>

    </body>
</html>
