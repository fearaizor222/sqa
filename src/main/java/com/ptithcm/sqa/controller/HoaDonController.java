package com.ptithcm.sqa.controller;

import com.ptithcm.sqa.entity.HoaDon;
import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.ChiTietHoaDon;
import com.ptithcm.sqa.repository.HoaDonRepository;
import com.ptithcm.sqa.service.CartService;
import com.ptithcm.sqa.repository.ChiTietHoaDonRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/paid-orders")
public class HoaDonController {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    // --- Hiển thị danh sách hóa đơn đã thanh toán ---
    @GetMapping
    public String getPaidOrders(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
        if (loggedUserObj == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
            return "redirect:/authen/login";
        }

        NguoiDung loggedUser = (NguoiDung) loggedUserObj;
        Long manguoidung = loggedUser.getMaNguoiDung();

        List<HoaDon> paidOrders = hoaDonRepository.findByNguoiDungIdAndTrangThai(manguoidung, "da thanh toan");

        model.addAttribute("paidOrders", paidOrders);
        model.addAttribute("userId", manguoidung);

        return "user/paid-orders";
    }

    // --- Xem chi tiết hóa đơn ---
    @GetMapping("/details/{mahoadon}/{manguoidung}")
    public String getOrderDetails(@PathVariable("mahoadon") Integer mahoadon,
            @PathVariable("manguoidung") Long manguoidung,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
        if (loggedUserObj == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
            return "redirect:/authen/login";
        }

        NguoiDung loggedUser = (NguoiDung) loggedUserObj;
        Long sessionUserId = loggedUser.getMaNguoiDung();

        // Kiểm tra xem user trong session có trùng với user trong URL không
        if (!sessionUserId.equals(manguoidung)) {
            redirectAttributes.addFlashAttribute("error", "Không có quyền truy cập hóa đơn này.");
            return "redirect:/paid-orders";
        }

        List<ChiTietHoaDon> orderDetails = chiTietHoaDonRepository
                .findByHoaDon_MaHoaDonAndHoaDon_NguoiDung_MaNguoiDung(mahoadon, manguoidung);

        model.addAttribute("orderDetails", orderDetails);
        model.addAttribute("userId", manguoidung);

        double totalValue = orderDetails.stream()
                .mapToDouble(ct -> ct.getGiaBan() * ct.getSoLuong()).sum();
        model.addAttribute("totalValue", totalValue);

        return "user/order-details";
    }
}
