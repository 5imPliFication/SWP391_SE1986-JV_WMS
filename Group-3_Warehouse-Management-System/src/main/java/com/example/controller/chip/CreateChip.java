/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.example.controller.chip;

import com.example.service.ChipService;
import static com.example.util.ChipFormatter.formatChipName;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author PC
 */
@WebServlet(name = "CreateChip", urlPatterns = {"/create-chip"})
public class CreateChip extends HttpServlet {

    private ChipService c;

    @Override
    public void init() {
        c = new ChipService();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");

        boolean active = Boolean.parseBoolean(
                request.getParameter("active")
        );

        c.CreateChip(formatChipName(name), active);

        response.sendRedirect(
                request.getContextPath()
                + "/specification/chip"
        );
    }

}
