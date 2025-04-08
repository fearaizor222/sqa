package com.ptithcm.sqa.controller;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.entity.HoaDon;
import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.ChiTietHoaDon;
import com.ptithcm.sqa.repository.ThuocRepository;
import com.ptithcm.sqa.repository.HoaDonRepository;
import com.ptithcm.sqa.repository.ChiTietHoaDonRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/product")
public class ProductDetailController {

    @Autowired
    private ThuocRepository thuocRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @GetMapping("/{tenThuoc}")
    public String productDetail(@PathVariable String tenThuoc, Model model, HttpSession session) {
        Optional<Thuoc> productOpt = thuocRepository.findByTenThuoc(tenThuoc);
        if (productOpt.isPresent()) {
            Thuoc product = productOpt.get();
            System.out.println("Giá của sản phẩm: " + product.getGia());
            double price = product.getGia();
            model.addAttribute("product", product);
            model.addAttribute("formattedPrice", String.format("%,.0f", price));

            Object loggedUser = session.getAttribute("LoggedNguoiDung");
            model.addAttribute("LoggedNguoiDung", loggedUser);

            return "user/product-detail";
        } else {
            return "error/404";
        }
    }

    @PostMapping("/add-to-cart")
    public String addToCart(@RequestParam("quantity") int quantity,
            @RequestParam("tenThuoc") String tenthuoc,
            HttpSession session,
            RedirectAttributes redirectAttributes, Model model) {
        try {
            Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
            if (loggedUserObj == null) {
                redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
                return "redirect:/authen/login";
            }

            NguoiDung loggedUser = (NguoiDung) loggedUserObj;
            Long manguoidung = loggedUser.getMaNguoiDung();

            Optional<Thuoc> productOpt = thuocRepository.findByTenThuoc(tenthuoc);
            if (!productOpt.isPresent()) {
                return "redirect:/";
            }
            Thuoc product = productOpt.get();

            if (manguoidung == null) {
                redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
                return "redirect:/login";
            }
            Optional<HoaDon> hoaDonOpt = hoaDonRepository.findChuaThanhToanByUserId(manguoidung);
            HoaDon hoaDon;
            if (hoaDonOpt.isPresent()) {
                hoaDon = hoaDonOpt.get();
            } else {
                hoaDon = new HoaDon();
                hoaDon.setNguoiDung(loggedUser);
                hoaDon.setTongTien(BigDecimal.ZERO);
                hoaDon.setTrangThai("chua thanh toan");
                hoaDon.setNgayLap(new Date());
                hoaDonRepository.save(hoaDon);
            }

            // Kiểm tra xem sản phẩm đã có trong chi tiết hóa đơn chưa
            Optional<ChiTietHoaDon> existingItemOpt = chiTietHoaDonRepository
                    .findByHoaDonAndThuoc(hoaDon, product); // Tìm dòng đã có sản phẩm này trong hóa đơn

            if (existingItemOpt.isPresent()) {
                // Nếu sản phẩm đã có trong hóa đơn, chỉ cần cập nhật số lượng
                ChiTietHoaDon existingItem = existingItemOpt.get();
                existingItem.setSoLuong(existingItem.getSoLuong() + quantity); // Cộng thêm số lượng
                chiTietHoaDonRepository.save(existingItem); // Lưu lại
            } else {
                // Nếu sản phẩm chưa có trong hóa đơn, thêm một dòng mới
                ChiTietHoaDon chiTiet = new ChiTietHoaDon();
                chiTiet.setHoaDon(hoaDon);
                chiTiet.setThuoc(product);
                chiTiet.setSoLuong(quantity);
                chiTiet.setGiaBan(product.getGia());
                chiTiet.setNgayLap(LocalDateTime.now());
                chiTietHoaDonRepository.save(chiTiet);
            }

            hoaDon.updateTongTien(chiTietHoaDonRepository.sumTongTienByMaHoaDon(hoaDon.getMaHoaDon()));
            hoaDonRepository.save(hoaDon);

            model.addAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
            model.addAttribute("product", product);

            redirectAttributes.addFlashAttribute("success", "Đã thêm sản phẩm vào giỏ hàng!");
            return "user/product-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Có lỗi xảy ra khi thêm vào giỏ hàng.");
            return "user/product-detail";
        }
    }
}
