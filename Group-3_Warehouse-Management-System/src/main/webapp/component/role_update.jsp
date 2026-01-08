<!DOCTYPE html>
<html>

    <head>
        <title>Home</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/css/bootstrap-select.min.css">
    </head>

    <body >
        <form action="#"  class="border border-dark w-25 d-flex flex-column justify-content-center p-3">
            <h1 class="text-center">Update Role</h1>
            <div >
                <label for="roleID">Role ID</label>
                <input class="w-100" id="roleID">
            </div>
            <div >
                <label for="roleName">Role Name</label>
                <input class="w-100" id="roleName">
            </div>
            <!-- role permission -->
            <div >
                <label class="form-label">Role Permissions</label>
                <select class="selectpicker w-100" multiple data-live-search="true">
                    <option value="admin">Admin</option>
                    <option value="editor">Editor</option>
                    <option value="viewer">Viewer</option>
                </select>
            </div>
            <div >
                <label for="roleDesscription">Desscription</label> 
                <textarea class="w-100" id="roleDesscription"></textarea>
            </div>

            <button class="btn btn-success w-100 mb-2">Active</button>
            <button class="btn btn-danger w-100 mb-2">Deactive</button>
            <button class="btn btn-primary w-100">Update</button>
        </form>

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap-select@1.13.14/dist/js/bootstrap-select.min.js"></script>
    </body>



</html>