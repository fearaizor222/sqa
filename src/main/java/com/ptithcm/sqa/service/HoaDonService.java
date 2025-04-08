package com.ptithcm.sqa.service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ptithcm.sqa.entity.ChiTietHoaDon;
import com.ptithcm.sqa.entity.HoaDon;
import com.ptithcm.sqa.repository.ChiTietHoaDonRepository;
import com.ptithcm.sqa.repository.HoaDonRepository;

@Service
public class HoaDonService {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private ChiTietHoaDonRepository chiTietHoaDonRepository;

    /**
     * Lấy danh sách hóa đơn có phân trang
     */
    public Page<HoaDon> getAllHoaDon(Pageable pageable) {
        return hoaDonRepository.findAllByOrderByNgayLapDesc(pageable);
    }

    /**
     * Lấy thông tin hóa đơn theo ID
     */
    public Optional<HoaDon> getHoaDonById(Integer id) {
        return hoaDonRepository.findById(id);
    }

    /**
     * Lấy chi tiết hóa đơn theo mã hóa đơn
     */
    public List<ChiTietHoaDon> getChiTietByMaHoaDon(Integer maHoaDon) {
        return chiTietHoaDonRepository.findByHoaDon_MaHoaDon(maHoaDon);
    }

    /**
     * Lấy tổng doanh thu
     */
    public BigDecimal getTotalRevenue() {
        BigDecimal total = hoaDonRepository.getTotalRevenue();
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Lấy doanh thu hôm nay
     */
    public BigDecimal getTodayRevenue() {
        BigDecimal total = hoaDonRepository.getTodayRevenue();
        return total != null ? total : BigDecimal.ZERO;
    }

    /**
     * Lấy số đơn hàng hôm nay
     */
    public Long getTodayOrderCount() {
        return hoaDonRepository.getTodayOrderCount();
    }

    /**
     * Lấy doanh thu 7 ngày gần nhất
     */
    public Map<String, Object> getRevenueLastWeek() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -6);
        Date startDate = cal.getTime();
        Date endDate = new Date();

        List<Object[]> results = hoaDonRepository.getRevenueByDay(startDate, endDate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM");
        List<String> labels = new ArrayList<>();
        List<BigDecimal> data = new ArrayList<>();

        // Tạo map để tìm kiếm nhanh
        Map<String, BigDecimal> revenueByDate = new HashMap<>();
        for (Object[] result : results) {
            Date date = (Date) result[0];
            BigDecimal revenue = (BigDecimal) result[1];
            revenueByDate.put(dateFormat.format(date), revenue);
        }

        // Lấy 7 ngày gần nhất
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -6);
        for (int i = 0; i < 7; i++) {
            Date date = cal.getTime();
            String dateStr = dateFormat.format(date);
            labels.add(dateStr);

            BigDecimal revenue = revenueByDate.getOrDefault(dateStr, BigDecimal.ZERO);
            data.add(revenue);

            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("labels", labels);
        result.put("data", data);

        return result;
    }

    /**
     * Lấy danh sách top nhân viên theo doanh thu
     */
    public List<Object[]> getTopEmployeesByRevenue() {
        return hoaDonRepository.getRevenueByEmployee();
    }

    /**
     * Lấy danh sách sản phẩm bán chạy nhất
     */
    public List<Object[]> getTopSellingProducts() {
        return chiTietHoaDonRepository.getTopSellingProducts();
    }

    /**
     * Lấy danh sách sản phẩm bán chạy nhất hôm nay
     */
    public List<Object[]> getTopSellingProductsToday() {
        return chiTietHoaDonRepository.getTopSellingProductsToday();
    }

    public List<HoaDon> getHoaDonByNguoiDungAndTrangThai(Long maNguoiDung, String trangThai) {
        return null;
    }
}