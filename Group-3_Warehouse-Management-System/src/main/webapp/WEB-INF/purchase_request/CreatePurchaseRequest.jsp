<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Create Purchase Request</title>
        <style>
            table {
                border-collapse: collapse;
                width: 100%;
            }
            th, td {
                border: 1px solid #ccc;
                padding: 8px;
            }
            th {
                background: #f5f5f5;
            }
            input, select {
                width: 100%;
            }
            .error {
                color: red;
            }
        </style>
    </head>

    <body>

        <h2>Create Purchase Request</h2>

        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/purchase-request/create" method="post">

            <label>Note:</label><br>
            <textarea name="note" rows="3" cols="80"></textarea>

            <br><br>
            <button type="button" onclick="addRow()">➕ Thêm dòng</button>

            <table border="1" width="100%">
                <thead>
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
                    <!-- ===== ROW 0 ===== -->
                    <tr>
                        <td>
                            <select name="productId[]">
                                <option value="">-- New Product --</option>
                                <option value="1">Laptop Dell XPS 13</option>
                                <option value="2">MacBook Air M2</option>
                            </select>
                        </td>

                        <td><input type="text" name="productName[]"></td>
                        <td><input type="text" name="brandName[]"></td>
                        <td><input type="text" name="categoryName[]"></td>
                        <td><input type="number" name="quantity[]" min="1" required></td>
                        <td>
                            <button type="button" onclick="removeRow(this)">❌</button>
                        </td>
                    </tr>
                </tbody>
            </table>

            <br>



            <br>

            <button type="submit">Create Purchase Request</button>

        </form>

    </body>
    <script>
        function addRow() {
            const tbody = document.getElementById("itemsBody");

            const row = document.createElement("tr");
            row.innerHTML = `
        <td>
            <select name="productId[]">
                <option value="">-- New Product --</option>
                <option value="1">Laptop Dell XPS 13</option>
                <option value="2">MacBook Air M2</option>
            </select>
        </td>

        <td><input type="text" name="productName[]"></td>
        <td><input type="text" name="brandName[]"></td>
        <td><input type="text" name="categoryName[]"></td>
        <td><input type="text" name="specs[]"></td>
        <td><input type="number" name="quantity[]" min="1" required></td>
        <td>
            <button type="button" onclick="removeRow(this)">❌</button>
        </td>
    `;
            tbody.appendChild(row);
        }

        function removeRow(btn) {
            const row = btn.closest("tr");
            row.remove();
        }
    </script>

</html>
