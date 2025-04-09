package com.ptithcm.sqa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.repository.ThuocRepository;

@ExtendWith(MockitoExtension.class)
public class ThuocServiceTest {

    @Mock
    private ThuocRepository thuocRepository;

    @InjectMocks
    private ThuocService thuocService;

    private Thuoc thuocMau;
    private List<Thuoc> danhSachThuoc;

    @BeforeEach
    void setUp() {
        // Tạo thuốc mẫu cho việc kiểm thử
        thuocMau = new Thuoc(
            "Paracetamol", 
            100, 
            15000.0, 
            "36 tháng", 
            "Paracetamol 500mg", 
            "Nơi khô ráo, tránh ánh nắng", 
            "Buồn nôn, chóng mặt", 
            "Hộp 10 vỉ x 10 viên", 
            "https://example.com/paracetamol.jpg", 
            "Công ty Dược phẩm ABC", 
            "Thuốc giảm đau, hạ sốt",
            "Paracetamol 2"
        );
        
        // Tạo danh sách thuốc mẫu
        danhSachThuoc = new ArrayList<>();
        danhSachThuoc.add(thuocMau);
    }

    @Test
    @DisplayName("TC_ADD_001: Thêm thuốc thành công với đầy đủ thông tin hợp lệ")
    void testThemThuocThanhCong() {
        // Chuẩn bị
        Thuoc thuocMoi = new Thuoc(
            "Paracetamol", 
            100, 
            15000.0, 
            "36 tháng", 
            "Paracetamol 500mg", 
            "Nơi khô ráo, tránh ánh nắng", 
            "Buồn nôn, chóng mặt", 
            "Hộp 10 vỉ x 10 viên", 
            "https://example.com/paracetamol.jpg", 
            "Công ty Dược phẩm ABC", 
            "Thuốc giảm đau, hạ sốt",
            "Paracetamol 2"
        );
        
        when(thuocRepository.save(any(Thuoc.class))).thenReturn(thuocMoi);
        
        // Thực hiện
        Thuoc ketQua = thuocService.saveThuoc(thuocMoi);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals("Paracetamol", ketQua.getTenThuoc());
        assertEquals(100, ketQua.getSoLuong());
        assertEquals(15000.0, ketQua.getGia());
        verify(thuocRepository, times(1)).save(any(Thuoc.class));
    }

    @Test
    @DisplayName("TC_ADD_002: Thêm thuốc với các trường bắt buộc để trống")
    void testThemThuocVoiTruongBatBuocTrong() {
        // Chuẩn bị
        Thuoc thuocTrong = new Thuoc();
        thuocTrong.setTenThuoc(""); // Tên trống
        thuocTrong.setGia(15000.0);
        thuocTrong.setSoLuong(100);
        thuocTrong.setTuoiSuDung(""); // Tuổi sử dụng trống
        
        // Thực hiện và kiểm tra
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            thuocService.saveThuoc(thuocTrong);
        });
        
        // Kiểm tra thông báo lỗi
        assertEquals("Tên thuốc không được để trống", exception.getMessage());
        verify(thuocRepository, never()).save(any(Thuoc.class));
    }

    @Test
    @DisplayName("TC_ADD_003: Thêm thuốc với giá trị âm cho giá tiền và số lượng")
    void testThemThuocVoiGiaAmVaSoLuongAm() {
        // Chuẩn bị
        Thuoc thuocGiaAm = new Thuoc();
        thuocGiaAm.setTenThuoc("Paracetamol Test");
        thuocGiaAm.setGia(-5000.0);  // Giá âm
        thuocGiaAm.setSoLuong(-10);   // Số lượng âm
        thuocGiaAm.setTuoiSuDung("36 tháng");
        
        // Thực hiện và kiểm tra
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            thuocService.saveThuoc(thuocGiaAm);
        });
        
        // Kiểm tra thông báo lỗi
        assertEquals("Giá thuốc không được âm", exception.getMessage());
        verify(thuocRepository, never()).save(any(Thuoc.class));
    }

    @Test
    @DisplayName("TC_EDIT_001: Chỉnh sửa thông tin thuốc thành công")
    void testChinhSuaThuocThanhCong() {
        // Chuẩn bị
        Thuoc thuocCapNhat = new Thuoc(
            "Paracetamol Cập Nhật", 
            150, 
            20000.0, 
            "48 tháng", 
            "Paracetamol 500mg", 
            "Nơi khô ráo, tránh ánh nắng", 
            "Buồn nôn, chóng mặt", 
            "Hộp 10 vỉ x 10 viên", 
            "https://example.com/paracetamol.jpg", 
            "Công ty Dược phẩm ABC", 
            "Thuốc giảm đau, hạ sốt",
            "Paracetamol Cập Nhật 2"
        );
        
        when(thuocRepository.findById(1)).thenReturn(Optional.of(thuocMau));
        when(thuocRepository.save(any(Thuoc.class))).thenReturn(thuocCapNhat);
        
        // Thực hiện
        Optional<Thuoc> thuocOptional = thuocService.getThuocById(1);
        assertTrue(thuocOptional.isPresent());
        
        Thuoc thuoc = thuocOptional.get();
        thuoc.setTenThuoc(thuocCapNhat.getTenThuoc());
        thuoc.setSoLuong(thuocCapNhat.getSoLuong());
        thuoc.setGia(thuocCapNhat.getGia());
        thuoc.setTuoiSuDung(thuocCapNhat.getTuoiSuDung());
        
        Thuoc ketQua = thuocService.saveThuoc(thuoc);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals("Paracetamol Cập Nhật", ketQua.getTenThuoc());
        assertEquals(150, ketQua.getSoLuong());
        assertEquals(20000.0, ketQua.getGia());
        assertEquals("48 tháng", ketQua.getTuoiSuDung());
        verify(thuocRepository, times(1)).findById(1);
        verify(thuocRepository, times(1)).save(any(Thuoc.class));
    }

    @Test
    @DisplayName("TC_EDIT_002: Chỉnh sửa thuốc với các trường bắt buộc để trống")
    void testChinhSuaThuocVoiCacTruongBatBuocTrong() {
        // Chuẩn bị
        Thuoc thuocCapNhat = new Thuoc(
            "", // Tên rỗng
            150, 
            20000.0, 
            "", // Tuổi sử dụng rỗng
            "Paracetamol 500mg", 
            "Nơi khô ráo, tránh ánh nắng", 
            "Buồn nôn, chóng mặt", 
            "Hộp 10 vỉ x 10 viên", 
            "https://example.com/paracetamol.jpg", 
            "Công ty Dược phẩm ABC", 
            "Thuốc giảm đau, hạ sốt",
            ""
        );
        
        when(thuocRepository.findById(1)).thenReturn(Optional.of(thuocMau));
        
        // Thực hiện
        Optional<Thuoc> thuocOptional = thuocService.getThuocById(1);
        assertTrue(thuocOptional.isPresent());
        
        Thuoc thuoc = thuocOptional.get();
        thuoc.setTenThuoc(thuocCapNhat.getTenThuoc());
        thuoc.setTuoiSuDung(thuocCapNhat.getTuoiSuDung());
        
        // Thực hiện và kiểm tra
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            thuocService.saveThuoc(thuoc);
        });
        
        assertEquals("Tên thuốc không được để trống", exception.getMessage());
        verify(thuocRepository, times(1)).findById(1);
        verify(thuocRepository, never()).save(any(Thuoc.class));
    }

    @Test
    @DisplayName("TC_EDIT_003: Chỉnh sửa giá thuốc thành giá trị âm")
    void testChinhSuaGiaThuocThanhGiaTriAm() {
        // Chuẩn bị
        Thuoc thuocCapNhat = new Thuoc(
            "Paracetamol", 
            100, 
            -15000.0, // Giá âm
            "36 tháng", 
            "Paracetamol 500mg", 
            "Nơi khô ráo, tránh ánh nắng", 
            "Buồn nôn, chóng mặt", 
            "Hộp 10 vỉ x 10 viên", 
            "https://example.com/paracetamol.jpg", 
            "Công ty Dược phẩm ABC", 
            "Thuốc giảm đau, hạ sốt",
            "Paracetamol 2"
        );
        
        when(thuocRepository.findById(1)).thenReturn(Optional.of(thuocMau));
        
        // Thực hiện
        Optional<Thuoc> thuocOptional = thuocService.getThuocById(1);
        assertTrue(thuocOptional.isPresent());
        
        Thuoc thuoc = thuocOptional.get();
        thuoc.setGia(thuocCapNhat.getGia());
        
        // Thực hiện và kiểm tra
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            thuocService.saveThuoc(thuoc);
        });
        
        assertEquals("Giá thuốc không được âm", exception.getMessage());
        verify(thuocRepository, times(1)).findById(1);
        verify(thuocRepository, never()).save(any(Thuoc.class));
    }

    @Test
    @DisplayName("TC_EDIT_004: Chỉnh sửa số lượng thuốc thành số âm")
    void testChinhSuaSoLuongThuocThanhSoAm() {
        // Chuẩn bị
        Thuoc thuocCapNhat = new Thuoc(
            "Paracetamol", 
            -100, // Số lượng âm
            15000.0, 
            "36 tháng", 
            "Paracetamol 500mg", 
            "Nơi khô ráo, tránh ánh nắng", 
            "Buồn nôn, chóng mặt", 
            "Hộp 10 vỉ x 10 viên", 
            "https://example.com/paracetamol.jpg", 
            "Công ty Dược phẩm ABC", 
            "Thuốc giảm đau, hạ sốt",
            "Paracetamol 2"
        );
        
        when(thuocRepository.findById(1)).thenReturn(Optional.of(thuocMau));
        
        // Thực hiện
        Optional<Thuoc> thuocOptional = thuocService.getThuocById(1);
        assertTrue(thuocOptional.isPresent());
        
        Thuoc thuoc = thuocOptional.get();
        thuoc.setSoLuong(thuocCapNhat.getSoLuong());
        
        // Thực hiện và kiểm tra
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            thuocService.saveThuoc(thuoc);
        });
        
        assertEquals("Số lượng thuốc không được âm", exception.getMessage());
        verify(thuocRepository, times(1)).findById(1);
        verify(thuocRepository, never()).save(any(Thuoc.class));
    }

    @Test
    @DisplayName("TC_DEL_001: Xóa thuốc thành công")
    void testXoaThuocThanhCong() {
        // Chuẩn bị
        int thuocId = 1;
        when(thuocRepository.findById(thuocId)).thenReturn(Optional.of(thuocMau));
        doNothing().when(thuocRepository).deleteById(thuocId);
        
        // Thực hiện
        Optional<Thuoc> thuocOptional = thuocService.getThuocById(thuocId);
        assertTrue(thuocOptional.isPresent());
        
        thuocService.deleteThuoc(thuocId);
        
        // Kiểm tra
        verify(thuocRepository, times(1)).findById(thuocId);
        verify(thuocRepository, times(1)).deleteById(thuocId);
    }
    
    @Test
    @DisplayName("TC_LIST_001: Phân trang danh sách thuốc")
    void testHienThiDanhSachThuoc() {
        // Chuẩn bị
        when(thuocRepository.findAll()).thenReturn(danhSachThuoc);
        
        // Thực hiện
        List<Thuoc> ketQua = thuocService.getAllThuoc();
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals(1, ketQua.size());
        assertEquals("Paracetamol", ketQua.get(0).getTenThuoc());
        verify(thuocRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TC_LIST_002: Hiển thị chi tiết thông tin thuốc")
    void testHienThiChiTietThuoc() {
        // Chuẩn bị
        int idThuoc = 1;
        Thuoc thuoc = danhSachThuoc.get(0);
        when(thuocRepository.findById(idThuoc)).thenReturn(Optional.of(thuoc));

        // Thực hiện
        Thuoc ketQua = thuocService.getThuocById(idThuoc).orElse(null);

        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals(thuoc.getTenThuoc(), ketQua.getTenThuoc());
        assertEquals(thuoc.getGia(), ketQua.getGia());
        assertEquals(thuoc.getMoTa(), ketQua.getMoTa());
        assertNotNull(ketQua.getHinhAnh());
    }
}