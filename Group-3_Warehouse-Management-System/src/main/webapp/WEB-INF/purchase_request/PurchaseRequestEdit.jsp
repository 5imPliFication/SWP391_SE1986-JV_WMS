<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!-- ================= EDIT MODAL ================= -->
<div class="modal fade" id="editModal" tabindex="-1">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content" id="editModalContent">

            <form method="post"
                  action="${pageContext.request.contextPath}/purchase-request/edit"
                  id="editForm">

                <!-- ========== HEADER ========== -->
                <div class="modal-header">
                    <h5 class="modal-title">Edit Purchase Request</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <!-- ========== BODY (FIXED HEIGHT) ========== -->
                <div class="modal-body d-flex flex-column" style="height: 70vh;">

                    <input type="hidden" name="id" value="${prList.id}"/>

                    <!-- NOTE -->
                    <div class="mb-3">
                        <label class="fw-semibold">Note</label>
                        <textarea class="form-control"
                                  name="note"
                                  rows="3">${prList.note}</textarea>
                    </div>

                    <!-- ADD ROW -->
                    <button type="button"
                            class="btn btn-primary btn-sm align-self-start"
                            onclick="addModalRow()">
                        ➕ Thêm dòng
                    </button>

                    <!-- ========== TABLE (SCROLL HERE ONLY) ========== -->
                    <div class="table-responsive mt-2"
                         style="flex: 1 1 auto; overflow-y: auto; min-height: 0;">

                        <table class="table table-bordered mb-0">
                            <thead class="table-primary text-center sticky-top">
                                <tr>
                                    <th>Product</th>
                                    <th>Qty</th>
                                    <th>Action</th>
                                </tr>
                            </thead>

                            <tbody id="modalItemsBody">
                                <c:forEach items="${items}" var="i">
                                    <tr>
                                        <td>
                                            <select name="productId[]"
                                                    class="form-select product-select"
                                                    onchange="onProductChange(this)">
                                                <option value="">-New Product-</option>
                                                <c:forEach var="p" items="${productName}">
                                                    <option value="${p.id}"
                                                            <c:if test="${p.id == i.productId}">selected</c:if>>
                                                        ${p.name}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </td>
                                        <td>
                                            <input type="number"
                                                   name="quantity[]"
                                                   class="form-control "
                                                   min="1"
                                                   value="${i.quantity}">
                                        </td>

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
                        </table>
                    </div>
                </div>

                <!-- ========== FOOTER (ALWAYS FIXED) ========== -->
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        Cancel
                    </button>
                    <button type="submit" class="btn btn-success">
                        💾 Save Changes
                    </button>
                </div>

            </form>
        </div>
    </div>
</div>

<!-- ================= SCRIPT ================= -->
<script>
    /* ================= PRODUCT CHANGE LOGIC ================= */
    function onProductChange(selectEl) {
        const row = selectEl.closest("tr");

        const productName = row.querySelector(".product-name");
        const brand = row.querySelector(".brand");
        const category = row.querySelector(".category");

        // NEW PRODUCT
        if (selectEl.value === "") {
            productName.readOnly = false;
            brand.disabled = false;
            category.disabled = false;
        }
        // EXISTING PRODUCT
        else {
            productName.readOnly = true;
            productName.value = "";

            brand.disabled = true;
            brand.value = "";

            category.disabled = true;
            category.value = "";
        }
    }

    /* ================= ADD ROW ================= */
    function addModalRow() {
        const tbody = document.getElementById("modalItemsBody");

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

            <td>
                <input type="number" name="quantity[]" min="1"
                       class="form-control ">
            </td>

            <td class="text-center">
                <button type="button"
                        class="btn btn-outline-danger btn-sm"
                        onclick="removeModalRow(this)">❌</button>
            </td>
        `;

        tbody.appendChild(row);
        row.scrollIntoView({behavior: "smooth", block: "end"});
    }

    function removeModalRow(btn) {
        btn.closest("tr").remove();
    }

    /* ================= RESET MODAL WHEN CLOSE ================= */
    let originalModalHtml = "";

    document.addEventListener("DOMContentLoaded", function () {
        const modalContent = document.getElementById("editModalContent");
        const editModal = document.getElementById("editModal");

        // SAVE INITIAL STATE
        originalModalHtml = modalContent.innerHTML;

        // RESET WHEN CLOSE
        editModal.addEventListener("hidden.bs.modal", function () {
            modalContent.innerHTML = originalModalHtml;
        });
    });
</script>
