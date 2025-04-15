package com.ptithcm.sqa.controller;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.service.NguoiDungService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/dashboard")
    public String getDashBoard() {
        return "user/dashboard";
    }

    @GetMapping("/userinfo")
    public String getUserInfo(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
        if (loggedUserObj == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
            return "redirect:/authen/login";
        }
        NguoiDung loggedUser = (NguoiDung) loggedUserObj;
        Long manguoidung = loggedUser.getMaNguoiDung();

        NguoiDung nguoiDung = nguoiDungService.getNguoiDungById(manguoidung);
        model.addAttribute("nguoiDung", nguoiDung);
        return "user/userinfo";
    }

    @PostMapping("/userinfo")
    public String updateUserInfo(@ModelAttribute("nguoiDung") NguoiDung nguoiDung, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");

        if (loggedUserObj == null) {
            return "redirect:/authen/login";
        }

        NguoiDung loggedUser = (NguoiDung) loggedUserObj;

        NguoiDung currentNguoiDung = nguoiDungService.getNguoiDungById(loggedUser.getMaNguoiDung());
        nguoiDung.setMaNguoiDung(loggedUser.getMaNguoiDung());
        nguoiDung.setMatKhau(currentNguoiDung.getMatKhau());
        nguoiDung.setVaiTro(currentNguoiDung.getVaiTro());

        try {
            nguoiDungService.updateNguoiDung(nguoiDung);
            redirectAttributes.addFlashAttribute("success", "Cập nhập thông tin thành công!");
        } catch (IllegalArgumentException e) {
            String errorMessage = e.getMessage();
            redirectAttributes.addFlashAttribute("error", errorMessage);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhập thông tin thất bại.");
        }
        return "redirect:/user/userinfo";
    }
}