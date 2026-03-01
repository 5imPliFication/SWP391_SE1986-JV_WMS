package com.example.controller.report;

import com.example.dao.StockMovementDAO;
import com.example.enums.MovementType;
import com.example.enums.ReferenceType;
import com.example.model.StockMovement;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/stock-history")
public class StockHistoryServlet extends HttpServlet {

    private final StockMovementDAO dao = new StockMovementDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String from = request.getParameter("fromDate");
        String to = request.getParameter("toDate");
        String typeParam = request.getParameter("type");
        String refTypeParam = request.getParameter("referenceType");
        String pageParam = request.getParameter("page");

        LocalDate fromDate = (from != null && !from.isEmpty())
                ? LocalDate.parse(from) : null;

        LocalDate toDate = (to != null && !to.isEmpty())
                ? LocalDate.parse(to) : null;

        MovementType type = (typeParam != null && !typeParam.isEmpty())
                ? MovementType.valueOf(typeParam)
                : null;

        ReferenceType referenceType = (refTypeParam != null && !refTypeParam.isEmpty())
                ? ReferenceType.valueOf(refTypeParam)
                : null;

        int page = 1;
        if (pageParam != null && !pageParam.isEmpty()) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int limit = 50;
        int offset = (page - 1) * limit;

        int totalCount = dao.getTotalCount(fromDate, toDate, type, referenceType);
        int totalPages = (int) Math.ceil((double) totalCount / limit);

        List<StockMovement> list =
                dao.getStockHistory(fromDate, toDate, type, referenceType, limit, offset);

        request.setAttribute("list", list);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("fromDate", from);
        request.setAttribute("toDate", to);
        request.setAttribute("type", typeParam);
        request.setAttribute("referenceType", refTypeParam);

        request.getRequestDispatcher("/WEB-INF/manager/stock-history.jsp")
                .forward(request, response);
    }
}