package com.ptithcm.sqa.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.repository.NguoiDungRepository;

@Service
public class AuthenticateService {
    @Autowired
    private NguoiDungRepository nguoiDungRepository;

    public Optional<NguoiDung> authenticate(String username, String password) {
        Optional<NguoiDung> user = nguoiDungRepository.findBySoDienThoai(username);
        if (user.isPresent()) {
            NguoiDung nguoiDung = user.get();
            return nguoiDung.getMatKhau().equals(password) ? Optional.of(nguoiDung) : Optional.empty(); // Check password
        }
        return Optional.empty(); // Return empty if user not found or password doesn't match
    }

    public boolean register(NguoiDung nguoiDung) {
        Optional<NguoiDung> user = nguoiDungRepository.findBySoDienThoai(nguoiDung.getSoDienThoai());
        if (user.isPresent()) {
            return false; // User already exists
        } else {
            nguoiDungRepository.save(nguoiDung);
            return true; // Registration successful
        }
    }
}