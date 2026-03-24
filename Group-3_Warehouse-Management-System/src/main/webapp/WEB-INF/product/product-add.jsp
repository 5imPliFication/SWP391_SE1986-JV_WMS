<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Product</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            background-color: #f4f7f6;
        }

        .main-content {
            padding: 20px;
        }

        .section-title {
            font-size: 1rem;
            font-weight: 700;
            color: #334155;
            margin-bottom: 1.5rem;
            border-left: 4px solid #4e73df;
            padding-left: 10px;
        }

        .card {
            border: none;
            border-radius: 12px;
            transition: all 0.3s;
        }

        .form-label {
            font-weight: 500;
            color: #64748b;
            font-size: 0.9rem;
        }

        .form-control, .form-select {
            border-radius: 8px;
            padding: 0.6rem 1rem;
            border: 1px solid #e2e8f0;
        }

        .form-control:focus {
            border-color: #4e73df;
            box-shadow: 0 0 0 3px rgba(78, 115, 223, 0.1);
        }

        .sticky-sidebar {
            position: sticky;
            top: 20px;
        }

        #preview {
            width: 100%;
            height: 250px;
            object-fit: contain;
            border-radius: 12px;
            background: #f8fafc;
            border: 2px dashed #cbd5e1;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class="main-content">
    <form action="${pageContext.request.contextPath}/products/add" method="post" enctype="multipart/form-data">
        <div class="container-fluid">

            <h2 class="mb-5">Create New Product</h2>

            <div class="row g-4">
                <div class="col-xl-9 col-lg-8">
                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h5 class="section-title">General Information</h5>
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label">Category</label>
                                    <select name="categoryId" class="form-select" required>
                                        <option value="">Select Category</option>
                                        <c:forEach items="${categories}" var="c">
                                            <option value="${c.id}">${c.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Brand</label>
                                    <select id="brandSelect" name="brandId" class="form-select" required>
                                        <option value="">Select Brand</option>
                                        <c:forEach items="${brands}" var="b">
                                            <option value="${b.id}">${b.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Model</label>
                                    <select id="modelSelect" name="modelId" class="form-select" required>
                                        <option value="">Select Model</option>
                                        <c:forEach var="m" items="${models}">
                                            <option value="${m.id}" data-brand="${m.brand.id}">${m.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h5 class="section-title">Technical Specifications</h5>
                            <div class="row g-3">
                                <div class="col-md-3">
                                    <label class="form-label">Chipset</label>
                                    <select name="chipId" class="form-select" required>
                                        <option value="">Select Chip</option>
                                        <c:forEach items="${chips}" var="c">
                                            <option value="${c.id}">${c.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">RAM Memory</label>
                                    <select name="ramId" class="form-select" required>
                                        <option value="">Select Ram</option>
                                        <c:forEach items="${rams}" var="c">
                                            <option value="${c.id}">${c.size}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Storage</label>
                                    <select name="storageId" class="form-select" required>
                                        <option value="">Select Storage</option>
                                        <c:forEach items="${storages}" var="c">
                                            <option value="${c.id}">${c.size}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Screen Size</label>
                                    <select name="sizeId" class="form-select" required>
                                        <option value="">Select Size</option>
                                        <c:forEach items="${sizes}" var="c">
                                            <option value="${c.id}">${c.size}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card shadow-sm">
                        <div class="card-body p-4">
                            <h5 class="section-title">Final Details</h5>
                            <div class="mb-4">
                                <label class="form-label">Auto-generated Product Name</label>
                                <input type="text" name="productName" class="form-control bg-light fw-bold text-primary"
                                       readonly
                                       placeholder="Automatically generated with format: [Brand] [Model] [Chip] [Ram] [Storage] [Screen]">
                            </div>
                            <div class="mb-0">
                                <label class="form-label">Product Description</label>
                                <textarea name="productDescription" class="form-control" rows="5"
                                          placeholder="Tell us more about this product..." required></textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-lg-4">
                    <div class="sticky-sidebar">
                        <div class="card shadow-sm mb-4">
                            <div class="card-body p-4">
                                <h5 class="section-title">Product Image</h5>
                                <div class="text-center">
                                    <img id="preview" src=""
                                         alt="Preview" class="mb-3">
                                    <input type="file" name="imageFile" id="imageInput" class="form-control"
                                           accept="image/*" required>
                                    <small class="text-muted d-block mt-2">Recommended size: 800x800px</small>
                                </div>
                            </div>
                        </div>

                        <div class="card shadow-sm">
                            <div class="card-body p-4">
                                <h5 class="section-title">Inventory</h5>
                                <label class="form-label">Measurement Unit</label>
                                <select name="unitId" class="form-select" required>
                                    <option value="">Select Unit</option>
                                    <c:forEach items="${units}" var="u">
                                        <option value="${u.id}">${u.name}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div class="d-grid gap-2 mt-5">
                            <button type="submit" class="btn btn-primary px-4 shadow-sm">Save Product</button>
                            <a href="${pageContext.request.contextPath}/products"
                               class="btn btn-outline-secondary px-4">Cancel</a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    // Giữ nguyên Logic lọc Model của bạn
    document.getElementById("brandSelect").addEventListener("change", function () {
        let brandId = this.value;
        let options = document.querySelectorAll("#modelSelect option");
        options.forEach(opt => {
            let modelBrand = opt.dataset.brand;
            if (!modelBrand) return;
            opt.style.display = (brandId === "" || modelBrand === brandId) ? "" : "none";
        });
        document.getElementById("modelSelect").value = "";
    });

    // Preview ảnh mượt mà hơn
    document.getElementById("imageInput").addEventListener("change", function (event) {
        const [file] = event.target.files;
        if (file) {
            const img = document.getElementById("preview");
            img.src = URL.createObjectURL(file);
            img.style.borderStyle = "solid";
        }
    });
</script>
</body>
</html>