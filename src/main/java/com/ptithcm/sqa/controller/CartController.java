package com.ptithcm.sqa.controller;

import com.ptithcm.sqa.service.CartService;
import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.model.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // Hiển thị giỏ hàng
    @GetMapping
    public String showCart(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
        if (loggedUserObj == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
            return "redirect:/authen/login";
        }
        NguoiDung loggedUser = (NguoiDung) loggedUserObj;
        Long manguoidung = loggedUser.getMaNguoiDung();
        List<CartItem> cartItems = cartService.getCartItems(manguoidung);
        double totalValue = cartService.calculateTotalValue(manguoidung);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalValue", totalValue);

        return "user/cart";
    }

    // Xóa sản phẩm khỏi giỏ hàng
    @PostMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable("productId") int productId, HttpSession session,
            RedirectAttributes redirectAttributes) {
        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
        if (loggedUserObj == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
            return "redirect:/authen/login";
        }
        NguoiDung loggedUser = (NguoiDung) loggedUserObj;
        Long manguoidung = loggedUser.getMaNguoiDung();

        cartService.removeFromCart(productId, manguoidung);
        return "redirect:/cart";
    }

    // Xử lý thanh toán
    @PostMapping("/payment")
    public String processPayment(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
        if (loggedUserObj == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
            return "redirect:/authen/login";
        }
        NguoiDung loggedUser = (NguoiDung) loggedUserObj;
        Long manguoidung = loggedUser.getMaNguoiDung();

        boolean success = cartService.processPayment(manguoidung);
        if (success) {
            redirectAttributes.addFlashAttribute("success", "Thanh toán thành công!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Lỗi không thể thanh toán.");
        }
        return "redirect:/cart";
    }

    // Hủy đơn hàng
    @PostMapping("/cancel")
    public String cancelOrder(HttpSession session, RedirectAttributes redirectAttributes) {
        Object loggedUserObj = session.getAttribute("LoggedNguoiDung");
        if (loggedUserObj == null) {
            redirectAttributes.addFlashAttribute("error", "Bạn cần đăng nhập.");
            return "redirect:/authen/login";
        }
        NguoiDung loggedUser = (NguoiDung) loggedUserObj;
        Long manguoidung = loggedUser.getMaNguoiDung();

        cartService.cancelOrder(manguoidung);

        try {
            cartService.cancelOrder(manguoidung);
            redirectAttributes.addFlashAttribute("success", "Hủy đơn hàng thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Không thể hủy đơn hàng.");
        }
        return "redirect:/cart";
    }
}
