package com.ptithcm.sqa.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ptithcm.sqa.entity.Thuoc;

public interface ThuocRepository extends JpaRepository<Thuoc, Integer> {

    Optional<Thuoc> findByTenThuoc(String tenthuoc);

    @Query("SELECT t FROM Thuoc t WHERE " +
            "(:group = 'all' OR t.tuoiSuDung = :group) AND " +
            "(LOWER(t.tenThuoc) LIKE LOWER(CONCAT('%', :search, '%')))")
    List<Thuoc> searchProducts(@Param("group") String group, @Param("search") String search);

}
