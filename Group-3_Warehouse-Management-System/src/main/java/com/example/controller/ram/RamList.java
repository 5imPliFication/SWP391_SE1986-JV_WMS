/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package com.example.controller.ram;

import com.example.model.Ram;
import com.example.service.RamService;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 *
 * @author PC
 */
@WebServlet(name="RamList", urlPatterns={"/specification/ram"})
public class RamList extends HttpServlet {
   
    private RamService r;
    private static final int PAGE_SIZE = 5;

    @Override
    public void init() {
        r = new RamService();
    }
  
   

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        int pageNo = 1;

        try {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        } catch (Exception e) {
        }

        int totalRams = r.countRam();
        int totalPages = (int) Math.ceil((double) totalRams / PAGE_SIZE);

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }

        HttpSession session = request.getSession(false);
        if (session != null) {
            Object flash = session.getAttribute("flashRamCreateError");
            if (flash != null) {
                request.setAttribute("error", flash);
                session.removeAttribute("flashRamCreateError");
            }
        }

        List<Ram> rams = r.getRamByPage(pageNo, PAGE_SIZE);

        request.setAttribute("rams", rams);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/WEB-INF/specification/ram/ramList.jsp")
                .forward(request, response);
    } 

 
   

}
