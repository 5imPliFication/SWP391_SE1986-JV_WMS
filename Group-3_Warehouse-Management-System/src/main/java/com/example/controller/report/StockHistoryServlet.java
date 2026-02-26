package com.example.controller.report;

import com.example.dao.StockMovementDAO;
import com.example.model.MovementType;
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

        LocalDate fromDate = (from != null && !from.isEmpty())
                ? LocalDate.parse(from) : null;

        LocalDate toDate = (to != null && !to.isEmpty())
                ? LocalDate.parse(to) : null;

        MovementType type = (typeParam != null && !typeParam.isEmpty())
                ? MovementType.valueOf(typeParam)
                : null;

        List<StockMovement> list =
                dao.getStockHistory(fromDate, toDate, type);

        request.setAttribute("list", list);

        request.getRequestDispatcher("/WEB-INF/views/stock-history.jsp")
                .forward(request, response);
    }
}