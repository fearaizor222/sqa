package com.ptithcm.sqa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ptithcm.sqa.entity.Thuoc;

public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {
    // Additional query methods (if needed)
}
