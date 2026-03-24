<%@ page contentType="text/html;charset=UTF-8" %>


<style>
    /* Wrapper */
    .table-container {
        background: #ffffff;
        border-radius: 8px;
        box-shadow: 0 1px 3px rgba(0,0,0,0.1);
        border: 1px solid #e2e8f0;
        margin-bottom: 24px;
        overflow: hidden;
    }

    /* Table */
    .modern-table {
        width: 100%;
        border-collapse: collapse;
        font-family: system-ui, -apple-system, Segoe UI, Roboto, sans-serif;
    }

    /* Header */
    .modern-table thead th {
        position: sticky;
        top: 0;
        z-index: 5;

        background: #1e293b; /* Distinct Dark Header */
        color: #ffffff;

        font-size: 13px;
        font-weight: 600;
        text-transform: uppercase;
        letter-spacing: 0.5px;

        padding: 14px 16px;
        border: 1px solid #cbd5e1;

        text-align: left !important; /* Guaranteed alignment */
    }

    /* Body */
    .modern-table tbody td {
        padding: 14px 16px;
        border: 1px solid #e5e7eb;
        font-size: 14px;
        color: #111827;

        text-align: left !important; /* Guaranteed alignment */
    }

    /* Zebra Striping */
    .modern-table tbody tr:nth-child(odd) {
        background: #ffffff;
    }

    .modern-table tbody tr:nth-child(even) {
        background: #f8fafc;
    }

    /* Hover Effect */
    .modern-table tbody tr:hover td {
        background: #e2e8f0;
        transition: background 0.15s ease;
    }

    /* Ensure no double bottom border on last row due to wrapper */
    .modern-table tbody tr:last-child td {
        border-bottom: none;
    }
</style>

<div class="table-container">
    <table class="modern-table">
        <thead>
            ${requestScope.tableHeader}
        </thead>
        <tbody id="${tbodyId}">
            ${requestScope.tableBody}
        </tbody>
    </table>
</div>
