package com.ptithcm.sqa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptithcm.sqa.entity.ChiTietHoaDon;
import com.ptithcm.sqa.entity.ChiTietHoaDonId;

@Repository
public interface ChiTietHoaDonRepository extends JpaRepository<ChiTietHoaDon, ChiTietHoaDonId> {

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