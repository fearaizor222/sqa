package com.ptithcm.sqa.entity;

import java.io.Serializable;
import java.util.Objects;

public class ChiTietHoaDonId implements Serializable {
    private Integer hoaDon; // Phải trùng với tên thuộc tính trong ChiTietHoaDon
    private Integer thuoc;  // Phải trùng với tên thuộc tính trong ChiTietHoaDon

    public ChiTietHoaDonId() {
    }

    public ChiTietHoaDonId(Integer hoaDon, Integer thuoc) {
        this.hoaDon = hoaDon;
        this.thuoc = thuoc;
    }

    public Integer getHoaDon() {
        return hoaDon;
    }

    public void setHoaDon(Integer hoaDon) {
        this.hoaDon = hoaDon;
    }

    public Integer getThuoc() {
        return thuoc;
    }

    public void setThuoc(Integer thuoc) {
        this.thuoc = thuoc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChiTietHoaDonId that = (ChiTietHoaDonId) o;
        return Objects.equals(hoaDon, that.hoaDon) && Objects.equals(thuoc, that.thuoc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hoaDon, thuoc);
    }
} 