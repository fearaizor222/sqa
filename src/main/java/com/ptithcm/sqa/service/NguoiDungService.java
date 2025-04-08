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
        return nguoiDungRepository.save(nguoiDung);
    }

    public void deleteEmployee(Long id) {
        nguoiDungRepository.deleteById(id);
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
        nguoiDungRepository.save(nguoiDung);
    }
}