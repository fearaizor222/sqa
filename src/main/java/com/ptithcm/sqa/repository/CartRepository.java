package com.ptithcm.sqa.repository;

import com.ptithcm.sqa.entity.ChiTietHoaDon;
import com.ptithcm.sqa.entity.ChiTietHoaDonId;
import com.ptithcm.sqa.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<ChiTietHoaDon, ChiTietHoaDonId> {

        // Lấy danh sách sản phẩm trong giỏ hàng
        @Query("SELECT new com.ptithcm.sqa.model.CartItem(ct.thuoc.maThuoc, ct.thuoc.tenThuoc, ct.thuoc.hinhAnh, ct.soLuong, ct.giaBan, ct.hoaDon.maHoaDon) "
                        + "FROM ChiTietHoaDon ct "
                        + "JOIN ct.thuoc t "
                        + "WHERE ct.hoaDon.maHoaDon IN (SELECT h.maHoaDon FROM HoaDon h WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = 'chua thanh toan')")
        List<CartItem> findCartItemsForUser(@Param("manguoidung") Long manguoidung);

        // Tính tổng giá trị giỏ hàng
        @Query("SELECT SUM(ct.soLuong * ct.giaBan) " +
                        "FROM ChiTietHoaDon ct " +
                        "WHERE ct.hoaDon.maHoaDon IN (SELECT h.maHoaDon FROM HoaDon h WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = 'chua thanh toan')")
        Double calculateTotalValue(@Param("manguoidung") Long manguoidung);

        // Xóa sản phẩm khỏi giỏ hàng
        @Modifying
        @Transactional
        @Query("DELETE FROM ChiTietHoaDon ct WHERE ct.thuoc.maThuoc = :productId AND ct.hoaDon.maHoaDon IN (SELECT h.maHoaDon FROM HoaDon h WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = 'chua thanh toan')")
        void removeProductFromCart(@Param("productId") int productId, @Param("manguoidung") Long manguoidung);

        // Xử lý thanh toán
        @Modifying
        @Transactional
        @Query("UPDATE HoaDon h SET h.trangThai = 'da thanh toan' WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = 'chua thanh toan'")
        void updateHoaDonToPaid(@Param("manguoidung") Long manguoidung);

        // Hủy đơn hàng
        @Modifying
        @Transactional
        @Query("UPDATE HoaDon h SET h.trangThai = 'da huy' WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = 'chua thanh toan'")
        void cancelOrder(@Param("manguoidung") Long manguoidung);
}
