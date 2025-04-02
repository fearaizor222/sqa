package com.ptithcm.sqa.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.NguoiDung.UserRole;


@Component
public class RoleInterceptor implements HandlerInterceptor {

    @SuppressWarnings("null")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        HttpSession session = request.getSession();
        NguoiDung loggedInUser = (NguoiDung) session.getAttribute("LoggedNguoiDung");
        
        // Nếu chưa đăng nhập, chuyển hướng đến trang login
        if (loggedInUser == null) {
            response.sendRedirect(request.getContextPath() + "/authen/login");
            return false;
        }
        
        // Lấy đường dẫn yêu cầu
        String requestURI = request.getRequestURI();
        
        // Kiểm tra quyền truy cập dựa trên URI và vai trò người dùng
        if (requestURI.startsWith("/admin")) {
            // Chỉ admin mới có quyền truy cập /admin/**
            if (loggedInUser.getVaiTro() != UserRole.ql) {
                response.sendRedirect(request.getContextPath() + "/access-denied");
                return false;
            }
        }
        return true;
    }
}