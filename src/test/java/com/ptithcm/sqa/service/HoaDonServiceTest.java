package com.ptithcm.sqa.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ptithcm.sqa.entity.ChiTietHoaDon;
import com.ptithcm.sqa.entity.HoaDon;
import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.NguoiDung.UserRole;
import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.repository.HoaDonRepository;

@ExtendWith(MockitoExtension.class)
public class HoaDonServiceTest {
    
    private List<HoaDon> danhSachHoaDon;
    
    @Mock
    private HoaDonRepository hoaDonRepository;
    
    @InjectMocks
    private HoaDonService hoaDonService;
    
    private NguoiDung nguoiDung;
    private Thuoc thuoc;
    
    @BeforeEach
    void setUp() {
        // Tạo người dùng mẫu
        nguoiDung = new NguoiDung();
        nguoiDung.setMaNguoiDung(1L);
        nguoiDung.setTenNguoiDung("Nguyễn Văn A");
        nguoiDung.setSoDienThoai("0123456789");
        nguoiDung.setDiaChi("123 Đường ABC");
        nguoiDung.setMatKhau("password");
        nguoiDung.setVaiTro(UserRole.khachhang);
        
        // Tạo thuốc mẫu
        thuoc = new Thuoc();
        thuoc.setMaThuoc(1);
        thuoc.setTenThuoc("Paracetamol");
        thuoc.setGia(15000.0);
        
        // Tạo danh sách hóa đơn mẫu
        danhSachHoaDon = new ArrayList<>();
        HoaDon hoaDon1 = new HoaDon();
        hoaDon1.setMaHoaDon(1);
        hoaDon1.setNguoiDung(nguoiDung);
        hoaDon1.setNgayLap(new Date());
        hoaDon1.setTongTien(new BigDecimal("15000"));
        hoaDon1.setTrangThai("Đã thanh toán");
        
        // Thêm chi tiết hóa đơn
        ChiTietHoaDon chiTiet = new ChiTietHoaDon();
        chiTiet.setHoaDon(hoaDon1);
        chiTiet.setThuoc(thuoc);
        chiTiet.setSoLuong(1);
        chiTiet.setGiaBan(new BigDecimal("15000").doubleValue());
        
        List<ChiTietHoaDon> chiTietHoaDon = new ArrayList<>();
        chiTietHoaDon.add(chiTiet);
        hoaDon1.setChiTietHoaDons(chiTietHoaDon);
        
        danhSachHoaDon.add(hoaDon1);
    }
    
    @Test
    @DisplayName("TC_REC_LIST_001: Xem danh sách hóa đơn")
    void testXemDanhSachHoaDon() {
        // Chuẩn bị mock
        Pageable pageable = PageRequest.of(0, 10);
        Page<HoaDon> page = new PageImpl<>(danhSachHoaDon, pageable, danhSachHoaDon.size());
        when(hoaDonRepository.findAllByOrderByNgayLapDesc(pageable)).thenReturn(page);
        
        // Thực hiện
        Page<HoaDon> ketQua = hoaDonService.getAllHoaDon(pageable);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertFalse(ketQua.isEmpty());
        assertEquals(danhSachHoaDon.size(), ketQua.getContent().size());
    }
    
    @Test
    @DisplayName("TC_REC_LIST_002: Phân trang danh sách hóa đơn")
    void testPhanTrangDanhSachHoaDon() {
        // Chuẩn bị
        int soHoaDonTrenTrang = 10;
        int trangHienTai = 1;
        Pageable pageable = PageRequest.of(trangHienTai - 1, soHoaDonTrenTrang);
        Page<HoaDon> page = new PageImpl<>(danhSachHoaDon, pageable, danhSachHoaDon.size());
        when(hoaDonRepository.findAllByOrderByNgayLapDesc(pageable)).thenReturn(page);
        
        // Thực hiện
        Page<HoaDon> ketQua = hoaDonService.getAllHoaDon(pageable);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertTrue(ketQua.getSize() <= soHoaDonTrenTrang);
        assertEquals(danhSachHoaDon.size(), ketQua.getContent().size());
    }
    
    @Test
    @DisplayName("TC_REC_DETAIL_001: Xem chi tiết hóa đơn")
    void testXemChiTietHoaDon() {
        // Chuẩn bị
        int maHoaDon = 1;
        HoaDon hoaDon = danhSachHoaDon.get(0);
        when(hoaDonRepository.findById(maHoaDon)).thenReturn(Optional.of(hoaDon));
        
        // Thực hiện
        HoaDon ketQua = hoaDonService.getHoaDonById(maHoaDon).orElse(null);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals(maHoaDon, ketQua.getMaHoaDon());
        assertNotNull(ketQua.getNguoiDung());
        assertNotNull(ketQua.getChiTietHoaDons());
        assertFalse(ketQua.getChiTietHoaDons().isEmpty());
    }
    
    @Test
    @DisplayName("TC_REC_DETAIL_002: Kiểm tra tính toán giá trị hóa đơn")
    void testTinhToanGiaTriHoaDon() {
        // Chuẩn bị
        HoaDon hoaDon = danhSachHoaDon.get(0);
        ChiTietHoaDon chiTiet = hoaDon.getChiTietHoaDons().get(0);
        
        // Kiểm tra tính toán thành tiền cho từng mặt hàng
        BigDecimal thanhTienDuKien = BigDecimal.valueOf(chiTiet.getGiaBan())
            .multiply(BigDecimal.valueOf(chiTiet.getSoLuong()));
        assertEquals(thanhTienDuKien.doubleValue(), chiTiet.getThanhTien());
        
        // Kiểm tra tổng tiền hóa đơn
        double tongTienDuKien = hoaDon.getChiTietHoaDons().stream()
            .mapToDouble(ChiTietHoaDon::getThanhTien)
            .sum();
        assertEquals(tongTienDuKien, hoaDon.getTongTien().doubleValue());
    }
} 