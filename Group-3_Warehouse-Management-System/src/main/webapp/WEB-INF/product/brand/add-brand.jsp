<div class="modal fade" id="addBrandModal" tabindex="-1">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content">

            <form action="brand" method="post">

                <div class="modal-header">
                    <h5 class="modal-title">Add New Brand</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>

                <div class="modal-body">

                    <div class="mb-3">
                        <label class="form-label text-center">Brand name</label>
                        <input type="text" name="name" class="form-control" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Description</label>
                        <textarea name="description" class="form-control"></textarea>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Status</label>
                        <select name="active" class="form-select">
                            <option value="true">Active</option>
                            <option value="false">Deactive</option>
                        </select>
                    </div>

                </div>

                <div class="modal-footer col justify-content-center">
                    <button type="submit" class="btn btn-success col-md-5">Add</button>
                    <button type="reset" class="btn btn-secondary col-md-5">
                        Clear
                    </button>
                </div>

            </form>

        </div>
    </div>
</div>
