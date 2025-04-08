package com.ptithcm.sqa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class LogoutController {

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // Xóa session của người dùng
        session.invalidate();
        // Chuyển hướng về trang đăng nhập
        return "redirect:/login";
    }
} 