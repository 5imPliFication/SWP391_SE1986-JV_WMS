<%@ page contentType="text/html;charset=UTF-8" %>


<style>
    body{
        font-family: Arial, sans-serif;
        background:#f5f6fa;
    }

    /* card */
    .table-card{
        width:100%;
        background:white;
        border-radius:12px;
        overflow:hidden;
        box-shadow:0 4px 12px rgba(0,0,0,0.08);
    }

    /* scroll */
    .table-scroll{
        max-height:500px;
        overflow-y:auto;
    }

    /* table */
    .modern-table{
        width:100%;
        border-collapse:collapse;
    }

    /* header */
    .modern-table thead{
        background:linear-gradient(90deg,#6670c9,#6f79d8);
        color:white;
    }
    .modern-table thead th{
        position: sticky;
        top: 0;
        z-index: 10;
        background: linear-gradient(90deg,#6670c9,#6f79d8); /* cần có lại background */
    }

    .modern-table th{
        text-align:left;
        padding:18px 22px;
        font-weight:600;
        font-size:16px;
    }

    /* body */
    .modern-table td{
        padding:18px 22px;
        color:#5d6b7c;
    }

    /* zebra rows */
    .modern-table tbody tr:nth-child(odd){
        background:#f1f2f7;
    }

    .modern-table tbody tr:nth-child(even){
        background:#e9e9ef;
    }

    /* hover */
    .modern-table tbody tr:hover{
        background:#dde2ff;
        transition:0.2s;
    }



</style>

<div class="table-scroll">

    <table class="modern-table">
        <thead class="table-primary text-center border">
            ${requestScope.tableHeader}
        </thead>

        <tbody  id="${tbodyId}" class="text-left">
            ${requestScope.tableBody}
        </tbody>
    </table>
</div>
