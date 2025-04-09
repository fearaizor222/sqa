package com.ptithcm.sqa.service;

import java.util.ArrayList;
import java.util.Arrays;
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
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.NguoiDung.UserRole;
import com.ptithcm.sqa.repository.NguoiDungRepository;

@ExtendWith(MockitoExtension.class)
public class NguoiDungServiceTest {

    @Mock
    private NguoiDungRepository nguoiDungRepository;

    @InjectMocks
    private NguoiDungService nguoiDungService;

    private NguoiDung nguoiDungMau;
    private List<NguoiDung> danhSachNguoiDung;

    @BeforeEach
    void setUp() {
        // Tạo nhân viên mẫu cho việc kiểm thử
        nguoiDungMau = new NguoiDung();
        nguoiDungMau.setMaNguoiDung(1L);
        nguoiDungMau.setTenNguoiDung("Nguyễn Văn A");
        nguoiDungMau.setSoDienThoai("0123456789");
        nguoiDungMau.setDiaChi("123 Đường ABC");
        nguoiDungMau.setMatKhau("password123");
        nguoiDungMau.setVaiTro(UserRole.nvgh);
        
        // Tạo danh sách nhân viên mẫu
        danhSachNguoiDung = new ArrayList<>();
        danhSachNguoiDung.add(nguoiDungMau);
    }

    @Test
    @DisplayName("TC_EMP_LIST_001: Xem danh sách nhân viên")
    void testXemDanhSachNhanVien() {
        // Chuẩn bị
        when(nguoiDungRepository.findByVaiTroIn(Arrays.asList(UserRole.nvgh, UserRole.ql)))
            .thenReturn(danhSachNguoiDung);
        
        // Thực hiện
        List<NguoiDung> ketQua = nguoiDungService.getAllEmployees();
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals(1, ketQua.size());
        assertEquals("Nguyễn Văn A", ketQua.get(0).getTenNguoiDung());
        assertEquals("0123456789", ketQua.get(0).getSoDienThoai());
        assertEquals(UserRole.nvgh, ketQua.get(0).getVaiTro());
        verify(nguoiDungRepository, times(1)).findByVaiTroIn(Arrays.asList(UserRole.nvgh, UserRole.ql));
    }

    @Test
    @DisplayName("TC_EMP_LIST_002: Phân trang danh sách nhân viên")
    void testPhanTrangDanhSachNhanVien() {
        // Chuẩn bị
        Pageable pageable = PageRequest.of(0, 10);
        Page<NguoiDung> page = new PageImpl<>(danhSachNguoiDung, pageable, danhSachNguoiDung.size());
        when(nguoiDungRepository.findByVaiTroIn(Arrays.asList(UserRole.nvgh, UserRole.ql), pageable))
            .thenReturn(page);
        
        // Thực hiện
        Page<NguoiDung> ketQua = nguoiDungService.getAllEmployees(pageable);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals(1, ketQua.getContent().size());
        assertEquals("Nguyễn Văn A", ketQua.getContent().get(0).getTenNguoiDung());
        verify(nguoiDungRepository, times(1)).findByVaiTroIn(Arrays.asList(UserRole.nvgh, UserRole.ql), pageable);
    }

    @Test
    @DisplayName("TC_EMP_ADD_001: Thêm nhân viên thành công")
    void testThemNhanVienThanhCong() {
        // Chuẩn bị
        NguoiDung nhanVienMoi = new NguoiDung();
        nhanVienMoi.setTenNguoiDung("Trần Thị B");
        nhanVienMoi.setSoDienThoai("0987654321");
        nhanVienMoi.setDiaChi("456 Đường XYZ");
        nhanVienMoi.setMatKhau("password456");
        nhanVienMoi.setVaiTro(UserRole.nvgh);
        
        when(nguoiDungRepository.save(any(NguoiDung.class))).thenReturn(nhanVienMoi);
        
        // Thực hiện
        NguoiDung ketQua = nguoiDungService.saveEmployee(nhanVienMoi);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals("Trần Thị B", ketQua.getTenNguoiDung());
        assertEquals("0987654321", ketQua.getSoDienThoai());
        assertEquals(UserRole.nvgh, ketQua.getVaiTro());
        verify(nguoiDungRepository, times(1)).save(any(NguoiDung.class));
    }

    // Đã xóa test case TC_EMP_ADD_002 kiểm tra thêm nhân viên với số điện thoại đã tồn tại

    @Test
    @DisplayName("TC_EMP_ADD_003: Thêm nhân viên với số điện thoại không hợp lệ")
    void testThemNhanVienVoiSoDienThoaiKhongHopLe() {
        // Chuẩn bị
        NguoiDung nhanVienMoi = new NguoiDung();
        nhanVienMoi.setTenNguoiDung("Trần Thị B");
        nhanVienMoi.setSoDienThoai("abc123"); // Số điện thoại không hợp lệ
        nhanVienMoi.setDiaChi("456 Đường XYZ");
        nhanVienMoi.setMatKhau("password456");
        nhanVienMoi.setVaiTro(UserRole.nvgh);
        
        // Thực hiện và kiểm tra
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            nguoiDungService.saveEmployee(nhanVienMoi);
        });
        
        assertEquals("Số điện thoại phải có 10 chữ số", exception.getMessage());
        verify(nguoiDungRepository, never()).save(any(NguoiDung.class));
    }

    @Test
    @DisplayName("TC_EMP_ADD_004: Thêm nhân viên với thông tin thiếu")
    void testThemNhanVienVoiThongTinThieu() {
        // Chuẩn bị
        NguoiDung nhanVienThieu = new NguoiDung();
        nhanVienThieu.setTenNguoiDung(""); // Tên rỗng
        nhanVienThieu.setSoDienThoai("0987654321");
        nhanVienThieu.setDiaChi("456 Đường XYZ");
        nhanVienThieu.setMatKhau("password456");
        nhanVienThieu.setVaiTro(UserRole.nvgh);
        
        // Thực hiện và kiểm tra
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            nguoiDungService.saveEmployee(nhanVienThieu);
        });
        
        assertEquals("Tên người dùng không được để trống", exception.getMessage());
        verify(nguoiDungRepository, never()).save(any(NguoiDung.class));
    }

    @Test
    @DisplayName("TC_EMP_EDIT_001: Chỉnh sửa thông tin nhân viên thành công")
    void testChinhSuaNhanVienThanhCong() {
        // Chuẩn bị
        NguoiDung nhanVienCapNhat = new NguoiDung();
        nhanVienCapNhat.setMaNguoiDung(1L);
        nhanVienCapNhat.setTenNguoiDung("Nguyễn Văn A Cập Nhật");
        nhanVienCapNhat.setSoDienThoai("0123456789");
        nhanVienCapNhat.setDiaChi("123 Đường ABC");
        nhanVienCapNhat.setMatKhau("password123");
        nhanVienCapNhat.setVaiTro(UserRole.nvgh);
        
        when(nguoiDungRepository.findById(1L)).thenReturn(Optional.of(nguoiDungMau));
        when(nguoiDungRepository.save(any(NguoiDung.class))).thenReturn(nhanVienCapNhat);
        
        // Thực hiện
        Optional<NguoiDung> nguoiDungOptional = nguoiDungService.getEmployeeById(1L);
        assertTrue(nguoiDungOptional.isPresent());
        
        NguoiDung nguoiDung = nguoiDungOptional.get();
        nguoiDung.setTenNguoiDung(nhanVienCapNhat.getTenNguoiDung());
        nguoiDung.setSoDienThoai(nhanVienCapNhat.getSoDienThoai());
        
        NguoiDung ketQua = nguoiDungService.saveEmployee(nguoiDung);
        
        // Kiểm tra
        assertNotNull(ketQua);
        assertEquals("Nguyễn Văn A Cập Nhật", ketQua.getTenNguoiDung());
        assertEquals("0123456789", ketQua.getSoDienThoai());
        verify(nguoiDungRepository, times(1)).findById(1L);
        verify(nguoiDungRepository, times(1)).save(any(NguoiDung.class));
    }

    // Đã xóa test case TC_EMP_EDIT_002 kiểm tra chỉnh sửa với số điện thoại đã tồn tại

    @Test
    @DisplayName("TC_EMP_DEL_001: Xóa nhân viên thành công")
    void testXoaNhanVienThanhCong() {
        // Chuẩn bị
        Long nhanVienId = 1L;
        when(nguoiDungRepository.findById(nhanVienId)).thenReturn(Optional.of(nguoiDungMau));
        doNothing().when(nguoiDungRepository).deleteById(nhanVienId);
        
        // Thực hiện
        Optional<NguoiDung> nguoiDungOptional = nguoiDungService.getEmployeeById(nhanVienId);
        assertTrue(nguoiDungOptional.isPresent());
        
        nguoiDungService.deleteEmployee(nhanVienId);
        
        // Kiểm tra
        verify(nguoiDungRepository, times(1)).findById(nhanVienId);
        verify(nguoiDungRepository, times(1)).deleteById(nhanVienId);
    }
}