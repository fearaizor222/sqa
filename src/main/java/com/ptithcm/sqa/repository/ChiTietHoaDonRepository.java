package com.ptithcm.sqa.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ptithcm.sqa.entity.ChiTietHoaDon;
import com.ptithcm.sqa.entity.ChiTietHoaDonId;
import com.ptithcm.sqa.entity.HoaDon;
import com.ptithcm.sqa.entity.Thuoc;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, ChiTietHoaDonId> {
        @Query("SELECT SUM(cth.giaBan * cth.soLuong) FROM ChiTietHoaDon cth WHERE cth.hoaDon.maHoaDon = :maHoaDon")
        BigDecimal sumTongTienByMaHoaDon(@Param("maHoaDon") Integer maHoaDon);

        @Query("SELECT h FROM HoaDon h WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = :trangthai")
        List<HoaDon> findHoaDonsByUserIdAndStatus(@Param("manguoidung") int manguoidung,
                        @Param("trangthai") String trangthai);

        Optional<ChiTietHoaDon> findByHoaDonAndThuoc(HoaDon hoaDon, Thuoc thuoc);

        List<ChiTietHoaDon> findByHoaDon_MaHoaDonAndHoaDon_NguoiDung_MaNguoiDung(Integer maHoaDon, Long maNguoiDung);

        List<ChiTietHoaDon> findByHoaDon_MaHoaDon(Integer maHoaDon);

        @Query("SELECT c.thuoc.tenThuoc, SUM(c.soLuong) FROM ChiTietHoaDon c GROUP BY c.thuoc.tenThuoc ORDER BY SUM(c.soLuong) DESC")
        List<Object[]> getTopSellingProducts();

        @Query(value = "SELECT t.tenthuoc, SUM(c.soluong) FROM chitiethoadon c " +
                        "JOIN thuoc t ON c.mathuoc = t.id " +
                        "JOIN hoadon h ON c.mahoadon = h.mahoadon " +
                        "WHERE h.ngaylap = CURRENT_DATE " +
                        "GROUP BY t.tenthuoc ORDER BY SUM(c.soluong) DESC LIMIT 5", nativeQuery = true)
        List<Object[]> getTopSellingProductsToday();

}