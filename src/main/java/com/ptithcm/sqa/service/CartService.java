package com.ptithcm.sqa.service;

import com.ptithcm.sqa.model.CartItem;
import com.ptithcm.sqa.repository.CartRepository;
import com.ptithcm.sqa.repository.ChiTietHoaDonRepository;
import com.ptithcm.sqa.repository.HoaDonRepository;
import com.ptithcm.sqa.repository.ThuocRepository;
import com.ptithcm.sqa.entity.HoaDon;
import com.ptithcm.sqa.entity.Thuoc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ThuocRepository thuocRepository;

    // Lấy danh sách các sản phẩm trong giỏ hàng
    public List<CartItem> getCartItems(Long manguoidung) {
        return cartRepository.findCartItemsForUser(manguoidung);
    }

    // Tính tổng giá trị giỏ hàng
    public double calculateTotalValue(Long manguoidung) {
        Double totalValue = cartRepository.calculateTotalValue(manguoidung);
        if (totalValue == null) {
            return 0.0;
        }
        return totalValue;
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public void removeFromCart(int productId, Long manguoidung) {
        cartRepository.removeProductFromCart(productId, manguoidung);
    }

    // Xử lý thanh toán
    @Transactional
    public boolean processPayment(Long manguoidung) {
        // Tính tổng tiền giỏ hàng
        List<CartItem> cartItems = cartRepository.findCartItemsForUser(manguoidung);
        Double totalValue = cartItems.stream()
                .mapToDouble(item -> item.getSoluong() * item.getGia())
                .sum();

        // Kiểm tra số lượng kho
        List<String> insufficientItems = new ArrayList<>();
        for (CartItem item : cartItems) {
            Optional<Thuoc> thuoc = thuocRepository.findById(item.getMathuoc());
            if (thuoc.isPresent()) {
                Thuoc product = thuoc.get();
                if (product.getSoLuong() < item.getSoluong()) {
                    insufficientItems.add(product.getTenThuoc());
                } else {
                    product.setSoLuong(product.getSoLuong() - item.getSoluong());
                    thuocRepository.save(product);
                }
            }
        }

        if (!insufficientItems.isEmpty()) {
            return false;
        }

        cartRepository.updateHoaDonToPaid(manguoidung);
        return true;
    }

    // Hủy đơn hàng
    @Transactional
    public void cancelOrder(Long manguoidung) {
        cartRepository.cancelOrder(manguoidung);
    }
}
