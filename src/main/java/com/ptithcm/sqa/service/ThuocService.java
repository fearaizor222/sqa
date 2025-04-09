package com.ptithcm.sqa.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ptithcm.sqa.entity.Thuoc;
import com.ptithcm.sqa.repository.ThuocRepository;

@Service
public class ThuocService {

    @Autowired
    private ThuocRepository thuocRepository;

    public List<Thuoc> getAllThuoc() {
        return thuocRepository.findAll();
    }

    public Optional<Thuoc> getThuocById(Integer id) {
        return thuocRepository.findById(id);
    }

    public Thuoc saveThuoc(Thuoc thuoc) {
        validateThuoc(thuoc);
        return thuocRepository.save(thuoc);
    }

    public void deleteThuoc(Integer id) {
        thuocRepository.deleteById(id);
    }

    /**
     * Validates the Thuoc object for required fields and constraints
     * @param thuoc The Thuoc object to validate
     * @throws IllegalArgumentException if validation fails
     */
    private void validateThuoc(Thuoc thuoc) {
        // Kiểm tra các trường bắt buộc
        validateRequiredFields(thuoc);
        // Kiểm tra giá trị số không âm
        validateNonNegativeValues(thuoc);
    }
    
    /**
     * Validates that all required fields are present and not empty
     * @param thuoc The Thuoc object to validate
     * @throws IllegalArgumentException if any required field is empty
     */
    private void validateRequiredFields(Thuoc thuoc) {
        if (thuoc.getTenThuoc() == null || thuoc.getTenThuoc().isEmpty()) {
            throw new IllegalArgumentException("Tên thuốc không được để trống");
        }
        if (thuoc.getTuoiSuDung() == null || thuoc.getTuoiSuDung().isEmpty()) {
            throw new IllegalArgumentException("Tuổi sử dụng không được để trống");
        }
    }
    
    /**
     * Validates that numeric values are non-negative
     * @param thuoc The Thuoc object to validate
     * @throws IllegalArgumentException if any numeric value is negative
     */
    private void validateNonNegativeValues(Thuoc thuoc) {
        if (thuoc.getGia() < 0) {
            throw new IllegalArgumentException("Giá thuốc không được âm");
        }
        if (thuoc.getSoLuong() < 0) {
            throw new IllegalArgumentException("Số lượng thuốc không được âm");
        }
        
        // Kiểm tra thêm các ràng buộc về giá hợp lệ
        if (thuoc.getGia() == 0) {
            throw new IllegalArgumentException("Giá thuốc phải lớn hơn 0");
        }
    }

    public List<Thuoc> findProducts(String group, String search) {
        List<Thuoc> thuocList;

        if (search == null || search.isEmpty()) {
            thuocList = thuocRepository.findAll();
        } else {
            thuocList = thuocRepository.searchProducts(group, search);
        }

        if (group == null || group.isEmpty()) {
            return thuocList;
        }

        return thuocList.stream()
                .filter(t -> {
                    String ageUse = t.getTuoiSuDung();
                    switch (group) {
                        case "over18":

                            return ageUse.equals("Trên 18 tuổi");
                        case "over12":

                            return ageUse.equals("Trên 12 tuổi");
                        case "over8":

                            return ageUse.equals("Trên 8 tuổi");
                        case "over4":

                            return ageUse.equals("Trên 4 tuổi");
                        default:
                            return true;
                    }
                })
                .collect(Collectors.toList());

    }
}
