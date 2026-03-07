<%@ page contentType="text/html;charset=UTF-8" %>


<style>
    .common-table th {
        font-weight: 600;
        white-space: nowrap;
    }

    .common-table td,
    .common-table th {
        vertical-align: middle;
        padding: 10px 14px;
    }

    .common-table tbody tr:hover {
        background-color: #f6f9fc;
    }


    .common-table .badge {
        font-size: 12px;
        padding: 4px 7px;
    }

    .table-fixed {
        table-layout: fixed;
        width: 100%;
    }
</style>

<div class="table-responsive">
    <table class="table table-bordered table-hover common-table table-fixed">
        <thead class="table-primary text-center border">
            ${requestScope.tableHeader}
        </thead>

        <tbody>
            ${requestScope.tableBody}
        </tbody>
    </table>


</div>
