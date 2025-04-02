package com.ptithcm.sqa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptithcm.sqa.entity.NguoiDung;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findBySoDienThoai(String soDienThoai);
}
