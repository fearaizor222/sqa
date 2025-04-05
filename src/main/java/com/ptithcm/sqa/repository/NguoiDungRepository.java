package com.ptithcm.sqa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptithcm.sqa.entity.NguoiDung;
import com.ptithcm.sqa.entity.NguoiDung.UserRole;

@Repository
public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    
    Optional<NguoiDung> findBySoDienThoai(String soDienThoai);
    
    boolean existsBySoDienThoai(String soDienThoai);
    
    Page<NguoiDung> findByVaiTroIn(List<UserRole> vaiTro, Pageable pageable);
    
    List<NguoiDung> findByVaiTroIn(List<UserRole> vaiTro);
}
