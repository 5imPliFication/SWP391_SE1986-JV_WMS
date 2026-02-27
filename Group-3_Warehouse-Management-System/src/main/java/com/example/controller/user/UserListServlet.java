package com.example.controller.user;

import com.example.model.Role;
import com.example.model.User;
import com.example.service.RoleService;
import com.example.service.UserService;
import com.example.util.AppConstants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/user/list")
public class UserListServlet extends HttpServlet {

    private UserService userService;
    private RoleService roleService;

    @Override
    public void init() {
        userService = new UserService();
        roleService = new RoleService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // get searchName
        String searchName = request.getParameter("searchName");

        // get type sort
        String typeSort = request.getParameter("sortName");

        // get filter role
        String roleIdStr = request.getParameter("roleId");
        Integer roleId;
        if(roleIdStr != null && !roleIdStr.isEmpty()){
            roleId = Integer.parseInt(roleIdStr);
        } else {
            roleId = null;
        }

        // get role list
        List<Role> roleList = roleService.getAllRoles();

        // set value filter role for jsp
        request.setAttribute("roleList", roleList);
        request.setAttribute("selectedRoleId", roleIdStr);

        // pagination
        int pageNo = 1;

        // get total record
        int totalUsers = userService.getTotalUsers(searchName, roleId);

        // get total pages
        int totalPages = (int)Math.ceil((double) totalUsers/ AppConstants.PAGE_SIZE);

        if (request.getParameter("pageNo") != null) {
            pageNo = Integer.parseInt(request.getParameter("pageNo"));
        }
        List<User> listUsers = userService.getListUsers(searchName, roleId, typeSort, pageNo);
        request.setAttribute("userList", listUsers);
        request.setAttribute("searchName", searchName);
        request.setAttribute("typeSort", typeSort);
        request.setAttribute("pageNo", pageNo);
        request.setAttribute("totalPages", totalPages);
        request.getRequestDispatcher("/WEB-INF/user/user-list.jsp").forward(request, response);

    }
}
