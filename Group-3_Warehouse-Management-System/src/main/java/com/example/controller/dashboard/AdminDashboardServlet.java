package com.example.controller.dashboard;

import com.example.model.User;
import com.example.model.UserActivityLog;
import com.example.service.ActivityLogService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

	private ActivityLogService dashboardService;

	@Override
	public void init() {
		dashboardService = new ActivityLogService();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		User user = (User) req.getSession().getAttribute("user");
		if (user == null) {
			resp.sendRedirect(req.getContextPath() + "/login");
			return;
		}

		if (!"Admin".equals(user.getRole().getName())) {
			resp.sendError(HttpServletResponse.SC_FORBIDDEN);
			return;
		}

		try {
			List<UserActivityLog> recentActivities = dashboardService.getRecentActivities(10);
			dashboardService.loadAdminDashboard(req);
			req.setAttribute("recentActivities", recentActivities);

			req.getRequestDispatcher("/WEB-INF/views/home/admin-dashboard.jsp").forward(req, resp);

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("error", "Failed to load dashboard: " + e.getMessage());
			req.getRequestDispatcher("/WEB-INF/views/error/error.jsp").forward(req, resp);
		}
	}
}
