package com.ptithcm.sqa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.service.ThuocService;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ThuocService thuocService;

    @GetMapping("dashboard")
    public String getDashBoard() {
        return "admin/dashboard";
    }
    @GetMapping("medicine")
    public String getMedicine(Model model) {
        List<Thuoc> thuocs = thuocService.getAllThuoc();
        model.addAttribute("thuocs", thuocs);
        return "admin/medicine";
    }
}
