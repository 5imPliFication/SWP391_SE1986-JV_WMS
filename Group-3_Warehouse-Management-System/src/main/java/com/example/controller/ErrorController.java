package com.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/error")
public class ErrorController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleError(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleError(req, resp);
    }

    private void handleError(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Get error attributes
        Integer statusCode = (Integer) req.getAttribute("javax.servlet.error.status_code");
        String message = (String) req.getAttribute("javax.servlet.error.message");
        Throwable exception = (Throwable) req.getAttribute("javax.servlet.error.exception");
        String requestUri = (String) req.getAttribute("javax.servlet.error.request_uri");

        // Log the error
        if (exception != null) {
            System.err.println("Error occurred at: " + requestUri);
            System.err.println("Status Code: " + statusCode);
            System.err.println("Message: " + message);
            exception.printStackTrace();
        }

        // Set attributes for JSP
        req.setAttribute("statusCode", statusCode != null ? statusCode : 500);
        req.setAttribute("errorMessage", message);
        req.setAttribute("exception", exception);
        req.setAttribute("requestUri", requestUri);

        // Forward to error page
        req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
    }
}