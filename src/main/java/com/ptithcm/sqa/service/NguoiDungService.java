package com.ptithcm.sqa.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.NguoiDung.UserRole;
import com.ptithcm.sqa.repository.NguoiDungRepository;

@Service
public class NguoiDungService {

    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public Page<NguoiDung> getAllEmployees(Pageable pageable) {
        return nguoiDungRepository.findByVaiTroIn(Arrays.asList(UserRole.nvgh, UserRole.ql), pageable);
    }

    public List<NguoiDung> getAllEmployees() {
        return nguoiDungRepository.findByVaiTroIn(Arrays.asList(UserRole.nvgh, UserRole.ql));
    }

    public Optional<NguoiDung> getEmployeeById(Long id) {
        return nguoiDungRepository.findById(id);
    }

    public NguoiDung saveEmployee(NguoiDung nguoiDung) {
        validateNguoiDung(nguoiDung);
        return nguoiDungRepository.save(nguoiDung);
    }

    public void deleteEmployee(Long id) {
        nguoiDungRepository.deleteById(id);
    }

    /**
     * Validates a NguoiDung object for all required fields and constraints
     * @param nguoiDung The NguoiDung object to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateNguoiDung(NguoiDung nguoiDung) {
        validateRequiredFields(nguoiDung);
        validatePhoneNumber(nguoiDung);
    }
    
    /**
     * Validates that all required fields for a NguoiDung are present
     * @param nguoiDung The NguoiDung object to validate
     * @throws IllegalArgumentException if any required field is missing
     */
    private void validateRequiredFields(NguoiDung nguoiDung) {
        if (nguoiDung.getTenNguoiDung() == null || nguoiDung.getTenNguoiDung().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được để trống");
        }
        
        if (nguoiDung.getSoDienThoai() == null || nguoiDung.getSoDienThoai().isEmpty()) {
            throw new IllegalArgumentException("Số điện thoại không được để trống");
        }
        
        if (nguoiDung.getMatKhau() == null || nguoiDung.getMatKhau().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        
        if (nguoiDung.getVaiTro() == null) {
            throw new IllegalArgumentException("Vai trò không được để trống");
        }
    }
    
    /**
     * Validates that the phone number is valid
     * @param nguoiDung The NguoiDung object to validate
     * @throws IllegalArgumentException if phone number is invalid
     */
    private void validatePhoneNumber(NguoiDung nguoiDung) {
        // Validate phone number format
        String phoneRegex = "^[0-9]{10}$"; // Simple regex for 10 digit phone number
        if (!nguoiDung.getSoDienThoai().matches(phoneRegex)) {
            throw new IllegalArgumentException("Số điện thoại phải có 10 chữ số");
        }
    }

    public boolean isPhoneNumberExists(String soDienThoai) {
        return nguoiDungRepository.existsBySoDienThoai(soDienThoai);
    }

    public boolean isPhoneNumberExistsExcept(String soDienThoai, Long id) {
        Optional<NguoiDung> nguoiDungOpt = nguoiDungRepository.findBySoDienThoai(soDienThoai);
        return nguoiDungOpt.isPresent() && !nguoiDungOpt.get().getMaNguoiDung().equals(id);
    }

    public NguoiDung getNguoiDungById(Long id) {
        return nguoiDungRepository.findById(id).orElse(null);
    }

    public void updateNguoiDung(NguoiDung nguoiDung) {
        validateNguoiDung(nguoiDung);
        nguoiDungRepository.save(nguoiDung);
    }
}