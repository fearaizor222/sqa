package com.ptithcm.sqa.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ptithcm.sqa.entity.HoaDon;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    Page<HoaDon> findAllByOrderByNgayLapDesc(Pageable pageable);

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h")
    BigDecimal getTotalRevenue();

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.ngayLap = CURRENT_DATE")
    BigDecimal getTodayRevenue();

    @Query("SELECT SUM(h.tongTien) FROM HoaDon h WHERE h.ngayLap BETWEEN ?1 AND ?2")
    BigDecimal getRevenueBetween(Date startDate, Date endDate);

    @Query("SELECT h.ngayLap, SUM(h.tongTien) FROM HoaDon h WHERE h.ngayLap BETWEEN ?1 AND ?2 GROUP BY h.ngayLap ORDER BY h.ngayLap")
    List<Object[]> getRevenueByDay(Date startDate, Date endDate);

    @Query("SELECT h.nguoiDung.tenNguoiDung, SUM(h.tongTien) FROM HoaDon h GROUP BY h.nguoiDung.tenNguoiDung ORDER BY SUM(h.tongTien) DESC")
    List<Object[]> getRevenueByEmployee();

    @Query("SELECT COUNT(h) FROM HoaDon h WHERE h.ngayLap = CURRENT_DATE")
    Long getTodayOrderCount();

    // HoaDon findByMaNguoiDung(Integer maNguoiDung);

    @Query("SELECT h FROM HoaDon h WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = 'chua thanh toan'")
    Optional<HoaDon> findChuaThanhToanByUserId(@Param("manguoidung") Long manguoidung);

    @Query("SELECT h FROM HoaDon h WHERE h.nguoiDung.maNguoiDung = :manguoidung AND h.trangThai = :trangthai")
    List<HoaDon> findByUserIdAndTrangThai(@Param("manguoidung") Long manguoidung,
            @Param("trangthai") String trangthai);

    @Query("SELECT h FROM HoaDon h WHERE h.nguoiDung.maNguoiDung = :maNguoiDung AND h.trangThai = :trangThai")
    List<HoaDon> findByNguoiDungIdAndTrangThai(@Param("maNguoiDung") Long maNguoiDung,
            @Param("trangThai") String trangThai);

}