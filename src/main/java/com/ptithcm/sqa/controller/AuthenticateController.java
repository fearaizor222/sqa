package com.ptithcm.sqa.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.NguoiDung.UserRole;
import com.ptithcm.sqa.repository.NguoiDungRepository;
import com.ptithcm.sqa.service.AuthenticateService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/authen")
public class AuthenticateController {
    @Autowired
    AuthenticateService authenticateService;

    @Autowired
    NguoiDungRepository nguoiDungRepository;

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "authen/login"; // Return the login page view
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {
        Optional<NguoiDung> isAuthenticated = authenticateService.authenticate(username, password);
        if (isAuthenticated.isPresent()) {
            session.setAttribute("LoggedNguoiDung",isAuthenticated.get());
            if(isAuthenticated.get().getVaiTro() == UserRole.ql) {
                return "redirect:/admin/dashboard";
            }
            else if (isAuthenticated.get().getVaiTro() == UserRole.khachhang) {
                return "redirect:/user/dashboard";
            }
        } else {
            model.addAttribute("messages", List.of("Invalid username or password"));
            return "authen/login"; // Return to login page with error message
        }
        // Handle unanticipated roles explicitly
        model.addAttribute("messages", List.of("Unexpected user role. Please contact support."));
        return "authen/login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        NguoiDung nguoiDung = new NguoiDung();
        model.addAttribute("NewNguoiDung", nguoiDung);
        return "authen/register";
    }

    @PostMapping("/register")
    public String register(NguoiDung nguoiDung, Model model) {
        nguoiDung.setVaiTro(UserRole.nvgh);
        boolean isRegistered = authenticateService.register(nguoiDung);
        if (isRegistered) {
            return "redirect:/authen/login"; // Redirect to home page on successful login
        } else {
            model.addAttribute("messages", List.of("Error"));
            return "authen/register"; // Return to login page with error message
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Invalidate the session to log out the user
        return "redirect:/authen/login"; // Redirect to login page after logout
    }
}
